package jatools.core.view;

import java.awt.Polygon;
import java.io.Serializable;


public class _Tip implements Serializable {
    static final long serialVersionUID = 2005120603L;
    public Polygon shape;
    public String alt;
    public String url;
    public String target;

    public boolean contain(int x, int y) {
        return shape.contains(x, y);
    }
}
