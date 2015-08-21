package jatools.engine.script.debug;

import bsh.Interpreter;

import jatools.dataset.CrossIndexField;

import jatools.dom.ArrayNode;
import jatools.dom.CrossIndexNode;
import jatools.dom.DatasetNode;
import jatools.dom.GroupNode;
import jatools.dom.IndexNode;
import jatools.dom.JatoolsDocument;
import jatools.dom.RootNode;
import jatools.dom.RowNode;

import jatools.dom.field.DatasetField;
import jatools.dom.field.FixedNodeField;
import jatools.dom.field.GroupField;
import jatools.dom.field.RowField;

import jatools.dom.src.ArrayNodeSource;
import jatools.dom.src.CrossIndexNodeSource;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.IndexNodeSource;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RootNodeSource;
import jatools.dom.src.RowNodeSource;

import jatools.engine.InterpreterAware;
import jatools.engine.ValueIfClosed;

import java.lang.reflect.Array;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ScriptDebuggerBean {
    private static Map<Class, String> typeDescriptions;
    private static Map<Class, String> typeDescriptions_zh;
    private String name;
    private String value;
    private String type;
    private Object val;

    /**
     * Creates a new ScriptDebuggerBean object.
     */
    public ScriptDebuggerBean() {
    }

    /**
     * Creates a new ScriptDebuggerBean object.
     *
     * @param name
     *            DOCUMENT ME!
     * @param value
     *            DOCUMENT ME!
     */
    public ScriptDebuggerBean(String name, String value, String type, Object val) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.val = val;
    }

    private static boolean isArray(Object val) {
        return (val != null) && val.getClass().isArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param val DOCUMENT ME!
     * @param defType DOCUMENT ME!
     * @param it DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ScriptDebuggerBean create(String name, Object val, Interpreter it) {
        if (val == null) {
            return new ScriptDebuggerBean(name, "null", null, null);
        }

        ScriptDebuggerBean result = new ScriptDebuggerBean();
        result.name = name;
        result.type = ScriptDebuggerBean.getTypeDescription(val.getClass());
        result.val = val;

        if (val instanceof ValueIfClosed) {
            ValueIfClosed vic = (ValueIfClosed) val;

            if (vic instanceof InterpreterAware) {
                ((InterpreterAware) vic).setInterpreter(it);
            }

            val = vic.value();
        }

        if (isArray(val)) {
            Class e = val.getClass().getComponentType();
            String formats = null;

            if (e != null) {
                formats = e.getName().substring(e.getName().lastIndexOf('.') + 1) + "[%s]";
            } else {
                formats = "[%s]";
            }

            int length = (val != null) ? Array.getLength(val) : 0;
            result.value = String.format(formats, length);
        } else {
            result.value = val.toString();
        }

        String uglyValue = result.val.getClass().getName() + "@" +
            Integer.toHexString(result.val.hashCode());

        if (uglyValue.equals(result.value)) {
            result.value = "";
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getVal() {
        return val;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    }

    static void ensureDescriptionCreated() {
        if (typeDescriptions == null) {
            typeDescriptions = new HashMap<Class, String>();
            typeDescriptions_zh = new HashMap<Class, String>();

            put(JatoolsDocument.class, "JatoolsDocument", "文档节点");
            put(RootNode.class, "RootNode", "根节点");
            put(ArrayNode.class, "ArrayNode", "数组节点");
            put(DatasetNode.class, "DatasetNode", "数据集节点");
            put(IndexNode.class, "DatasetNode", "索引节点");
            put(CrossIndexNode.class, "CrossIndexNode", "交叉索引节点");
            put(GroupNode.class, "GroupNode", "分组节点");
            put(RowNode.class, "RowNode", "行节点");

            put(RootNodeSource.class, "RootNodeSource", "根节点源");
            put(ArrayNodeSource.class, "ArrayNodeSource", "数组节点源");
            put(DatasetNodeSource.class, "DatasetNodeSource", "数据集节点源");
            put(IndexNodeSource.class, "DatasetNodeSource", "索引节点源");
            put(CrossIndexNodeSource.class, "CrossIndexNodeSource", "交叉索引节点源");
            put(GroupNodeSource.class, "GroupNodeSource", "分组节点源");
            put(RowNodeSource.class, "RowNodeSource", "行节点源");
            put(FixedNodeField.class, "FixedNodeField", "固定字段");
            put(NodeSource.class, "NodeSource", "节点源");

            put(DatasetField.class, "DatasetField", "数据库字段");
            put(RowField.class, "RowField", "行字段");
            put(GroupField.class, "GroupField", "分组字段");
            put(CrossIndexField.class, "CrossIndexField", "交叉字段");
        }
    }

    private static void put(Class class1, String en, String zh) {
        typeDescriptions.put(class1, en);
        typeDescriptions_zh.put(class1, zh);
    }

    /**
    * DOCUMENT ME!
    *
    * @param type2 DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static String getTypeDescription(Class type2) {
        ensureDescriptionCreated();

        String result = "";

        if ((type2 != null) && !type2.isArray()) {
            result = typeDescriptions_zh.get(type2);

            if (result == null) {
                result = type2.getName().substring(type2.getName().lastIndexOf('.') + 1);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
}
