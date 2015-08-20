/*
 * Created on 2004-2-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.designer.property.event;

import jatools.designer.Main;
import jatools.designer.data.CustomFormulaDialog;
import jatools.swingx.Chooser;

import javax.swing.JComponent;



/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventChooser implements Chooser {
    Object value;

    /* (non-Javadoc)
     * @see com.jatools.swing.ZChooser#showChooser(javax.swing.JComponent, java.lang.Object)
     */
    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object val) {

        CustomFormulaDialog d = new CustomFormulaDialog(Main.getInstance(), true, true,false); //
        d.setLocationRelativeTo(owner);

        if (d.showChooser(val)) {
            value = d.getValue();

            return true;
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.jatools.swing.ZChooser#getValue()
     */
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }
}
