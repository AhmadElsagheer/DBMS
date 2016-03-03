package BPTree;

public class BPTree<T extends Comparable<T>> {

	private int order;
	private BPTreeInnerNode<T> root;

	public BPTree(int order) 
	{
		this.order = order;
		root = new BPTreeInnerNode<T>(this.order);
	}

	
	/**
	 * Insert a new key and its associated value into the B+ tree.
	 */
	public void insert(T key, Ref recordReference) 
	{
		// TODO 
	}
	
	
	/**
	 * Delete a key and its associated value from the tree.
	 */
	public boolean delete(T key)
	{
		return root.delete(key, null, -1);
	}
	
	/**
	 * Search a key value on the tree and return its associated value.
	 */
	public Object search(T key)
	{
		//TODO
		return null;
	}
	
}
