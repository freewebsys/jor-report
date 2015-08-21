package jatools.designer.toolbox;

import jatools.designer.action.ReportAction;
import jatools.swingx.SwingUtil;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class Icon25x25ToggleButton extends JToggleButton {
    /**
     * DOCUMENT ME!
     */
    public static final String BUTTON = "for.button";

    /**
     * Creates a new Icon25x25ToggleButton object.
     *
     * @param action DOCUMENT ME!
     */
    public Icon25x25ToggleButton(Action action) {
        super(action);

        setText(null);
        SwingUtil.setSize( this,new Dimension(25, 25));
        action.putValue(BUTTON, this);
        if (action != null) {
            Icon icon2 = (Icon) action.getValue(ReportAction.ICON2);

            if (icon2 != null) {
                this.setDisabledIcon(icon2);
            }
        }
    }
}
