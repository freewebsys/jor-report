package jatools.data.reader;

import jatools.engine.ValueIfClosed;

public class ScrollableField implements ValueIfClosed {
	/**
	 * @uml.property name="fieldIndex"
	 */
	private int col;

	/**
	 * @uml.property name="cursor"
	 */
	private DatasetCursor cursor;

	/**
	 * Creates a new ScrollableField object.
	 * 
	 * @param col
	 *            DOCUMENT ME!
	 * @param cursor
	 *            DOCUMENT ME!
	 */
	public ScrollableField(int col, DatasetCursor cursor) {
		this.col = col;
		this.cursor = cursor;
	}

	/**
	 */
	public Object value() {
		return cursor.getValue(col);
	}

	
}
