package jatools.engine.script;

import java.util.Iterator;
import java.util.List;

import bsh.NameSpace;
import bsh.SimpleNode;
import bsh.UtilEvalError;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ForEach {
    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     * @param list DOCUMENT ME!
     * @param scripts DOCUMENT ME!
     */
    public static void forEach(Script script, List list, String scripts) {
        if ((scripts != null) && (script != null) && (list != null) && !list.isEmpty()) {
            SimpleNode scriptNode = script.parse(scripts);
            NameSpace ns = script.createNameSpace();

            script.pushNameSpace(ns);

            Iterator it = list.iterator();

            while (it.hasNext()) {
                try {
                    ns.setLocalVariable("self", it.next());
                } catch (UtilEvalError e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                script.eval(scriptNode);
            }

            script.popNameSpace();
        }

        //    	
    }
}
