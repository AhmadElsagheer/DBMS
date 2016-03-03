package BPTree;

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

	
	/**
	 * split : create a new Inner Node , adjust keys and children between the
	 * splitted and new node When splits an Inner node, the middle key is pushed
	 * to parent node.
	 */
	@Override
	public BPTreeNode<T> split() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean delete(T key, BPTreeInnerNode<T> parent, int ptr) 
	{
		for(int i = 0; i < numberOfKeys; ++i)
			if(keys[i].compareTo(key) > 0)
				return children[i].delete(key, this, i);
		return children[numberOfKeys].delete(key, this, numberOfKeys);
	}
	
	public void deleteAt(int index)
	{
		
	}

}
