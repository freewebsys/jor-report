package jatools.viewer;

import jatools.PageFormat;
import jatools.core.view.PageView;
import jatools.core.view.TextView;
import jatools.core.view.TransformView;
import jatools.core.view.View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Element;
import javax.swing.text.FieldView;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class PageCanvas extends JLabel {
    PageView pp;
    Rectangle imageable;
    private float scale = 1.0f;
    Rectangle hitrect = new Rectangle();
    _inplaceEditor ie;
    

    PageCanvas(PageView pp) {
        if (pp != null) {
            setPageView(pp);
        }

        this.setFocusable(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void hit(int x, int y) {
        if (scale == 1.0) {
            TextView text = findText(x, y, hitrect);

            if (text != null) {
                if (ie == null) {
                    ie = new _inplaceEditor();
                }

                ie.showIn(this, text.getText(), hitrect, text);
            }
        }
    }

    PageView getPage() {
        return pp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (pp != null) {
            Graphics2D gc = (Graphics2D) g.create();

            if (scale != 1.0f) {
                AffineTransform af = new AffineTransform();
                af.scale(scale, scale);
                gc.transform(af);
            }

            //  gc.clipRect(imageable.x, imageable.y, (int) (imageable.width), (int) (imageable.height));
            pp.paint(gc);
            gc.dispose();
        }

        getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
    }

    private TextView findText(int _x, int _y, Rectangle rect) {
        int x = 0;
        int y = 0;
        Iterator it = pp.getElements().iterator();

        while (it.hasNext()) {
            View v = (View) it.next();

            if (v instanceof TextView) {
                TextView tv = (TextView) v;

                //                if (tv.isBacklayer()) {
                //                    continue;
                //                }
                tv.getBounds().translate(x, y);

                if (tv.getBounds().contains(_x, _y)) {
                    rect.setBounds(tv.getBounds());

                    tv.getBounds().translate(-x, -y);

                    return tv;
                }

                tv.getBounds().translate(-x, -y);
            } else if (v instanceof TransformView) {
                TransformView tv = (TransformView) v;
                x += tv.x;
                y += tv.y;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _x DOCUMENT ME!
     * @param _y DOCUMENT ME!
     * @param tipParent DOCUMENT ME!
     */
    public void buildTooptip(int _x, int _y, JComponent tipParent) {
        if (scale != 1.0) {
            if (this.getToolTipText() != null) {
                this.setToolTipText(null);
            }

            return;
        }

        int x = 0;
        int y = 0;
        Iterator it = pp.getElements().iterator();

//        while (it.hasNext()) {
//            View v = (View) it.next();
//
//            if (v instanceof ImageView) {
//                ImageView tv = (ImageView) v;
//
//                if (tv.getLinkMap() != null) {
//                    tv.getBounds().translate(x, y);
//
//                    if (tv.getBounds().contains(_x, _y)) {
//                        _Tip tip = tv.getToolTip(_x - tv.getBounds().x, _y - tv.getBounds().y);
//
//                        if (tip != null) {
//                            if (tip != activeTip) {
//                                tipParent.setToolTipText(tip.alt);
//                            }
//                        } else {
//                            tipParent.setToolTipText(null);
//                        }
//
//                        activeTip = tip;
//
//                        tv.getBounds().translate(-x, -y);
//
//                        return;
//                    }
//
//                    tv.getBounds().translate(-x, -y);
//                }
//            } else if (v instanceof TransformView) {
//                TransformView tv = (TransformView) v;
//                x += tv.x;
//                y += tv.y;
//            }
//        }

        this.setToolTipText(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param scale DOCUMENT ME!
     */
    public void setScale(float scale) {
        if (pp != null) {
            PageFormat format = pp.getPageFormat();

            if (scale == -1.0f) {
                scale = ((0.0f + this.getParent().getParent().getParent().getWidth()) - 20) / format.getWidth();

                if (scale > 1.0f) {
                    scale = 1.0f;
                }
            }

            if (scale != this.scale) {
                this.scale = scale;

                Dimension nowSize = new Dimension((int) (format.getWidth() * scale),
                        (int) (format.getHeight() * scale));
                rvr.defaultPageSize = nowSize;
                this.revalidate();
                repaint();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pp DOCUMENT ME!
     */
    public void setPageView(PageView pp) {
        this.pp = pp;

        PageFormat format = pp.getPageFormat();

        this.imageable = new Rectangle((int) format.left, (int) format.top,
                (int) format.getImageableWidth(), (int) format.getImageableHeight());

        Dimension nowSize = new Dimension((int) format.getWidth(), (int) format.getHeight());

        if (!nowSize.equals(rvr.defaultPageSize)) {
            rvr.defaultPageSize = nowSize;
            this.setPreferredSize(rvr.defaultPageSize);
            this.revalidate();
        }

        this.setPreferredSize(rvr.defaultPageSize);
        this.revalidate();
        this.repaint();
    }

    class _inplaceEditor extends JTextField implements KeyListener, FocusListener {
        private String oldText;
        Rectangle oldRect;
        private TextView textView;

        public _inplaceEditor() {
            addKeyListener(this);
            addFocusListener(this);
            setUI(new BasicTextFieldUI() {
                    public javax.swing.text.View create(Element element) {
                        return new FieldView(element) {
                                protected Shape adjustAllocation(Shape shape) {
                                    if (shape == null) {
                                        return null;
                                    }

                                    if (textView.getVerticalAlignment() == TextView.MIDDLE) {
                                        return (Rectangle) super.adjustAllocation(shape);
                                    } else {
                                        Rectangle bounds = shape.getBounds();
                                        int height = bounds.height;
                                        int y = bounds.y;
                                        int vspan = (int) getPreferredSpan(Y_AXIS);

                                        bounds = (Rectangle) super.adjustAllocation(shape);

                                        if (height != vspan) {
                                            int slop = bounds.height - vspan;

                                            if (textView.getVerticalAlignment() == TextView.BOTTOM) {
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
        }

        public boolean isChanged() {
            String newText = this.getText();

            return !((newText != null) ? newText.equals(oldText) : (oldText == null));
        }

        public boolean isManagingFocus() {
            return true;
        }

        public void autoResize() {
            Graphics g = this.getGraphics();
            FontMetrics fm = g.getFontMetrics(g.getFont());
            Dimension newValue = super.getSize();
            newValue.width = Math.max(fm.stringWidth(getText()) + 15, oldRect.width);

            if (newValue.width > oldRect.width) {
                this.setSize(newValue);
            }
        }

        public void focusGained(FocusEvent evt) {
        }

        public void focusLost(FocusEvent evt) {
            endEdit();
        }

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
            case KeyEvent.VK_ENTER:
                e.consume();
                getParent().requestFocus();
                sendKey(getParent(), e.getKeyCode(), e.getKeyChar());

                break;

            case KeyEvent.VK_ESCAPE:
                textView.setText(oldText);
                oldText = getText();

                getParent().requestFocus();

                break;
            }

            autoResize();
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }

        public void sendKey(Component comp, int vk_code, char keyChar) {
            try {
                comp.dispatchEvent(new KeyEvent(comp, KeyEvent.KEY_PRESSED,
                        System.currentTimeMillis(), 0, vk_code, keyChar));
                Thread.sleep(1);
                comp.dispatchEvent(new KeyEvent(comp, KeyEvent.KEY_RELEASED,
                        System.currentTimeMillis(), 0, vk_code, keyChar));
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
        }

        public void showIn(JComponent comp, String oldText, Rectangle rect, TextView textView) {
            setFont(textView.getFont());

            int a = 0;

            switch (textView.getHorizontalAlignment()) {
            case TextView.CENTER:
                a = CENTER;

                break;

            case TextView.LEFT:
                a = LEFT;

                break;

            case TextView.RIGHT:
                a = RIGHT;

                break;
            }

            setHorizontalAlignment(a);

            this.oldText = oldText;
            setText(oldText);

            rect.grow(-2, -2);
            oldRect = rect;

            if (this.getParent() != comp) {
                comp.add(this);
            }

            setBounds(rect.x, rect.y, rect.width, rect.height);
            setVisible(true);
            textView.setText(null);
            requestFocus();

            this.textView = textView;
            getParent().repaint();
        }

        private void endEdit() {
            if (isChanged()) {
                textView.setText(this.getText());
            } else {
                textView.setText(oldText);
            }

            setVisible(false);
        }
    }
}
