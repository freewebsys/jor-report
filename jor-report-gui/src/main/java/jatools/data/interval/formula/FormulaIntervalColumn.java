package jatools.data.interval.formula;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.data.reader.udc.UserDefinedColumn;

import java.util.ArrayList;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class FormulaIntervalColumn implements PropertyAccessor, UserDefinedColumn {
    String name;
    ArrayList formulas;

    /**
     * Creates a new ComputedColumn object.
     *
     * @param name DOCUMENT ME!
     * @param computedItems DOCUMENT ME!
     */
    public FormulaIntervalColumn(String name, ArrayList computedItems) {
        this.name = name;
        this.formulas = computedItems;
    }

    /**
     * Creates a new ComputedColumn object.
     */
    public FormulaIntervalColumn() {
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public PropertyDescriptor[] getRegistrableProperties() {
        PropertyDescriptor exps = new PropertyDescriptor("Formulas", ArrayList.class,
                PropertyDescriptor.SERIALIZABLE);

        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            exps
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getFormulas() {
        return formulas;
    }

    /**
     * DOCUMENT ME!
     *
     * @param computedItems DOCUMENT ME!
     */
    public void setFormulas(ArrayList computedItems) {
        this.formulas = computedItems;
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
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return this.getName();
    }
}
