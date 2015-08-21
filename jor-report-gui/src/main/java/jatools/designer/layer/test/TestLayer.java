package jatools.designer.layer.test;


import jatools.designer.ReportPanel;
import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.Layer;
import jatools.designer.layer.page.PageTester;
import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TestLayer extends AbstractLayer {
    ReportPanel owner;
    ArrayList testers = new ArrayList();
    boolean selected;

    /**
     * Creates a new TestLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public TestLayer(ReportPanel owner) {
        this.owner = owner;
        setCursor(CursorUtil.MOVE_CURSOR);
        this.registerTester(new PageTester(owner));
        this.registerTester(new BorderTester(owner));
        this.registerTester(new ContainesTester(owner));
    }

    /**
     * DOCUMENT ME!
     *
     * @param tester DOCUMENT ME!
     */
    public void registerTester(Tester tester) {
        this.testers.add(tester);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tester DOCUMENT ME!
     * @param index DOCUMENT ME!
     */
    public void registerTester(Tester tester, int index) {
        this.testers.add(index, tester);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return Layer.WAKEN_BY_MOUSE_MOVE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker DOCUMENT ME!
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        Iterator it = this.testers.iterator();

        while (it.hasNext()) {
            Tester tester = (Tester) it.next();
            Cursor c = tester.test(modifier, x, y);

            if (c != null) {
                this.setCursor(c);

                return false;
            }
        }

        this.setCursor(CursorUtil.DEFAULT_CURSOR);

        return false;
    }
}
