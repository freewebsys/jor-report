package jatools.engine.printer;


import jatools.ReportDocument;
import jatools.VariableContext;
import jatools.accessor.ProtectPublic;
import jatools.core.view.CompoundView;
import jatools.core.view.DisplayStyleManager;
import jatools.core.view.PageView;
import jatools.core.view.View;
import jatools.data.Formula;
import jatools.data.Parameter;
import jatools.data.reader.sql.SqlReader;
import jatools.dataset.Dataset;
import jatools.dataset.Key;
import jatools.designer.App;
import jatools.dom.ReportDataModel;
import jatools.engine.PrintConstants;
import jatools.engine.PrinterListener;
import jatools.engine.System2;
import jatools.engine.imgloader.AwtImageLoader;
import jatools.engine.script.Context;
import jatools.engine.script.Script;
import jatools.formatter.DateFormat;
import jatools.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportPrinter implements PrintConstants, ProtectPublic {
    private static boolean NO_CACHE_MODEL = Util.getProperty("no.cache.model") != null;

  //  private final static ThreadLocal localScript2 = new ThreadLocal();
    private final static ThreadLocal localReaders = new ThreadLocal();
    private ReportDocument doc;
    private Context context;
    private ArrayList listeners;
    private ArrayList pages = new ArrayList();
   
    private PagePrinter printer;
    private PageNumberDelegate pageNumberDelegate = new PageNumberDelegate();

    /**
     * Creates a new ReportPrinter object.
     *
     * @param doc DOCUMENT ME!
     * @param parameters DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ReportPrinter(ReportDocument doc, Map parameters)
        throws Exception {
        this(doc, parameters, new DisplayStyleManager(null));
    }

    /**
     * Creates a new ReportPrinter object.
     *
     * @param doc DOCUMENT ME!
     * @param parameters DOCUMENT ME!
     * @param styleManager DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ReportPrinter(ReportDocument doc, Map parameters, DisplayStyleManager styleManager)
        throws Exception {
        this.doc = doc;
        
        this.context = new Context();
        
        if (this.doc.isDebugOff()) {
            this.context.getScript().hideErrors(null);
        }

        context.getScript().set(SCRIPT, context.getScript());

        if (styleManager == null) {
            styleManager = new DisplayStyleManager(null);
        }

        context.getScript().set(DISPLAY_STYLE_MANAGER, styleManager);
        context.getScript().set(IMAGE_LOADER, new AwtImageLoader(context.getScript()));

        context.getScript().set("$$nodestack", context.getScript().getNodeStack(0));
        // ALL 已经不需要了，已经很丰富了 用 all2,all3
        context.getScript().set("ALL", Key.ANY);
        context.getScript().set("ANY", Key.ANY);
        
        

        declareDocumentVariables(context.getScript(), doc);

        if (parameters != null) {
            declareExternalVariables(context.getScript(), parameters);
        }

        declareSystemVariables(context.getScript());
        context.getScript().set(AS2, parameters.get("as"));

        ReportDataModel model = (ReportDataModel) parameters.get(MODEL);
        context.getScript().get("copyrights");

        if (model == null) {
            model = new ReportDataModel(doc.getNodeSource(), context.getScript());

          //  if (!NO_CACHE_MODEL) {
            //    parameters.put(MODEL, model);
            //}
            

        } else {
            model.setScript(context.getScript());
        }

        context.getScript().set(MODEL, model);

    
        context.u((String) context.getScript().get("copyrights"));
    }

//    /**
//     * DOCUMENT ME!
//     *
//     * @return DOCUMENT ME!
//     */
//    public static Script getLocalScript() {
//        return (Script) localScript2.get();
//    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static List getLocalReaders() {
        ArrayList result = (ArrayList) localReaders.get();

        if (result == null) {
            result = new ArrayList();
            localReaders.set(result);
        }

        return result;
    }

    private void declareDocumentVariables(Script script, ReportDocument doc) {
        doc.getComponent(null);

        VariableContext vc = doc.getVariableContext();
        Iterator it = vc.names();

        while (it.hasNext()) {
            String var = (String) it.next();
            Object value = vc.getVariable(var);
            script.set(var, value);

            if (value instanceof Formula) {
                ((Formula) value).setCalculator(script);
            }
        }
    }

  
    private void declareExternalVariables(Script script, Map parameters) {
        Iterator it = parameters.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            Object val = parameters.get(key);

            script.set(key, val);
        }

        it = this.doc.getVariableContext().variables(VariableContext.PARAMETER);

        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            Object value = null;

            value = parameters.get(p.getName());

            if (value != null) {
                if (value instanceof String && (p.type() != String.class)) {
                    try {
                        value = p.castValue(p.getType1(), (String) value);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (value == null) {
                value = p;
            }

            script.set(p.getName(), value);
        }
    }

    private void declareSystemVariables(Script script) {
        script.set(TOTAL_PAGE_NUMBER, pageNumberDelegate);
        script.set(WORKING_DIR, System2.getWorkingDirectory().replace('\\', '/'));
        script.set(NOW, DateFormat.format(new Date(), System2.getProperty("timeformat")));
        script.set(TODAY, DateFormat.format(new Date(), System2.getProperty("dayformat")));
        script.set(COMPANY, System2.getProperty("company"));
        script.set(PRINTER, new PrinterVariable(context));
        script.set(STYLE_CLASSE,
            new StyleClassVariable((DisplayStyleManager) script.get(DISPLAY_STYLE_MANAGER)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addChangeListener(PrinterListener lst) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }

        this.listeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     */
    public void cancelPrint() {
        
    }

    private void firePageAdded(CompoundView view) {
        if (this.listeners != null) {
            for (int i = 0; i < this.listeners.size(); i++) {
                PrinterListener list = (PrinterListener) this.listeners.get(i);
                list.pageAdded(this, view);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void print() throws Exception {
        if (context.uo == 0) {
            throw new Exception(App.messages.getString("res.62"));
        }

        int c = 0;
        doBeforePrint(context.getScript());

        this.printer = new PagePrinter(doc.getPage(), context);

        boolean done = false;

        while (!done) {
            setPageIndex(c);

            View view = printer.print(context);

            if (view != null) {
                printer.resetPageFormat();
                ((CompoundView) view).doLayout();
                addPage((CompoundView) view);
            }

            c++;

            done = printer.isDone(context);
        }
        doAfterPrint(context.getScript());
    }

    private void setPageIndex(int c) {
        context.getScript().set(PAGE_INDEX, new Integer(c + 1));
    }

    protected void doBeforePrint(Script script) {
    }

    protected void doAfterPrint(Script script) {
    	pageNumberDelegate.close(this.pages.size()+"");
//        localScript2.set(null);

        ArrayList result = (ArrayList) localReaders.get();

        if ((result != null) && !result.isEmpty()) {
            Iterator it = result.iterator();

            while (it.hasNext()) {
                SqlReader reader = (SqlReader) it.next();
                reader.destroy();
            }
        }
    }

    private void addPage(CompoundView page) {
        this.pages.add(page);
        firePageAdded(page);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PageView[] getPages() {
        return (PageView[]) this.pages.toArray(new PageView[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Script getScript() {
        return context.getScript();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocuement() {
        return doc;
    }
}
