package jatools.data;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Path {
    Path parent;
    String name;
    ArrayList children = new ArrayList();

    /**
     * Creates a new Path object.
     *
     * @param name DOCUMENT ME!
     */
    public Path(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void add(Path c) {
        c.parent = this;
        children.add(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }
}
