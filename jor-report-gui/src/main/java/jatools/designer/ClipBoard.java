package jatools.designer;

import jatools.component.Component;
import jatools.component.ComponentContainer;

import jatools.xml.XmlReader;
import jatools.xml.XmlWriter;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class ClipBoard {
    private final static String XML_ID = "XmlforAnyReport"; //
    private static ClipBoard defaultClipBoard;
    private static Clipboard delegate;
    private static int pasteCount = 0;

    /**
     * Creates a new ZClipBoard object.
     */
    private ClipBoard(Clipboard sysClipBoard) {
        this.delegate = sysClipBoard;
    }

    /**
     * Creates a new ClipBoard object.
     */
    public ClipBoard() {
        this.delegate = new Clipboard(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Component[] clone(Component[] src) {
        try {
            ClipBoard cp = new ClipBoard();
            cp.setContents(src);

            return cp.getContents();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new Component[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ClipBoard getDefaultClipBoard() {
        if (defaultClipBoard == null) {
            defaultClipBoard = new ClipBoard(Toolkit.getDefaultToolkit().getSystemClipboard());
        }

        return defaultClipBoard;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void setContents(Component[] source) throws Exception {
        if ((source == null) || (source.length == 0)) {
            return;
        }

        // if (!isSameParent(source)) {

        // }
        Component parent = source[0].getParent();
        ComponentContainer comp = new ComponentContainer();
        comp.setName(XML_ID);

        for (int i = 0; i < source.length; i++)
            comp.add(source[i]);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        XmlWriter.write(comp, os);

        String text = os.toString("UTF8"); //

        StringSelection selection = new StringSelection(text);
        delegate.setContents(selection, null);

        for (int i = 0; i < source.length; i++)
            source[i].setParent(parent);

        pasteCount = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Component[] getContents() throws Exception {
        Transferable contents = delegate.getContents(this);

        if (contents == null) {
            return null;
        }

        String text = (String) (contents.getTransferData(DataFlavor.stringFlavor));

        if (text.indexOf(XML_ID) == -1) {
            return null;
        }

        ComponentContainer comp = (ComponentContainer) XmlReader.read(new ByteArrayInputStream(
                    text.getBytes("UTF8"))); //

        emptyName(comp);

        pasteCount++;

        Component[] comps = new Component[comp.getChildCount()];

        for (int i = 0; i < comps.length; i++) {
            comps[i] = comp.getChild(i);
        }

        return comps;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPasteCount() {
        return pasteCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isSameParent(Component[] source) {
        try {
            Component firstOne = source[0];

            for (int i = 1; i < source.length; i++)
                if (firstOne.getParent() != source[i].getParent()) {
                    return false;
                }
        } catch (Exception ex) {
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp
     *            DOCUMENT ME!
     */
    private void emptyName(Component comp) {
        comp.setName(""); //

        for (int i = 0; i < comp.getChildCount(); i++)
            emptyName(comp.getChild(i));
    }
}
