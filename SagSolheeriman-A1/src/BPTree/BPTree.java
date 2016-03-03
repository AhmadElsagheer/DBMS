package BPTree;

public class BPTree<T extends Comparable<T>> {

	private int order;
	private BPTreeNode<T> root;

	public BPTree(int order) 
	{
		this.order = order;
		root = new BPTreeLeafNode<T>(this.order);
		root.setRoot(true);
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
		boolean done = root.delete(key, null, -1);
		findRoot();
		return done;
	}
	
	/**
	 * Search a key value on the tree and return its associated value.
	 */
	public Object search(T key)
	{
		//TODO
		return null;
	}
	
	public void findRoot()
	{
		while(root instanceof BPTreeInnerNode && !root.isRoot())
			root = ((BPTreeInnerNode<T>) root).getFirstChild();
	}
	
}
