package db;

public class Record {


	private Object[] values;
		
	public Record(int size)
	{
		values = new Object[size];
	}
	
	public void addValue(int idx, Object val)
	{
		values[idx] = val;
	}
	
	public Object[] getValues()
	{
		return values;
	}
	
	
}
