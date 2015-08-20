package jatools.swingx;

import jatools.db.TypeUtil;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TypesCombobox extends JComboBox {
    /**
     * Creates a new TypesCombobox object.
     *
     * @param nameid DOCUMENT ME!
     */
    public TypesCombobox(int nameid) {
        Object[] names = TypeUtil.getSupportedTypes(nameid);

        setModel(new DefaultComboBoxModel(names));
    }
}
