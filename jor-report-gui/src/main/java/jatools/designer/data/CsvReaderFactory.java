package jatools.designer.data;

import jatools.VariableContext;
import jatools.data.reader.DatasetReader;
import jatools.data.reader.csv.CsvReader;
import jatools.util.Util;

import java.awt.Component;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class CsvReaderFactory implements DatasetReaderFactory {
    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader create(Component parent, NameChecker checker) {
        CsvReader result = new CsvReader();
        result.setLocalFile( true);

        CsvReaderDialog editor = new CsvReaderDialog("创建CSV数据集", (CsvReader) result,
                Util.getCDF(parent));
        editor.setNameChecker(checker);
        editor.setLocationRelativeTo(parent);
        editor.show();

        if (editor.isExitOK()) {
            editor.getReader().copy((CsvReader) result);

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
        CsvReaderDialog editor = new CsvReaderDialog("编辑CSV数据集", (CsvReader) reader,
                Util.getCDF(parent));
        editor.setNameChecker(checker);
        editor.setLocationRelativeTo(parent);
        editor.show();

        if (editor.isExitOK()) {
            editor.getReader().copy((CsvReader) reader);
        }

        return editor.isExitOK();
    }

    /**
     * DOCUMENT ME!
     *
     * @param vm DOCUMENT ME!
     */
    public void setVM(VariableContext vm) {
    }
}
