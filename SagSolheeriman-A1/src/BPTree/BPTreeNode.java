package BPTree;

public abstract class BPTreeNode<T extends Comparable<T>> {
	
	protected Comparable<T>[] keys;
	protected int numberOfKeys;
	protected int order;
	protected int index;		//for printing the tree
	private boolean isRoot;
	private static int nextIdx = 0;

	public BPTreeNode(int order) 
	{
		index = nextIdx++;
		numberOfKeys = 0;
		this.order = order;
	}
	public boolean isRoot()
	{
		return isRoot;
	}
	
	public void setRoot(boolean isRoot)
	{
		this.isRoot = isRoot;
	}

	public Comparable<T> getKey(int index) 
	{
		return keys[index];
	}

	public void setKey(int index, Comparable<T> key) 
	{
		keys[index] = key;
	}

	public boolean isFull() 
	{
		return numberOfKeys == order;
	}
	
	public Comparable<T> getLastKey()
	{
		return keys[numberOfKeys-1];
	}
	
	public Comparable<T> getFirstKey()
	{
		return keys[0];
	}
		
	public abstract int minKeys();

	public abstract PushUp<T> insert(T key, Ref recordReference, BPTreeInnerNode<T> parent, int ptr);
	
	public abstract Ref search(T key);

	public abstract boolean delete(T key, BPTreeInnerNode<T> parent, int ptr);
	
	public String toString()
	{		
		String s = "(" + index + ")";

		s += "[";
		for (int i = 0; i < order; i++)
		{
			String key = " ";
			if(i < numberOfKeys)
				key = keys[i].toString();
			
			s+= key;
			if(i < order - 1)
				s += "|";
		}
		s += "]";
		return s;
	}

}
