package BPTree;

public class BPTree {

	public int n;
	private int NonLeafMinptrs;
	private int NonLeafMinKeys;
	private int LeafMinPtrs;
	private int LeafMinKeys;
	private BPTreeLeafNode root;

	public BPTree(int n) {
		this.n = n;
		NonLeafMinptrs = (int) Math.ceil((n + 1) / 2);
		NonLeafMinKeys = NonLeafMinptrs - 1;
		LeafMinPtrs = (int) Math.floor((n + 1) / 2);
		LeafMinKeys = LeafMinPtrs - 1;
		this.root = new BPTreeLeafNode(n);
	}

	
	/**
	 * Insert a new key and its associated value into the B+ tree.
	 */
	public void insert(Object key, Object value) {
		// TODO 
	}
	
	
	/**
	 * Delete a key and its associated value from the tree.
	 */
	public void delete(Object key){
		//TODO
	}
	
	/**
	 * Search a key value on the tree and return its associated value.
	 */
	public Object search(Object key)
	{
		//TODO
		return null;
	}
	
	
	
	
	

}
