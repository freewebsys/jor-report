package jatools.component.table;

import jatools.accessor.ProtectPublic;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class GroupLayout implements ProtectPublic{
    private String[] groups;

    /**
     * Creates a new GroupLayout object.
     *
     * @param path DOCUMENT ME!
     */
    public GroupLayout(String path) {
        this.groups = path.split(",");
    }

    /**
     * DOCUMENT ME!
     *
     * @param objs DOCUMENT ME!
     */
    public void layout(Object[] objs) {
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof GLayout) {
                GLayout gl = (GLayout) objs[i];
                gl.layoutGroup(this.groups);
            }
        }
    }
}
