package jatools.dataset;

import bsh.Path;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RowPath implements Path {
    RowMeta meta;
    Row row;

    /**
     * Creates a new RowPath object.
     *
     * @param meta DOCUMENT ME!
     * @param row DOCUMENT ME!
     */
    public RowPath(RowMeta meta, Row row) {
        this.meta = meta;
        this.row = row;
    }

    /**
    * DOCUMENT ME!
    *
    * @param field DOCUMENT ME!
    * @param obj DOCUMENT ME!
    * @param callstack DOCUMENT ME!
    * @param it DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Object path(String field) {
        return row.getValueAt(meta.getIndexByColumnName(field));
    }
}
