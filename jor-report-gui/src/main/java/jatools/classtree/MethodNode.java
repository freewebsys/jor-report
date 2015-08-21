package jatools.classtree;

import jatools.util.Util;

import java.lang.reflect.Method;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class MethodNode extends AbstractNode {
    private boolean statik;

	/**
     * Creates a new MethodNode object.
     *
     * @param m DOCUMENT ME!
     */
    public MethodNode(Method m,boolean statik) {
        setUserObject(m);
        setIsLeaf(true);
        this.statik = statik;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        Method method = (Method) getUserObject();

        try {
            StringBuffer sb = new StringBuffer();
            sb.append(Util.getTypeName(method.getReturnType()) + " ");

            sb.append(method.getName() + "(");

            Class[] params = method.getParameterTypes(); // avoid clone

            for (int j = 0; j < params.length; j++) {
                if (j > 0) {
                    sb.append(",");
                }

                sb.append(Util.getTypeName(params[j]));
            }

            sb.append(")");

            return sb.toString();
        } catch (Exception e) {
            return "<" + e + ">";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Icon getIcon() {
    
        return statik ?STATIC_ICON:PUBLIC_ICON;
    }
}
