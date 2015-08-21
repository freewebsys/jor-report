package jatools.data;

import jatools.accessor.AutoAccessor;
import jatools.engine.ValueIfClosed;
import jatools.engine.script.ForEach;
import jatools.engine.script.Script;

import java.util.List;

import bsh.Interpreter;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Formula extends AutoAccessor implements ValueIfClosed {
    String text;
    transient Script script;

    /**
     * Creates a new Formula object.
     *
     * @param formula DOCUMENT ME!
     */
    public Formula(String formula) {
        this.text = formula;
    }

    /**
     * Creates a new Formula object.
     */
    public Formula() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        return (script != null) ? script.eval(text) : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param formula DOCUMENT ME!
     */
    public void setText(String formula) {
        this.text = formula;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void setCalculator(Script script) {
        this.script = script;
    }

    /**
     * DOCUMENT ME!
     *
     * @param list DOCUMENT ME!
     */
    public void forEach(List list) {
        ForEach.forEach(this.script, list, this.text);
    }
}
