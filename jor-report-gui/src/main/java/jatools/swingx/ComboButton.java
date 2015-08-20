/*
 * Created on 2004-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.swingx;

import jatools.designer.action.ReportAction;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;



/**
 * @author   java9
 */
public class ComboButton extends JPanel implements ActionListener {
    static Icon dropIcon = Util.getIcon("/jatools/icons/drop.gif"); //
                                                                        // Action[] actions;
    AbstractButton selectedButton;
    int selectedIndex = -1;
    JPopupMenu ppm = new JPopupMenu();
    private JButton dropbutton;
    private boolean selectTextNull = true;

    /**
     * Creates a new ComboButton object.
     */
    public ComboButton() {
        super(new BorderLayout());

        dropbutton = new JButton(dropIcon);
        dropbutton.setPreferredSize(new Dimension(13, 2));
        dropbutton.setFocusable(false);

        dropbutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    ppm.show(ComboButton.this, 0, ComboButton.this.getHeight());
                }
            });

        add(dropbutton, BorderLayout.EAST);

        selectedButton = new Icon25x25Button((Action) null);
        selectedButton.setPreferredSize(new Dimension(25, 25));
        // selectedButton.setHorizontalAlignment( JButton.LEFT );
        selectedButton.setMargin(new Insets(0, 0, 0, 0));

        selectedButton.setText(null);

        add(selectedButton, BorderLayout.CENTER);

        setSize2(38, 25);
    }

    /**
     * Creates a new ZComboButton object.
     *
     * @param actions DOCUMENT ME!
     * @param toggle DOCUMENT ME!
     */
    public ComboButton(Action[] actions) {
        this();

        if (actions.length > 0) {
            selectAction(actions[0]);
        }

        for (int i = 0; i < actions.length; i++) {
            JMenuItem m = addAction(actions[i], true);

            if (i == 0) {
                enabledBy(m);
            }
        }

        selectedButton.setText(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void selectAction(Action action) {
        selectedButton.setAction(null);
        selectedButton.setAction(action);

        Icon icon2 = (Icon) action.getValue(ReportAction.ICON2);

        if (icon2 != null) {
            selectedButton.setDisabledIcon(icon2);
        }

        if (this.getToolTipText() != null) {
            selectedButton.setToolTipText(this.getToolTipText());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    public void setSize2(int w, int h) {
        setPreferredSize(new Dimension(w, h));
        this.setMinimumSize(getPreferredSize());
        this.setMaximumSize(getPreferredSize());
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void enabledBy(JMenuItem m) {
        m.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("enabled".equals(evt.getPropertyName())) {
                        SwingUtil.enabled(ComboButton.this,
                            ((JMenuItem) evt.getSource()).isEnabled());
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     * @param first DOCUMENT ME!
     */
    public JMenuItem addAction(Action action, boolean canTop) {
        JMenuItem mi = ppm.add(action);
        Icon icon2 = (Icon) action.getValue(ReportAction.ICON2);

        if (icon2 != null) {
            mi.setDisabledIcon(icon2);
        }

        if (canTop) {
            mi.addActionListener(this);
        }

        return mi;
    }

    /**
     * DOCUMENT ME!
     */
    public void clearPPM() {
        ppm.removeAll();
    }

    /**
     * DOCUMENT ME!
     */
    public void addSeperator() {
        ppm.addSeparator();
    }

    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param actions DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public static ZComboButton createButton(Action[] actions) {
    //        return new ZComboButton(actions,false);
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void setSelectIndex(int index) {
    }

    /**
         * @return   Returns the selectedButton.
         * @uml.property   name="selectedButton"
         */
    public AbstractButton getSelectedButton() {
        return selectedButton;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        JMenuItem mi = (JMenuItem) e.getSource();
        selectedButton.setAction(mi.getAction());

        if (this.selectTextNull) {
            selectedButton.setText(null);
        }

        selectedButton.setSelected(true);

        Icon icon2 = (Icon) mi.getAction().getValue(ReportAction.ICON2);

        if (icon2 != null) {
            selectedButton.setDisabledIcon(icon2);
        }

        if (this.getToolTipText() != null) {
            selectedButton.setToolTipText(this.getToolTipText());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectTextNull DOCUMENT ME!
     */
    public void setSelectTextNull(boolean selectTextNull) {
        this.selectTextNull = selectTextNull;
    }
}
