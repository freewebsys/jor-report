package jatools.swingx.scripteditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.ComponentUI;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ScriptEditor extends JTextPane {
    private boolean asTemplate;

    /**
    * Creates a new Highlighted object.
    *
    * @param args DOCUMENT ME!
    */
    public ScriptEditor() {
        this(false);
    }

    /**
     * Creates a new ScriptEdtor object.
     *
     * @param template DOCUMENT ME!
     */
    public ScriptEditor(boolean asTemplate) {
        setEditorKit(new HighlightKit(asTemplate));
        this.asTemplate = asTemplate;

        UndoManager manager = new UndoManager();
        this.getDocument().addUndoableEditListener(manager);

        Action undoAction = new UndoAction(manager);
        Action redoAction = new RedoAction(manager);

        // Assign the actions to keys
        registerKeyboardAction(undoAction,
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
        registerKeyboardAction(redoAction,
            KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI ui = getUI();

        return (parent != null) ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        JDialog d = new JDialog();
        d.getContentPane().add(new ScriptEditor());
        d.show();
    }

    public class UndoAction extends AbstractAction {
        private UndoManager manager;

        public UndoAction(UndoManager manager) {
            this.manager = manager;
        }

        public void actionPerformed(ActionEvent evt) {
            try {
                manager.undo();
            } catch (CannotUndoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    // The Redo action
    public class RedoAction extends AbstractAction {
        private UndoManager manager;

        public RedoAction(UndoManager manager) {
            this.manager = manager;
        }

        public void actionPerformed(ActionEvent evt) {
            try {
                manager.redo();
            } catch (CannotRedoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
