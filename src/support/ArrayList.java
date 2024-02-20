package support;

public class ArrayList<E> extends java.util.ArrayList<E> { // TODO: Why do I have this?

	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 */
	public void add(final E... e) {
		for(final E a : e) {
			this.add(a);
		}
	}

}