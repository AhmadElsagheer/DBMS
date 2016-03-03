package BPTree;

public class BPTree<T extends Comparable<T>> {

	private int order;
	private BPTreeNode<T> root;
	
	/**
	 * Creates an empty B+ tree
	 * @param order the maximum number of keys in the nodes of the tree
	 */
	public BPTree(int order) 
	{
		this.order = order;
		root = new BPTreeLeafNode<T>(this.order);
		root.setRoot(true);
	}
	
	/**
	 * Inserts the specified key associated with the given record in the B+ tree
	 * @param key the key to be inserted
	 * @param recordReference the reference of the record associated with the key
	 */
	public void insert(T key, Ref recordReference)
	{
		PushUp<T> pushUp = root.insert(key, recordReference, null, -1);
		if(pushUp != null)
		{
			BPTreeInnerNode<T> newRoot = new BPTreeInnerNode<T>(order);
			newRoot.insertLeftAt(0, pushUp.key, root);
			newRoot.setChild(1, pushUp.newNode);
			root.setRoot(false);
			root = newRoot;
			root.setRoot(true);
		}
	}
	
	
	/**
	 * Looks up for the record that is associated with the specified key
	 * @param key the key to find its record
	 * @return the reference of the record associated with this key 
	 */
	public Ref search(T key)
	{
		return root.search(key);
	}
	
	/**
	 * Delete a key and its associated record from the tree.
	 * @param key the key to be deleted
	 * @return a boolean to indicate whether the key is successfully deleted or it was not in the tree
	 */
	public boolean delete(T key)
	{
		boolean done = root.delete(key, null, -1);
		//go down and find the new root in case the old root is deleted
		while(root instanceof BPTreeInnerNode && !root.isRoot())
			root = ((BPTreeInnerNode<T>) root).getFirstChild();
		return done;
	}
	
	/**
	 * Returns a string representation of the B+ tree.
	 */
	public String toString()
	{	
		String s = "";
		BPTreeNode<T> curNode = this.root;
		while(curNode instanceof BPTreeInnerNode) 
			curNode = ((BPTreeInnerNode<T>) curNode).getFirstChild();
		
		BPTreeLeafNode<T> curLeaf = (BPTreeLeafNode<T>) curNode;
	
		while(curLeaf != null)
		{
			s += "[";
			for (int i = 0; i < order; i++)
			{
				String key = " ";
				if(i < curLeaf.numberOfKeys)
					key = curLeaf.keys[i].toString();
				s+= key;
				if(i < order - 1)
					 s += "|";
			}
			s += "]";
			if((curLeaf = curLeaf.getNext()) != null)
				s += " -> ";
		}	
		return s;
	}
}
