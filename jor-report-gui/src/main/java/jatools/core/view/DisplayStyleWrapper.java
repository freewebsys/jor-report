package jatools.core.view;

import jatools.formatter.Format2;

import java.awt.Color;
import java.awt.Font;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DisplayStyleWrapper extends DisplayStyle {
    DisplayStyleManager manager;
    boolean dirty;

    /**
     * Creates a new DisplayStyleWrapper object.
     *
     * @param manager DOCUMENT ME!
     * @param style DOCUMENT ME!
     */
    public DisplayStyleWrapper(DisplayStyleManager manager, DisplayStyle style) {
        this.manager = manager;
        assignFrom(style);
    }

    private void assignFrom(DisplayStyle style) {
        this.name = style.getName();

        this.font = style.font;
        this.backColor = style.backColor;
        this.foreColor = style.foreColor;
        this.format = style.format;
        this.border = style.border;
        this.horizontalAlignment = style.horizontalAlignment;
        this.verticalAlignment = style.verticalAlignment;
        this.wordwrap = style.wordwrap ;
        this.enables = style.enables ;
       
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public int getId() {
        if (dirty) {
            this.setId(manager.generateId());
            dirty = false;
        }

        return super.getId();
    }

    /**
    * DOCUMENT ME!
    *
    * @param backColor DOCUMENT ME!
    */
    public void setBackColor(Color backColor) {
        if ((this.backColor != null) && !this.backColor.equals(backColor)) {
            this.backColor = backColor;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setBorder(Border border) {
        if ((this.border != null) && !this.border.equals(border)) {
            this.border = border;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setFont(Font font) {
        if ((this.font != null) && !this.font.equals(font)) {
            this.font = font;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param foreColor DOCUMENT ME!
     */
    public void setForeColor(Color foreColor) {
        if ((this.foreColor != null) && !this.foreColor.equals(foreColor)) {
            this.foreColor = foreColor;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setFormat(Format2 format) {
        if ((this.format != null) && !this.format.equals(format)) {
            this.format = format;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param horizontalAlignment DOCUMENT ME!
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        if ((this.horizontalAlignment != -1) && (this.horizontalAlignment != horizontalAlignment)) {
            this.horizontalAlignment = horizontalAlignment;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param verticalAlignment DOCUMENT ME!
     */
    public void setVerticalAlignment(int verticalAlignment) {
        if ((this.verticalAlignment != -1) && (this.verticalAlignment != verticalAlignment)) {
            this.verticalAlignment = verticalAlignment;
            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param wordwrap DOCUMENT ME!
     */
    public void setWordwrap(boolean wordwrap) {
        if (this.isWordwrapEnabled()  && (this.wordwrap != wordwrap)) {
            this.wordwrap = wordwrap;
            dirty = true;
        }
    }
}
