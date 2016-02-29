package BPTree;



public class nodeEntry {
	private Object key;
	private Object ref;
	
	public nodeEntry(Object key , Object ref)
	{
		this.key = key;
		this.ref = ref;
	}
	
	public Object getKey()
	{
		return this.key;
	}
	
	public Object getRef()
	{
		return this.ref;
	}
}
