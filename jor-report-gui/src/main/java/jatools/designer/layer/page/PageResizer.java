package jatools.designer.layer.page;


import jatools.PageFormat;
import jatools.component.ComponentConstants;
import jatools.component.Page;
import jatools.component.Panel;
import jatools.designer.ReportPanel;
import jatools.designer.layer.table.RubberLine;
import jatools.designer.peer.ComponentPeer;
import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageResizer extends PageTester {
    final static int MIN_BODY_WIDTH = 50;
    final static int MIN_BODY_HEIGHT = 50;
    int hit;
    int firsty;
    int firstx;
    int lasty;
    int lastx;
    RubberLine rubberline;

    /**
     * Creates a new PageResizer object.
     *
     * @param owner DOCUMENT ME!
     */
    public PageResizer(ReportPanel owner) {
        super(owner);
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean start(int modifier, int x, int y, int deltaX, int deltaY) {
        hit = testSide(x, y);

        if (hit != UNKNOWN) {
            if ((hit == LEFT_MARGIN) || (hit == RIGHT_MARGIN) || (hit == PAGE_WIDTH)) {
                rubberline = new RubberLine(RubberLine.VERTICAL, x, new Point(),
                        owner.getPage().getHeight());
            } else {
                rubberline = new RubberLine(RubberLine.HORIZONTAL, y, new Point(),
                        owner.getPage().getWidth());
            }

            owner.repaint();
            this.firsty = y;
            this.firstx = x;
            this.lasty = y;
            this.lastx = x;

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void drag(int x, int y, int deltaX, int deltaY) {
        this.rubberline.moveTo(x, y);
        lastx = x;
        lasty = y;
        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     */
    public void end() {
        this.rubberline = null;

        owner.repaint();

        boolean edit = false;
        Page p = this.owner.getPage();
        ComponentPeer bodyPeer = owner.getComponentPeer(p.getBody());
        owner.openEdit();

        if (this.hit == TOP) {
            ComponentPeer headerPeer = owner.getComponentPeer(p.getHeader());

            int delta = lasty - firsty;

            if (delta != 0) {
                int hh = headerPeer.getComponent().getHeight();

                int bh = bodyPeer.getComponent().getHeight();

                if ((hh + delta) < 5) {
                    delta = 5 - hh;
                }

                if ((bh - delta) < 20) {
                    delta = bh - 20;
                }

                headerPeer.setValue(ComponentConstants.PROPERTY_HEIGHT, new Integer(hh + delta),
                    Integer.TYPE);

                bodyPeer.setValue(ComponentConstants.PROPERTY_HEIGHT, new Integer(bh - delta),
                    Integer.TYPE);
                edit = true;
            }
        } else if (this.hit == BOTTOM) {
            ComponentPeer footerPeer = owner.getComponentPeer(p.getFooter());

            int delta = lasty - firsty;

            if (delta != 0) {
                int fh = footerPeer.getComponent().getHeight();
                int bh = bodyPeer.getComponent().getHeight();

                if ((fh - delta) < 5) {
                    delta = -5 + fh;
                }

                if ((bh + delta) < 20) {
                    delta = -bh + 20;
                }

                footerPeer.setValue(ComponentConstants.PROPERTY_HEIGHT, new Integer(fh - delta),
                    Integer.TYPE);

                bodyPeer.setValue(ComponentConstants.PROPERTY_HEIGHT, new Integer(bh + delta),
                    Integer.TYPE);
                edit = true;
            }
        } else {
            PageFormat format = (PageFormat) p.getPageFormat().clone();

            if (this.hit == TOP_MARGIN) {
                int delta = lasty - firsty;

                if (delta != 0) {
                    format.top += delta;

                    if (format.top < 0) {
                        format.top = 0;
                    }

                    edit = true;
                }
            } else if (this.hit == BOTTOM_MARGIN) {
                int delta = lasty - firsty;

                if (delta != 0) {
                    format.bottom -= delta;

                    if (format.bottom < 0) {
                        format.bottom = 0;
                    }

                    edit = true;
                }
            } else if (this.hit == LEFT_MARGIN) {
                int delta = lastx - firstx;

                if (delta != 0) {
                    format.left += delta;

                    if (format.left < 0) {
                        format.left = 0;
                    }

                    edit = true;
                }
            } else if (this.hit == RIGHT_MARGIN) {
                int delta = lastx - firstx;

                if (delta != 0) {
                    format.right -= delta;

                    if (format.right < 0) {
                        format.right = 0;
                    }

                    edit = true;
                }
            } else if (this.hit == PAGE_WIDTH) {
                int delta = lastx - firstx;

                if (delta != 0) {
                    int width = delta + format.getWidth();
                    int min = MIN_BODY_WIDTH + format.left + format.right;

                    if (width < min) {
                        width = min;
                    }

                    format = new PageFormat(width, format.getHeight(), format.top, format.left,
                            format.bottom, format.right);

                    edit = true;
                }
            } else if (this.hit == PAGE_HEIGHT) {
                int delta = lasty - firsty;

                if (delta != 0) {
                    int height = delta + format.getHeight();

                    int min = MIN_BODY_WIDTH + format.bottom + format.top;
                    Panel header = p.getHeader();

                    if (header != null) {
                        min -= header.getHeight();
                    }

                    Panel footer = p.getFooter();

                    if (footer != null) {
                        min -= footer.getHeight();
                    }

                    if (height < min) {
                        height = min;
                    }

                    format = new PageFormat(format.getWidth(), height, format.top, format.left,
                            format.bottom, format.right);

                    edit = true;
                }
            }

            if (edit) {
                this.owner.getComponentPeer(p)
                          .setValue(ComponentConstants.PROPERTY_PAGE_FORMAT, format,
                    PageFormat.class);
            }
        }

        if (edit) {
            owner.closeEdit();
        } else {
            owner.discardEdit();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor() {
        if ((hit == LEFT_MARGIN) || (hit == RIGHT_MARGIN) || (hit == PAGE_WIDTH)) {
            return CursorUtil.E_RESIZE_CURSOR;
        } else {
            return CursorUtil.S_RESIZE_CURSOR;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        if (this.rubberline != null) {
            this.rubberline.paint(g);
        }
    }
}
