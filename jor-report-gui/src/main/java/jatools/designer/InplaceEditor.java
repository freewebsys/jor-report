package jatools.designer;

import jatools.core.view.TextView;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class InplaceEditor extends JTextArea implements KeyListener, FocusListener {
    private String oldText;
    Rectangle oldRect;
    private int vertalign;
    ActionListener listener;
    boolean keepFocused = false;
    private JPopupMenu menu;
    boolean discard = false;

    /**
     * Creates a new InplaceEditor object.
     */
    public InplaceEditor() {
        addKeyListener(this);
        addFocusListener(this);

        setUI(new BasicTextFieldUI() {
                public View create(Element element) {
                    return new FieldView(element) {
                            protected Shape adjustAllocation(Shape shape) {
                                if (shape == null) {
                                    return null;
                                }

                                if (vertalign == TextView.MIDDLE) {
                                    return (Rectangle) super.adjustAllocation(shape);
                                } else {
                                    Rectangle bounds = shape.getBounds();
                                    int height = bounds.height;
                                    int y = bounds.y;
                                    int vspan = (int) getPreferredSpan(Y_AXIS);

                                    bounds = (Rectangle) super.adjustAllocation(shape);

                                    if (height != vspan) {
                                        int slop = bounds.height - vspan;

                                        if (vertalign == TextView.BOTTOM) {
                                            bounds.y += (bounds.y - y);
                                        } else {
                                            bounds.y = y;
                                        }

                                        bounds.height -= slop;
                                    }

                                    return bounds;
                                }
                            }
                        };
                }
            });
        setBorder(null);
        initialize();
    }

    protected void initialize() {
        this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    dealWithMousePress(evt);
                }
            });
    }

    protected void dealWithMousePress(MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            this.keepFocused = true;

            if (menu == null) {
                menu = new JPopupMenu() {
                            public boolean isManagingFocus() {
                                return true;
                            }
                        };
                menu.add(new CutAction(this));
                menu.add(new CopyAction(this));
                menu.add(new PasteAction(this));
                menu.add(new DeleteAction(this));
                menu.addSeparator();
                menu.add(new SelectAllAction(this));
            }

            Point pt = SwingUtilities.convertPoint(evt.getComponent(), evt.getPoint(), this);

            menu.show(this, pt.x, pt.y);
            this.requestFocus();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isChanged() {
        return !getText().equals(oldText);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isManagingFocus() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void autoResize() {
        Graphics g = this.getGraphics();
        FontMetrics fm = g.getFontMetrics(g.getFont());
        Dimension newValue = super.getSize();
        newValue.width = Math.max(fm.stringWidth(getText()) + 15, oldRect.width);

        if (newValue.width > oldRect.width) {
            this.setSize(newValue);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void focusGained(FocusEvent evt) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void focusLost(FocusEvent evt) {
        if (!this.keepFocused) {
            endEdit();
        }

        this.keepFocused = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_TAB:
        case KeyEvent.VK_ENTER:
            e.consume();
            getParent().requestFocus();
            sendKey(getParent(), e.getKeyCode(), e.getKeyChar());

            break;

        case KeyEvent.VK_ESCAPE:
            discard = true;
            getParent().requestFocus();

            break;
        }

        autoResize();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param vk_code DOCUMENT ME!
     * @param keyChar DOCUMENT ME!
     */
    public static void sendKey(Component comp, int vk_code, char keyChar) {
        try {
            comp.dispatchEvent(new KeyEvent(comp, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                    0, vk_code, keyChar));
            Thread.sleep(1);
            comp.dispatchEvent(new KeyEvent(comp, KeyEvent.KEY_RELEASED,
                    System.currentTimeMillis(), 0, vk_code, keyChar));
            Thread.sleep(1);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param oldText DOCUMENT ME!
     * @param rect DOCUMENT ME!
     * @param vertalign DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public void showIn(JComponent comp, String oldText, Rectangle rect, int vertalign,
        ActionListener listener) {
        this.oldText = oldText;

        setText(oldText);

        rect.grow(-2, -2);
        oldRect = rect;

        if (this.getParent() != comp) {
            comp.add(this);
        }

        setBounds(rect.x, rect.y, rect.width, rect.height);
        setVisible(true);

        requestFocus();

        this.listener = listener;

        comp.repaint();

        discard = false;
    }

    private void endEdit() {
        ActionEvent e = new ActionEvent(this, 0, null);
        listener.actionPerformed(e);
        setVisible(false);

        if ((menu != null) && menu.isVisible()) {
            menu.setVisible(false);
        }
    }

    class CutAction extends AbstractAction {
        JTextComponent comp;

        public CutAction(JTextComponent comp) {
            super(App.messages.getString("res.125"));
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent e) {
            comp.cut();
        }

        public boolean isEnabled() {
            return comp.isEditable() && comp.isEnabled() && (comp.getSelectedText() != null);
        }
    }

    class PasteAction extends AbstractAction {
        JTextComponent comp;

        public PasteAction(JTextComponent comp) {
            super(App.messages.getString("res.126"));
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent e) {
            comp.paste();
        }

        public boolean isEnabled() {
            if (comp.isEditable() && comp.isEnabled()) {
                Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard()
                                               .getContents(this);

                return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            } else {
                return false;
            }
        }
    }

    class DeleteAction extends AbstractAction {
        JTextComponent comp;

        public DeleteAction(JTextComponent comp) {
            super(App.messages.getString("res.96"));
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent e) {
            comp.replaceSelection(null);
        }

        public boolean isEnabled() {
            return comp.isEditable() && comp.isEnabled() && (comp.getSelectedText() != null);
        }
    }

    class SelectAllAction extends AbstractAction {
        protected JTextComponent comp;

        public SelectAllAction(JTextComponent comp) {
            super(App.messages.getString("res.127"));
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent e) {
            comp.selectAll();

            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        comp.selectAll();
                    }
                });
        }

        public boolean isEnabled() {
            return comp.isEnabled() && (comp.getText().length() > 0);
        }
    }

    class CopyAction extends AbstractAction {
        JTextComponent comp;

        public CopyAction(JTextComponent comp) {
            super(App.messages.getString("res.128"));
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent e) {
            comp.copy();
        }

        public boolean isEnabled() {
            return comp.isEnabled() && (comp.getSelectedText() != null);
        }
    }
}
