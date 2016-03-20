package sagSolheeriman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;

public class DBApp {
	
	/**
	 * DB engine. It contains all methods that the user deal with
	 */
	private static String mainDirectory = "databases/";
	private String dbName, dbDirectory;
	private File metadata;
	private TreeSet<String> dataTypes;
	private Properties dbProps; 
	
	/**
	 * Initialize a new database with the specified and maximum number of records per page.
	 * If the database already exists, this method will reset it.
	 * @param dbName the name of the database
	 * @param MaximumRowsCountinPage maximum number of records per table page
	 * @throws IOException If an I/O error occurred
	 */
	public void init(String dbName, Integer MaximumRowsCountinPage) throws IOException
	{
		this.dbName = dbName;
		
		// drop any existing database with this name
		dropDatabase(dbName);

		// create a directory for the db
		File dbDir = new File(dbDirectory = mainDirectory+this.dbName+"/");
		dbDir.mkdirs();
		
		// initialize config file
		dbProps = new Properties();
		dbProps.put("MaximumRowsCountinPage", MaximumRowsCountinPage.toString());
		new File(dbDirectory+"/config").mkdirs();
		File config = new File(dbDirectory+"/config/DBApp.config");
		config.createNewFile();
		FileOutputStream fos = new FileOutputStream(config);
		dbProps.store(fos, "DB Properties");
		fos.close();
		
		// initialize metadata file
		new File(dbDirectory+"data").mkdirs();
		this.metadata = new File(dbDirectory+"data/"+"/metadata.csv");
		if(this.metadata.createNewFile())
		{
			PrintWriter out = new PrintWriter(this.metadata);
			out.println("Table Name, Column Name, Column Type, Key, Indexed, References");
			out.flush();
			out.close();
		}
		this.initDataTypes();
    }
	
	/**
	 * Switch to the database with the specified name
	 * @param dbName the name of the database to be used
	 * @throws DBEngineException If the database with the specified name does not exist
	 * @throws IOException If an I/O error occurred
	 * @throws FileNotFoundException If there is an error with an existing database directory
	 */
	public void use(String dbName) throws DBEngineException, FileNotFoundException, IOException
	{
		File f = new File(mainDirectory+dbName);
		if(!f.exists())
			throw new DBEngineException("Database does not exist: " + dbName);
		
		this.dbName = dbName;
		this.dbDirectory = mainDirectory+this.dbName+"/";
		
		dbProps = new Properties();
		File config = new File(dbDirectory+"/config/DBApp.config");
		dbProps.load(new FileInputStream(config));
		
		this.metadata = new File(dbDirectory+"data/"+"/metadata.csv");
		this.initDataTypes();
		
	}
	
	/**
	 * Drop any database found in the working directory with the specified name.
	 * @param dbName the name of the database to be dropped
	 * @throws IOException If an I/O error occurred
	 */
	private static void dropDatabase(String dbName) throws IOException
	{
		delete(new File(mainDirectory + dbName));
	}
	
	/**
	 * Delete a directory with all its contents.
	 * @param file the directory to be deleted
	 * @throws IOException
	 */
	private static void delete(File file) throws IOException 
	{
		if(!file.exists())
			return;
		if (file.isDirectory()) 
		{
			for (File c : file.listFiles())
				delete(c);
		}
		if(!file.delete())
			throw new FileNotFoundException("Failed to delete file: " + file);
	}
	
	/**
	 * Initialize the available column types
	 */
	private void initDataTypes()
	{
		dataTypes = new TreeSet<String>();
		dataTypes.add("Integer");
		dataTypes.add("String");
		dataTypes.add("Boolean");
		dataTypes.add("Date");
		dataTypes.add("Double");
		
	}

	/**
	 * Add a new property to the database (added to config file)
	 * @param key the property to be added
	 * @param value the value of the property
	 * @throws FileNotFoundException If there is an error in the config file
	 * @throws IOException If an I/O error occurred
	 */
	public void addProperty(String key, String value) throws FileNotFoundException, IOException
	{
		File config = new File(mainDirectory+"/config/DBApp.config");
		Properties dbProps = new Properties();
			
		FileInputStream fis = new FileInputStream(config);
		dbProps.load(fis);
		fis.close();
		
		dbProps.put(key, value);
		
		FileOutputStream fos = new FileOutputStream(config);
		dbProps.store(fos, "Added property: " + key);
		fos.close();
		
	}
	
	/**
	 * Check that the given table has the specified foreign key matching its name and type
	 * @param tableName the table to be checked for a foreign key
	 * @param foreignKeyName the name of the foreign key
	 * @param foreignKeyType the tpye of the foreign key
	 * @return wether there exists a foreign key with this name and type in the table schema
	 * @throws IOException If an I/O error occurred
	 */
	private boolean isValidForeignKey(String tableName , String foreignKeyName , String foreignKeyType) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(metadata));
		while(br.ready())
		{
			String [] data = br.readLine().split(",");
			if(tableName.equals(data[0]) && foreignKeyName.equals(data[1]) && foreignKeyType.equals(data[2]))
			{
				br.close();
				return true;
			}
		}
		br.close();
		return false;
	}


	/**
	 * Check that the given reference list for foreign keys of the specified table are valid.
	 * @param strTableName the table name with which the reference list is associated
	 * @param htblColNameType the types of the table columns
	 * @param htblColNameRefs the reference list of the table foreign keys
	 * @return the name of the first column that has an invalid foreign key
	 * @throws DBEngineException If there exists a column that is not in the specified table schema
	 * @throws IOException If an I/O error occurred
	 */
	private String checkForeignKeys(String strTableName, Hashtable<String,String> htblColNameType, 
            Hashtable<String,String> htblColNameRefs) throws DBEngineException, IOException{
		
		for(Entry<String, String> entry: htblColNameRefs.entrySet())
		{
			String[] foreignKey = entry.getValue().split("\\.");
			if(htblColNameType.get(entry.getKey()) == null)
				throw new DBEngineException("Column "+entry.getKey()+" does not exist");
			if(!isValidForeignKey(foreignKey[0], foreignKey[1], htblColNameType.get(entry.getKey())))
				return entry.getKey();
		}
		return null;
	}
	
	/**
	 * Check that the given column types are valid.
	 * @param htblColNameType the column names with their types to check
	 * @return the name of first column that has invalid type
	 */
	private String checkColumnTypes(Hashtable<String, String> htblColNameType)
	{
		for(Entry<String, String> entry: htblColNameType.entrySet())
			if(!dataTypes.contains(entry.getValue()))
				return entry.getKey();
		return null;
	}
	
	/**
	 * Add the table schema to the metadata file.
	 * @param strTableName the table to be added
	 * @param htblColNameType the column types of the table
	 * @param htblColNameRefs the references of all foreign key columns
	 * @param strKeyColName the primary key of the table
	 * @throws IOException If an I/O error occurred 
	 */
	private void addToMetaData(String strTableName,    Hashtable<String,String> htblColNameType, 
                           Hashtable<String,String> htblColNameRefs, String strKeyColName) throws IOException
	{
		PrintWriter pr = new PrintWriter(new FileWriter(metadata, true));
		for(Entry<String, String> entry:htblColNameType.entrySet())
		{
			String colName = entry.getKey();
			String colType = entry.getValue();
			String refs = htblColNameRefs.get(colName);
			boolean key = (colName == strKeyColName);
			pr.append(strTableName+"," + colName + "," + colType + "," + key + "," + "false" + "," + refs+"\n");
		}		
		pr.flush();
		pr.close();
	}
	
    
	/**
	 * Creates a new table with the specified name, columns and referential integrity constraints.
	 * @param strTableName the name of the table to be created
	 * @param htblColNameType the types of all table columns
	 * @param htblColNameRefs the references of the foreign key columns
	 * @param strKeyColName the primary key of the table (only one column)
	 * @throws DBEngineException If the name, column types or foreign keys are not valid
	 * @throws IOException If an I/O error occurred 
	 */
    public void createTable(String strTableName, Hashtable<String,String> htblColNameType, 
                            Hashtable<String,String> htblColNameRefs, String strKeyColName)  throws DBEngineException, IOException{
    	
    	// A.Check constraints
    	
    		//1.check for valid name
    	File dir = new File(dbDirectory+strTableName);
    	if(dir.exists())
    		throw new DBEngineException("There exists a table with this name.");
    	
    		//2.check for valid types
    	String invalidColumnType = checkColumnTypes(htblColNameType);
    	if(invalidColumnType != null)
    		throw new DBEngineException("Invalid column type: "+invalidColumnType + " references "+htblColNameType.get(invalidColumnType)+".");
    	
    		//3.check for valid foreign keys
    	String invalidForeignKey = checkForeignKeys(strTableName, htblColNameType, htblColNameRefs);
    	if(invalidForeignKey != null)
    		throw new DBEngineException("Invalid foreign key: "+invalidForeignKey + " references "+htblColNameRefs.get(invalidForeignKey)+".");
    	
    	// B. Add info to metadata
    	addToMetaData(strTableName, htblColNameType, htblColNameRefs, strKeyColName);
    	
    	// C. Add TouchDate
    	htblColNameType.put("TouchDate", "Date");
    	
    	// D. Create new table and store the table object in a binary file
    	int maxTuplesPerPage = Integer.parseInt(dbProps.getProperty("MaximumRowsCountinPage"));
    	new Table(dbDirectory, strTableName, htblColNameType, htblColNameRefs, strKeyColName, maxTuplesPerPage);
    	System.out.println("Table is created successfully: " + strTableName);
    }

    /**
     * Returns the table with the specified name. 
     * @param strTableName the name of the table required
     * @return the table with the specified name
     * @throws IOException If an I/O error occurred 
     * @throws ClassNotFoundException  If an error occurred in the stored table file 
     * @throws FileNotFoundException If an error occurred in the stored table file
     */
    private Table getTable(String strTableName) throws FileNotFoundException, IOException, ClassNotFoundException
    {
    	File tableFile = new File(dbDirectory+strTableName+"/"+strTableName+".class");
    	if(!tableFile.exists())
    		return null;
    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tableFile));
    	Table table = (Table) ois.readObject();
    	ois.close();
    	return table;
    }

    public void createIndex(String strTableName, String strColName)  throws DBEngineException
    {
//    	 TODO: 
//    	1. create index for any new table on the primary key (in createTable method) 
//    	2. update the index for any update/delete/insert query
//    	3. use the index for queries on columns that have this index
//    	4. save the BPTree on the Hard Disk
//    	5. max nodes is defined in config file
    }

    /**
     * Insert a new record into the specified table
     * @param strTableName the table to insert a record into.
     * @param htblColNameValue the record column name-value pairs
     * @throws IOException If an I/O error occurred
	 * @throws ClassNotFoundException If an error occurred in the stored table pages format
     * @throws DBEngineException if the table does not exist or the insertion failed
     */
    public void insertIntoTable(String strTableName, Hashtable<String,Object> htblColNameValue)  throws DBEngineException, ClassNotFoundException, IOException
    {
    	Table table = getTable(strTableName);
    	if(table == null)
    		throw new DBEngineException("Table \""+strTableName+"\" does not exist");
    	if(!table.insert(htblColNameValue))
    		throw new DBEngineException("Insertion in table \""+strTableName+"\" failed");
    }

    
    /**
     * Update the record that match the given primary key in the specified table with the given set of values
     * @param strTableName the table whose record to be updated
     * @param strKey the primary key of the target record
     * @param htblColNameValue the set of columns associated with the new values to be updated
     * @throws DBEngineException if the table does not exist or the update failed
     * @throws FileNotFoundException If an error occurred in the stored table file
     * @throws ClassNotFoundException If an error occurred in the stored table pages format
     * @throws IOException If an I/O error occurred
     */
    public void updateTable(String strTableName, Object strKey,
    		Hashtable<String,Object> htblColNameValue)  throws DBEngineException, FileNotFoundException, ClassNotFoundException, IOException{
    	
    	Table table = getTable(strTableName);
    	if(table == null)
    		throw new DBEngineException("Table \""+strTableName+"\" does not exist");
    	if(!table.update(strKey, htblColNameValue))
    		System.out.println("Update in table \""+strTableName+"\" failed");
    }
    
    /**
     * delete all records from the table that matches the specified column name-value pairs
	 * with a given conditional operator (AND or OR) 
     * @param strTableName the name of the table to select from
     * @param htblColNameValue the column name-value pairs to which records will be compared
     * @param strOperator the conditional operator to be exectuted ("AND, "OR" only)
     * @throws DBEngineException if the table does not exist
     * @throws FileNotFoundException If an error occurred in the stored table file
     * @throws ClassNotFoundException If an error occurred in the stored table pages format
     * @throws IOException If an I/O error occurred
     */

    public void deleteFromTable(String strTableName, Hashtable<String,Object> htblColNameValue, 
    		String strOperator) throws DBEngineException, FileNotFoundException, ClassNotFoundException, IOException{
    	Table table = getTable(strTableName);
    	if(table == null)
    		throw new DBEngineException("Table \"" + strTableName+"\" does not exist");
    	int deletedRecords = table.delete(htblColNameValue, strOperator);
    	System.out.println(deletedRecords + " was deleted !");
    	
    }

    /**
     * Select all records from the table that matches the specified column name-value pairs
	 * with a given conditional operator (AND or OR) 
     * @param strTableName the name of the table to select from
     * @param htblColNameValue the column name-value pairs to which records will be compared
     * @param strOperator the conditional operator to be exectuted ("AND, "OR" only)
     * @return an iterator pointing to the first record in the result set
     * @throws IOException If an I/O error occurred
     * @throws FileNotFoundException If an error occurred in the stored table file
	 * @throws ClassNotFoundException If an error occurred in the stored table pages format
     * @throws DBEngineException if the table does not exist
     */
    public Iterator<Record> selectFromTable(String strTableName,  Hashtable<String,Object> htblColNameValue, 
    		String strOperator) throws FileNotFoundException, ClassNotFoundException, IOException, DBEngineException{
    	Table table = getTable(strTableName);
    	if(table == null)
    		throw new DBEngineException("Table \""+strTableName+"\" does not exist");
    	Iterator<Record> itr = table.select(htblColNameValue, strOperator);
    	return itr;
    }

    /**
     * Print the result set of a select query
     * @param itr the iterator that points to the first record in the result set
     * @param strTableName the table name from which the result set is selected
     * @throws IOException If an I/O error occurred 
     * @throws ClassNotFoundException  If an error occurred in the stored table file 
     * @throws FileNotFoundException If an error occurred in the stored table file
     */
    public void printResult(Iterator<Record> itr, String strTableName) throws FileNotFoundException, ClassNotFoundException, IOException
    {
    	if(!itr.hasNext())
    		System.out.println("No records exist");
    	else{
    	System.out.println(getTable(strTableName).getTableHeader());
    	while(itr.hasNext()) {
    		System.out.println(itr.next());
    	}
    }
    }

}

class DBEngineException extends Exception {

	/**
	 * Any errors related to the DB engine can be detected using these exceptions
	 */
	private static final long serialVersionUID = 1L;
	
	public DBEngineException(String string) { super(string); }

}
