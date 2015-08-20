package jatools.swingx;

/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
class CheckableItem {
    private Object item;
    private boolean isSelected;

    /**
     * Creates a new ZCheckableItem object.
     *
     * @param item DOCUMENT ME!
     */
    public CheckableItem(Object item) {
        this.item = item;
        isSelected = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getItem() {
        return item;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setSelected(boolean b) {
        isSelected = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return item.toString();
    }
}
