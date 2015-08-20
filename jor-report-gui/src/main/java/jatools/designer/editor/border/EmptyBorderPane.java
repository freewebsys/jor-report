package jatools.designer.editor.border;

import jatools.core.view.Border;
import jatools.designer.App;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class EmptyBorderPane extends JPanel implements BorderPane {
    /**
     * Creates a new ZEmptyBorderPane object.
     */
    public EmptyBorderPane() {
        buildUI();
    }

    /**
     * DOCUMENT ME!
     */
    public String toString() {
        return App.messages.getString("res.346"); //
    }

    /**
     * DOCUMENT ME!
     */
    private void buildUI() {
        setLayout(new BorderLayout());

        JLabel prompt = new JLabel(App.messages.getString("res.446")); //
        prompt.setHorizontalAlignment(JLabel.CENTER);
        this.add(prompt, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JComponent getComponent() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener lst) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener lst) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Border getValue() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setValue(Object value) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCompatible(Object value) {
        return value == null;
    }
}
