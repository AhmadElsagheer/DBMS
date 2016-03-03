package BPTree;

public abstract class BPTreeNode<T extends Comparable<T>> {

	protected Comparable<T>[] keys;
	protected int numberOfKeys;
	protected int order;
	private boolean isRoot;

	public BPTreeNode(int order) 
	{
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

	public void setKey(int index, Comparable<T> comparable) 
	{
		keys[index] = comparable;
	}

	public boolean ifFull() 
	{
		return numberOfKeys == order;
	}
	
	public Comparable<T> getLastKey()
	{
		return keys[numberOfKeys];
	}
	
	public Comparable<T> getFirstKey()
	{
		return keys[0];
	}
	
	/**
	 * for a leaf Node : Search a key on current node, if found the key then
	 * return its position, otherwise return -1, for an Inner Node : return the
	 * child node index which should contain the key.
	 */
	public abstract int search(T key);

	public abstract BPTreeNode<T> split();
	
	public abstract boolean delete(T key, BPTreeInnerNode<T> parent, int ptr);
	
	public abstract void deleteAt(int index);
	
	public abstract int minKeys();

}
