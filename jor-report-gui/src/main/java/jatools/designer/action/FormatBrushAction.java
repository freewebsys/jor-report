package jatools.designer.action;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.ReportEditor;
import jatools.designer.SelectionState;
import jatools.designer.layer.Layer;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.property.PropertyTableModel;
import jatools.designer.toolbox.Icon25x25ToggleButton;
import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FormatBrushAction extends ReportAction implements Layer, ChangeListener {
    private static final Object NULL = new Object();
    private static PropertyDescriptor[] props;
    Map src;
    private boolean selectionChanged;
    private ReportEditor lastEditor;

    /**
     * Creates a new FormatBrushAction object.
     */
    public FormatBrushAction() {
        super(App.messages.getString("res.584"), getIcon("/jatools/icons/brush.gif"),
            getIcon("/jatools/icons/formatbrush2.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (isFirstAction()) {
            Main.getInstance().getEditorPanel().addEditorChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        editorChanged();
                    }
                });
        }

        if (isFormatting()) {
            endFormat();
        } else {
            startFormat();
        }
    }

    private void startFormat() {
        Object[] sel = this.getEditor().getReportPanel().getSelection();

        if (sel.length > 0) {
            src = new HashMap();

            ComponentPeer peer = (ComponentPeer) sel[0];

            PropertyDescriptor[] properties = getProperties();

            for (int i = 0; i < properties.length; i++) {
                if (PropertyTableModel.existProperty(peer, properties[i])) {
                    Object val = peer.getValue(properties[i].getPropertyName(),
                            properties[i].getPropertyType());

                    if (val == null) {
                        val = NULL;
                    }

                    src.put(properties[i], val);
                }
            }

            startFormat(this.getEditor());
        }
    }

    private void editorChanged() {
        if (isFormatting()) {
            endFormat(lastEditor);

            lastEditor = getEditor();

            if (lastEditor != null) {
                startFormat(lastEditor);
            }
        }
    }

    private boolean isFormatting() {
        return lastEditor != null;
    }

    private void endFormat() {
        if (lastEditor != null) {
            endFormat(lastEditor);

            if (lastEditor != null) {
                SelectionState state = lastEditor.getReportPanel().getPeerSelection().getState();
                this.enabled(state);
            }

            lastEditor = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getProperties() {
        if (props == null) {
            props = new PropertyDescriptor[] {
                    ComponentConstants._BACK_COLOR, ComponentConstants._FORE_COLOR,
                    ComponentConstants._BORDER, ComponentConstants._FONT,
                    ComponentConstants._HORIZONTAL_ALIGNMENT, ComponentConstants._VERTICAL_ALIGNMENT,
                    ComponentConstants._WORDWRAP, ComponentConstants._FORMAT
                };
        }

        return props;
    }

    private boolean isFirstAction() {
        return props == null;
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasTooltip() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTooltip(int x, int y) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeActionListener(ActionListener lst) {
    }

    /**
     * DOCUMENT ME!
     */
    public void wake() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWaken() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker DOCUMENT ME!
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(KeyEvent e) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseMoved(int modifier, int x, int y, int deltaX, int deltaY) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseDoublePressed(int modifier, int x, int y) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mousePressed(int modifier, int x, int y) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        if (this.selectionChanged) {
            ComponentPeer[] peers = (ComponentPeer[]) this.getEditor().getReportPanel()
                                                          .getSelection();

            boolean done = false;

            this.getEditor().getReportPanel().openEdit();

            Iterator it = src.entrySet().iterator();

            while (it.hasNext()) {
                Entry e = (Entry) it.next();
                PropertyDescriptor des = (PropertyDescriptor) e.getKey();
                Object val = e.getValue();

                if (val == NULL) {
                    val = null;
                }

                for (int i = 0; i < peers.length; i++) {
                    ComponentPeer peer = peers[i];

                    if (PropertyTableModel.existProperty(peer, des)) {
                        peer.setValue(des.getPropertyName(), val, des.getPropertyType());
                        done = true;
                    }
                }
            }

            if (done) {
                this.getEditor().getReportPanel().closeEdit();
            } else {
                this.getEditor().getReportPanel().discardEdit();
            }

            this.selectionChanged = false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            endFormat();

            JToggleButton b = (JToggleButton) getValue(Icon25x25ToggleButton.BUTTON);
            b.setSelected(false);
        }

        return false;
    }

    private void endFormat(ReportEditor editor) {
        editor.getReportPanel().getGroupPanel().removeMouseReleaseListener(this);
        editor.getReportPanel().getGroupPanel().removeKeyTypedListener(this);
        editor.getReportPanel().getGroupPanel().setCursorLocked(false);
    }

    private void startFormat(ReportEditor editor) {
        editor.getReportPanel().getGroupPanel().setCursor(CursorUtil.FORMAT_CURSOR);
        editor.getReportPanel().getGroupPanel().setCursorLocked(true);
        editor.getReportPanel().getGroupPanel().addMouseReleaseListener(this);
        editor.getReportPanel().getGroupPanel().addKeyTypedListener(this);
        editor.getReportPanel().getGroupPanel().requestFocus();
        editor.getReportPanel().addSelectionListener1(this);
        this.lastEditor = editor;
        this.selectionChanged = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyPressed(KeyEvent e) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyReleased(KeyEvent e) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasPopupMenu() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        this.selectionChanged = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        JToggleButton b = (JToggleButton) getValue(Icon25x25ToggleButton.BUTTON);

        setEnabled((state.getCount() > 0) || b.isSelected());
    }

    /**
     * DOCUMENT ME!
     *
     * @param focused DOCUMENT ME!
     */
    public void setFocused(boolean focused) {
    }
}
