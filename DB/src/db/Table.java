package db;

import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;

public class Table implements Serializable{

	private String path;
	private int maxTuplesPerPage;
	private int curPageIndex;
	private int curPageTuples;
	
	private String name, primaryKey;
	private Hashtable<String, String> colTypes, colRefs;
	
	public Table(String path, String strTableName, Hashtable<String,String> htblColNameType, 
            Hashtable<String,String> htblColNameRefs, String strKeyColName){
		
		this.path = path + "/" + strTableName + "/";
		name = strTableName;
		primaryKey = strKeyColName;
		colTypes = htblColNameType;
		colRefs = htblColNameRefs;
		maxTuplesPerPage = calculatePageSize();
		curPageIndex = -1;
		createDirectory();
		createPage();
	}
	
	private int calculatePageSize()
	{
		int tupleSize = 0;
		for(String type : colTypes.values())
			tupleSize += getTypeSize(type);
		return 64000 / tupleSize;
	}
	
	private int getTypeSize(String type)
	{
		if(type.equals("Integer"))
			return 32;
		if(type.equals("Double"))
			return 64;
		if(type.equals("String"))
			return 255 * 8;
		if(type.equals("Boolean"))
			return 9 * 8;
		if(type.equals("Date"))
			return 12 * 8;
		return -1; 	//invalid type
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
		
	}
	
	
	
	
}
