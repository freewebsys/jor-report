/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
/*
 * Created on 2004-2-8
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



/**
 * @author zhou
 *
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TableGroupEdit extends AbstractUndoableEdit {
    TablePeer tablePeer;
    ComponentPeer[] cellPeers;
    boolean detable = false;

    /**
     * @param tablePeer
     * @param cellPeers
     */
    public TableGroupEdit(
        TablePeer tablePeer,
        ComponentPeer[] cellPeers) {
        this(tablePeer, cellPeers, false);
    }

    /**
     * Creates a new ZTableGroupEdit object.
     *
     * @param tablePeer DOCUMENT ME!
     * @param cellPeers DOCUMENT ME!
     * @param detable DOCUMENT ME!
     */
    public TableGroupEdit(
        TablePeer tablePeer,
        ComponentPeer[] cellPeers,
        boolean detable) {
        this.tablePeer = tablePeer;
        this.cellPeers = cellPeers;
        this.detable = detable;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException
     *             DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();

        if (detable) {
            degroup();
        } else {
            group();
        }
    }

    /**
     *
     */
    private void group() {
        ComponentPeer parent = tablePeer.getParent();

        for (int i = 0; i < cellPeers.length; i++) {
            parent.remove(cellPeers[i]);

            //cellPeers[i].updateTargetBounds() ;
        }

        //parent.remove( tablePeer);
        parent.add(tablePeer);
//        tablePeer.invalidate();
//        parent.getOwner().getReportPeer().validate();
    }

    /**
     *
     */
    private void degroup() {
        ComponentPeer parent = tablePeer.getParent();

        parent.remove(tablePeer);

        for (int i = 0; i < cellPeers.length; i++) {
            parent.add(cellPeers[i]);
            cellPeers[i].updateTargetBounds();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException
     *             DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

        if (detable) {
            group();
        } else {
            degroup();
        }
    }
}
