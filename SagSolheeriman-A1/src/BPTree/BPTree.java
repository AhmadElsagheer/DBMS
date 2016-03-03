package BPTree;

public class BPTree<T extends Comparable<T>> {

	private int n;
	private int NonLeafMinptrs;
	private int NonLeafMinKeys;
	private int LeafMinPtrs;
	private int LeafMinKeys;
	private BPTreeInnerNode<T> root;

	public BPTree(int n) 
	{
		this.n = n;
		NonLeafMinptrs = (n + 2) / 2;
		NonLeafMinKeys = NonLeafMinptrs - 1;
		LeafMinPtrs = LeafMinKeys = (n + 1) / 2;
		root = new BPTreeInnerNode<T>(n);
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
		return root.delete(key);
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
