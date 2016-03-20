package sagSolheeriman;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

/**
 * Test file:
 * Use this java file to test any functionality
 * <p>
 * Available Commands (check docs for the signature):-
 * <br> new DBApp(): start session, all other commands relies on this object
 * <br> init/1: create a database (drop old database with the same name, if any)
 * <br> use/1: switch to the database with the specified name
 * <br> createTable/4: create a new table
 * <br> insertIntoTable/2: insert a record in a table
 * <br> selectFromTable/3: select records from table that match some conditions
 * @author Sagheer, Soliman
 */

public class DBAppTest {

	private static final Random rand = new Random();
	
	public static void main(String [] args) throws Exception 
	{
		
//		1. Start a session
		DBApp myDB = new DBApp();
		tests(myDB);
//		testUpdate(myDB);
//		customize tests here...	
	}
	
	public static void tests(DBApp myDB) throws Exception
	{
		
//		2. Create a new database
		myDB.init("University", 200, 150);

//		3. Create table "Faculty"
		Hashtable<String, String> fTblColNameType = new Hashtable<String, String>();
		fTblColNameType.put("ID", "Integer");
		fTblColNameType.put("Name", "String");
		Hashtable<String, String> fTblColNameRefs = new Hashtable<String, String>();
		myDB.createTable("Faculty", fTblColNameType, fTblColNameRefs, "ID");

//		4. Create table "Major"
		Hashtable<String, String> mTblColNameType = new Hashtable<String, String>();
		mTblColNameType.put("ID", "Integer");
		mTblColNameType.put("Name", "String");
		mTblColNameType.put("Faculty_ID", "Integer");
		Hashtable<String, String> mTblColNameRefs = new Hashtable<String, String>();
		mTblColNameRefs.put("Faculty_ID", "Faculty.ID");
		myDB.createTable("Major", mTblColNameType, mTblColNameRefs, "ID");

//		5. Create table "Course"
		Hashtable<String, String> coTblColNameType = new Hashtable<String, String>();
		coTblColNameType.put("ID", "Integer");
		coTblColNameType.put("Name", "String");
		coTblColNameType.put("Code", "String");
		coTblColNameType.put("Hours", "Integer");
		coTblColNameType.put("Semester", "Integer");
		coTblColNameType.put("Major_ID", "Integer");
		Hashtable<String, String> coTblColNameRefs = new Hashtable<String, String>();
		coTblColNameRefs.put("Major_ID", "Major.ID");
		myDB.createTable("Course", coTblColNameType, coTblColNameRefs, "ID");

//		6. Create table "Student"
		Hashtable<String, String> stTblColNameType = new Hashtable<String, String>();
		stTblColNameType.put("ID", "Integer");
		stTblColNameType.put("First_Name", "String");
		stTblColNameType.put("Last_Name", "String");
		stTblColNameType.put("GPA", "Double");
		stTblColNameType.put("Age", "Integer");
		Hashtable<String, String> stTblColNameRefs = new Hashtable<String, String>();
		myDB.createTable("Student", stTblColNameType, stTblColNameRefs, "ID");

//		7. Create table "Student in Course"
		Hashtable<String, String> scTblColNameType = new Hashtable<String, String>();
		scTblColNameType.put("ID", "Integer");
		scTblColNameType.put("Student_ID", "Integer");
		scTblColNameType.put("Course_ID", "Integer");
		Hashtable<String, String> scTblColNameRefs = new Hashtable<String, String>();
		scTblColNameRefs.put("Student_ID", "Student.ID");
		scTblColNameRefs.put("Course_ID", "Course.ID");
		myDB.createTable("Student_in_Course", scTblColNameType, scTblColNameRefs, "ID");

////		8. Insert in table "Faculty"
//		Hashtable<String,Object> ftblColNameValue1 = new Hashtable<String,Object>();
//		ftblColNameValue1.put("ID", 1);
//		ftblColNameValue1.put("Name", "Media Engineering and Technology");
//		myDB.insertIntoTable("Faculty", ftblColNameValue1);
//
//		Hashtable<String,Object> ftblColNameValue2 = new Hashtable<String,Object>();
//		ftblColNameValue2.put("ID", 2);
//		ftblColNameValue2.put("Name", "Management Technology");
//		myDB.insertIntoTable("Faculty", ftblColNameValue2);
//
//		for(int i=1;i<1000;i++)
//		{
//			Hashtable<String,Object> ftblColNameValueI = new Hashtable<String,Object>();
//			ftblColNameValueI.put("ID", i+2);
//			ftblColNameValueI.put("Name", "f"+(i+2));
//			myDB.insertIntoTable("Faculty", ftblColNameValueI);
//		}
//
////		9. Insert in table "Major"
//		Hashtable<String,Object> mtblColNameValue1 = new Hashtable<String,Object>();
//		mtblColNameValue1.put("ID", 1);
//		mtblColNameValue1.put("Name", "Computer Science & Engineering");
//		mtblColNameValue1.put("Faculty_ID", 1);
//		myDB.insertIntoTable("Major", mtblColNameValue1);
//
//		Hashtable<String,Object> mtblColNameValue2 = new Hashtable<String,Object>();
//		mtblColNameValue2.put("ID", 2);
//		mtblColNameValue2.put("Name", "Business Informatics");
//		mtblColNameValue2.put("Faculty_ID", 2);
//		myDB.insertIntoTable("Major", mtblColNameValue2);
//
//		for(int i=1;i<1000;i++)
//		{
//			Hashtable<String,Object> mtblColNameValueI = new Hashtable<String,Object>();
//			mtblColNameValueI.put("ID", i+2);
//			mtblColNameValueI.put("Name", "m"+(i+2));
//			mtblColNameValueI.put("Faculty_ID", rand.nextInt(1001) + 1);
//			myDB.insertIntoTable("Major", mtblColNameValueI);
//		}
//
//
////		10. Insert in table "Course"
//		Hashtable<String,Object> ctblColNameValue1 = new Hashtable<String,Object>();
//		ctblColNameValue1.put("ID", Integer.valueOf( "1" ) );
//		ctblColNameValue1.put("Name", "Data Bases II");
//		ctblColNameValue1.put("Code", "CSEN 604");
//		ctblColNameValue1.put("Hours", rand.nextInt(7) + 2);
//		ctblColNameValue1.put("Semester", rand.nextInt(10) + 1);
//		ctblColNameValue1.put("Major_ID", rand.nextInt(1001) + 1);		
//		myDB.insertIntoTable("Course", ctblColNameValue1);
//
//		for(int i=1;i<1000;i++)
//		{
//			Hashtable<String,Object> ctblColNameValueI = new Hashtable<String,Object>();
//			ctblColNameValueI.put("ID", i+2);
//			ctblColNameValueI.put("Name", "c"+(i+2));
//			ctblColNameValueI.put("Code", "co "+(i+2));
//			ctblColNameValueI.put("Hours", rand.nextInt(7) + 2);
//			ctblColNameValueI.put("Semester", rand.nextInt(10) + 1);
//			ctblColNameValueI.put("Major_ID", rand.nextInt(1001) + 1);
//			myDB.insertIntoTable("Course", ctblColNameValueI);
//		}

//		11. Insert in table "Student"
		for(int i=0;i<1000;i++)
		{
			Hashtable<String,Object> sttblColNameValueI = new Hashtable<String,Object>();
			sttblColNameValueI.put("ID", i);
			sttblColNameValueI.put("First_Name", "FN"+i);
			sttblColNameValueI.put("Last_Name", "LN"+i);
			sttblColNameValueI.put("GPA", (rand.nextInt(44) + 7) / 10.0) ;
			sttblColNameValueI.put("Age", rand.nextInt(5) + 18);
			myDB.insertIntoTable("Student", sttblColNameValueI);
		}
		System.out.println("---------------------------------------------------------");
		
//		12. Select with one condition
		Hashtable<String,Object> stblColNameValue = new Hashtable<String,Object>();
		stblColNameValue.put("Age", rand.nextInt(5) + 18);
		long startTime = System.currentTimeMillis();
		Iterator<Record> myIt = myDB.selectFromTable("Student", stblColNameValue,"AND");
		long endTime   = System.currentTimeMillis();
		System.out.printf("Time for query = %d ms\n", endTime - startTime);
		myDB.printResult(myIt, "Student");
		System.out.println("---------------------------------------------------------");
		

//		12. Select with one condition (using index)
		myDB.createIndex("Student", "Age");
		stblColNameValue = new Hashtable<String,Object>();
		stblColNameValue.put("Age", rand.nextInt(5) + 18);
		startTime = System.currentTimeMillis();
		myIt = myDB.selectFromTable("Student", stblColNameValue,"AND");
		endTime   = System.currentTimeMillis();
		System.out.printf("Time for query = %d ms\n", endTime - startTime);
		myDB.printResult(myIt, "Student");
		System.out.println("---------------------------------------------------------");

//		13. Select with two ANDed conditions
		Hashtable<String,Object> stblColNameValue4 = new Hashtable<String,Object>();
		stblColNameValue4.put("Hours", rand.nextInt(7) + 2);
		stblColNameValue4.put("Semester", rand.nextInt(10) + 1);
		startTime   = System.currentTimeMillis();
		Iterator<Record> myIt3 = myDB.selectFromTable("Course", stblColNameValue4,"AND");
		endTime   = System.currentTimeMillis();
		System.out.printf("Time for query = %d ms\n", endTime - startTime);
		myDB.printResult(myIt3, "Course");
		System.out.println("---------------------------------------------------------");

//		14. Select with two ORed conditions
		startTime   = System.currentTimeMillis();
		Iterator<Record> myIt4 = myDB.selectFromTable("Course", stblColNameValue4,"OR");
		endTime   = System.currentTimeMillis();
		System.out.printf("Time for query = %d ms\n", endTime - startTime);
		myDB.printResult(myIt4, "Course");

	}
	
	
	public static void testUpdate(DBApp myDB) throws Exception
	{

		myDB.printResult(myDB.selectFromTable("Major", new Hashtable<String, Object>(), ""), "Major");
		Hashtable<String, Object> hh = new Hashtable<String, Object>();
		hh.put("Name", "m100");
		hh.put("Faculty_ID", 500);
		myDB.updateTable("Major", 7, hh);
		myDB.printResult(myDB.selectFromTable("Major", new Hashtable<String, Object>(), ""), "Major");
		
	}
	

	public static void testDelete(DBApp myDB) throws IOException, DBEngineException, ClassNotFoundException
	{		
//		Select the record to be deleted from table "Faculty"
		Hashtable<String,Object> ftblColNameValue4 = new Hashtable<String,Object>();
		ftblColNameValue4.put("Name", "Management Technology");
		myDB.printResult(myDB.selectFromTable("Faculty", ftblColNameValue4, "AND"), "Faculty");
		
//		Delete from table "Faculty" :
		Hashtable<String,Object> ftblColNameValue3 = new Hashtable<String,Object>();
		ftblColNameValue3.put("Name", "Management Technology");
		myDB.deleteFromTable("Faculty", ftblColNameValue3, "AND");
		
//		Select from table "Faculty" the deleted record :
		myDB.printResult(myDB.selectFromTable("Faculty", ftblColNameValue4, "AND"), "Faculty");
	}

}