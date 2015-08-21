package jatools.dataset;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface ScriptableRow {
    public static int NOP = 0;
    public static int UPDATE = 3;
    public static int INSERT = 1;
    public static int DELETE = 2;

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void setAction(int action);

    /**
     * DOCUMENT ME!
     */
    public int getAction2();
    
    
}
