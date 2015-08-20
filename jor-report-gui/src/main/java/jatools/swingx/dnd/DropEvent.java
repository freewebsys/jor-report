package jatools.swingx.dnd;

/**
 * @author   java9
 */

public class DropEvent {
	Object source;
	Object target;
	int x;
	int y;

	/**
	 * Creates a new ZDropEvent object.
	 *
	 * @param source DOCUMENT ME!
	 * @param target DOCUMENT ME!
	 */
	public DropEvent(Object target, Object source, int x, int y) {
		this.source = source;
		this.target = target;
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a new ZDropEvent object.
	 *
	 * @param target DOCUMENT ME!
	 * @param source DOCUMENT ME!
	 */
	public DropEvent(Object target, Object source) {
		this.source = source;
		this.target = target;
	}

	/**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="source"
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="target"
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="x"
	 */
	public int getX() {
		return x;
	}

	/**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="y"
	 */
	public int getY() {
		return y;
	}
}
