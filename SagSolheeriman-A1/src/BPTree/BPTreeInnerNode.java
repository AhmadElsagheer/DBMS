package BPTree;

public class BPTreeInnerNode extends BPTreeNode {
	protected Object[] children; 
	
	public BPTreeInnerNode(int n) {
		super(n);
		this.keys = new Object[n];
		this.children = new Object[n+1];
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
	 * insert a key at specified index , adjust the pointers right and left to that key
	 */
	private void insertAt(int index, Object key, BPTreeNode leftChild, BPTreeNode rightChild) {
		
		
	}
	@Override
	public int search(Object key) {
		return 0;
	}

	@Override
	/**
	 * split : create a new Inner Node , adjust keys and children between the splitted and new node
	 * When splits an Inner node, the middle key is  pushed to parent node.
	 */
	protected BPTreeNode split() {
		// TODO Auto-generated method stub
		return null;
	}

}
