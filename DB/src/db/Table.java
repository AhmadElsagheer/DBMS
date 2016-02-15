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
//		maxTuplesPerPage = calculatePageSize();
		curPageIndex = -1;
		createDirectory();
		createPage();
	}
	
//	private int calculatePageSize()
//	{
//		
//	}
	
		
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
