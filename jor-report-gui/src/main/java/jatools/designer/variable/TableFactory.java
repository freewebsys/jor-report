package jatools.designer.variable;


import jatools.component.table.Table;
import jatools.designer.layer.table.GroupTableFactory;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RowNodeSource;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TableFactory {
    /**
     * DOCUMENT ME!
     *
     * @param nodeSource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Table getTable(NodeSource nodeSource) {
        if (nodeSource instanceof RowNodeSource) {
            return GroupTableFactory.getTable((RowNodeSource) nodeSource);
        } else {

            return null;
        }
    }
}
