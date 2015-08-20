package jatools.engine.printer;

import jatools.accessor.ProtectPublic;
import jatools.component.Component;
import jatools.engine.script.Context;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PrinterVariable implements ProtectPublic {
    private Context context;

    PrinterVariable(Context context) {
        this.context = context;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrinterVariableProxy get(String name) {
        AbstractPrinter p = (AbstractPrinter) context.getPrinter((Component) context.getScript()
                                                                                    .get(name));

        return new PrinterVariableProxy(context, p);
    }
}
