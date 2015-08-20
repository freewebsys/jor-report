package jatools.engine;

import bsh.Interpreter;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ImportFunctions {
    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     * @param cls DOCUMENT ME!
     */
    public static void importFunctions(Interpreter it, Class cls) {
        it.getNameSpace().getRoot().importStatic(cls);
    }
}
