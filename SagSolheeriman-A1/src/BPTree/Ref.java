package BPTree;

import java.io.Serializable;

public class Ref implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int pageNo, indexInPage;
	
	public Ref(int pageNo, int indexInPage)
	{
		this.pageNo = pageNo;
		this.indexInPage = indexInPage;
	}
}
