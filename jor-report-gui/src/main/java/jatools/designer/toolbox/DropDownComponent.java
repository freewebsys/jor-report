package jatools.designer.toolbox;

import jatools.designer.Main;
import jatools.util.Util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public abstract class DropDownComponent extends JComponent implements ActionListener,
    AncestorListener {
    /**
     * DOCUMENT ME!
     */
    public final static String CANCEL_VALUE = "cancel.value";

    /**
     * DOCUMENT ME!
     */
    public final static String NEW_VALUE = "new.value";

    /**
     * DOCUMENT ME!
     */
    public static Icon dropIcon = Util.getIcon("/jatools/icons/drop.gif");
    private JComponent popupComponent;
    protected JComponent active_comp;
    protected JButton arrow;
    protected JWindow popupWindow;
    boolean locked;
    private ArrayList actionlisteners;

    /**
     * Creates a new DropDownComponent object.
     *
     * @param active_comp DOCUMENT ME!
     */
    public DropDownComponent(JComponent active_comp) {
        this(active_comp, false);
    }

    /**
     * Creates a new DropDownComponent object.
     *
     * @param active_comp DOCUMENT ME!
     * @param smallbutton DOCUMENT ME!
     */
    public DropDownComponent(JComponent active_comp, boolean smallbutton) {
        this.active_comp = active_comp;

        this.arrow = new JButton(dropIcon);

        if (smallbutton) {
            arrow.setPreferredSize(new Dimension(13, 17));
            arrow.setMinimumSize(arrow.getPreferredSize());
            arrow.setMaximumSize(arrow.getPreferredSize());
        } else {
            this.arrow.setPreferredSize(new Dimension(13, 23));
        }

        this.arrow.addActionListener(this);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 100;
        c.fill = GridBagConstraints.BOTH;

        this.add(active_comp, c);
        c.weightx = 0;
        this.add(this.arrow, c);

        addAncestorListener(this);
    }

    protected JComponent getPopupComponent() {
        if (popupComponent == null) {
            popupComponent = createPopupComponent();
            popupComponent.setBorder(UIManager.getDefaults().getBorder("PopupMenu.border"));
            popupComponent.putClientProperty("ddc", this);
        }

        return popupComponent;
    }

    protected abstract JComponent createPopupComponent();

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addActionListener(ActionListener listener) {
        if (this.actionlisteners == null) {
            this.actionlisteners = new ArrayList();
        }

        this.actionlisteners.add(listener);
    }

    protected void fireActionListener(int id) {
        if ((this.actionlisteners != null) && !this.actionlisteners.isEmpty()) {
            ActionEvent e = new ActionEvent(this, id, null);
            Iterator it = this.actionlisteners.iterator();

            while (it.hasNext()) {
                ActionListener a = (ActionListener) it.next();
                a.actionPerformed(e);
            }
        }
    }

    protected Frame getFrame(Component comp) {
        if (comp == null) {
            comp = this;
        }

        if (comp.getParent() instanceof Frame) {
            return (Frame) comp.getParent();
        }

        return getFrame(comp.getParent());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
    	if(!this.isEnabled() )
    		return ;
    	
        lock(true);

        if (popupWindow == null) {
            Container c = this.getTopLevelAncestor();

            if (c instanceof Window) {
                popupWindow = new JWindow((Window) c);
            } else if (c instanceof Frame) {
                popupWindow = new JWindow((Frame) c);
            } else {
                popupWindow = new JWindow(Main.getInstance());
            }

            popupWindow.getContentPane().add(getPopupComponent());
        }

        popupWindow.pack();

        Point pt = active_comp.getLocationOnScreen();
        pt.translate(0, active_comp.getHeight());
        popupWindow.setLocation(pt);
        popupWindow.toFront();
        popupWindow.setVisible(true);

        popupWindow.requestFocusInWindow();
        popupWindow.addWindowFocusListener(new WindowAdapter() {
                public void windowLostFocus(WindowEvent evt) {
                    hidePopup();
                }
            });
    }

    private void lock(boolean b) {
        Container p = this;

        while ((p = p.getParent()) != null) {
            if (p instanceof JComponent) {
                DropDownComponent ddc = (DropDownComponent) ((JComponent) p).getClientProperty(
                        "ddc");

                if (ddc != null) {
                    ddc.locked = b;

                    if (!b) {
                        ddc.getPopupComponent().requestFocus();
                    }

                    p = ddc;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void ancestorAdded(AncestorEvent event) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void ancestorRemoved(AncestorEvent event) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void ancestorMoved(AncestorEvent event) {
        if (event.getSource() != popupWindow) {
            hidePopup();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void hidePopup() {
        if ((popupWindow != null) && popupWindow.isVisible() && !locked) {
            lock(false);
            popupWindow.setVisible(false);
        }
    }
}
