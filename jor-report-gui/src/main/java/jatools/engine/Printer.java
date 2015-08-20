package jatools.engine;

import jatools.component.Component;
import jatools.core.view.CompoundView;
import jatools.core.view.View;
import jatools.engine.css.PrintStyle;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.script.Context;
import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface Printer extends ProtectClass {
    static View BROKEN = new CompoundView();
    static View KILLED = new CompoundView();

    //  static View OVERFLOW = new CompoundView();

    /**
     * DOCUMENT ME!
     */
    public static boolean DEBUG = true;

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void open(Component c,Script script);

    /**
     * DOCUMENT ME!
     */
    public Component getComponent();

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public View print(Context context) throws Exception;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone(Context context);
    
    public boolean isForcedBreak();

    /**
     * DOCUMENT ME!
     */
    public void reset();

    /**
     * DOCUMENT ME!
     *
     * @param layout DOCUMENT ME!
     */
    public void requestFooterReservation(PrinterLayout layout, Script script);

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isVisible(Script script);

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEveryPagePrint(Script script);
    
    
    public boolean isForceBreak2(Script script);


    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public boolean isPrintIf(Context context);

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param layout DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View unitView(Script script, View view, PrinterLayout layout, boolean down);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintStyle getPrintStyle();

    /**
     * DOCUMENT ME!
     *
     * @param columnPrinting DOCUMENT ME!
     */
    public void setColumnPrinting(boolean columnPrinting);

    /**
     * DOCUMENT ME!
     */
    public void close();
    
    public void setLocalID(int id);
    
}
