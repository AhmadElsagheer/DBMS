package sagSolheeriman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Table implements Serializable {

	/**
	 * Table instances control pages that contain the stored records on the hard disk.
	 * All actual read and write operations are done in this class.
	 */
	private static final long serialVersionUID = 1L;
	
	private int maxTuplesPerPage, curPageIndex, numOfColumns;
	private String path, tableName, tableHeader, primaryKey;
	private Hashtable<String, String> colTypes, colRefs;
	private Hashtable<String, Integer> colIndex;

	/**
	 * Create a new table with the specified parameter list
	 * @param path
	 * @param strTableName the table name
	 * @param htblColNameType the types of table columns
	 * @param htblColNameRefs a mapping of foreign key references
	 * @param strKeyColName the primary key of the table
	 * @param maxTuplesPerPage the maximum number of records a page can hold
	 * @throws IOException If an I/O error occurred
	 */
	public Table(String path, String strTableName, Hashtable<String,String> htblColNameType, 
            Hashtable<String,String> htblColNameRefs, String strKeyColName, int maxTuplesPerPage) throws IOException{
		
		this.path = path + strTableName + "/";
		this.tableName = strTableName;
		this.primaryKey = strKeyColName;
		this.colTypes = htblColNameType;
		this.colRefs = htblColNameRefs;
		this.maxTuplesPerPage = maxTuplesPerPage;
		this.curPageIndex = -1;
		this.numOfColumns =  0;
		initializeColumnsIndexes();
		createDirectory();
		createPage();
		saveTable();
	}

	/**
	 * Save the table object in a binary file on the secondary storage
	 * @throws IOException If an I/O error occurred
	 */
	private void saveTable() throws IOException
	{
		File f = new File(path+tableName+".class");
		if(f.exists())
			f.delete();
		f.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    	oos.writeObject(this);
    	oos.close();
   	}

	/**
	 * Map every table column to an index to store records in arrays.
	 */
	private void initializeColumnsIndexes() {
		tableHeader = "";
		colIndex = new Hashtable<String, Integer>();
		int index = 0;
		for (Entry<String, String> entry : colTypes.entrySet()) 
		{
			colIndex.put(entry.getKey(), index++);
			numOfColumns++;
			tableHeader += entry.getKey() + ", ";
		}
	}

	/**
	 * Create a directory for the table in which the table object
	 * and table pages are stored.
	 */
	private void createDirectory()
	{
    	File tableDir = new File(path);
    	tableDir.mkdir();
	}

	/**
	 * Create a page to hold records for this table.
	 * @return the created page
	 * @throws IOException If an I/O error occurred
	 */
	private Page createPage() throws IOException {
		curPageIndex++;
		Page p = new Page(maxTuplesPerPage, path+tableName+"_"+ curPageIndex+".class");
		saveTable();
		return p;
		
	}

	/**
	 * Check wether a an object matches its specified type
	 * @param value the object to be checked
	 * @param type the object type to be matched
	 * @return a boolean to indicate whether they match or not
	 */
	private boolean checkType(Object value, String type) {

		if (type.equals("Integer"))
			if (!(value instanceof Integer))
				return false;
		if (type.equals("String"))
			if (!(value instanceof String))
				return false;
		if (type.equals("Double"))
			if (!(value instanceof Double))
				return false;
		if (type.equals("Boolean"))
			if (!(value instanceof Boolean))
				return false;
		if (type.equals("Date"))
			if (!(value instanceof Date))
				return false;
		return true;

	}

	/**
	 * Check that all specified columns are in the table schema and matches the defined types
	 * @param htblColNameValue some columns to be checked against the table schema
	 * @throws DBEngineException If a column does not exist or does not match the type
	 */
	private void checkColumns(Hashtable<String, Object> htblColNameValue) throws DBEngineException 
	{
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) 
		{
			String colName = entry.getKey();
			if (!colTypes.containsKey(colName))
				throw new DBEngineException("Column "+ colName + " does not exist");
			if(!checkType(entry.getValue(), colTypes.get(colName)))
				throw new DBEngineException("Type mismatch on column "+ colName);
		}
	}

	/**
	 * @param htblColNameValue
	 * @return a boolean to indicate a successful or failed insertion operation
	 * @throws DBEngineException If columns, foreign keys or the primary key are not valid
	 * @throws IOException If an I/O error occurred
	 * @throws ClassNotFoundException If an error occurred in the stored table pages format
	 */
	public boolean insert(Hashtable<String, Object> htblColNameValue) throws DBEngineException, ClassNotFoundException, IOException {

//		1. check column names and types
		checkColumns(htblColNameValue);
			
//		2. check for primary key
		Object primaryValue = htblColNameValue.get(primaryKey);
		if (primaryValue == null)
			throw new DBEngineException("Primary key is not allowed to be null");

		if (checkRecordExists(primaryKey, primaryValue))
			throw new DBEngineException("Primary key is already used before");
	
		
//		3. check the foreign keys
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) {
			String referencedColumn = colRefs.get(entry.getKey());

			if (referencedColumn != null) 
			{
				String[] tokens = referencedColumn.split("\\.");
//				TableName.colName
				String tableName = tokens[0];	//TableName
				String colName = tokens[1];		//colName

				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(new File(path +"../" + tableName +"/" + tableName + ".class")));
				Table fetchedTable = (Table) ois.readObject();
				ois.close();
				if (!fetchedTable.checkRecordExists(colName, entry.getValue()))
					throw new DBEngineException("Invalid value for the foreign key: " + entry.getKey() + " = " + entry.getValue());
			}
		}

//		4. add the record
		Record r = new Record(numOfColumns);
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) 
		{
			String colName = entry.getKey();
			Object value = entry.getValue();
			r.addValue(colIndex.get(colName), value);
		}
		r.addValue(colIndex.get("TouchDate"), (Date) Calendar.getInstance().getTime());
		addRecord(r);
		return true;

	}

	/**
	 * Add a new record to the table
	 * @param record the record to be added
	 * @throws IOException If an I/O error occurred
	 * @throws ClassNotFoundException If an error occurred in the stored table pages format
	 */
	private void addRecord(Record record) throws IOException, ClassNotFoundException {

		File f = new File(path + tableName + "_" + curPageIndex+".class");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		Page curPage = (Page) ois.readObject();
		if(curPage.isFull())
			curPage = createPage();
		curPage.addRecord(record);
		ois.close();
	}
	
	/**
	 * Check if there exists a record, with the specified column value, in the table
	 * @param colName the column to be looked at while searching
	 * @param colValue the value to be matched
	 * @return whether the record exists or not
	 * @throws IOException If an I/O error occurred
	 * @throws ClassNotFoundException If an error occurred in the stored table pages format
	 */
	private boolean checkRecordExists(String colName, Object colValue) throws  IOException, ClassNotFoundException  
	{
		Hashtable<String, Object> htbl = new Hashtable<String, Object>();
		htbl.put(colName, colValue);
		Iterator<Record> itr = select(htbl, "AND");
		if (itr.hasNext())
			return true;
		return false;
	}

	/**
	 * Check whether a given record matches any of the given column name-value pairs
	 * @param htblColNameValue the column name-value pairs against which the record is checked
	 * @param record the record to be checked
	 * @return a boolean to indicate whether this record matches any of the pairs or not
	 */
	private boolean checkOr(Hashtable<String, Object> htblColNameValue, Record record) 
	{
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) 
		{
			String colName = entry.getKey();
			Object value = entry.getValue();

			int index = colIndex.get(colName);
			if(record.get(index).equals(value))
				return true;
		}
		return false;
	}

	/**
	 * Check whether a given record matches all the given column name-value pairs
	 * @param htblColNameValue the column name-value pairs against which the record is checked
	 * @param record the record to be checked
	 * @return a boolean to indicate whether this record matches all the pairs or not
	 */
	private boolean checkAND(Hashtable<String, Object> htblColNameValue, Record record) 
	{
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) 
		{
			String colName = entry.getKey();
			Object value = entry.getValue();
			int index = colIndex.get(colName);
			if(!record.get(index).equals(value))
				return false;
		}
		return true;
	}

	/**
	 * Select all records from the table that matches the specified column name-value pairs
	 * with a given conditional operator (AND or OR)
	 * @param htblColNameValue the column name-value pairs to which records will be compared
	 * @param strOperator the conditional operator to be exectuted ("AND, "OR" only)
	 * @return an iterator pointing to the first record in the result set
	 * @throws IOException If an I/O error occurred
	 * @throws ClassNotFoundException If an error occurred in the stored table pages format
	 */
	public Iterator<Record> select(Hashtable<String, Object> htblColNameValue, String strOperator)
		throws  IOException, ClassNotFoundException {

		boolean isOr = strOperator.equals("OR");
		ObjectInputStream ois;
		LinkedList<Record> answer = new LinkedList<Record>();
		
		for (int index = 0; index <= curPageIndex; index++) {
			File f = new File(path + tableName + "_" + index+".class");
			
	    	ois = new ObjectInputStream(new FileInputStream(f));
	    	Page p = (Page) ois.readObject();
			for(int i = 0; i < p.size(); ++i)
			{
				Record r = p.get(i);
				if(isOr && checkOr(htblColNameValue, r) || !isOr && checkAND(htblColNameValue, r))
					answer.add(r);
			}
			ois.close();
		}
		return answer.listIterator();
	}
	
	/**
	 * Get the column names in the order the are indexed
	 * @return the header of the table
	 */
	public String getTableHeader()
	{
		return tableHeader;
	}
}