package jatools.designer.action;

import jatools.designer.Main;
import jatools.designer.ReportEditor;
import jatools.designer.SelectionState;
import jatools.util.Util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class ReportAction extends AbstractAction {
    static ArrayList caches = new ArrayList();
    public final static String ICON2 = "icon.disabled";
    public static final Icon EMPTY_ICON = new _EmptyIcon();
    public static final String CLASS = "class";
    private boolean oldEnables;

    /**
     * Creates a new ReportAction object.
     *
     * @param name DOCUMENT ME!
     * @param icon DOCUMENT ME!
     * @param icon2 DOCUMENT ME!
     */
    public ReportAction(String name, Icon icon, Icon icon2) {
        this(name, name, icon, icon2);
    }

    /**
     * Creates a new ReportAction object.
     *
     * @param name DOCUMENT ME!
     * @param icon DOCUMENT ME!
     */
    public ReportAction(String name, Icon icon) {
        this(name, name, icon, null);
    }

    /**
     * Creates a new ReportAction object.
     *
     * @param name DOCUMENT ME!
     */
    public ReportAction(String name) {
        this(name, EMPTY_ICON, null);
    }

    private ReportAction(String name, String tooltip, Icon icon, Icon icon2) {
        super(name, icon);
        setTooltip(tooltip);

        if (icon2 != null) {
            this.putValue(ICON2, icon2);
        }

        caches.add(this);
    }

    /**
     * DOCUMENT ME!
     */
    public void saveEnables() {
        this.oldEnables = this.isEnabled();
    }

    /**
     * DOCUMENT ME!
     */
    public void restoreEnables() {
        this.setEnabled(this.oldEnables);
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KeyStroke ctrl(int k) {
        return KeyStroke.getKeyStroke(k, KeyEvent.CTRL_MASK);
    }

    KeyStroke key(int k) {
        return KeyStroke.getKeyStroke(k, 0);
    }

    static protected Icon getIcon(String url) {
        return Util.getIcon(url);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tooltip DOCUMENT ME!
     */
    public void setTooltip(String tooltip) {
        putValue(SHORT_DESCRIPTION, tooltip);
    }

    /**
     * DOCUMENT ME!
     *
     * @param stroke DOCUMENT ME!
     */
    public void setStroke(KeyStroke stroke) {
        if (stroke != null) {
            putValue(AbstractAction.ACCELERATOR_KEY, stroke);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon DOCUMENT ME!
     */
    public void setSmallIcon(Icon icon) {
        putValue(SMALL_ICON, icon);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportEditor getEditor() {
        return Main.getInstance().getActiveEditor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getCaches() {
        return caches;
    }

    /**
     * DOCUMENT ME!
     */
    public static void disables() {
        Iterator it = caches.iterator();

        while (it.hasNext()) {
            ReportAction a = (ReportAction) it.next();
            a.saveEnables();
            a.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public static void restore() {
        Iterator it = caches.iterator();

        while (it.hasNext()) {
            ReportAction a = (ReportAction) it.next();
            a.restoreEnables();
        }
    }
}


class _EmptyIcon implements Icon {
    private int height = 16;
    private int width = 16;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
    }
}
