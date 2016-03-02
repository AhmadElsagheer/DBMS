package BPTree;

public class BPTreeLeafNode extends BPTreeNode {

	Object[] values;

	public BPTreeLeafNode(int n) {
		super(n);
		this.keys = new key[n + 1];
		this.values = new Object[n + 1];

	}

	public Object getValue(int index) {
		return (Object) this.values[index];
	}

	public void setValue(int index, Object value) {
		this.values[index] = value;
	}

	public void insertKey(key key, Object value) {
		int index = 0;
		while (index < this.countKeys() && this.getKey(index).compareTo(key) < 0)
			++index;
		this.insertAt(index, key, value);
	}

	private void insertAt(int index, key key, Object value) {
		for (int i = this.countKeys() - 1; i >= index; --i) {
			this.setKey(i + 1, this.getKey(i));
			this.setValue(i + 1, this.getValue(i));
		}

		// insert new key and value
		this.setKey(index, key);
		this.setValue(index, value);
		++this.numberOfKeys;
	}

	public boolean delete(Object key) {
		// TODO
		return false;
	}

	private void deleteAt(int index) {
		// TODO
	}

	public NodeType getNodeType() {
		return NodeType.LeafNode;
	}

	/**
	 * On splitting a leaf node, the middle key is kept on the new node and
	 * pushed to the parent node.
	 */
	protected BPTreeNode split() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override

	// handle object compare
	public int search(key key) {
		for (int i = 0; i < this.countKeys(); ++i) {
			int cmp = this.getKey(i).compareTo(key);
			if (cmp == 0) {
				return i;
			} else if (cmp > 0) {
				return -1;
			}
		}

		return -1;
	}

}
