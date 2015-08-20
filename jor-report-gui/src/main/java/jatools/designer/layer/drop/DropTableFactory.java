package jatools.designer.layer.drop;

import jatools.component.Component;

import jatools.component.table.Table;

import jatools.designer.variable.TableFactory;

import jatools.dom.src.NodeSource;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DropTableFactory implements DropComponentFactory {
    NodeSource nodeSource;

    /**
         * @param variables
         */
    public DropTableFactory(NodeSource nodeSource) {
        this.nodeSource = nodeSource;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Component[] create() {
        Table table = TableFactory.getTable(nodeSource);

        if (table != null) {
            return new Component[] { table };
        } else {
            return null;
        }
    }
}
