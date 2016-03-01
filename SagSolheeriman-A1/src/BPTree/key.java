package BPTree;

public class key {

	Object value;

	/**
	 * Calling the appropriate compareTo method
	 * 
	 * @param k
	 * @return
	 */
	public int compareTo(key k) {
		if (value instanceof Integer) {
			Integer I = (Integer) this.value;
			Integer K = (Integer) k.value;
			return I.compareTo(K);
		} else {
			if (value instanceof String) {
				String S = (String) this.value;
				String K = (String) k.value;
				return S.compareTo(K);
			}
		}
		return 0;

	}
}
