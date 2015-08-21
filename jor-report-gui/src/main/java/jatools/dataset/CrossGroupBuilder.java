package jatools.dataset;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CrossGroupBuilder {
    private static CrossGroupBuilder defaults;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CrossGroupBuilder getDefaults() {
        if (defaults == null) {
            defaults = new CrossGroupBuilder();
        }

        return defaults;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param key2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowSet[] build(CrossIndexView view, Key key, Key key2) {
        ArrayList result = new ArrayList();

        if ("*".equals(key.lastValue())) {
            key = key.getParent();

            Iterator it = view.keys();

            while (it.hasNext()) {
                Key k = (Key) it.next();

                if (k.isChildOf(key)) {
                    CrossRowSet cset = view.locate(k);

                    try {
                        RowSet rset = cset.locate(key2);

                        if (rset != null) {
                            result.add(rset);
                        }
                    } catch (DatasetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if ("*".equals(key2.lastValue())) {
            key2 = key2.getParent();

            CrossRowSet cset = view.locate(key);
            Iterator it = cset.keys();

            while (it.hasNext()) {
                Key k = (Key) it.next();

                if (k.isChildOf(key2)) {
                    try {
                        RowSet rset = cset.locate(k);

                        if (rset != null) {
                            result.add(rset);
                        }
                    } catch (DatasetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                RowSet rset = view.locate(key, key2);

                if (rset != null) {
                    result.add(rset);
                }
            } catch (DatasetException e) {
                e.printStackTrace();
            }
        }

        return (RowSet[]) result.toArray(new RowSet[0]);
    }
}
