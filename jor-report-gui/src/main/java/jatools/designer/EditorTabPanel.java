package jatools.designer;

import jatools.ReportDocument;

import jatools.designer.action.CloseAction;

import jatools.swingx.CloseableTabbedPane;
import jatools.swingx.CloseableTabbedPaneListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class EditorTabPanel extends CloseableTabbedPane implements CloseableTabbedPaneListener {
    /**
     * Creates a new EditorTabPanel object.
     */
    public EditorTabPanel() {
    	
    	
    	
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.addCloseableTabbedPaneListener(this);
        this.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    resetTitle();

                    SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ReportEditor editor = getActiveEditor();

                                if (editor != null) {
                                    editor.activate();
                                }
                            }
                        });

                    // System.out.println("jjj");
                    // getActiveEditor().getReportPanel().pagePanel.groupLayer.requestFocus();
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportEditor getActiveEditor() {
        return (ReportEditor) this.getSelectedComponent();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     * @param tip
     *            DOCUMENT ME!
     * @param doc
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportEditor createEditor(String name, String tip, ReportDocument doc) {
        ReportEditor editor = new ReportEditor();
        editor.setDocument(doc);
        this.addTab(name, editor);
        this.setToolTipTextAt(this.getTabCount() - 1, tip);
        editor.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName() == ReportEditor.DIRTY_PROP) {
                        Boolean b = (Boolean) evt.getNewValue();
                        ReportEditor editor = (ReportEditor) evt.getSource();
                        resetName(editor, b.booleanValue());
                    }

                    ;
                }
            });

        return editor;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetTitle() {
        int index = this.getSelectedIndex();

        String title = App.messages.getString("res.63");

        if (index > -1) {
            String tip = this.getToolTipTextAt(index);

            if (tip == null) {
                title += App.messages.getString("res.119");
            } else {
                title += (" - " + tip);
            }
        }

        Main.getInstance().setTitle(title);
    }

    protected void resetName(ReportEditor editor, boolean b) {
        int tab = indexOfComponent(editor);

        String newtitle = this.getTitleAt(tab).trim();

        if (b && !newtitle.endsWith("*")) {
            newtitle += " *";
        } else if (!b && newtitle.endsWith("*")) {
            newtitle = newtitle.substring(0, newtitle.length()).trim();
        }

        this.setTitleAt(tab, newtitle);

        resetTitle();
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor
     *            DOCUMENT ME!
     */
    public void setActiveEditor(ReportEditor editor) {
        this.setSelectedComponent(editor);
        resetTitle();
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor
     *            DOCUMENT ME!
     * @param name
     *            DOCUMENT ME!
     * @param tip
     *            DOCUMENT ME!
     */
    public void setPrompt(ReportEditor editor, String name, String tip) {
        int i = indexOfComponent(editor);

        this.setTitleAt(i, name);
        this.setToolTipTextAt(i, tip);

        resetTitle();
    }

    /**
     * DOCUMENT ME!
     *
     * @param tabIndex
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean closeTab(int tabIndex) {
        ReportEditor editor = (ReportEditor) this.getComponentAt(tabIndex);

        return new CloseAction().closeQuery(editor);
    }
}
