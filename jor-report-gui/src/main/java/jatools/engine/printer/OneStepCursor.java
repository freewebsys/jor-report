package jatools.engine.printer;

import jatools.data.reader.Cursor;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class OneStepCursor implements Cursor {
    private static OneStepCursor EOF;
    boolean eof;
    private boolean save;

    /**
     * DOCUMENT ME!
     */
    public void next() {
        eof = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void open() {
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        save = this.eof;
    }

    /**
     * DOCUMENT ME!
     */
    public void rollback() {
        this.eof = this.save;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return !eof;
    }

    /**
     * DOCUMENT ME!
     */
    public void last() {
        eof = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cursor getEofInstance() {
        if (EOF == null) {
            EOF = new OneStepCursor();
            EOF.next();
        }

        return EOF;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowBased() {
        return false;
    }
}
