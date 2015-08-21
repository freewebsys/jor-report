package jatools.designer.componenteditor;



import jatools.component.ComponentConstants;
import jatools.component.Text;
import jatools.component.Var;
import jatools.data.Formula;
import jatools.designer.Main;
import jatools.designer.ReportPanel;
import jatools.designer.data.CustomFormulaDialog;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.PropertyEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableSelectEditor implements ComponentEditor {
    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void show(ComponentPeer peer) {
        ReportPanel owner = peer.getOwner();

        CustomFormulaDialog d = new CustomFormulaDialog(Main.getInstance(), true, true, false);

        String old = (String) peer.getValue("Variable", String.class);
        Formula formula = d.start(old);

        if (formula != null) {
            String newVar = formula.getText();

            String oldVar = null;

            if (peer.getComponent() instanceof Var) {
                Var var = (Var) peer.getComponent();
                oldVar = var.getVariable();
                var.setVariable(newVar);
            }

            owner.addEdit(new PropertyEdit(peer, ComponentConstants.PROPERTY_VARIABLE, oldVar,
                    newVar));

            owner.repaint();
        }

        peer.setEditing(false);
    }
}
