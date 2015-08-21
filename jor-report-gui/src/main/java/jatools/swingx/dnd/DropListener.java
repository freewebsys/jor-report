package jatools.swingx.dnd;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.1 $
 *  @author $author$
 * 
 */

public interface DropListener {
	/**
	 * DOCUMENT ME!
	 *
	 * @param comp DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	boolean dropEnter(DropEvent e);

	/**
	 * DOCUMENT ME!
	 *
	 * @param comp DOCUMENT ME!
	 */
	void dropMove(DropEvent e);

	/**
	 * DOCUMENT ME!
	 *
	 * @param comp DOCUMENT ME!
	 */
	void dropLeave(DropEvent e);

	/**
	 * DOCUMENT ME!
	 *
	 * @param comp DOCUMENT ME!
	 */
	void drop(DropEvent e);
}
