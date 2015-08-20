package jatools.engine.printer;

import jatools.core.view.CompoundView;
import jatools.core.view.View;
import jatools.engine.Printer;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.script.Context;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractFlowPrinter extends AbstractPrinter {
    protected Printer[] childPrinters;
    
    int step;

    protected void printChildren(Context context, PrinterLayout layout)
        throws Exception {
        if (childPrinters == null) {
            childPrinters = prepareChildPrinters(context);
        }

//        if (_newPrint == 0) {
//            for (int i = 0; i < childPrinters.length; i++) {
//                childPrinters[i].reset();
//            }
//        }

        for (int i = 0; i < childPrinters.length; i++) {
            Printer printer = childPrinters[i];

            if (!printer.isDone( context) /*|| printer.isPrintIf(context)*/) {
                printer.requestFooterReservation(layout, context.getScript());
            }
        }

        done = true;

        for (int i = 0; i < childPrinters.length; i++) {
            Printer printer = childPrinters[i];

            if (!printer.isDone( context) /*|| printer.isPrintIf(context)*/) {
                View view = printer.print(context);

                if (!printer.isDone(context)) {
                    done = false;
                }

                layout.add(view);
            }
        }

        step++;
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public View print(Context context) throws Exception {
        this.doBeforePrint(context.getScript());

        
        CompoundView view = createView(context);
        PrinterLayout layout = createLayout(context, view);
        context.pushLayout(layout);

        
        
        printChildren(context, layout);
        context.popLayout();
        this.doAfterPrint(context.getScript());

        return view;
    }

    protected abstract PrinterLayout createLayout(Context context, CompoundView view);

    protected abstract CompoundView createView(Context context);
}
