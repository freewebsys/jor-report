package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.data.reader.results.ResultSetReader;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dom.ArrayNode;
import jatools.dom.ElementBase;
import jatools.dom.src.util.GetterReflect;
import jatools.engine.script.Script;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.TableModel;

import org.w3c.dom.Node;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class ArrayNodeSource extends NodeSource {
    private String tagName;
    private String expression;
    private String beanName;
    private String elementClass;

    /**
     * Creates a new ArrayNodeSource object.
     *
     * @param tagName DOCUMENT ME!
     * @param expression DOCUMENT ME!
     */
    public ArrayNodeSource(String tagName, String expression, String elementClass) {
        super();
        this.tagName = tagName;
        this.expression = expression;

        this.elementClass = elementClass;
    }

    /**
     * Creates a new ArrayNodeSource object.
     */
    public ArrayNodeSource() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExpression() {
        return expression;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node create(Node parent, Script script) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node[] createNodeList(Node parent, Script script) {
        ArrayList groups = new ArrayList();

        Object val = script.eval(this.getExpression());

        Object[] keys = null;

        if (val instanceof Object[]) {
            keys = (Object[]) val;
        } else if (val instanceof List) {
            keys = ((List) val).toArray();
        } else if (val instanceof Iterator) {
            ArrayList a = new ArrayList();
            Iterator it = (Iterator) val;

            while (it.hasNext()) {
                a.add(it.next());
            }

            keys = a.toArray();
        } else if (val instanceof ResultSet) {
            keys = toArrays((ResultSet) val);
        } else if (val instanceof TableModel) {
            keys = toArrays((TableModel) val);
        } else {
            keys = new Object[] {
                    val
                };
        }

        for (int i = 0; i < keys.length; i++) {
            ArrayNode groupNode = new ArrayNode((ElementBase) parent, this.getTagName(), keys[i]);

            groupNode.setSource(this);
            groups.add(groupNode);
        }

        return (Node[]) groups.toArray(new Node[0]);
    }

    private Object[] toArrays(TableModel model) {
        ArrayList a = new ArrayList();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] vals = new Object[model.getColumnCount()];

            for (int j = 0; j < vals.length; j++) {
                vals[j] = model.getValueAt(i, j);
            }

            a.add(vals);
        }

        return a.toArray();
    }

    private Object[] toArrays(ResultSet set) {
        ResultSetReader reader = new ResultSetReader(set);

        try {
            ArrayList a = new ArrayList();

            Dataset ds = reader.read(null, -1);

            for (int i = 0; i < ds.getRowCount(); i++) {
                a.add(new RowElement(ds, i));
            }

            return a.toArray();
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        return new Object[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._CHILDREN,
            new PropertyDescriptor("TagName", String.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("Expression", String.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("ElementClass", String.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("KeyExpression", String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param expression DOCUMENT ME!
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tagName DOCUMENT ME!
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beanName DOCUMENT ME!
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getElementClass() {
        return elementClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param elementClass DOCUMENT ME!
     */
    public void setElementClass(String elementClass) {
        this.elementClass = elementClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getFields() {
        if (this.elementClass != null) {
            return GetterReflect.getFields(this.elementClass);
        } else {
            return null;
        }
    }
}
