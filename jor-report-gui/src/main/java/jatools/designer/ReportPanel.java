package jatools.designer;

import jatools.ReportDocument;
import jatools.VariableContext;

import jatools.component.Component;
import jatools.component.Page;

import jatools.component.table.TableBase;

import jatools.designer.layer.LayerContainer;
import jatools.designer.layer.click.CreateLayer;
import jatools.designer.layer.click.SelectLayer;
import jatools.designer.layer.dbclick.DoubleClickLayer;
import jatools.designer.layer.drag.FrameSelectLayer;
import jatools.designer.layer.drag.MoveLayer;
import jatools.designer.layer.drag.ResizeLayer;
import jatools.designer.layer.keyboard.KeyboardMoveLayer;
import jatools.designer.layer.keyboard.KeyboardResizeLayer;
import jatools.designer.layer.page.PageLayer;
import jatools.designer.layer.painter.ComponentPaintLayer;
import jatools.designer.layer.painter.FocusedBoxPainter;
import jatools.designer.layer.painter.SelectionFramePainter;
import jatools.designer.layer.painter.TopmostPaintLayer;
import jatools.designer.layer.table.BlankCellLoader;
import jatools.designer.layer.table.FocusRequest;
import jatools.designer.layer.table.TableEditKit;
import jatools.designer.layer.table.TableLayer;
import jatools.designer.layer.table.TablePanelEditLayer;
import jatools.designer.layer.test.TestLayer;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.peer.TablePeer;

import jatools.designer.property.PropertyEditor;
import jatools.designer.property.Selectable;

import jatools.designer.ruler.RulerManager;

import jatools.designer.undo.GroupEdit;

import jatools.util.CursorUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ReportPanel extends JPanel implements Selectable, ActionListener {
    private PeerSelection peerSelection = new PeerSelection();
    private CompoundEdit compoundEdit = null;
    private JScrollPane scrollPanel;
    private SelectionFramePainter selectedFrameLayer;
    private TableLayer tableLayer;
    public boolean simple;
    private Action[] clipActions;
    private TagSelector ts;
    private Map comp2peer = new HashMap();
    private UndoManager undoManager = new UndoManager(this);
    private ReportDocument doc;
    private _LayeredPane pagePanel;

    /**
     * Creates a new ReportPanel object.
     */
    public ReportPanel() {
        pagePanel = new _LayeredPane();

        JPanel c = new JPanel();
        c.setLayout(new FlowLayout(FlowLayout.LEFT));
        c.setBorder(BorderFactory.createLoweredBevelBorder());
        c.add(pagePanel);

        ts = new TagSelector();

        JPanel north = new JPanel(new BorderLayout());
        north.add(ts, BorderLayout.CENTER);

        scrollPanel = new JScrollPane(c);

        JPanel p = new JPanel(new BorderLayout());
        p.add(scrollPanel, BorderLayout.CENTER);
        p.add(north, BorderLayout.NORTH);

        ts.setSelectable(this);

        setLayout(new BorderLayout());
        add(p);

        this.undoManager.addListener(new LayoutAfterUndo(this));
    }

    /**
     * DOCUMENT ME!
     */
    public void reselect() {
        peerSelection.reselect();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action[] getClipActions() {
        if (clipActions == null) {
            return new Action[0];
        } else {
            return clipActions;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        return Main.getInstance().getActiveEditor().getReportPanel() == this;
    }

    /**
     * DOCUMENT ME!
     */
    public void prepareChildDone() {
        this.setBaby(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param baby
     *            DOCUMENT ME!
     */
    public void setBaby(Component baby) {
        this.pagePanel.setBaby(baby);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        collectPropertyEdit(command);
    }

    private void collectPropertyEdit(String command) {
        if (command == PropertyEditor.START_EDIT) {
            this.openEdit();
        } else if (command == PropertyEditor.COMMIT_EDIT) {
            this.closeEdit();
        } else {
            this.compoundEdit = null;
        }

        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param condition
     *            DOCUMENT ME!
     * @param stroke
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     */
    public void registerKeyAction(int condition, KeyStroke stroke, Action action) {
        this.pagePanel.groupLayer.registerKeyboardAction(action, stroke, condition);
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit
     *            DOCUMENT ME!
     */
    public void appendEdit(UndoableEdit edit) {
        compoundEdit.addEdit(edit);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditOpen() {
        return compoundEdit != null;
    }

    /**
     * DOCUMENT ME!
     */
    public void openEdit() {
        compoundEdit = new GroupEdit();
    }

    /**
     * DOCUMENT ME!
     */
    public void closeEdit() {
        if (compoundEdit != null) {
            addEdit(compoundEdit);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void discardEdit() {
        this.compoundEdit = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param anEdit
     *            DOCUMENT ME!
     */
    public void addEdit(UndoableEdit anEdit) {
        this.undoManager.addEdit(anEdit);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectionCount() {
        return peerSelection.getCount();
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp
     *            DOCUMENT ME!
     */
    public void select(Component comp) {
        ComponentPeer peer = getComponentPeer(comp);

        if (peer != null) {
            peerSelection.select(peer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected(Component comp) {
        ComponentPeer peer = getComponentPeer(comp);

        return peerSelection.isSelected(peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComponentPeer findComponentPeerAt(ComponentPeer peer, int x, int y) {
        if (!peer.hit(x, y)) {
            return null;
        }

        int x0 = peer.getX() + peer.getComponent().getPadding().left;
        int y0 = peer.getY() + peer.getComponent().getPadding().top;

        for (int i = peer.getChildCount() - 1; i >= 0; i--) {
            ComponentPeer child = peer.getChild(i);

            if (child.hit(x - x0, y - y0)) {
                x -= x0;
                y -= y0;

                child = findComponentPeerAt(child, x, y);

                return child;
            }
        }

        return peer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer findComponentPeerAt(int x, int y) {
        return findComponentPeerAt(getReportPeer(), x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp
     *            DOCUMENT ME!
     */
    public void unselect(Component comp) {
        ComponentPeer peer = getComponentPeer(comp);
        unselect(peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     */
    public void unselect(ComponentPeer peer) {
        if (peer != null) {
            peerSelection.unselect(peer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     * @param screenPoint
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point screenPointAsChildPoint(ComponentPeer child, Point screenPoint) {
        screenPoint.x -= (child.getX());
        screenPoint.y -= (child.getY());

        if (child.getParent() == null) {
            return screenPoint;
        } else {
            Insets is = child.getParent().getComponent().getPadding();
            screenPoint.x -= is.left;
            screenPoint.y -= is.top;

            return screenPointAsChildPoint(child.getParent(), screenPoint);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     * @param childPoint
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point childPointAsScreenPoint(ComponentPeer child, Point childPoint) {
        Insets is = child.getComponent().getPadding();
        childPoint.x += (child.getX() + is.left);
        childPoint.y += (child.getY() + is.top);

        if (child.getParent() == null) {
            return childPoint;
        } else {
            return childPointAsScreenPoint(child.getParent(), childPoint);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getSelection(int index) {
        return peerSelection.getSelection(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected(ComponentPeer peer) {
        return peerSelection.isSelected(peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc
     *            DOCUMENT ME!
     */
    public void setDocument(ReportDocument doc) {
        setDocument(doc, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc
     *            DOCUMENT ME!
     * @param auto
     *            DOCUMENT ME!
     */
    public void setDocument(ReportDocument doc, boolean auto) {
        unselectAll();

        if (doc == null) {
            this.pagePanel.setPage(null);
        } else {
            this.doc = doc;

            this.pagePanel.setPage(doc.getPage());
        }

        undoManager.clear();
        this.doc = doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        return this.doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getSelection() {
        return peerSelection.getSelection();
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst
     *            DOCUMENT ME!
     */
    public void addSelectionListener1(ChangeListener lst) {
        peerSelection.addSelectionListener(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst
     *            DOCUMENT ME!
     */
    public void removeSelectionListener(ChangeListener lst) {
        peerSelection.removeSelectionListener(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer createPeer(Component target) {
        if (target instanceof TableBase) {
            TableBase t = (TableBase) target;

            if (t.isRootTable()) {
                BlankCellLoader.load(t);
            }
        }

        ComponentPeer peer = ComponentPeerFactory.createPeer(this, target);

        for (int i = 0; i < target.getChildCount(); i++) {
            Component child = target.getChild(i);

            peer.add(createPeer(child));
        }

        return peer;
    }

    private static ComponentPeer findPeer(String peerName, ComponentPeer from) {
        if (peerName == null) {
            return null;
        }

        if (peerName.equals(from.getComponent().getName())) {
            return from;
        }

        for (int i = 0; i < from.getChildCount(); i++) {
            ComponentPeer found = findPeer(peerName, from.getChild(i));

            if (found != null) {
                return found;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Component getComponent() {
        return this.pagePanel.groupLayer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frame
     *            DOCUMENT ME!
     */
    public void selectAt(Rectangle frame) {
        peerSelection.selectAt(this.pagePanel.reportPeer, frame);
    }

    /**
     * DOCUMENT ME!
     */
    public void unselectAll() {
        peerSelection.unselectAll();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getReportPeer() {
        return this.pagePanel.reportPeer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     */
    public void select(ComponentPeer peer) {
        peerSelection.select(peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isUndoable() {
        return this.undoManager.canUndo();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Page getPage() {
        return this.pagePanel.page;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JScrollPane getScrollPanel() {
        return scrollPanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LayerContainer getGroupPanel() {
        return this.pagePanel.groupLayer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SelectionFramePainter getSelectedFrameLayer() {
        return selectedFrameLayer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TablePeer getActiveTable() {
        return tableLayer.getEditKit().getTablePeer();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getBaby() {
        return this.pagePanel.getBaby();
    }

    /**
     * DOCUMENT ME!
     *
     * @param clipActions
     *            DOCUMENT ME!
     */
    public void setClipActions(Action[] clipActions) {
        this.clipActions = clipActions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param target
     *            DOCUMENT ME!
     * @param peer
     *            DOCUMENT ME!
     */
    public void setComponent2Peer(Component target, ComponentPeer peer) {
        comp2peer.put(target, peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getComponentPeer(Component target) {
        return (ComponentPeer) comp2peer.get(target);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableEditKit getTableEditKit() {
        if (this.pagePanel.groupLayer.getActiveLayer() == tableLayer) {
            return tableLayer.getEditKit();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UndoManager getUndoManager() {
        return this.undoManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PeerSelection getPeerSelection() {
        return peerSelection;
    }

    /**
     * DOCUMENT ME!
     */
    public void rollbackSelection() {
        this.peerSelection.rollback();
    }

    /**
     * DOCUMENT ME!
     */
    public void saveSelection() {
        this.peerSelection.save();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getScale() {
        return this.pagePanel.scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f
     *            DOCUMENT ME!
     */
    public void setScale(float f) {
        this.pagePanel.setScale(f);
    }

    /**
     * DOCUMENT ME!
     */
    public void activate() {
        this.pagePanel.groupLayer.requestFocus();
    }

    class _LayeredPane extends JLayeredPane {
        private float scale = 1.0f;
        private ComponentPaintLayer componentLayer;
        private MoveLayer moveLayer;
        private LayerContainer groupLayer;
        private FrameSelectLayer groupSelectLayer;
        private Page page;
        private ComponentPeer reportPeer;
        private RulerManager rulerManager;
        private Component baby;

        _LayeredPane() {
            groupLayer = new LayerContainer(ReportPanel.this);
            selectedFrameLayer = new SelectionFramePainter(ReportPanel.this, groupLayer, Color.black);

            CreateLayer createLayer = new CreateLayer(ReportPanel.this);
            moveLayer = new MoveLayer(ReportPanel.this, selectedFrameLayer);
            tableLayer = new TableLayer(ReportPanel.this);

            groupSelectLayer = new FrameSelectLayer(ReportPanel.this);
            componentLayer = new ComponentPaintLayer(ReportPanel.this, this, true);

            this.setLayout(new OverlayLayout(this));

            TopmostPaintLayer paintSelectionLayer = new TopmostPaintLayer(ReportPanel.this);

            paintSelectionLayer.registerPainter(componentLayer);
            paintSelectionLayer.registerPainter(new FocusedBoxPainter(ReportPanel.this, 4,
                    Color.blue));

            TablePanelEditLayer tablelistLayer = new TablePanelEditLayer(ReportPanel.this);
            tableLayer.setQuitRequest((FocusRequest) tablelistLayer);
            tableLayer.setMenuProvider(tablelistLayer);

            paintSelectionLayer.registerPainter(tablelistLayer);
            paintSelectionLayer.registerPainter(tableLayer);
            paintSelectionLayer.registerPainter(groupSelectLayer);

            add(paintSelectionLayer, new Integer(4));

            add(groupLayer, new Integer(11));

            ResizeLayer resizeLayer = new ResizeLayer(ReportPanel.this, selectedFrameLayer);
            PageLayer pageResizer = new PageLayer(ReportPanel.this);
            paintSelectionLayer.registerPainter(pageResizer);

            groupLayer.addLayer(new TestLayer(ReportPanel.this));

            groupLayer.addLayer(createLayer);
            groupLayer.addLayer(tablelistLayer);
            groupLayer.addLayer(tableLayer);
            groupLayer.addLayer(pageResizer);
            groupLayer.addLayer(new SelectLayer(ReportPanel.this));

            groupLayer.addLayer(resizeLayer);
            groupLayer.addLayer(groupSelectLayer);
            groupLayer.addLayer(new DoubleClickLayer(ReportPanel.this));

            groupLayer.addLayer(moveLayer);

            groupLayer.addLayer(new KeyboardResizeLayer(ReportPanel.this));
            groupLayer.addLayer(new KeyboardMoveLayer(ReportPanel.this));
        }

        public Component getBaby() {
            return this.baby;
        }

        public void setBaby(Component baby) {
            if (baby != null) {
                groupLayer.setCursor(CursorUtil.CLICK_PLAY_CURSOR);

                groupLayer.setCursorLocked(true);
            } else {
                groupLayer.setCursorLocked(false);
                groupLayer.setCursor(CursorUtil.DEFAULT_CURSOR);
            }

            this.baby = baby;
        }

        void setPage(Page page) {
            if (page == null) {
                this.page = null;
                reportPeer = null;
                componentLayer.setReport(null);

                if (rulerManager != null) {
                    rulerManager.setSizer(null);
                }
            } else {
                this.page = page;

                this.reportPeer = createPeer(page);

                componentLayer.setReport(page);

                for (Iterator iter = doc.getVariableContext().names(VariableContext.INVISIBLE);
                        iter.hasNext();) {
                    String var = (String) iter.next();
                    Object value = doc.getVariable(var);
                }

                select(reportPeer);
                invalidate();
                validate();
            }
        }

        public Dimension getPreferredSize() {
            if (page != null) {
                if (getScale() == 1.0f) {
                    return new Dimension(page.getWidth() + 1, page.getHeight() + 1);
                } else {
                    return new Dimension((int) ((page.getWidth() + 1) * scale),
                        (int) ((page.getHeight() + 1) * scale));
                }
            } else {
                return new Dimension(0, 0);
            }
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
            reselect();
            invalidate();
            getParent().validate();
        }
    }
}
