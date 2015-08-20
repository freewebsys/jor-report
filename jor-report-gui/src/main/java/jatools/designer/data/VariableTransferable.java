package jatools.designer.data;

import jatools.component.Component;
import jatools.component.Text;

import jatools.designer.layer.drop.DropComponentFactory;
import jatools.designer.layer.drop.ScriptProvider;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableTransferable implements Transferable, DropComponentFactory, ScriptProvider {
    private String variable;

    /**
     * Creates a new NodeSourceTransferable object.
     *
     * @param nodeSource DOCUMENT ME!
     * @param fields DOCUMENT ME!
     * @param modifiers DOCUMENT ME!
     */
    public VariableTransferable(String variable) {
        this.variable = variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DropComponentFactory.FLAVOR, ScriptProvider.FLAVOR };
    }

    /**
     * DOCUMENT ME!
     *
     * @param flavor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return ((flavor == DropComponentFactory.FLAVOR) || (ScriptProvider.FLAVOR == flavor));
    }

    /**
     * DOCUMENT ME!
     *
     * @param flavor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedFlavorException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getScript() {
        return this.variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component[] create() {
        Text text = new Text(0, 0, 90, 20);
        text.setVariable("=" + this.variable);

        return new Component[] { text };
    }
}
