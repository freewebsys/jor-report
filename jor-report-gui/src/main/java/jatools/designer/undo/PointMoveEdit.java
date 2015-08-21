/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.designer.undo;

import jatools.designer.Point2;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class PointMoveEdit extends AbstractUndoableEdit {
    Point2 point;
    int deltaX;
    int deltaY;

    /**
     * Creates a new ZPointMoveEdit object.
     *
     * @param point DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public PointMoveEdit(Point2 point, int deltaX, int deltaY) {
        this.point = point;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();

        point.move(deltaX, deltaY);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

        point.move(-deltaX, -deltaY);
    }
}
