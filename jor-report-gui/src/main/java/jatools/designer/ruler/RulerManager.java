package jatools.designer.ruler;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class RulerManager implements ChangeListener {
    final static _EmptySizer EMPTY_SIZER = new _EmptySizer();
    private Sizer sizer;
    private final Ruler columnView;
    private final Ruler rowView;

    /**
     * Creates a new RulerManager object.
     *
     * @param scrollPanel DOCUMENT ME!
     */
    public RulerManager(JScrollPane scrollPanel) {
        columnView = new Ruler(Ruler.HORIZONTAL, true) {
                    public Dimension getPreferredSize() {
                        return new Dimension(sizer.getWidth(), 20);
                    }
                };

        JPanel columnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        columnPanel.add(columnView);

        rowView = new Ruler(Ruler.VERTICAL, true) {
                    public Dimension getPreferredSize() {
                        return new Dimension(20, sizer.getHeight());
                    }
                };

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 7));
        rowPanel.add(rowView);

        scrollPanel.setColumnHeaderView(columnPanel);
        scrollPanel.setRowHeaderView(rowPanel);

        this.sizer = EMPTY_SIZER;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sizer DOCUMENT ME!
     */
    public void setSizer(Sizer sizer) {
        this.sizer = (sizer == null) ? EMPTY_SIZER : sizer;
        this.sizer.addChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        rowView.revalidate();
        rowView.repaint();
        columnView.revalidate();
        columnView.repaint();
    }
}


class _EmptySizer implements Sizer {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener cl) {
    }
}
