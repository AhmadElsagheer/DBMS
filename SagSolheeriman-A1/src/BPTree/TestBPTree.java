package BPTree;

import java.util.Scanner;

public class TestBPTree {

	public static void main(String[] args) 
	{
		BPTree<Integer> tree = new BPTree<Integer>(4);
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 10; i++) 
		{
			tree.insert(sc.nextInt(), null);
			System.out.println(tree.toString());

		}
		
		for (int i = 0; i < 10; i++) 
		{
			tree.delete(sc.nextInt());
			System.out.println(tree.toString());

		}
		sc.close();
	}
	
}
