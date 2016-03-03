package BPTree;

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
		// TODO Auto-generated method stub
		return null;
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
	
	public boolean delete(T key) 
	{
		for(int i = 0; i < numberOfKeys; ++i)
			if(keys[i].compareTo(key) == 0)
			{
				//shift remaining keys and records
				while(i < numberOfKeys - 1)
				{
					keys[i] = keys[i+1];
					records[i] = records[i+1];
					i--;
				}
				numberOfKeys--;
				return true;
			}
		return false;
	}


}
