package BPTree;

import java.util.Stack;

public class BPTreeLeafNode<T extends Comparable<T>> extends BPTreeNode<T> {

	Ref[] records;
	private BPTreeLeafNode<T> next;
	
	@SuppressWarnings("unchecked")
	public BPTreeLeafNode(int n) 
	{
		super(n);
		keys = new Comparable[n];
		records = new Ref[n];

	}

	public Ref getValue(int index) 
	{
		return records[index];
	}

	public void setValue(int index, Ref recordReference) 
	{
		records[index] = recordReference;
	}

	public void insertKey(T key, Ref recordReference) 
	{
		int index = 0;
		while (index < numberOfKeys && getKey(index).compareTo(key) < 0)
			++index;
		insertAt(index, key, recordReference);
	}

	private void insertAt(int index, T key, Ref recordReference) 
	{
		for (int i = numberOfKeys - 1; i >= index; --i) 
		{
			setKey(i + 1, getKey(i));
			setValue(i + 1, getValue(i));
		}

		// insert new key and value
		setKey(index, key);
		setValue(index, recordReference);
		++numberOfKeys;
	}

	public boolean delete(T key) 
	{
		// TODO
		return false;
	}

	private void deleteAt(int index) 
	{
		// TODO
	}

	@Override
	/**
	 * On splitting a leaf node, the middle key is kept on the new node and
	 * pushed to the parent node.
	 */
	public BPTreeNode<T> split() 
	{
		int midIndex = numberOfKeys / 2;
		
		BPTreeLeafNode<T> newRNode = new BPTreeLeafNode<T>(ORDER);
		for (int i = midIndex; i < numberOfKeys; ++i) {
			newRNode.setKey(i - midIndex, this.getKey(i));
			newRNode.setValue(i - midIndex, this.getValue(i));
			this.setKey(i, null);
			this.setValue(i, null);
		}
		newRNode.numberOfKeys = numberOfKeys - midIndex;
		numberOfKeys = midIndex;
		
		return newRNode;
	}

	@Override
	public int search(T key) 
	{
		for (int i = 0; i < numberOfKeys; ++i) 
		{
			int cmp = getKey(i).compareTo(key);
			if (cmp == 0) 
				return i;
			else 
				if (cmp > 0)
					break;
		}
		return -1;
	}

	@Override
	/**
	 * This method is not being used here because we never push up a key to a leaf node !.
	 */
	protected BPTreeNode<T> pushUpKey(T key, BPTreeNode<T> leftChild, BPTreeNode<T> rightNode,
			Stack<BPTreeNode<T>> recStack) {
		
		return null;
	}

	


}
