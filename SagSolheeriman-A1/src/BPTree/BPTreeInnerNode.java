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
	
	public int minKeys()
	{
		if(this.isRoot())
			return 1;
		return (order + 2) / 2 - 1;
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
	private void insertAt(int index, Comparable<T> key) 
	{
		// move space for the new key
		for (int i = numberOfKeys + 1; i > index; --i) 
			setChild(i, this.getChild(i - 1));
		
		for (int i = numberOfKeys; i > index; --i)
			setKey(i, this.getKey(i - 1));
		
		
		// insert the new key and adjust pointers .
		this.setKey(index, key);
		
		
		numberOfKeys += 1;
	}
	
	public void insertLeftAt(int index, Comparable<T> key, BPTreeNode<T> leftChild) 
	{
		insertAt(index, key);
		this.setChild(index, leftChild);
	}
	
	public void insertRightAt(int index, Comparable<T> key, BPTreeNode<T> rightChild)
	{
		insertAt(index, key);
		this.setChild(index + 1, rightChild);
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
		boolean done = false;
		for(int i = 0; !done && i < numberOfKeys; ++i)
			if(keys[i].compareTo(key) > 0)
			{
				done = children[i].delete(key, this, i);
			}
		if(!done)
			done = children[numberOfKeys].delete(key, this, numberOfKeys);
		if(numberOfKeys < this.minKeys())
		{
			if(isRoot())
			{
				this.getFirstChild().setRoot(true);
				this.setRoot(false);
				return done;
			}
			//1.try to borrow
			if(borrow(parent, ptr))
				return done;
			//2.merge
			merge(parent, ptr);
		}
		return done;
	}
	
	public boolean borrow(BPTreeInnerNode<T> parent, int ptr)
	{
		//check left sibling
		if(ptr > 0)
		{
			BPTreeInnerNode<T> leftSibling = (BPTreeInnerNode<T>) parent.getChild(ptr-1);
			if(leftSibling.numberOfKeys > leftSibling.minKeys())
			{
				this.insertLeftAt(0, parent.getKey(ptr-1), leftSibling.getLastChild());
				parent.deleteAt(ptr-1);
				parent.insertRightAt(ptr-1, leftSibling.getLastKey(), this);
				leftSibling.deleteAt(leftSibling.numberOfKeys - 1);
				return true;
			}
		}
		
		//check right sibling
		if(ptr < parent.numberOfKeys)
		{
			BPTreeInnerNode<T> rightSibling = (BPTreeInnerNode<T>) parent.getChild(ptr);
			if(rightSibling.numberOfKeys > rightSibling.minKeys())
			{
				this.insertRightAt(0, parent.getKey(ptr), rightSibling.getFirstChild());
				parent.deleteAt(ptr);
				parent.insertRightAt(ptr, rightSibling.getLastKey(), rightSibling);
				rightSibling.deleteAt(0);
				return true;
			}
		}
		return false;
	}
	
	public void merge(BPTreeInnerNode<T> parent, int ptr)
	{
		if(ptr > 0)
		{
			//merge with left
			BPTreeInnerNode<T> leftSibling = (BPTreeInnerNode<T>) parent.getChild(ptr-1);
			leftSibling.merge(parent.getKey(ptr-1), this);
			parent.deleteAt(ptr-1);			
		}
		else
		{
			//merge with right
			BPTreeInnerNode<T> rightSibling = (BPTreeInnerNode<T>) parent.getChild(ptr);
			this.merge(parent.getKey(ptr), rightSibling);
			parent.deleteAt(ptr);
		}
	}
	
	public void merge(Comparable<T> parentKey, BPTreeInnerNode<T> foreignNode)
	{
		this.insertRightAt(numberOfKeys, parentKey, foreignNode.getFirstChild());
		for(int i = 0; i < foreignNode.numberOfKeys; ++i)
			this.insertRightAt(numberOfKeys, foreignNode.getKey(i), foreignNode.getChild(i+1));
	}
	
	public void deleteAt(int index)
	{
		for(int i = index; i < numberOfKeys - 1; ++i)
		{
			keys[i] = keys[i+1];
			children[i+1] = children[i+2];
		}
		numberOfKeys--;
	}
	
	public BPTreeNode<T> getLastChild()
	{
		return children[numberOfKeys];
	}
	
	public BPTreeNode<T> getFirstChild()
	{
		return children[numberOfKeys];
	}

}
