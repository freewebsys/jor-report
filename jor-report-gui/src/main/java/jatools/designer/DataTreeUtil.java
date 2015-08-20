package jatools.designer;

import jatools.data.reader.DatasetReader;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.designer.config.Configuration;
import jatools.engine.script.ReportContext;
import jatools.swingx.SimpleTreeNode;
import jatools.util.Util;

import java.util.Iterator;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DataTreeUtil {
    public static final int FIELD = 1;
    public static final int EDITABLE = 1;
    public static final int REMOVABLE = 2;
    public static final int INSERTABLE = 4;
    static final Icon fieldIcon = Util.getIcon("/jatools/icons/field.gif");
    static final Icon sqlIcon = Util.getIcon("/jatools/icons/dataset.gif");

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimpleTreeNode asTree(Configuration context) {
        SimpleTreeNode rootNode = new SimpleTreeNode(App.messages.getString("res.107"), sqlIcon);

        if (context != null) {
            for (Iterator iter = context.getReaders().iterator(); iter.hasNext();) {
                DatasetReader reader = (DatasetReader) iter.next();

                SimpleTreeNode d = new SimpleTreeNode(reader, sqlIcon);
                d.setTooltip(reader.getDescription());
                rootNode.add(d);
            }
        }

        return rootNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimpleTreeNode createSqlNode(DatasetReader reader) {
        SimpleTreeNode result = new SimpleTreeNode(reader, sqlIcon);
        result.setTooltip(reader.getDescription());

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimpleTreeNode asTree(DatasetReader reader) {
        if (reader == null) {
            return null;
        }

        SimpleTreeNode readerCatagory = new SimpleTreeNode(reader, sqlIcon);

        try {
            Dataset rows = reader.read(ReportContext.getDefaultContext(), 0);

            for (int i = 0; i < rows.getColumnCount(); i++) {
                String alias = reader.getName() + "." + rows.getColumn(i);

                SimpleTreeNode treeNode = new SimpleTreeNode(rows.getColumn(i), fieldIcon, FIELD);

                //     treeNode.setProperty(SimpleTreeNode.ALIAS, alias);
                readerCatagory.add(treeNode);
            }
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        return readerCatagory;
    }

    static boolean isAcceptable(int acceptableType, int checkedType) {
        return (acceptableType & checkedType) != 0;
    }
}
