package BPTree;

public abstract class BPTreeNode<T extends Comparable<T>> {

	protected Comparable<T>[] keys;
	protected int numberOfKeys;
	protected int ORDER;

	public BPTreeNode(int n) 
	{
		numberOfKeys = 0;
		ORDER = n;
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
		return numberOfKeys == ORDER;
	}
	
	/**
	 * for a leaf Node : Search a key on current node, if found the key then
	 * return its position, otherwise return -1, for an Inner Node : return the
	 * child node index which should contain the key.
	 */
	public abstract int search(T key);

	public abstract BPTreeNode<T> split();
	
	public abstract boolean delete(T key);

}
