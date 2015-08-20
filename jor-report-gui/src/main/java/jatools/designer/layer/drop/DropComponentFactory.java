package jatools.designer.layer.drop;

import java.awt.datatransfer.DataFlavor;

import jatools.component.Component;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface DropComponentFactory {
	 public static DataFlavor FLAVOR = new DataFlavor(DropComponentFactory.class,
	    "class");
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component[] create();
}
