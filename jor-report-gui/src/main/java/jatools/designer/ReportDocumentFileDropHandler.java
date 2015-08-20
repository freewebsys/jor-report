package jatools.designer;

import jatools.ReportDocument;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;


/**
 * 用于监听系统文件拖放到设计器，自动打开被拖放ReportDocument 的xml文件
 * 在Main,和ReportPanel中使用
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportDocumentFileDropHandler extends TransferHandler {
    private DataFlavor fileFlavor;

    /**
     * Creates a new FileDropHandler object.
     */
    public ReportDocumentFileDropHandler() {
        fileFlavor = DataFlavor.javaFileListFlavor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean importData(JComponent c, Transferable t) {
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }

        try {
            java.util.List files = (java.util.List) t.getTransferData(fileFlavor);

            for (int i = 0; i < files.size(); i++) {
                File file = (File) files.get(i);

                try {
                    ReportDocument doc = ReportDocument.load(file);
                    Main.getInstance()
                        .createEditor(doc, ReportDocument.getCachedFile(doc).getName(),
                        ReportDocument.getCachedFile(doc).getAbsolutePath(), true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedFlavorException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param flavors DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasFileFlavor(flavors)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasFileFlavor(DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (fileFlavor.equals(flavors[i])) {
                return true;
            }
        }

        return false;
    }

   
}
