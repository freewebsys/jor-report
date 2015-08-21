package jatools;

import jatools.component.Component;
import jatools.data.Formula;
import jatools.data.Parameter;
import jatools.engine.PrintConstants;
import jatools.util.Map;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableContext extends Map implements PrintConstants {
    public static final String DECLARE = "declare";
    public static final String UNDECLARE = "undeclare";
    public static final int PARAMETER = 2;
    public static final int FORMULA = 8;
    public static final int COMMAND = 16;
    public static final int DATADICTIONARY = 32;
    public static final int SYSTEM0 = 128;
    public static final int COMPONENT = 256;
    public static final int INVISIBLE = 512;
    public static final String PARAMETER_PREFIX = "$parameter";
    public static final int ALL = PARAMETER | FORMULA | COMPONENT | COMMAND | DATADICTIONARY |
        INVISIBLE | SYSTEM0;
    public static final int ALL_BUT_COMP = PARAMETER | FORMULA | COMMAND | DATADICTIONARY |
        INVISIBLE | SYSTEM0;
    private static String[] systemVariables = {
            PrintConstants.NOW, PrintConstants.TODAY, PrintConstants.TOTAL_PAGE_NUMBER,
            PrintConstants.PAGE_INDEX, PrintConstants.MODEL, PrintConstants.WORKING_DIR,
            PrintConstants.HTTP_REQUEST, PrintConstants.HTTP_SESSION, PrintConstants.AS2
        };
    private ReportDocument document;
    private PropertyChangeSupport listenerSupport;

    /**
     * Creates a new VariableContext object.
     */
    public VariableContext() {
        this(null);
    }

    /**
     * Creates a new VariableContext object.
     *
     * @param doc DOCUMENT ME!
     */
    public VariableContext(ReportDocument doc) {
        this.document = doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator names(int type) {
        return new _Iterator(type, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void declareVariable(String name, Object value) {
        put(name, value);

        if (this.listenerSupport != null) {
            this.listenerSupport.firePropertyChange(DECLARE, name, value);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param list DOCUMENT ME!
     */
    public void addChangeListener(PropertyChangeListener list) {
        if (this.listenerSupport == null) {
            this.listenerSupport = new PropertyChangeSupport(this);
        }

        this.listenerSupport.addPropertyChangeListener(list);
    }

    /**
     * DOCUMENT ME!
     *
     * @param oldKey DOCUMENT ME!
     * @param newKey DOCUMENT ME!
     * @param newVal DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean replaceVariable(String oldKey, String newKey, Object newVal) {
        boolean ret = replace(oldKey, newKey, newVal);

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param varName DOCUMENT ME!
     */
    public void undeclareVariable(String varName) {
        Object val = this.getVariable(varName);

        remove(varName);

        if (this.listenerSupport != null) {
            this.listenerSupport.firePropertyChange(UNDECLARE, varName, val);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param varName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getVariable(String varName) {
        return get(varName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getSystemVariables() {
        return systemVariables;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void removeAll(int i) {
        Iterator it = names(i);

        while (it.hasNext()) {
            it.remove();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator variables(int type) {
        return new _Iterator(type, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param document DOCUMENT ME!
     */
    public void setDocument(ReportDocument document) {
        this.document = document;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        return document;
    }

    class _Iterator implements Iterator {
        private int type;
        private Iterator iterator;
        private Object nextValue;
        private String nextName;
        private boolean nameResult;

        _Iterator(int type, boolean nameResult) {
            this.type = type;
            this.iterator = ((ArrayList) keyCache.clone()).iterator();
            this.nameResult = nameResult;
        }

        public void remove() {
            undeclareVariable(nextName);
        }

        public boolean hasNext() {
            while (this.iterator.hasNext()) {
                nextName = (String) this.iterator.next();
                nextValue = getVariable(nextName);

                if (accept(nextName, nextValue)) {
                    return true;
                }
            }

            return false;
        }

        public Object next() {
            return nameResult ? nextName : nextValue;
        }

        private boolean accept(String name, Object value) {
            if ((this.type == PARAMETER) && value instanceof Parameter) {
                return true;
            } else if ((this.type == FORMULA) && value instanceof Formula) {
                return true;
            } else if ((this.type == COMPONENT) && value instanceof Component) {
                return true;
            } else {
                return false;
            }
        }
    }
}
