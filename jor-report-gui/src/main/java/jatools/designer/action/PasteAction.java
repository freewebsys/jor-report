package jatools.designer.action;


import jatools.component.Component;
import jatools.component.table.Cell;
import jatools.designer.App;
import jatools.designer.ClipBoard;
import jatools.designer.ReportPanel;
import jatools.designer.layer.table.TableEditKit;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;
import jatools.designer.undo.AddEdit;
import jatools.designer.undo.GroupEdit;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PasteAction extends EditClipAction {
    boolean cellPasteOnly = false;

    /**
     * Creates a new PasteAction object.
     */
    public PasteAction() {
        super(null, App.messages.getString("res.126"), getIcon("/jatools/icons/paste.gif"),
            getIcon("/jatools/icons/paste2.gif"));
        setStroke(ctrl(KeyEvent.VK_V));
    }

    /**
     * Creates a new PasteAction object.
     *
     * @param panel DOCUMENT ME!
     */
    public PasteAction(ReportPanel panel) {
        super(panel, App.messages.getString("res.126"), getIcon("/jatools/icons/paste.gif"),
            getIcon("/jatools/icons/paste2.gif"));
        setStroke(ctrl(KeyEvent.VK_V));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportPanel pagePanel = getReportPanel();
        TableEditKit kit = pagePanel.getTableEditKit();

        if (kit != null) {
            kit.paste();
        } else {
            ComponentPeer pagePeer = pagePanel.getComponentPeer(pagePanel.getPage().getBody());

            try {
                Component[] children = ClipBoard.getDefaultClipBoard().getContents();

                if ((children == null) || (children.length < 0)) {
                    return;
                }

                pagePanel.unselectAll();
                paste(pagePanel, children, pagePeer);

                pagePeer.getOwner().getReportPeer().getComponent().validate();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void paste(ReportPanel pagePanel, Component[] children, ComponentPeer pagePeer) {
        CompoundEdit edit = null;

        int offset = 10 * (ClipBoard.getDefaultClipBoard().getPasteCount() + 1);

        for (int i = 0; i < children.length; i++) {
            Component comp = children[i];
            comp.setCell(null);

            ComponentPeer peer = pagePanel.createPeer(comp);

            pagePeer.add(peer);
            peer.move(offset, offset);

            if (edit == null) {
                edit = new GroupEdit();
            }

            edit.addEdit(new AddEdit(peer, true));
            pagePanel.select(peer);
        }

        if (edit != null) {
            pagePanel.getUndoManager().addEdit(edit);
            pagePanel.repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cellPasteOnly DOCUMENT ME!
     */
    public void setCellPasteOnly(boolean cellPasteOnly) {
        this.cellPasteOnly = cellPasteOnly;
    }

    private ComponentPeer[] childrenAt(TablePeer tablePeer, Rectangle selection) {
        ArrayList chi = new ArrayList();

        for (int i = 0; i < tablePeer.getChildCount(); i++) {
            ComponentPeer peer = tablePeer.getChild(i);
            Cell cell = (Cell) peer.getComponent().getCell();

            if (selection.contains(cell.column, cell.row)) {
                chi.add(peer);
            }
        }

        return (ComponentPeer[]) chi.toArray(new ComponentPeer[0]);
    }
}
