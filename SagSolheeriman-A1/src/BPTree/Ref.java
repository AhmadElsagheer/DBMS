package BPTree;

import java.io.Serializable;

public class Ref implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pageNo, indexInPage;
	
	public Ref(int pageNo, int indexInPage)
	{
		this.pageNo = pageNo;
		this.indexInPage = indexInPage;
	}
	
	public int getPage()
	{
		return pageNo;
	}
	
	public int getIndexInPage()
	{
		return indexInPage;
	}
}
