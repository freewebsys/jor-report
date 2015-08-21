package jatools.classtree;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ClassNode extends AbstractNode {
    /**
     * Creates a new ClassNode object.
     *
     * @param string DOCUMENT ME!
     */
    public ClassNode(String string) {
        super(string.substring(0, string.length() - 6).replace('/', '.'));
    }

    /**
     * DOCUMENT ME!
     */
    public void explore() {
        if (!isExplored()) {
            Class c = null;

            try {
                c = Class.forName(toString());
            } catch (Exception e) {
                //	e.printStackTrace();
            }

            Method[] ms = c.getDeclaredMethods();

            for (int i = 0; i < ms.length; i++) {
                Method method = ms[i];
                int modifiers = method.getModifiers();

                if ((method.getExceptionTypes().length == 0)//						&& Modifier.isStatic(modifiers)
                         && Modifier.isPublic(modifiers)) {
                    MethodNode mn = new MethodNode(method,Modifier.isStatic(modifiers));

                    add(mn);
                }
            }

            setIsLeaf(getChildCount() <= 0);
            explored = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = (String) getUserObject();

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Icon getIcon() {
        return CLASS_ICON;
    }
}
