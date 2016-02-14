package db;
import java.io.*;
import java.util.*;

// Additional Reference:
// http://www.tutorialspoint.com/java/java_serialization.htm

class Test implements Serializable
{
	String [] data;
	int index;
	public Test()
	{
		data = new String [10];
		index = 0;
	}

	public void add(String val)
	{
		data [index] = val;
		index++;
	}

	public String toString()
	{
		String res = "";
		for(int i=0;i<index;i++)
		{
			res+=data[i]+'\n';
		}
		return res;
	}
}



public class Main
{
    public static void main( String [] args ) throws Exception
    {
		for(int j=0;j<10;j++)
		{
        	Test t = new Test();

			for(int i=0;i<10;i++)
			{
				t.add("T "+j+i);
			}

			System.out.println(t);


        	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("t"+j+".class")));
       		oos.writeObject(t);
        	oos.close();
		}


        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("t5.class")));
        Test t = (Test)ois.readObject();
        ois.close();

        System.out.println(t);

    }
}