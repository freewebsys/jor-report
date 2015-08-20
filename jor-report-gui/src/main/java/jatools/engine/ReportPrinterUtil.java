package jatools.engine;

import jatools.ReportDocument;
import jatools.VariableContext;

import jatools.core.view.PageView;

import jatools.data.Parameter;

import jatools.engine.script.ReportContext;

import jxl.write.WritableSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportPrinterUtil implements PrintConstants {
    public static final String FIELD_NAME_PREFIX = "$";

    private ReportPrinterUtil() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param paramValues DOCUMENT ME!
     */
    public static void parse23(ReportDocument doc, ReportContext context, Map paramValues) {
        VariableContext vm = doc.getVariableContext();
        context.set(ReportContext.VARIABLE_MANAGER, vm);

        Iterator it = vm.names();

        while (it.hasNext()) {
            String var = (String) it.next();
            Object value = vm.getVariable(var);
            context.set(var, value);
        }

        it = parameters(doc);

        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            Object value = null;

            if (paramValues != null) {
                value = paramValues.get(p.getName());

                if (value != null) {
                    if (value instanceof String && (p.type() != String.class)) {
                        try {
                            value = p.castValue(p.getType1(), (String) value);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

            if (value == null) {
                value = p;
            }

            context.set(p.getName(), value);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Iterator parameters(ReportDocument doc) {
        VariableContext vm = doc.getVariableContext();
        Map params = new HashMap();

        Iterator it = vm.variables(VariableContext.PARAMETER);

        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            params.put(p.getName(), p);
        }

        return params.values().iterator();
    }

    static void setParameter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param xs DOCUMENT ME!
     * @param ys DOCUMENT ME!
     * @param ts DOCUMENT ME!
     * @param sheet DOCUMENT ME!
     */
    public void splitView(PageView view, ArrayList xs, ArrayList ys, ArrayList ts,
        WritableSheet sheet) {
    }
}
