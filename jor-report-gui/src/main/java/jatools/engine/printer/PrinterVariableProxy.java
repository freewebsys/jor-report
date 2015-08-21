package jatools.engine.printer;

import jatools.engine.script.Context;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PrinterVariableProxy {
    private Context context;
    private AbstractPrinter printer;

    PrinterVariableProxy(Context context, AbstractPrinter printer) {
        this.context = context;
        this.printer = printer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone() {
        return this.printer.isDone(context);
    }
}
