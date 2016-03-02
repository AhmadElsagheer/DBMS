package BPTree;

public class BPTree<T> {

	private int n;
	private int NonLeafMinptrs;
	private int NonLeafMinKeys;
	private int LeafMinPtrs;
	private int LeafMinKeys;
	private BPTreeLeafNode root;

	public BPTree(int n) 
	{
		this.n = n;
		NonLeafMinptrs = (n + 2) / 2;
		NonLeafMinKeys = NonLeafMinptrs - 1;
		LeafMinPtrs = LeafMinKeys = (n + 1) / 2;
		
		this.root = new BPTreeLeafNode(n);
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
	public void delete(T key)
	{
		//TODO
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
