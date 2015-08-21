package jatools.designer.peer;

import jatools.accessor.PropertyAccessorWrapper;
import jatools.accessor.PropertyDescriptor;
import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.table.GridComponent;
import jatools.designer.ReportPanel;
import jatools.designer.undo.PropertyEdit;
import jatools.designer.undo.PropertyWithLayoutEdit;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class ComponentPeer implements Observer, PropertyAccessorWrapper {
    public static final Insets NULL_INSETS = new Insets(0, 0, 0, 0);
    public static final int FOCUSED_POINT_SIZE = 10;
    public static final int HALF_FOCUSED_POINT_SIZE = FOCUSED_POINT_SIZE / 2;
    public static final int NOT_HIT = -1;
    public static final int EAST = 1;
    public static final int WEST = 2;
    public static final int SOUTH = 4;
    public static final int NORTH = 8;
    public static final int NORTH_WEST = NORTH | WEST;
    public static final int NORTH_EAST = NORTH | EAST;
    public static final int SOUTH_WEST = SOUTH | WEST;
    public static final int SOUTH_EAST = SOUTH | EAST;
    public static final int CENTER = 0;
    private Component component;
    private ArrayList children = new ArrayList();
    private ComponentPeer parent;
    private ReportPanel owner;
    private Insets insets;
    private boolean editing;
    private Map properties;

    public ComponentPeer(ReportPanel owner, Component component) {
        this.owner = owner;
        this.component = component;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected() {
        return this.owner.isSelected(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pop DOCUMENT ME!
     * @param val DOCUMENT ME!
     */
    public void setClientProperty(String pop, Object val) {
        if (this.properties == null) {
            this.properties = new HashMap();
        }

        this.properties.put(pop, val);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getClientProperty(String pop) {
        if (this.properties != null) {
            return this.properties.get(pop);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param insets DOCUMENT ME!
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResizable() {
        return this.getComponent().getCell() == null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param forced DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean remove(ComponentPeer child, boolean forced) {
        return remove(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableChild(Component child) {
        return this.getComponent().isContainer();
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableDropedChild(Component child) {
        return isAcceptableChild(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean remove(ComponentPeer child) {
        children.remove(child);
        component.remove(child.getComponent());

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getChildPeer(Component child) {
        for (int i = 0; i < this.getChildCount(); i++) {
            ComponentPeer peer = getChild(i);

            if (peer.getComponent() == child) {
                return peer;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer descendingPeer(Component child) {
        for (int i = 0; i < this.getChildCount(); i++) {
            ComponentPeer peer = getChild(i);

            if (peer.getComponent() == child) {
                return peer;
            } else {
                ComponentPeer p = peer.descendingPeer(child);

                if (p != null) {
                    return p;
                }
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAll() {
        for (int i = children.size() - 1; i >= 0; i--) {
            remove((ComponentPeer) children.get(i));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param focusedBoxes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFocusedBoxes(Point[] focusedBoxes) {
        if ((this.getComponent().getCell() != null) &&
                (!(this.getComponent() instanceof GridComponent))) {
            return 0;
        }

        Insets is = this.getComponent().getPadding();
        Point pointCache = focusedBoxes[0];
        pointCache.setLocation(-is.left, -is.top);
        owner.childPointAsScreenPoint(this, pointCache);

        int x1 = pointCache.x + 2;
        int y1 = pointCache.y + 2;
        int x2 = (x1 + getWidth()) - 5;
        int y2 = (y1 + getHeight()) - 5;

        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;

        focusedBoxes[0].setLocation(x1, y1);
        focusedBoxes[1].setLocation(cx, y1);
        focusedBoxes[2].setLocation(x2, y1);
        focusedBoxes[3].setLocation(x2, cy);
        focusedBoxes[4].setLocation(x2, y2);
        focusedBoxes[5].setLocation(cx, y2);
        focusedBoxes[6].setLocation(x1, y2);
        focusedBoxes[7].setLocation(x1, cy);

        return 8;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getComponent() {
        return component;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getTargetClass() {
        return component.getClass();
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void setComponent(Component target) {
        this.component = target;
    }

    /**
     * DOCUMENT ME!
     *
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void move(int deltaX, int deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAncestorOf(ComponentPeer peer) {
        ComponentPeer p = peer.getParent();

        if (p == this) {
            return true;
        } else if (p == null) {
            return false;
        } else {
            return isAncestorOf(p);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void setParent(ComponentPeer parent) {
        this.parent = parent;

        component.setParent(parent.getComponent());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getParent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getChild(int index) {
        if ((index >= 0) && (index < children.size())) {
            return (ComponentPeer) children.get(index);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void add(ComponentPeer child) {
        if (child == null) {
            return;
        }

        children.add(child);
        child.setParent(this);

        component.add(child.getComponent());
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param index DOCUMENT ME!
     */
    public void insert(ComponentPeer child, int index) {
        if (child == null) {
            return;
        }

        children.add(index,child);
        child.setParent(this);

        component.insert(child.getComponent(), index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int indexOf(ComponentPeer child) {
        return children.indexOf(child);
    }

    /**
     * DOCUMENT ME!
     */
    public void refreshPosition() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param loc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hitHot(Point loc) {
        int x = loc.x;
        int y = loc.y;

        int size = FOCUSED_POINT_SIZE;

        int off = size / 2;

        int x1 = 0;

        int x2 = getWidth();
        int y1 = 0;
        int y2 = getHeight();

        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;

        if ((x >= (x1)) && (x < (x1 + off))) {
            if ((y >= (y1)) && (y <= (y1 + off))) {
                return NORTH_WEST;
            } else if ((y >= (cy - off)) && (y <= (cy + off))) {
                return WEST;
            } else if ((y >= (y2 - off)) && (y <= (y2))) {
                return SOUTH_WEST;
            }
        } else if ((x >= (cx - off)) && (x <= (cx + off))) {
            if ((y >= (y1)) && (y <= (y1 + off))) {
                return NORTH;
            } else if ((y >= (y2 - off)) && (y <= (y2))) {
                return SOUTH;
            }
        } else if ((x >= (x2 - off)) && (x <= (x2))) {
            if ((y >= (y1)) && (y <= (y1 + off))) {
                return NORTH_EAST;
            } else if ((y >= (cy - off)) && (y <= (cy + off))) {
                return EAST;
            } else if ((y >= (y2 - off)) && (y <= (y2))) {
                return SOUTH_EAST;
            }
        }

        return ComponentPeer.NOT_HIT;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hit(int x, int y) {
        Rectangle b = this.getBounds();

        return b.contains(x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds() {
        return this.component.getBounds();
    }

    /**
     * DOCUMENT ME!
     *
     * @param frame DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hit(Rectangle frame) {
        Rectangle bounds = getBounds();

        return frame.contains(bounds) || frame.intersects(bounds);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void insert(ComponentPeer child) {
        children.add(0,child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @param arg DOCUMENT ME!
     */
    public synchronized void update(Observable o, Object arg) {
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void updateTargetBounds() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return component.getRegistrableProperties();
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param valueType DOCUMENT ME!
     */
    public void setValue(String prop, Object newValue, Class valueType) {
        if (owner != null && owner.isEditOpen()) {
            Object oldVal = getValue(prop, valueType);

            boolean different = false;

            if (newValue != null) {
                if (!newValue.equals(oldVal)) {
                    different = true;
                }
            } else if (oldVal != null) {
                different = true;
            }

            if (different) {
                Util.setValue(component, prop, newValue, valueType);

                if ((prop == ComponentConstants.PROPERTY_X) ||
                        (prop == ComponentConstants.PROPERTY_Y) ||
                        (prop == ComponentConstants.PROPERTY_WIDTH) ||
                        (prop == ComponentConstants.PROPERTY_HEIGHT)) {
                    owner.appendEdit(new PropertyWithLayoutEdit(this, prop, oldVal, newValue));
                } else {
                    owner.appendEdit(new PropertyEdit(this, prop, oldVal, newValue));
                }

                if (prop == ComponentConstants.PROPERTY_NAME) {
                    if ((oldVal != null) && (oldVal.toString().trim().length() > 0)) {
                        getOwner().getDocument().undeclareVariable((String) oldVal);
                    }

                    if ((newValue != null) && (newValue.toString().trim().length() > 0)) {
                        getOwner().getDocument().declareVariable((String) newValue, getComponent());
                    }
                }
            }
        } else {
            Util.setValue(component, prop, newValue, valueType);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param valueType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(String prop, Class valueType) {
        return Util.getValue(component, prop, valueType == Boolean.TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportPanel getOwner() {
        return owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getFocusColor() {
        return Color.blue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * DOCUMENT ME!
     *
     * @param editing DOCUMENT ME!
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return component.getHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return component.getWidth();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getX() {
        return component.getX();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getY() {
        return component.getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        component.setHeight(height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        component.setWidth(width);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void setX(int x) {
        component.setX(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     */
    public void setY(int y) {
        component.setY(y);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer[] getAncestorPath() {
        ComponentPeer p = this;

        while ((p = p.getParent()) != null) {
            ComponentPeer[] path = p.getAncestorPath(this);

            if (path != null) {
                return path;
            }
        }

        p = this;

        ArrayList res = new ArrayList();
        res.add(p);

        while ((p = p.getParent()) != null) {
            res.add(p);
        }

        return (ComponentPeer[]) res.toArray(new ComponentPeer[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer[] getAncestorPath(ComponentPeer peer) {
        return null;
    }

    protected int getFocusedBoxes4(Point[] focusedBoxes) {
        Point pointCache = focusedBoxes[0];
        Insets is = this.getComponent().getPadding();
        pointCache.setLocation(-is.left, -is.top);
        getOwner().childPointAsScreenPoint(this, pointCache);

        int x1 = pointCache.x + 2;
        int y1 = pointCache.y + 2;
        int x2 = (x1 + getWidth()) - 5;
        int y2 = (y1 + getHeight()) - 5;

        focusedBoxes[0].setLocation(x1, y1);

        focusedBoxes[1].setLocation(x2, y1);

        focusedBoxes[2].setLocation(x2, y2);

        focusedBoxes[3].setLocation(x1, y2);

        return 4;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMoveable() {
        return this.getComponent().getCell() == null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRemoveable() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCopiable() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     */
    public void updateBounds(int x1, int y1, int x2, int y2) {
        getComponent().setBounds(Util.toRactangle(x1, y1, x2, y2));
    }
}
