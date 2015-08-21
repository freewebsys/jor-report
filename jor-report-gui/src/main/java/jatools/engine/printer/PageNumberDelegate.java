package jatools.engine.printer;

import jatools.core.view.TextView;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageNumberDelegate {
    ArrayList texts = null;

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void add(TextView v) {
        if (texts == null) {
            texts = new ArrayList();
        }

        texts.add(v);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pageNumber DOCUMENT ME!
     */
    public void close(String pageNumber) {
        if (texts != null) {
            for (Iterator it = texts.iterator(); it.hasNext();) {
                TextView element = (TextView) it.next();
                element.setText(pageNumber);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return (texts == null) || texts.isEmpty();
    }
}
