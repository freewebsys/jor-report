package jatools.swingx;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;


public class EmptyIcon implements Icon {
    private int height;
    private int width;

    public EmptyIcon(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
    }
}
