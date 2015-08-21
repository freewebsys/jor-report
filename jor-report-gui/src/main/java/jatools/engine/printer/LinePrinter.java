package jatools.engine.printer;

import jatools.component.Line;
import jatools.core.view.LineView;
import jatools.core.view.View;
import jatools.engine.script.Context;
import jatools.engine.script.Script;

import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class LinePrinter extends AbstractPrinter {
    /**
    * DOCUMENT ME!
    *
    * @param c DOCUMENT ME!
    * @param dataProvider DOCUMENT ME!
    * @param doScript DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public View print(Context context) {
        Script script = context.getScript();
        doBeforePrint(script);

        Line line = (Line) this.getComponent();

        LineView e = new LineView();

        if (line.getParent() != null) {
            e.setClip(new Rectangle(0, 0, line.getParent().getWidth(), line.getParent().getHeight()));
        }

        e.setLinePattern(line.getLinePattern());
        e.setLineSize(line.getLineSize());
        e.setBounds(line.getBounds());
        e.setForeColor(line.getForeColor());

        doAfterPrint(script);
        this.done = true;

        return e;
    }
}
