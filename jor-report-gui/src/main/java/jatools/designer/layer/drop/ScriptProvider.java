package jatools.designer.layer.drop;

import java.awt.datatransfer.DataFlavor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface ScriptProvider {
	public static DataFlavor FLAVOR = new DataFlavor(ScriptProvider.class, "class");
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getScript();
}
