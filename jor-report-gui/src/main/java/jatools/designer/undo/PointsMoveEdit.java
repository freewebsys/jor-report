package jatools.designer.undo;

import jatools.designer.Point2;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class PointsMoveEdit extends AbstractUndoableEdit {
    Point2[] points;
    int deltaX;
    int deltaY;

    public PointsMoveEdit(Point2[] points,
                            int deltaX,
                            int deltaY) {
        this.points = points;
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

        for (int i = 0; i < points.length; i++) {
            points[i].move(deltaX, deltaY);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

        for (int i = 0; i < points.length; i++) {
            points[i].move(-deltaX, -deltaY);
        }
    }
}
