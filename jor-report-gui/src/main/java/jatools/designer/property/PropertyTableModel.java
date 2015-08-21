package jatools.designer.property;



import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyAccessorWrapper;
import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.core.view.Border;
import jatools.designer.App;
import jatools.designer.Main;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.table.DefaultTableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PropertyTableModel extends DefaultTableModel {
    static Map editableDescriptorsCache = new HashMap();
    private ArrayList propertyNames = new ArrayList();
    private ArrayList propertyEntries = new ArrayList();
    private boolean selectionChanged = true;
    ArrayList filters = new ArrayList();
    private Object[] selections;

    PropertyTableModel() {
        super(new Object[] { App.messages.getString("res.303"), App.messages.getString("res.304") }, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        } else {
            return super.isCellEditable(row, column);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getClassAt(int row) {
        if (row >= propertyEntries.size()) {
            return null;
        }

        return ((PropertyDescriptor) propertyEntries.get(row)).getPropertyType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int row, int column) {
        if (selectionChanged) {
            resetPropertyEntries();
            selectionChanged = false;
        }

        PropertyDescriptor des = (PropertyDescriptor) propertyEntries.get(row);
        String propertyName = des.getPropertyName();

        if (column == 0) {
            return App.localize(propertyName);
        }

        Object value = null;

        Object[] selection = getSelection();

        for (int i = 0; i < selection.length; i++) {
            PropertyAccessorWrapper wrapper = (PropertyAccessorWrapper) selection[i];
            Object curValue = null;

            try {
                curValue = wrapper.getValue(propertyName, des.getPropertyType());
            } catch (Exception ex) {
            }

            if (i == 0) {
                value = curValue;

                continue;
            }

            if (value != null) {
                if (!value.equals(curValue)) {
                    return null;
                }
            } else if (curValue == null) {
                return null;
            }
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getSelection() {
        if (this.selections == null) {
            if (Main.getInstance().getActiveEditor() != null) {
                return Main.getInstance().getActiveEditor().getReportPanel().getSelection();
            } else {
                return new Object[0];
            }
        } else {

            return selections;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPropertyValue(String prop, Class type) {
        Object[] selection = getSelection();

        if ((selection != null) && (selection.length > 0)) {
            PropertyAccessorWrapper wrapper = (PropertyAccessorWrapper) selection[0];

            return wrapper.getValue(prop, type);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor getPropertyEntry(int row) {
        return (PropertyDescriptor) propertyEntries.get(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPropertyValue(int row, int column) {
        return getValueAt(row, column);
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectionChanged DOCUMENT ME!
     */
    public void setSelectionChanged(boolean selectionChanged) {
        this.selectionChanged = selectionChanged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param selection DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setValueAtX(Object value, int row, int column, Object[] selection)
        throws Exception {
        if (column == 0) {
            return;
        }

        PropertyDescriptor propertyEntry = (PropertyDescriptor) propertyEntries.get(row);

        if (selection == null) {
            selection = getSelection();
        }

        for (int i = 0; i < selection.length; i++) {
            PropertyAccessorWrapper comp = (PropertyAccessorWrapper) selection[i];
            String propertyName = propertyEntry.getPropertyName();

            Object newValue = toPrimitiveObject(value, propertyEntry.getPropertyType());
            Class valueType = propertyEntry.getPropertyType();

            comp.setValue(propertyName, newValue, valueType);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void resetPropertyEntries() {
        propertyEntries.clear();

        Object[] selection = getSelection();

        for (int i = 0; i < selection.length; i++) {
            ArrayList props = editablePropertyDescriptors((PropertyAccessor) selection[i]);

            if (propertyEntries.isEmpty()) {
                propertyEntries.addAll(props);
            } else {
                propertyEntries.retainAll(props);
            }
        }

        doFilter(propertyEntries);
        setRowCount(propertyEntries.size());
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void select(Object a) {
        this.selections = new Object[] { a };
        propertyEntries.clear();

        ArrayList props = editablePropertyDescriptors((PropertyAccessor) a);
        propertyEntries.addAll(props);
        setRowCount(propertyEntries.size());
    }

    void doFilter(ArrayList props) {
        for (Iterator i = filters.iterator(); i.hasNext();) {
            PropertyFilter filter = (PropertyFilter) i.next();
            filter.filter(props);
        }
    }

    void addPropertyFilter(PropertyFilter filter) {
        filters.add(filter);
    }

    private Object toPrimitiveObject(Object value, Class propertyClass)
        throws Exception {
        if (!propertyClass.isPrimitive()) {
            return value;
        }

        try {
            if (propertyClass == Integer.TYPE) {
                return Integer.valueOf((String) value);
            } else if (propertyClass == Float.TYPE) {
                return Float.valueOf((String) value);
            } else if (propertyClass == Boolean.TYPE) {
                return Boolean.valueOf((String) value);
            } else if (propertyClass == Double.TYPE) {
                return Double.valueOf((String) value);
            }
        } catch (Exception ex) {
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList editablePropertyDescriptors(Object object) {
        if (!(object instanceof PropertyAccessorWrapper)) {
            throw new java.lang.IllegalArgumentException(App.messages.getString("res.305") + object.getClass().getName() +
                App.messages.getString("res.306"));
        }

        PropertyAccessorWrapper wrapper = (PropertyAccessorWrapper) object;

        ArrayList descriptors = (ArrayList) editableDescriptorsCache.get(wrapper.getTargetClass());

        if (descriptors == null) {
            descriptors = new ArrayList();

            PropertyDescriptor[] props = wrapper.getRegistrableProperties();

            for (int i = 0; i < props.length; i++) {
                PropertyDescriptor descriptor = props[i];

                if (descriptor.isEditable()) {
                    descriptors.add(descriptor);
                }
            }

            editableDescriptorsCache.put(wrapper.getTargetClass(), descriptors);
        }

        return descriptors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean existProperty(Object obj, PropertyDescriptor prop) {
        return editablePropertyDescriptors(obj).contains(prop);
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList _editablePropertyDescriptors(Object object) {
        if (!(object instanceof PropertyAccessorWrapper)) {
            throw new java.lang.IllegalArgumentException(App.messages.getString("res.305") + object.getClass().getName() +
                App.messages.getString("res.306"));
        }

        PropertyAccessorWrapper wrapper = (PropertyAccessorWrapper) object;

        ArrayList descriptors = (ArrayList) editableDescriptorsCache.get(wrapper.getTargetClass());

        if (descriptors == null) {
            descriptors = new ArrayList();

            PropertyDescriptor[] props = wrapper.getRegistrableProperties();

            for (int i = 0; i < props.length; i++) {
                PropertyDescriptor descriptor = props[i];

                if (descriptor.isEditable()) {
                    descriptors.add(descriptor);
                }
            }

            editableDescriptorsCache.put(wrapper.getTargetClass(), descriptors);
        }

        return descriptors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean existProperty(String prop) {
        for (int i = 0; i < getRowCount(); i++) {
            PropertyDescriptor des = (PropertyDescriptor) propertyEntries.get(i);
            String propertyName = des.getPropertyName();

            if (propertyName.equals(prop)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param fontProp DOCUMENT ME!
     */
    public void setFontAt(Object value, String fontProp) {
        Object[] selection = getSelection();

        for (int i = 0; i < selection.length; i++) {
            PropertyAccessorWrapper comp = (PropertyAccessorWrapper) selection[i];

            Font oldVal = (Font) comp.getValue(ComponentConstants.PROPERTY_FONT, Font.class);
            Font newVal = null;

            if (fontProp == ComponentConstants.PROPERTY_FONT_NAME) {
                newVal = new Font((String) value, oldVal.getStyle(), oldVal.getSize());
            } else if (fontProp == ComponentConstants.PROPERTY_FONT_BOLD) {
                int style = oldVal.getStyle();
                style = ((Boolean) value).booleanValue() ? (style | Font.BOLD) : (style &
                    (~Font.BOLD));
                newVal = new Font(oldVal.getFontName(), style, oldVal.getSize());
            } else if (fontProp == ComponentConstants.PROPERTY_FONT_ITALIC) {
                int style = oldVal.getStyle();
                style = ((Boolean) value).booleanValue() ? (style | Font.ITALIC)
                                                         : (style & (~Font.ITALIC));
                newVal = new Font(oldVal.getFontName(), style, oldVal.getSize());
            } else if (fontProp == ComponentConstants.PROPERTY_FONT_SIZE) {
                newVal = new Font(oldVal.getFontName(), oldVal.getStyle(),
                        ((Integer) value).intValue());
            }

            comp.setValue(ComponentConstants.PROPERTY_FONT, newVal, Font.class);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setBorder(String border) {
        Object[] selection = getSelection();

        for (int i = 0; i < selection.length; i++) {
            PropertyAccessorWrapper comp = (PropertyAccessorWrapper) selection[i];

            Border newVal = (Border) comp.getValue(ComponentConstants.PROPERTY_BORDER, Border.class);

            newVal = unitBorder(newVal, border);
            comp.setValue(ComponentConstants.PROPERTY_BORDER, newVal, Border.class);
        }
    }

    protected Border unitBorder(Border border, String b) {
        if (b == null) {
            return null;
        }

        if ((border == null) || (!(border instanceof Border))) {
            return new Border(1, b, Color.BLACK);
        }

        Border newVal = (Border) border;

        return newVal;
    }
}
