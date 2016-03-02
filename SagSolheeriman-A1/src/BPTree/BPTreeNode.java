package BPTree;

enum NodeType {
	InnerNode, LeafNode,
}

public abstract class BPTreeNode {

	protected key[] keys;
	protected BPTreeNode parentNode;
	protected BPTreeNode rightSibling;
	protected BPTreeNode leftSibling;
	protected int numberOfKeys;
	protected int ORDER;

	public BPTreeNode(int n) {

		numberOfKeys = 0;
		parentNode = null;
		rightSibling = null;
		leftSibling = null;
		ORDER = n;
	}

	public int countKeys() {
		return numberOfKeys;
	}

	public boolean isRoot() {
		return (parentNode == null);
	}

	public key getKey(int index) {
		return (key) this.keys[index];
	}

	public void setKey(int index, key key) {
		this.keys[index] = key;
	}

	public BPTreeNode getParent() {
		return this.parentNode;
	}

	public void setParent(BPTreeNode parent) {
		this.parentNode = parent;
	}

	/**
	 * for a leaf Node : Search a key on current node, if found the key then
	 * return its position, otherwise return -1, for an Inner Node : return the
	 * child node index which should contain the key.
	 */
	public abstract int search(key key);

	public boolean isOverflow() {
		return this.countKeys() == this.keys.length;
	}

	public abstract NodeType getNodeType();

	protected abstract BPTreeNode split();

}
