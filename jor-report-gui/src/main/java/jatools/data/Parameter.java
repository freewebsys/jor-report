package jatools.data;

import bsh.EvalError;
import bsh.Interpreter;

import jatools.accessor.AutoAccessor;

import jatools.db.TypeUtil;

import jatools.engine.InterpreterAware;
import jatools.engine.ValueIfClosed;

import jatools.engine.printer.ReportPrinter;

import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Parameter extends AutoAccessor implements ValueIfClosed ,InterpreterAware{
    private static Logger logger = Logger.getLogger("ZParameter");
    String name;
    String type1;
    String defaultValue;
    String prompt;
    transient Object value;
	private Interpreter it;

    /**
     * Creates a new Parameter object.
     */
    public Parameter() {
    }

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @param prompt DOCUMENT ME!
     * @param defaultValue DOCUMENT ME!
     */
    public Parameter(String name, String type, String prompt, String defaultValue) {
        this.name = name;
        this.type1 = type;
        this.defaultValue = defaultValue;
        this.prompt = prompt;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object castValue(String type, String expr)
        throws Exception {
        if (expr != null) {
            if (expr.startsWith("=")) {
                return eval(expr.substring(1));
            } else {
                return TypeUtil.valueOf(expr, TypeUtil.getSqlID(type));
            }
        } else {
            return null;
        }
    }

    private Object eval(String expr) {
        try {
			return it.eval(expr);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public void setInterpreter(Interpreter it)
    {
    	this.it = it;
    	
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
     * @param it DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        try {
            return (value != null) ? value : defaultValue();
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class type() {
        return TypeUtil.getClass(type1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param defaultValue DOCUMENT ME!
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType1() {
        return type1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object defaultValue() throws Exception {
        return castValue(type1, defaultValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType1(String type) {
        this.type1 = type;
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
    public String getPrompt() {
        return prompt;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prompt DOCUMENT ME!
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    }
}
