package BPTree;

import java.util.Stack;

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

	/**
	 * Tell me whether the node is in overflow mode or not .
	 * @return
	 */
	public boolean ifFull() 
	{
		return numberOfKeys == ORDER;
	}
	
	/**
	 * Helper method to handle overflow
	 * split the overflow node and go to the parent to adjust pointers and so on until no overflow occurs 
	 * @param recStack : contains parents .
	 * @return
	 */
	public BPTreeNode<T> dealOverflow(Stack<BPTreeNode<T>>recStack) {
		int midIndex = this.numberOfKeys / 2;
		BPTreeNode<T> parent = recStack.pop();
		// key to be pushed up to the root .
		T upKey = (T) getKey(midIndex);
		
		BPTreeNode<T> newRNode = this.split();
				
		if (parent == null)    
			parent = (BPTreeInnerNode<T>) new BPTreeInnerNode<T>(ORDER);
		

		// push up a key to parent internal node
		return parent.pushUpKey(upKey, this, newRNode , recStack);
	}
	
	protected abstract BPTreeNode<T> pushUpKey(T key, BPTreeNode<T> leftChild, BPTreeNode<T> rightNode , Stack<BPTreeNode<T>>recStack);

	
	/**
	 * for a leaf Node : Search a key on current node, if found the key then
	 * return its position, otherwise return -1, for an Inner Node : return the
	 * child node index which should contain the key.
	 */
	public abstract int search(T key);

	public abstract BPTreeNode<T> split();

}
