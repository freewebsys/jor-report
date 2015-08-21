package jatools.designer.data;

import jatools.designer.App;
import jatools.designer.Main;
import jatools.swingx.Chooser;

import javax.swing.JComponent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableChooser implements Chooser {
    String value;

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        VariableExplorer explorer = new VariableExplorer(Main.getInstance(), App.messages.getString("res.554"));
        explorer.setLocationRelativeTo(owner);

        explorer.setVariable((String) value);
        explorer.show();

        this.value = (explorer.xok == VariableExplorer.NULL) ? null : explorer.getVariable();

        return (explorer.xok != VariableExplorer.CANCEL);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }
}
