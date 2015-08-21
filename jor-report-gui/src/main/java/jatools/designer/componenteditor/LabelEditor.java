package jatools.designer.componenteditor;


import jatools.component.ComponentConstants;
import jatools.component.Label;
import jatools.component.Text;
import jatools.core.view.TextView;
import jatools.designer.InplaceEditor;
import jatools.designer.ReportPanel;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.PropertyEdit;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class LabelEditor implements ComponentEditor, ActionListener {
    static InplaceEditor editor;
    Label label;
    ComponentPeer peer;
    String textOld;
    private String oldText;

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void show(ComponentPeer peer) {
        this.peer = peer;

        if (editor == null) {
            editor = new InplaceEditor();
        }

        ReportPanel owner = peer.getOwner();
        label = (Label) peer.getComponent();

        int vertalign = 0;

        if (label instanceof Text) {
            Text text = (Text) label;
            textOld = text.getText();
            text.setText(text.getVariable());
            editor.setFont(Text.DEFAULT_FONT);

            vertalign = TextView.TOP;
        } else {
            editor.setFont(label.getFont());

            vertalign = label.getVerticalAlignment();
        }

        oldText = label.getText();

        Point p = new Point();

        owner.childPointAsScreenPoint(peer, p);

        float scale = owner.getScale();
        editor.showIn(owner.getGroupPanel(), label.getText(),
            new Rectangle((int) (scale * p.x), (int) (scale * p.y),
                (int) (scale * peer.getWidth()), (int) (scale * peer.getHeight())), vertalign, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (label instanceof Text) {
            Text text = (Text) label;

            if (editor.isChanged()) {
                text.setVariable(editor.getText());
                peer.getOwner()
                    .addEdit(new PropertyEdit(peer, ComponentConstants.PROPERTY_VARIABLE, oldText,
                        text.getVariable()));
            } else {
                text.setVariable(oldText);
            }

            text.setText(textOld);
        } else {
            if (editor.isChanged()) {
                label.setText(editor.getText());
                peer.getOwner()
                    .addEdit(new PropertyEdit(peer, ComponentConstants.PROPERTY_TEXT, oldText,
                        label.getText()));
            } else {
                label.setText(oldText);
            }
        }

        peer.getOwner().repaint();
        peer.setEditing(false);
    }
}
