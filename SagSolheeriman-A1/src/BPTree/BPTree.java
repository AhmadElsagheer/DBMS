package BPTree;

import java.util.Stack;

public class BPTree<T extends Comparable<T>> {

	private int n;
	private int NonLeafMinptrs;
	private int NonLeafMinKeys;
	private int LeafMinPtrs;
	private int LeafMinKeys;
	private Stack<BPTreeNode<T>> recStack;
	private BPTreeNode<T> root;

	public BPTree(int n) 
	{
		this.n = n;
		NonLeafMinptrs = (n + 2) / 2;
		NonLeafMinKeys = NonLeafMinptrs - 1;
		LeafMinPtrs = LeafMinKeys = (n + 1) / 2;
		
		this.root = new BPTreeLeafNode(n);
	}

	
	/**
	 * Insert a new key and its associated value into the B+ tree.
	 */
	public void insert(T key, Ref recordReference) // stack
	{
		//find the target leaf node to insert the key in the right place 
		BPTreeLeafNode<T> leaf = this.findTargetLeafNode(key);
		leaf.insertKey(key, recordReference);
		
		if (leaf.ifFull()) {
			BPTreeNode<T> n = leaf.dealOverflow(recStack);
			if (n != null)
				this.root = (BPTreeNode<T>) n; 
		}
	}
	
	/**
	 * Insertion Helper method to search the tree to find the target leaf node 
	 * RecStack keeps track of parents to be used in overflow situations !..
	 * @param key
	 * @return
	 */
	private BPTreeLeafNode<T> findTargetLeafNode(T key) {
		BPTreeNode<T> curNode = root;
		 recStack = new Stack<BPTreeNode<T>>();
		while (curNode instanceof BPTreeInnerNode) {
			// find the proper navigation pointer until you reach the leafNode
			recStack.push(curNode);
			curNode = ((BPTreeInnerNode<T>)curNode).getChild(curNode.search(key));
		}
		
		// return the target Leaf node
		return (BPTreeLeafNode<T>) curNode;
	}
	
	
	/**
	 * Delete a key and its associated value from the tree.
	 */
	public void delete(T key)
	{
		//TODO
	}
	
	/**
	 * Search a key value on the tree and return its associated value.
	 */
	public Object search(T key)
	{
		//TODO
		return null;
	}
	
	public String toString(){
		
		String s = "";
		BPTreeNode<T> curNode = this.root;
		while(curNode instanceof BPTreeInnerNode)
			curNode = ((BPTreeInnerNode<T>) curNode).getChild(0);
		
		BPTreeLeafNode<T> curLeaf = (BPTreeLeafNode<T>) curNode;
		// at least one leaf 
		for (int i = 0; i < curLeaf.keys.length; i++) {
			s+= curLeaf.keys[i] + "|";
		}
		while(curLeaf.getNext() != null){
			curLeaf = curLeaf.getNext();
			s += " -> ";
			for (int i = 0; i < curLeaf.keys.length; i++) {
				s+= curLeaf.keys[i] + "|";
			}
			
		}
		
		return s;
	}
	
	public static void main(String[] args) {
		BPTree<Integer> tree = new BPTree<Integer>(4);
		tree.insert(4, null);
		tree.insert(6, null);
		tree.insert(9, null);
		tree.insert(14, null);
		tree.insert(20, null);
		tree.insert(5, null);
		tree.insert(8, null);

		System.out.println(tree.toString());
	}
	
}
