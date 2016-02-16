package db;

public class Record {


	private Object[] values;
		
	public Record(int size)
	{
		
		values = new Object[size];
	}
	
	public void addValue(int index , Object value)
	{
		values[index] = value;
	}
	
	public Object[] getValues()
	{
		return values;
	}
	
	
	
}
