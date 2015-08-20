package jatools.designer.data.jdbc;

import jatools.VariableContext;
import jatools.data.reader.DatasetReader;
import jatools.data.reader.sql.SqlReader;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.SqlReaderDialog;
import jatools.designer.data.DatasetReaderFactory;
import jatools.designer.data.NameChecker;

import java.awt.Component;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SqlReaderFactory implements DatasetReaderFactory {
	
    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader create(Component parent, NameChecker checker) {
        SqlReader result = new SqlReader();

        SqlReaderDialog editor = new SqlReaderDialog(App.messages.getString("res.556"), Main.getInstance(),
                (SqlReader) result, (NameChecker) checker);
        editor.show();

        if (editor.isOK()) {
            editor.getReader().copy((SqlReader) result);

            return result;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean edit(DatasetReader reader, Component parent, NameChecker checker) {
        SqlReaderDialog editor = new SqlReaderDialog(App.messages.getString("res.557"), Main.getInstance(),
                (SqlReader) reader, (NameChecker) checker);
        editor.show();

        if (editor.isOK()) {
            editor.getReader().copy((SqlReader) reader);
        }

        return editor.isOK();
    }

    /**
     * DOCUMENT ME!
     *
     * @param vm DOCUMENT ME!
     */
    public void setVM(VariableContext vm) {
    }
}
