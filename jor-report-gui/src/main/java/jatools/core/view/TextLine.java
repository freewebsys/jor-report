/*
 * Created on 2004-8-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.core.view;

import java.awt.font.TextLayout;


/**
 * @author   java9
 */
public class TextLine {
    /**
     * DOCUMENT ME!
     */
    public int x;

    /**
     * DOCUMENT ME!
     */
    public int y;

    /**
     * DOCUMENT ME!
     */
    public String text;
    TextLayout textLayout;

    /**
     * @param x
     * @param y
     * @param text
     * @param textLayout
     */
    public TextLine(int x, int y, String text, TextLayout textLayout) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.textLayout = textLayout;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBottom() {
        return (int) (y + textLayout.getDescent());
    }

    /**
     * @return   Returns the text.
     * @uml.property   name="text"
     */
    public String getText() {
        return text;
    }

    /**
     * @return   Returns the textLayout.
     * @uml.property   name="textLayout"
     */
    public TextLayout getTextLayout() {
        return textLayout;
    }

    /**
     * @return   Returns the x.
     * @uml.property   name="x"
     */
    public int getX() {
        return x;
    }

    /**
     * @return   Returns the y.
     * @uml.property   name="y"
     */
    public int getY() {
        return y;
    }
}
