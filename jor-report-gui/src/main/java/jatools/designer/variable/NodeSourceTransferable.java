package jatools.designer.variable;

import jatools.component.Component;
import jatools.component.Label;
import jatools.component.Text;

import jatools.component.table.Table;

import jatools.designer.layer.drop.DropComponentFactory;
import jatools.designer.layer.drop.ScriptProvider;

import jatools.dom.src.NodeSource;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;

import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NodeSourceTransferable implements Transferable, DropComponentFactory, ScriptProvider {
    private int modifiers;
    private String[] fields;
    private NodeSource nodeSource;
    private boolean lastPressedNodeSource;

    /**
     * Creates a new NodeSourceTransferable object.
     *
     * @param nodeSource DOCUMENT ME!
     * @param fields DOCUMENT ME!
     * @param modifiers DOCUMENT ME!
     */
    public NodeSourceTransferable(NodeSource nodeSource, boolean lastPressedNodeSource,
        String[] fields, int modifiers) {
        this.nodeSource = nodeSource;
        this.lastPressedNodeSource = lastPressedNodeSource;
        this.fields = fields;
        this.modifiers = modifiers;
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

    private boolean ctrl() {
        return (this.modifiers & InputEvent.CTRL_MASK) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getScript() {
        if (this.lastPressedNodeSource) {
            return ctrl() ? this.nodeSource.getFullPath() : this.nodeSource.getTagName();
        } else if ((this.fields != null) && (this.fields.length > 0)) {
            String result = "";
            String prefix = ctrl() ? "$." : "";

            for (int i = 0; i < fields.length; i++) {
                String field = prefix + fields[i] + "\n";
                result += field;
            }

            return result;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component[] create() {
        if (this.lastPressedNodeSource) {
            Table table = TableFactory.getTable(nodeSource);

            if (table != null) {
                return new Component[] { table };
            }
        } else if ((this.fields != null) && (this.fields.length > 0)) {
            boolean ctrl = ctrl();

            Component[] result = new Component[this.fields.length];

            for (int i = 0; i < result.length; i++) {
                if (ctrl) {
                    Text text = new Text(0, 0, 90, 20);
                    text.setVariable("=$." + fields[i]);
                    result[i] = text;
                } else {
                    Label label = new Label(0, 0, 90, 20);
                    label.setText(fields[i]);
                    result[i] = label;
                }
            }

            return result;
        }

        return null;
    }
}
