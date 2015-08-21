package jatools.engine.printer;

import bsh.Primitive;

import jatools.component.Text;

import jatools.core.view.TextView;
import jatools.core.view.View;

import jatools.dom.field.NodeField;

import jatools.engine.script.Context;
import jatools.engine.script.Script;

import jatools.formatter.Format2;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TextPrinter extends LabelPrinter implements ExternalEval {
    PageNumberDelegate pageNumberDelegate = null;
    private Object externalValue;
    private Object lastValue;
    private Object value2;
    private NodeField lastField;

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View print(Context context) {
        this.pageNumberDelegate = null;

        Text t = (Text) this.getComponent();

        View result = super.print(context);

        if ((this.pageNumberDelegate != null) && (result != null)) {
            this.pageNumberDelegate.add((TextView) result);
        }

        return result;
    }

    protected String getText(Script script) {
        if (this.unprinted != null) {
            return this.unprinted;
        }

        lastValue = null;

        if (externalValue != null) {
            lastValue = (externalValue == Primitive.NULL) ? null : externalValue;
        } else {
            String var = ((Text) this.getComponent()).getVariable();

            if (var != null) {
                lastValue = script.get(var);

                if (lastValue instanceof PageNumberDelegate) {
                    this.pageNumberDelegate = (PageNumberDelegate) lastValue;
                    lastValue = " ";
                }
            }
        }
        


        return format(lastValue);
    }

    private String format(Object data) {
        if (data != null) {
            Format2 format1 = this.lastStyle.getFormat();

            return (format1 != null) ? format1.format(data) : data.toString();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void setExternalValue(Object val) {
        this.externalValue = val;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getLastValue() {
        return lastValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasExternalValue() {
        return externalValue != null;
    }
}
