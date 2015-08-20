package jatools.designer.componenteditor;

import jatools.component.BarCode;
import jatools.component.Component;
import jatools.component.Label;
import jatools.component.Text;

import jatools.designer.peer.ComponentPeer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ComponentEditorFactory {
    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     * @param ctrl
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComponentEditor createEditor(ComponentPeer peer, boolean ctrl) {
        ComponentEditor editor = null;
        Component target = peer.getComponent();

        if (ctrl && target instanceof Text) {
            editor = new VariableSelectEditor();
        } else if (target instanceof Label) {
            editor = new LabelEditor();
        } else if (target instanceof jatools.component.chart.Chart) {
            editor = new ChartEditor();
        } else if (target instanceof BarCode) {
            editor = new VariableSelectEditor();
        } else if (target instanceof jatools.component.Image) {
            editor = new ImageEditor();
        } else {
            return null;
        }

        peer.setEditing(true);

        return editor;
    }
}
