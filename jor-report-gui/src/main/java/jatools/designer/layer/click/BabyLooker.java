package jatools.designer.layer.click;

import jatools.component.Component;

import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.PanelStore;
import jatools.component.table.TableBase;

import jatools.designer.Point2;
import jatools.designer.ReportPanel;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.peer.TablePeer;

import jatools.designer.undo.AddEdit;
import jatools.designer.undo.DeleteEdit;
import jatools.designer.undo.GroupEdit;
import jatools.designer.undo.TablePropertyEdit;

import java.awt.Insets;
import java.awt.Point;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BabyLooker {
    private static Point location = new Point();
    ReportPanel owner;
    ComponentPeer childPeer;
    Point2 xPoint;
    Point2 yPoint;

    /**
     * Creates a new BabyLooker object.
     *
     * @param owner DOCUMENT ME!
     */
    public BabyLooker(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void clickGrow(Component comp, int x, int y) {
        location.setLocation(x, y);

        comp.validate();

        ComponentPeer nearestContainer = findNearestContainter(comp, x, y);

        if (nearestContainer != null) {
            owner.screenPointAsChildPoint(nearestContainer, location);

            Insets is = nearestContainer.getComponent().getPadding();
            location.translate(-is.left, -is.top);
            comp.setX(location.x);
            comp.setY(location.y);

            childPeer = ComponentPeerFactory.createPeerDeeply(owner, comp);
            add(nearestContainer, childPeer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void dragDropGrow(Component[] comps, int x, int y) {
        if (this.owner.getScale() != 1.0f) {
            x = (int) (x / owner.getScale());
            y = (int) (y / owner.getScale());
        }

        location.setLocation(x, y);

        Component comp = comps[0];
        comp.validate();

        ComponentPeer nearestContainer = findNearestDroppableContainter(comp, x, y);
        CompoundEdit edit = new GroupEdit();

        if (nearestContainer != null) {
            owner.screenPointAsChildPoint(nearestContainer, location);

            Insets is = nearestContainer.getComponent().getPadding();
            location.translate(-is.left, -is.top);
            childPeer = ComponentPeerFactory.createPeerDeeply(owner, comp);

            if (nearestContainer instanceof TablePeer) {
                edit.addEdit(new TablePropertyEdit(nearestContainer));

                TableBase t = (TableBase) nearestContainer.getComponent();

                Cell cell = t.getCellAt(location.x, location.y);
                PanelStore liststore = t.getPanelstore();
                CellStore cellstore = t.getCellstore();

                int i = 0;

                while (true) {
                    add(childPeer, comp, edit, cell, liststore, cellstore);
                    cell = new Cell(comp.getCell().row, comp.getCell().column2() + 1);
                    i++;

                    if (i < comps.length) {
                        comp = comps[i];
                        
                        childPeer = ComponentPeerFactory.createPeerDeeply(owner, comp);
                    } else {
                        break;
                    }
                }

                edit.addEdit(new TablePropertyEdit(nearestContainer));
                owner.addEdit(edit);
            } else {
                x = location.x;

                int i = 0;

                while (true) {
                    comp.setX(x);
                    comp.setY(location.y);

                    nearestContainer.add(childPeer);

                    AddEdit e = new AddEdit(childPeer, true);
                    edit.addEdit(e);

                    i++;
                    x += (comp.getWidth() + 2);

                    if (i < comps.length) {
                        comp = comps[i];
                        childPeer = ComponentPeerFactory.createPeerDeeply(owner, comp);
                    } else {
                        break;
                    }
                }

                owner.addEdit(edit);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param edit DOCUMENT ME!
     * @param cell DOCUMENT ME!
     * @param liststore DOCUMENT ME!
     * @param cellstore DOCUMENT ME!
     */
    public void add(ComponentPeer childPeer, Component comp, CompoundEdit edit, Cell cell,
        PanelStore liststore, CellStore cellstore) {
        Component deleted = cellstore.getComponentOver(cell.row, cell.column);

        if (deleted != null) {
            ComponentPeer deletedPeer = owner.getComponentPeer(deleted);

            int index = deletedPeer.getParent().indexOf(deletedPeer);

            if (deletedPeer.getParent().remove(deletedPeer)) {
                edit.addEdit(new DeleteEdit(deletedPeer, index));
            }

            cell = (Cell) deleted.getCell().clone();
        }

        comp.setCell(cell);

        Component p = liststore.getComponentOver(cell.row, cell.column);
        ComponentPeer parentPeer = owner.getComponentPeer(p);

        parentPeer.add(childPeer);

        AddEdit e1 = new AddEdit(childPeer, true);
        edit.addEdit(e1);
    }

    private void add(ComponentPeer parent, ComponentPeer child) {
        parent.add(child);

        AddEdit edit = new AddEdit(child, true);

        owner.addEdit(edit);
        owner.repaint();
    }

    private ComponentPeer findNearestContainter(Component comp, int x, int y) {
        ComponentPeer hitPeer = owner.findComponentPeerAt(x, y);

        while (hitPeer != null) {
            if (hitPeer.isAcceptableChild(comp)) {
                return hitPeer;
            }

            hitPeer = hitPeer.getParent();
        }

        return null;
    }

    private ComponentPeer findNearestDroppableContainter(Component comp, int x, int y) {
        ComponentPeer hitPeer = owner.findComponentPeerAt(x, y);

        while (hitPeer != null) {
            if (hitPeer.isAcceptableDropedChild(comp)) {
                return hitPeer;
            }

            hitPeer = hitPeer.getParent();
        }

        return null;
    }
}
