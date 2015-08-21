package jatools.designer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableNodeRenderer extends DefaultTreeCellRenderer {
    private Component strut;
    private JPanel panel;

    /**
     * Creates a new VariableNodeRenderer object.
     */
    public VariableNodeRenderer() {
        strut = Box.createHorizontalStrut(5);
        panel = new JPanel();
        panel.setBackground(UIManager.getColor("Tree.textBackground"));
        setOpaque(false);

        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(0, 0, 0));
        panel.add(this);
        panel.add(strut);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param selected DOCUMENT ME!
     * @param expanded DOCUMENT ME!
     * @param leaf DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        return panel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getCheckBoxOffset() {
        Graphics g = panel.getGraphics();
        int xoffset = 0;

        if (g != null) {
            try {
                FontMetrics fm = g.getFontMetrics();
                xoffset = fm.stringWidth(getText()) + strut.getPreferredSize().width;
            } finally {
                g.dispose();
            }
        }

        return new Dimension(xoffset, 0);
    }
}
