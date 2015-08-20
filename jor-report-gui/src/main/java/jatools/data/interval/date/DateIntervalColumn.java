package jatools.data.interval.date;

import jatools.accessor.AutoAccessor;
import jatools.data.reader.udc.UserDefinedColumn;




/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class DateIntervalColumn extends AutoAccessor implements UserDefinedColumn {
    String name;
    String srcField;
    int type;

    /**
     * Creates a new DateRangeColumn object.
     *
     * @param name DOCUMENT ME!
     * @param srcField DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public DateIntervalColumn(String name, String srcField, int type) {
        this.name = name;
        this.srcField = srcField;
        this.type = type;
    }

    /**
     * Creates a new DateRangeColumn object.
     */
    public DateIntervalColumn() {
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSrcField() {
        return srcField;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcField DOCUMENT ME!
     */
    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getName();
    }
}
