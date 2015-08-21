package jatools.designer.data;


import jatools.VariableContext;
import jatools.data.reader.DatasetReader;

import java.awt.Component;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface DatasetReaderFactory {
    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader create(Component parent, NameChecker checker);

    /**
     * DOCUMENT ME!
     *
     * @param proxy DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean edit(DatasetReader proxy, Component parent, NameChecker checker);

    /**
     * DOCUMENT ME!
     *
     * @param vm DOCUMENT ME!
     */
    public void setVM(VariableContext vm);
}
