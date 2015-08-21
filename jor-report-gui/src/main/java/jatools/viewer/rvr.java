/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.viewer;

import jatools.PageFormat;
import jatools.accessor.ProtectPublic;
import jatools.core.view.PageView;
import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;



/**
 * DOCUMENT ME!
 *
 * ZReportViewer:ZReportPreviewer _PageView:JPanel ZPageCanvas:JLabel *
 *
 *
 *
 * @version $Revision: 1.4 $
 * @author $author$
 */
public class rvr extends JPanel implements ProtectPublic{
    static Dimension defaultPageSize = new Dimension(120, 300);
    static Icon printIcon;
    static String FIT_WIDTH = App.messages.getString("res.5");
    private Cursor hand0;
    private Cursor hand1;
    protected _PageView view = new _PageView();
    private JComboBox scaleCombo;
    protected Action printAction;

    // private float scale = 1.0f;
    protected JPanel toolpane;
    private JPanel toolbar;
    ZDraggableListener scroller = new ZDraggableListener();
    JViewport _view;
    int H_GAP = 16;
    int V_GAP = 10;

    //	MouseMotionListener pageListener ;
    /**
     * Creates a new ZSimpleReportViewer object.
     */
    public rvr(boolean showToolbar) {
        if (printIcon == null) {
            printIcon = new ImageIcon(this.getClass().getResource("/jatools/icons/print.gif")); //
        }

        setLayout(new BorderLayout());
        buildUI(showToolbar);
    }

    /**
     * Creates a new rvr object.
     */
    public rvr() {
        this(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param iconUrl
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Icon getIcon(String iconUrl) {
        return new ImageIcon(rvr.class.getResource(iconUrl));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JPanel toolbar() {
        return toolbar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param showToolbar
     *            DOCUMENT ME!
     */
    void buildUI(boolean showToolbar) {
        toolpane = new JPanel(new FlowLayout(FlowLayout.LEADING,1,1));

        toolpane.add(buildToolbar());

        if (showToolbar) {
            add(toolpane, BorderLayout.NORTH);
        }

        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        p.add(view, gbc);

        JScrollPane scrollPane = new JScrollPane(p);
        _view = scrollPane.getViewport();
        _view.addMouseListener(scroller);
        _view.addMouseMotionListener(scroller);
        add(scrollPane, BorderLayout.CENTER);
        loadCursor();
        view.setCursor(hand0);
    }

    /**
     * DOCUMENT ME!
     */
    public void print() {
        view.print();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JPanel buildToolbar() {
        toolbar = getToolbar();

        printAction = new _PrintAction();

        JButton button = new JButton(printIcon);
        button.setPreferredSize(new Dimension(25, 25));
        button.setToolTipText( App.messages.getString("res.6"));
        button.addActionListener(printAction);

        toolbar.add(button);
        toolbar.add(Box.createHorizontalStrut(10));

        ImageIcon icon = new ImageIcon(this.getClass()
                                           .getResource("/jatools/icons/firstpage.gif")); //

        button = new JButton(icon);
        button.setPreferredSize(new Dimension(25, 25));
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    view.firstRow();
                }
            });

        toolbar.add(button);

        icon = new ImageIcon(this.getClass().getResource("/jatools/icons/prevpage.gif")); //
        button = new JButton(icon);
        button.setPreferredSize(new Dimension(25, 25));
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    view.prevRow();
                }
            });

        toolbar.add(button);

        icon = new ImageIcon(this.getClass().getResource("/jatools/icons/nextpage.gif")); //
        button = new JButton(icon);
        button.setPreferredSize(new Dimension(25, 25));
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    view.nextRow();
                }
            });

        toolbar.add(button);
        icon = new ImageIcon(this.getClass().getResource("/jatools/icons/lastpage.gif")); //

        button = new JButton(icon);

        // button.setMaximumSize(new Dimension(25, 25));
        button.setPreferredSize(new Dimension(25, 25));
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    view.lastRow();
                }
            });

        toolbar.add(button);

        toolbar.add(Box.createHorizontalStrut(10));
        scaleCombo = new JComboBox(new Object[] {
                    "30%",
                    "50%",
                    "75%",
                    "100%",
                    "125%",
                    "200%",
                    "300%",
                    "350%",
                    "400%",
                    FIT_WIDTH
                }); // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
        scaleCombo.setPreferredSize(new Dimension(90, 25));
        scaleCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setScale(toScale());
                }
            });
        scaleCombo.setSelectedIndex(3);
        toolbar.add(scaleCombo);

        
        toolbar = this.getToolbar2(toolbar);

        return toolbar;
    }

    /**
     * @return
     */
    protected JPanel getToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));

        return toolbar;
    }

    protected JPanel getToolbar2(JPanel toolbar) {
        return toolbar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float toScale() {
        String scale = (String) scaleCombo.getSelectedItem();

        return (scale == FIT_WIDTH) ? (-1.0f)
                                    : (Integer.parseInt(scale.substring(0, scale.length() - 1)) / 100.0f);
    }

    /**
     * @param f
     */
    protected void setScale(float f) {
        // Preferences prefsdemo =Preferences.userRoot().node("/com/jatools/j");
        //
        // prefsdemo.put("scale", ""+f);
        for (int i = 0; i < view.getComponentCount(); i++) {
            PageCanvas canvas = (PageCanvas) view.getComponent(i);
            canvas.setScale(f);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void enabledFitPageWidth() {
        this.scaleCombo.setSelectedItem(FIT_WIDTH);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPageCount() {
        return view.getComponentCount();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getPageView(int index) {
        return view.getComponent(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cursorImage
     *            DOCUMENT ME!
     */
    void loadCursor() {
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension cursorSize = tool.getBestCursorSize(32, 32);
        int colors = tool.getMaximumCursorColors();

        if (cursorSize.equals(new Dimension(0, 0)) || (colors == 0)) {
            return;
        }

        ImageIcon icon = null;

        try {
            icon = new ImageIcon(this.getClass().getResource("/jatools/icons/hand0.gif")); //

            if (icon == null) {
                throw new Exception("could not create cursor hand0"); //
            } else {
                hand0 = tool.createCustomCursor(icon.getImage(), new Point(16, 16), "hand0.cursor"); //
            }

            icon = new ImageIcon(this.getClass().getResource("/jatools/icons/hand1.gif")); //

            if (icon == null) {
                throw new Exception("could not create cursor hand1"); //
            } else {
                hand1 = tool.createCustomCursor(icon.getImage(), new Point(16, 16), "hand1.cursor"); //
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pp
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PageCanvas addPage(PageView pp) {
        PageCanvas c = view.add(pp);

        // c.addMouseListener(scroller);
        // c.addMouseMotionListener(scroller);
        return c;
    }

    /**
     * DOCUMENT ME!
     */
    public void clearPage() {
        view.removeAll();
    }

    /**
     * DOCUMENT ME!
     */
    public void fitPageWidth() {
        String scale = (String) scaleCombo.getSelectedItem();

        if (scale == FIT_WIDTH) {
            setScale(-1.0f);
        }
    }

    // /**
    // * DOCUMENT ME!
    // *
    // * @param defaultPageSize DOCUMENT ME!
    // */
    // public void setDefaultPageSize(Dimension defaultPageSize) {
    // this.defaultPageSize = defaultPageSize;
    // }
    class ZDraggableListener extends MouseAdapter implements MouseMotionListener {
        int lastY;

        public void mousePressed(MouseEvent e) {
            if (e.getClickCount() == 1) {
                view.setCursor(hand1);
                lastY = e.getY();
                _view.requestFocus();
            } else {
                Component c = SwingUtilities.getDeepestComponentAt(_view, e.getX(), e.getY());

                if (c instanceof PageCanvas) {
                    Point p = SwingUtilities.convertPoint(_view, e.getX(), e.getY(), c);
                    ((PageCanvas) c).hit(p.x, p.y);
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            view.setCursor(hand0);
        }

        public void mouseDragged(MouseEvent e) {
            int cy = e.getY() - lastY;

            if (cy == 0) {
                return;
            }

            Point pos = (Point) _view.getViewPosition().clone();
            pos.y -= cy;

            if ((pos.y <= 0) ||
                    ((_view.getExtentSize().height + pos.y) > _view.getViewSize().height)) {
                return;
            }

            // if (_view.contains(_p)) {
            _view.setViewPosition(pos);

            if (e.getSource() != _view) {
                lastY += cy;
            } else {
                lastY = e.getY();
            }
        }

        public void mouseMoved(MouseEvent e) {
            Component c = SwingUtilities.getDeepestComponentAt(_view, e.getX(), e.getY());

            if (c instanceof PageCanvas) {
                Point p = SwingUtilities.convertPoint(_view, e.getX(), e.getY(), c);
                ((PageCanvas) c).buildTooptip(p.x, p.y, _view);
            }
        }
    }

    /*
     *
     * _PageView:JPanel ZPageCanvas:JLabel *
     */
    public class _PageView extends JPanel implements Printable {
        Border border = new MatteBorder(1, 1, 2, 2, Color.black);
        int nCol = 1;
        int nRow = 1;
        int lastY;

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public void print() {
            if (this.getComponentCount() == 0) {
                JOptionPane.showConfirmDialog(null, "nothing can be printed."); //

                return;
            }

            
            PrinterJob job = PrinterJob.getPrinterJob();

            
            PageView pp0 = ((PageCanvas) this.getComponent(0)).getPage();
            java.awt.print.PageFormat pf = pp0.getPageFormat().toAwtFormat(true);
            job.setPrintable(this, pf);

            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /**
         * DOCUMENT ME!
         *
         * @param bi
         *            DOCUMENT ME!
         */
        public PageCanvas add(PageView pp) {
            PageCanvas pageLabel = new PageCanvas(pp);

            //pageLabel.addMouseMotionListener(pageListener);
            pageLabel.setBorder(border);
            add(pageLabel);
            pageLabel.setScale(toScale());

            return pageLabel;
        }

        public void firstRow() {
            setCurrentRow(0);
        }

        JViewport getViewPort() {
            return (JViewport) getParent().getParent();
        }

        public void nextRow() {
            setCurrentRow(getCurrentRow() + 1);
        }

        public int getCurrentRow() {
            return getViewPort().getViewPosition().y / (defaultPageSize.height + V_GAP);
        }

        public void setCurrentRow(int index) {
            index = Math.max(Math.min(index, nRow - 1), 0);

            JViewport port = getViewPort();

            Point pos = port.getViewPosition();
            pos.y = index * (defaultPageSize.height + V_GAP);

            if (((pos.y + port.getExtentSize().height) > port.getViewSize().height) &&
                    (index != 0)) {
                pos.y -= (port.getExtentSize().height - (defaultPageSize.height + V_GAP));
            }

            getViewPort().setViewPosition(pos);
        }

        public void prevRow() {
            setCurrentRow(getCurrentRow() - 1);
        }

        public void lastRow() {
            setCurrentRow(nRow - 1);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getPreferredSize() {
            int n = getComponentCount();

            if (n == 0) {
                return new Dimension(H_GAP, V_GAP);
            }

            Dimension dc = defaultPageSize; // comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;

            Dimension dp = getParent().getSize();
            int viewwidth = this.getViewPort().getWidth();

            dp.width = Math.min(dp.width, viewwidth);

            int nCol = Math.max((dp.width - H_GAP) / (w + H_GAP), 1);

            if (nCol >= n) {
                nCol = n;
            }

            int nRow = n / nCol;

            if ((nRow * nCol) < n) {
                nRow++;
            }

            int ww = (nCol * (w + H_GAP)) + H_GAP;
            int hh = (nRow * (h + V_GAP)) + V_GAP;
            Insets ins = getInsets();

            return new Dimension(ww + ins.left + ins.right, hh + ins.top + ins.bottom);
        }

        /**
         * DOCUMENT ME!
         */
        public void doLayout() {
            Insets ins = getInsets();
            int x = ins.left + H_GAP;
            int y = ins.top + V_GAP;

            int n = getComponentCount();

            if (n == 0) {
                return;
            }

            Component comp = getComponent(0);
            Dimension dc = defaultPageSize; // comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;

            Dimension dp = getParent().getSize();
            nCol = Math.max((dp.width - H_GAP) / (w + H_GAP), 1);
            nRow = n / nCol;

            if ((nRow * nCol) < n) {
                nRow++;
            }

            int index = 0;

            for (int k = 0; k < nRow; k++) {
                x = H_GAP;

                for (int m = 0; m < nCol; m++) {
                    if (index >= n) {
                        return;
                    }

                    comp = getComponent(index++);
                    comp.setBounds(x, y, w, h);
                    x += (w + H_GAP);
                }

                y += (h + V_GAP);
            }
        }

        /**
         * @return
         */
        public PageView[] getPages() {
            PageView[] pages = new PageView[getComponentCount()];

            for (int i = 0; i < pages.length; i++) {
                PageView pp = ((PageCanvas) this.getComponent(i)).getPage();
                pages[i] = pp;
            }

            return pages;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.awt.print.Printable#print(java.awt.Graphics,
         *      java.awt.print.PageFormat, int)
         */
        public int print(Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex)
            throws PrinterException {
            if (pageIndex < getComponentCount()) {
                PageView pp = ((PageCanvas) this.getComponent(pageIndex)).getPage();
                Graphics2D g2 = (Graphics2D) g;
                g2.scale(PageFormat.DOTS_PER_PX, PageFormat.DOTS_PER_PX);
                pp.paint(g2);

                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }
    }

    class _PrintAction extends AbstractAction {
        _PrintAction() {
        	super(App.messages.getString("res.6"), printIcon);
        //    putValue(Action.SMALL_ICON, printIcon);
  
        }

        /*
         * (non-Javadoc)
         *
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            print();
        }
    }
}
