package BPTree;

public class BPTreeInnerNode<T extends Comparable<T>> extends BPTreeNode<T> 
{
	private BPTreeNode<T>[] children;

	@SuppressWarnings("unchecked")
	public BPTreeInnerNode(int n) 
	{
		super(n);
		keys = new Comparable[n];
		children = new BPTreeNode[n+1];
	}


	public BPTreeNode<T> getChild(int index) 
	{
		return children[index];
	}

	public void setChild(int index, BPTreeNode<T> child) 
	{
		children[index] = child;
	}
	
	public BPTreeNode<T> getFirstChild()
	{
		return children[0];
	}

	public BPTreeNode<T> getLastChild()
	{
		return children[numberOfKeys];
	}

	public int minKeys()
	{
		if(this.isRoot())
			return 1;
		return (order + 2) / 2 - 1;
	}

	public PushUp<T> insert(T key, Ref recordReference, BPTreeInnerNode<T> parent, int ptr)
	{
		int index = findIndex(key);
		PushUp<T> pushUp = children[index].insert(key, recordReference, this, index);
		
		if(pushUp == null)
			return null;
		
		if(this.isFull())
		{
			BPTreeNode<T> newNode = this.split(pushUp);
			Comparable<T> newKey = newNode.getFirstKey();
			return new PushUp<T>(newNode, newKey);
		}
		else
		{
			index = 0;
			while (index < numberOfKeys && getKey(index).compareTo(key) < 0)
				++index;
			this.insertRightAt(index, pushUp.key, pushUp.newNode);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public BPTreeNode<T> split(PushUp<T> pushup) 
	{
		int keyIndex = this.findIndex((T)pushup.key);
		int midIndex = numberOfKeys / 2;
		if((numberOfKeys & 1) == 1 && keyIndex > midIndex)	//split nodes evenly
			++midIndex;		

		int totalKeys = numberOfKeys + 1;
		//move keys to a new node
		BPTreeInnerNode<T> newNode = new BPTreeInnerNode<T>(order);
		for (int i = midIndex; i < totalKeys - 1; ++i) 
		{	
			newNode.insertRightAt(i, this.getKey(i), this.getChild(i+1));
			numberOfKeys--;
		}
		newNode.setChild(0, this.getChild(midIndex));
		
		//insert the new key
		if(keyIndex < totalKeys / 2)
			this.insertRightAt(keyIndex, pushup.key, newNode);
		else
			newNode.insertRightAt(keyIndex - midIndex, pushup.key, newNode);

		return newNode;
	}

	public int findIndex(T key) 
	{
		for (int i = 0; i < numberOfKeys; ++i) 
		{
			int cmp = getKey(i).compareTo(key);
			if (cmp > 0)
				return i;
		}
		return numberOfKeys;
	}

	private void insertAt(int index, Comparable<T> key) 
	{
		for (int i = numberOfKeys + 1; i > index; --i) 
		{
			this.setKey(i, this.getKey(i - 1));
			this.setChild(i+1, this.getChild(i));
		}
		this.setKey(index, key);
		numberOfKeys++;
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
		if(ptr <= parent.numberOfKeys)
		{
			BPTreeInnerNode<T> rightSibling = (BPTreeInnerNode<T>) parent.getChild(ptr+1);
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
			BPTreeInnerNode<T> rightSibling = (BPTreeInnerNode<T>) parent.getChild(ptr+1);
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

	public void deleteAt(int keyIndex, int childPtr)	//0 for left and 1 for right
	{
		for(int i = keyIndex; i < numberOfKeys - 1; ++i)
		{
			keys[i] = keys[i+1];
			children[i+childPtr] = children[i+childPtr+1];
		}
		numberOfKeys--;
	}
	
	@Override
	public Ref search(T key) 
	{
		return children[findIndex(key)].search(key);
	}

	public void deleteAt(int index) 
	{
		deleteAt(index, 1);	
	}

}
