package jatools.swingx;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class SpinEditor extends JSpinner {
    private static Integer MAX_VALUE = new Integer(Integer.MAX_VALUE);
    private static Integer MIN_VALUE = new Integer(0);
    private static Integer STEP_SIZE = new Integer(1);
    SpinnerNumberModel numberModel;

    /**
    * Creates a new ZSpinEditor object.
    */
    private SpinEditor() {
        this(MIN_VALUE, MAX_VALUE);
        
    }

    /**
     * Creates a new ZSpinEditor object.
     *
     * @param value DOCUMENT ME!
     */
    public SpinEditor(double value) {
        this(new Double(value), new Double(0), new Double(Double.MAX_VALUE), new Double(1.0));
    }

    /**
    * Creates a new ZSpinEditor object.
    *
    * @param minValue DOCUMENT ME!
    * @param maxValue DOCUMENT ME!
    */
    public SpinEditor(Integer minValue, Integer maxValue) {
        this(minValue, minValue, maxValue, STEP_SIZE);
    }

    /**
    * Creates a new ZSpinEditor object.
    *
    * @param value DOCUMENT ME!
    * @param minValue DOCUMENT ME!
    * @param maxValue DOCUMENT ME!
    * @param step DOCUMENT ME!
    */
    public SpinEditor(Number value, Integer minValue, Integer maxValue, Integer step) {
        super(new SpinnerNumberModel(value, minValue, maxValue, step));

        numberModel = (SpinnerNumberModel) getModel();

        add(new JSpinner.NumberEditor(this));
      
    }

    /**
     * Creates a new ZSpinEditor object.
     *
     * @param value DOCUMENT ME!
     * @param minValue DOCUMENT ME!
     * @param maxValue DOCUMENT ME!
     * @param step DOCUMENT ME!
     */
    public SpinEditor(Double value, Double minValue, Double maxValue, Double step) {
        super(new SpinnerNumberModel(value, minValue, maxValue, step));

        numberModel = (SpinnerNumberModel) getModel();

        add(new JSpinner.NumberEditor (this));
      
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int intValue() {
        return ((Integer) getValue()).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double doubleValue() {
        return Double.parseDouble(getValue() + ""); //
    }

    /**
    * DOCUMENT ME!
    *
    * @param minValue DOCUMENT ME!
    */
    public void setMin(Integer minValue) {
        numberModel.setMinimum(minValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(int value) {
        setValue(new Integer(value));
    }

    /**
    * DOCUMENT ME!
    *
    * @param maxValue DOCUMENT ME!
    */
    public void setMax(Integer maxValue) {
        numberModel.setMaximum(maxValue);
    }

    /**
    * DOCUMENT ME!
    *
    * @param step DOCUMENT ME!
    */
    public void setStep(Integer step) {
        numberModel.setStepSize(step);
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        JComponent content = new SpinEditor();

        JDialog d = new JDialog((Frame) null, "Just For Test !"); //
        d.setModal(true);
        d.getContentPane().add(content, BorderLayout.CENTER);
        d.pack();
        d.show();
    }
}
