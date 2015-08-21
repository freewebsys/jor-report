package jatools.designer.data;

import jatools.accessor.ProtectPublic;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Variable implements Transferable, ProtectPublic {
    public static DataFlavor thisFlavor = new DataFlavor(Variable.class, "class");
    public static DataFlavor[] flavors = new DataFlavor[] { thisFlavor };
    String displayName;
    String variableName;
    int permission;
    private Object variable;

    /**
     * Creates a new _Variable object.
     *
     * @param displayName DOCUMENT ME!
     * @param permission DOCUMENT ME!
     * @param variableName DOCUMENT ME!
     */
    public Variable(String displayName, int permission, String variableName) {
        this.displayName = displayName;
        this.variableName = variableName;
        this.permission = permission;
    }

    /**
     * Creates a new _Variable object.
     *
     * @param displayName DOCUMENT ME!
     * @param permission DOCUMENT ME!
     * @param variableName DOCUMENT ME!
     * @param var DOCUMENT ME!
     */
    public Variable(String displayName, int permission, String variableName, Object var) {
        this.displayName = displayName;
        this.variableName = variableName;
        this.permission = permission;
        this.variable = var;
    }

    /**
     * Creates a new _Variable object.
     *
     * @param displayName DOCUMENT ME!
     * @param permission DOCUMENT ME!
     */
    public Variable(String displayName, int permission) {
        this(displayName, permission, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPermission() {
        return permission;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return displayName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSettable() {
        return (permission & VariableTree.SETTABLE) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param another DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object another) {
        if (another instanceof Variable) {
            Variable an = (Variable) another;

            return (an.variableName == null) ? (this.variableName == null)
                                             : an.variableName.equals(this.variableName);
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flavor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param displayName DOCUMENT ME!
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param variable DOCUMENT ME!
     */
    public void setVariable(Object variable) {
        this.variable = variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param variableName DOCUMENT ME!
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
