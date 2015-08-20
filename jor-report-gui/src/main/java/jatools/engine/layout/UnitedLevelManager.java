package jatools.engine.layout;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class UnitedLevelManager {
    private static final int NEW = 0;
    private static final int USED = 1;
    private int[] unitedLevels = new int[16];

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNew(int level) {
        if ((level >= 0) && (level < 16)) {
            return unitedLevels[level] == NEW;
        } else {
            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     */
    public void useLevel(int level) {
        if ((level >= 0) && (level < 16)) {
            unitedLevels[level] = USED;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     */
    public void newNext(int level) {
        level++;

        if ((level >= 0) && (level < 16)) {
            unitedLevels[level] = NEW;
        }
    }
}
