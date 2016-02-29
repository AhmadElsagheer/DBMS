package BPTree;

public class BPTreeLeafNode extends BPTreeNode {

	Object [] values;
	public BPTreeLeafNode(int n) {
		super(n);
		this.keys = new Object[n + 1];
		this.values = new Object[n+1];
		
		}

	public Object getValue(int index) {
		return (Object)this.values[index];
	}

	public void setValue(int index, Object value) {
		this.values[index] = value;
	}
	
	public void insertKey(Object key, Object value){
		
	}
	
	private void insertAt(int index, Object key, Object value){
		
	}
	
	
	public boolean delete(Object key){
		//TODO 
		return false;
	}

	private void deleteAt(int index){
		//TODO
	}
	
	
	public NodeType getNodeType() {
		return NodeType.LeafNode;
	}


	
	public int search(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	/**
	 * On splitting a leaf node, the middle key is kept on the new node and  pushed to the parent node.
	 */
	protected BPTreeNode split() {
		// TODO Auto-generated method stub
		return null;
	}

}
