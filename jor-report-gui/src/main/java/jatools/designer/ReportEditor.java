package jatools.designer;

import jatools.ReportDocument;

import jatools.designer.variable.NodeSourceTree;

import jatools.engine.imgloader.ImageLoader;
import jatools.engine.imgloader.PainterImageLoader;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */

public class ReportEditor extends JPanel {
    public final static String DIRTY_PROP = "dirty.prop";
    public static final String CLOSE = "close";
    public static final String OPEN = "open";
    public static final String SAVE = "save";
    public static final String PRINT_ACTION = "print";
    public static final String EXPORT_ACTION = "export";
    private ReportPanel reportPanel;
    private boolean dirty;
    private ArrayList actionListeners = new ArrayList();
    private ImageLoader imageLoader;
    private ImageCacher imageCacher;
    private VariableTreeModel treeModel;
    private NodeSourceTree nodeSourceTree;

    /**
    * Creates a new ReportEditor object.
    */
    public ReportEditor() {
        reportPanel = new ReportPanel();
        setLayout(new GridLayout());
        add(reportPanel);
        setDocument(null);

        this.getUndoManager().addListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getID() == UndoManager.ADD_UNDO) {
                        setDirty(true);
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSourceTree getNodeSourceTree() {
        if (nodeSourceTree == null) {
            nodeSourceTree = new NodeSourceTree(true);
            nodeSourceTree.setEditor(this);
            nodeSourceTree.expandPath(nodeSourceTree.getPathForRow(0));
        }

        return nodeSourceTree;
    }

    /**
     * DOCUMENT ME!
     *
     * @param keyCode DOCUMENT ME!
     * @param modifiers DOCUMENT ME!
     * @param action DOCUMENT ME!
     * @param condition DOCUMENT ME!
     */
    public void registerKeyAction(int keyCode, int modifiers, Action action, int condition) {
        KeyStroke stroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        reportPanel.registerKeyAction(condition, stroke, action);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TreeModel getVariableModel() {
        if (treeModel == null) {
            this.treeModel = new VariableTreeModel(this.getDocument().getVariableContext());
        }

        return treeModel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param keyCode DOCUMENT ME!
     * @param modifiers DOCUMENT ME!
     * @param action DOCUMENT ME!
     */
    public void registerKeyAction(int keyCode, int modifiers, Action action) {
        registerKeyAction(keyCode, modifiers, action, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     */
    public void setDocument(ReportDocument doc) {
        reportPanel.setDocument(doc);
        setDirty(false);

        fireActionListener((doc != null) ? OPEN : CLOSE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addSelectionChangeListener(ChangeListener lst) {
        reportPanel.addSelectionListener1(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeSelectionChangeListener(ChangeListener lst) {
        reportPanel.removeSelectionListener(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        return reportPanel.getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportPanel getReportPanel() {
        return reportPanel;
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        setDocument(null);
    }

    private void fireActionListener(String command) {
        ActionEvent e = new ActionEvent(this, 0, command);

        for (Iterator it = actionListeners.iterator(); it.hasNext();) {
            ActionListener l = (ActionListener) it.next();
            l.actionPerformed(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst) {
        actionListeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeActionListener(ActionListener lst) {
        actionListeners.remove(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getReport() {
        return getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UndoManager getUndoManager() {
        return reportPanel.getUndoManager();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dirty DOCUMENT ME!
     */
    public void setDirty(boolean dirty) {
        boolean old = this.dirty;

        this.dirty = dirty;
        this.firePropertyChange(DIRTY_PROP, old, this.dirty);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageLoader getImageLoader() {
        if (this.imageLoader == null) {
            this.imageLoader = new PainterImageLoader();
        }

        return imageLoader;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageCacher getImageCacher() {
        if (this.imageCacher == null) {
            this.imageCacher = new ImageCacher();
        }

        return this.imageCacher;
    }

    /**
     * DOCUMENT ME!
     */
    public void activate() {
        this.reportPanel.activate();
    }
}
