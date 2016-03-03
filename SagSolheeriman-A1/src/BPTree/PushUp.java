package BPTree;

public class PushUp<T extends Comparable<T>> {

	BPTreeNode<T> newNode;
	Comparable<T> key;
	
	public PushUp(BPTreeNode<T> newNode, Comparable<T> key)
	{
		this.newNode = newNode;
		this.key = key;
	}
}
