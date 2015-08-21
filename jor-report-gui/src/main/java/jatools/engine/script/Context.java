package jatools.engine.script;

import jatools.component.Component;
import jatools.engine.Printer;
import jatools.engine.PrinterFactory;
import jatools.engine.layout.PrinterLayout;

import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Context {
    //  private static String user = "hNLzO8cGFkuk=,5r4KN23yArfAin35oV/kMSc5pCwhQqmHN0FzkNnLXDA=";
    Script script;
    PrinterFactory printerFactory = new PrinterFactory();
    Stack printerLayouts = new Stack();
    public int uo =1;

    /**
     * Creates a new Context object.
     * @throws Exception
     */
    public Context() throws Exception {
        script = new ReportContext(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param layout DOCUMENT ME!
     */
    public void pushLayout(PrinterLayout layout) {
        this.printerLayouts.push(layout);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void u(String c) {
//        if (System2.csd.equals( "S") && !c.equals(EncryptUtil.decrypt(
//        		"IM6A2mt1tR8=,sMtq+l8MuGOgZ3RhozqKduUXndgBAWcW62wusTVlxeH4nYNnCRofszRAEJvscN/vM9a+s8877Da9\nE91nwMDdUu9bkZqS4jfs"))) {
//            uo = 0;
//        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object popLayout() {
        return this.printerLayouts.pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrinterLayout getLayout() {
        return this.printerLayouts.isEmpty() ? null : (PrinterLayout) this.printerLayouts.peek();
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Script getScript() {
        return this.script;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Printer getPrinter(Component c) {
        return this.printerFactory.getPrinter(c, script);
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        this.printerFactory.close();
    }
}
