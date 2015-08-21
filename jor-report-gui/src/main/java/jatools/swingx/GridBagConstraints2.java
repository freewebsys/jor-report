package jatools.swingx;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class GridBagConstraints2 extends GridBagConstraints {
    private JComponent parent;

    public GridBagConstraints2(JComponent c) {
        this.parent = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param w DOCUMENT ME!
     */
    public void add(JComponent child, int w) {
        int old = this.fill;
        this.fill = NONE;
        SwingUtil.setSize(child, new Dimension(w, 23));
        this.parent.add(child, this);
        this.fill = old;
    }
}
