package db;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String tablesDir = "bin/db/tables/";
	private String path;
	private int maxTuplesPerPage;
	private int curPageIndex;
	private int curPageTuples;
	private String tableName;
	private String name, primaryKey;
	private Hashtable<String, String> colTypes, colRefs;
	private Hashtable<String, Integer> colIndex;

	public Table(String path, String strTableName, Hashtable<String,String> htblColNameType, 
            Hashtable<String,String> htblColNameRefs, String strKeyColName, int maxTuplesPerPage) throws IOException{
		
		this.path = path + strTableName + "/";
		this.name = strTableName;
		this.primaryKey = strKeyColName;
		this.colTypes = htblColNameType;
		this.colRefs = htblColNameRefs;
		this.maxTuplesPerPage = maxTuplesPerPage;
		this.curPageIndex = -1;
		createDirectory();
		createPage();
		saveTable();
	}
	
	public void saveTable() throws IOException
	{
		File f = new File(tablesDir+tableName+".class");
		if(f.exists())
			f.delete();
		f.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    	oos.writeObject(this);
    	oos.close();
	}

	private void createDirectory()
	{
    	File tableDir = new File(path);
    	tableDir.mkdir();

	}
	
	private void createPage()
	{
		curPageIndex++;
		curPageTuples = 0;
		File newPage = new File(path);
		try 
		{
			newPage.createNewFile();
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
	}

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

	private boolean checkOr(Hashtable<String, Object> htblColNameValue, Object[] values) {
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) {
			String colName = entry.getKey();
			Object value = entry.getValue();

			// check value at index (colName)

			int index = colIndex.get(colName);
			if (values[index].equals(value))
				return true;
		}
		return false;
	}

	private boolean checkAND(Hashtable<String, Object> htblColNameValue, Object[] values) {
		for (Entry<String, Object> entry : htblColNameValue.entrySet()) {
			String colName = entry.getKey();
			Object value = entry.getValue();

			int index = colIndex.get(colName);
			if (!values[index].equals(value))
				return false;
		}
		return true;
	}

	public Iterator<Record> select(Hashtable<String, Object> htblColNameValue, String strOperator)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		
		boolean isOr = strOperator.equals("OR");
		ObjectInputStream ois;
		LinkedList<Record> answer = new LinkedList<Record>();
		for (int index = 0; index <= curPageIndex; index++) {
			ois = new ObjectInputStream(new FileInputStream(new File(tableName + "_" + index)));
			int numOfRecords = 0;
			if (index == curPageIndex)
				numOfRecords = curPageTuples;
			else
				numOfRecords = maxTuplesPerPage;

			while (numOfRecords > 0) {
				Record r = (Record) ois.readObject();
				Object[] values = r.getValues();
				if (isOr)
					if (checkOr(htblColNameValue, values))
						answer.add(r);
					else if (checkAND(htblColNameValue, values))
						answer.add(r);

				numOfRecords--;
			}
			ois.close();
		}

		return answer.iterator();
	}

	
}
