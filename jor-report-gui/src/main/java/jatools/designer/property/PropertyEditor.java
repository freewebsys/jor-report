package jatools.designer.property;

import java.awt.event.ActionListener;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface PropertyEditor {
    public static final String START_EDIT = "started.edit";
    public static final String COMMIT_EDIT = "commit.edit";
    public static final String CANCEL_EDIT = "canceled.edit";

    void addActionListener(ActionListener lst);

    void removeActionListener(ActionListener lst);

    void setSelectable(Selectable selectable);

    void registerPropertyEditor(Object key, TableCellEditor editor);

    void registerPropertyRenderer(Object key, TableCellRenderer renderer);
}
