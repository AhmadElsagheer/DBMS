package BPTree;

import java.util.Stack;

public class BPTreeInnerNode<T extends Comparable<T>> extends BPTreeNode<T> 
{
	protected BPTreeNode<T>[] children;

	@SuppressWarnings("unchecked")
	public BPTreeInnerNode(int n) 
	{
		super(n);
		keys = new Comparable[n];
		children = new BPTreeNode[n + 1];
	}

	public BPTreeNode<T> getChild(int index) 
	{
		return children[index];
	}

	public void setChild(int index, BPTreeNode<T> child) 
	{
		children[index] = child;
	}

	/**
	 * insert a key at specified index , adjust the pointers right and left to
	 * that key
	 */
	private void insertAt(int index, T key, BPTreeNode<T> leftChild, BPTreeNode<T> rightChild) 
	{
		// move space for the new key
		for (int i = numberOfKeys + 1; i > index; --i) 
			setChild(i, this.getChild(i - 1));
		
		for (int i = numberOfKeys; i > index; --i)
			setKey(i, this.getKey(i - 1));
		
		
		// insert the new key and adjust pointers .
		setKey(index, key);
		setChild(index, leftChild);
		setChild(index + 1, rightChild);
		numberOfKeys += 1;
	}

	/**
	 * Do linear search to find the position of a key .
	 */
	public int search(T key) 
	{
		int index = 0;
		for (index = 0; index < numberOfKeys; ++index) 
		{
			int cmp = getKey(index).compareTo(key);
			if (cmp == 0)
				return index + 1;
			else 
				if (cmp > 0)
					return index;
		}
		return index;
	}

	@Override
	/**
	 * split : create a new Inner Node , adjust keys and children between the
	 * splitted and new node When splits an Inner node, the middle key is pushed
	 * to parent node.
	 */
	public BPTreeNode split() {
		int midIndex = numberOfKeys/ 2;
		
		BPTreeInnerNode<T> newRNode = new BPTreeInnerNode<T>(ORDER);
		for (int i = midIndex + 1; i <  numberOfKeys; ++i) {
		//transfer nodes to the new node 
			newRNode.setKey(i - midIndex - 1, getKey(i));
			this.setKey(i, null);
		}
		for (int i = midIndex + 1; i <= numberOfKeys; ++i) {
			//adjust pointers
			newRNode.setChild(i - midIndex - 1, getChild(i));
//			newRNode.getChild(i - midIndex - 1).setParent(newRNode);
			this.setChild(i, null);
		}
		setKey(midIndex, null);
		newRNode.numberOfKeys = this.numberOfKeys - midIndex - 1;
		numberOfKeys = midIndex;
		
		return newRNode;
	
	}

	@Override
	/**
	 * This method takes key and 2 nodes , old and new created one to insert a new navigation key with adjusting pointers
	 * recStack : containing parents.
	 */
	protected BPTreeNode<T> pushUpKey(T key, BPTreeNode<T> leftChild, BPTreeNode<T> rightNode , Stack<BPTreeNode<T>> recStack) {
		// find the target position of the new key
				int index = this.search(key);
				
				// insert the new key
				this.insertAt(index, key, leftChild, rightNode);
				
				// check whether current node need to be split
				if (this.ifFull()) 
					return this.dealOverflow(recStack);
				else
				{
					// No parent for me so I am the root . Check in the calling method to adjust the root of the tree .
					if(recStack.isEmpty())
						return this;
					else
						return null;
				}
}
}
