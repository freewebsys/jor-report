package jatools.data.reader.udc;

import jatools.dataset.Dataset;
import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface UserColumnBuilder {
    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public void build(Dataset data, Object col,Script script);
}
