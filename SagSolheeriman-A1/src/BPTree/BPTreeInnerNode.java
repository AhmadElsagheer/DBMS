package BPTree;

public class BPTreeInnerNode extends BPTreeNode {
	protected Object[] children;

	public BPTreeInnerNode(int n) {
		super(n);
		this.keys = new key[n];
		this.children = new Object[n + 1];
	}

	public BPTreeNode getChild(int index) {
		return (BPTreeNode) this.children[index];
	}

	public void setChild(int index, BPTreeNode child) {
		this.children[index] = child;
		if (child != null)
			child.setParent(this);
	}

	public NodeType getNodeType() {
		return NodeType.InnerNode;
	}

	/**
	 * insert a key at specified index , adjust the pointers right and left to
	 * that key
	 */
	private void insertAt(int index, key key, BPTreeNode leftChild, BPTreeNode rightChild) {
		// move space for the new key
				for (int i = this.countKeys() + 1; i > index; --i) {
					this.setChild(i, this.getChild(i - 1));
				}
				for (int i = this.countKeys(); i > index; --i) {
					this.setKey(i, this.getKey(i - 1));
				}
				
		// insert the new key and adjust pointers .
				this.setKey(index, key);
				this.setChild(index, leftChild);
				this.setChild(index + 1, rightChild);
				this.numberOfKeys += 1;
	}

	/**
	 * Do linear search to find the position of a key .
	 */
	public int search(key key) {
		int index = 0;
		for (index = 0; index < this.countKeys(); ++index) {
			int cmp = this.getKey(index).compareTo(key);
			if (cmp == 0) {
				return index + 1;
			}
			else if (cmp > 0) {
				return index;
			}
		}
		
		return index;
	}

	@Override
	/**
	 * split : create a new Inner Node , adjust keys and children between the
	 * splitted and new node When splits an Inner node, the middle key is pushed
	 * to parent node.
	 */
	protected BPTreeNode split() {
		// TODO Auto-generated method stub
		return null;
	}

}
