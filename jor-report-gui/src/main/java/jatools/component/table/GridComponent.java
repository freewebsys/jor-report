package jatools.component.table;

import jatools.component.Component;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public interface GridComponent {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableBase getRootTable();
    
    public boolean isInline();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableBase getNearestTable();

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    public void add(Component child, int row, int column);

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param colSpan DOCUMENT ME!
     * @param rowSpan DOCUMENT ME!
     */
    public void add(Component child, int row, int column, int colSpan, int rowSpan);
}
