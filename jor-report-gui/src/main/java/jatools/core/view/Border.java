package jatools.core.view;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;

import jatools.designer.App;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;

import java.io.Serializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Border implements Serializable, PropertyAccessor, BorderBase {
    private static final Pattern ASSURE_PX_PATTERN = Pattern.compile("([\\s|:]+\\d+[\\s|$])",
            Pattern.CASE_INSENSITIVE);

    /**
     * DOCUMENT ME!
     */
    private BorderStyle topStyle;
    private BorderStyle bottomStyle;
    private BorderStyle leftStyle;
    private BorderStyle rightStyle;
    private String styleText = DEFAULT_STYLE_TEXT;
    private boolean valid;

    /**
     * Creates a new Border object.
     */
    public Border() {
    }

    /**
     * Creates a new Border object.
     *
     * @param text DOCUMENT ME!
     */
    public Border(String text) {
        this.setStyleText(text);
    }

    /**
     * Creates a new Border object.
     *
     * @param thickness DOCUMENT ME!
     * @param color DOCUMENT ME!
     */
    public Border(int thickness, Color color) {
        this(thickness, BorderStyle.BORDER_STYLE_SOLID, color);
    }

    /**
     * Creates a new Border object.
     *
     * @param thickness DOCUMENT ME!
     * @param style DOCUMENT ME!
     * @param color DOCUMENT ME!
     */
    public Border(int thickness, String style, Color color) {
        this.setStyleText("border:" + thickness + " " + style + " " +
            StyleAttributes.toString(color));
    }

    /**
    * DOCUMENT ME!
    *
    * @param top DOCUMENT ME!
    * @param left DOCUMENT ME!
    * @param bottom DOCUMENT ME!
    * @param right DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static Border create(BorderStyle top, BorderStyle left,
        BorderStyle bottom, BorderStyle right) {
        if ((top != null) && top.equals(left) && top.equals(bottom) &&
                top.equals(right)) {
            return new Border("border:" + top.toString());
        }

        if ((top == null) && (left == null) && (bottom == null) &&
                (right == null)) {
            return null;
        }

        StringBuffer styletext = new StringBuffer();

        if (top != null) {
            styletext.append("border-top:");
            styletext.append(top.toString());
            styletext.append(";");
        }

        if (left != null) {
            styletext.append("border-left:");
            styletext.append(left.toString());
            styletext.append(";");
        }

        if (right != null) {
            styletext.append("border-right:");
            styletext.append(right.toString());
            styletext.append(";");
        }

        if (bottom != null) {
            styletext.append("border-bottom:");
            styletext.append(bottom.toString());
            styletext.append(";");
        }

        return new Border(styletext.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Border create(String text) {
        Border b = new Border(text);

        return create(b.getTopStyle(), b.getLeftStyle(), b.getBottomStyle(),
            b.getRightStyle());
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Border) {
            Border an = (Border) obj;
            boolean eq = (this.styleText == null) ? (an.styleText == null)
                                                  : this.styleText.equals(an.styleText);

            return eq;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param r DOCUMENT ME!
     */
    public void paint(Graphics2D g, Rectangle r) {
        int x0;
        int y0;
        int x1;
        int y1;

        if (getTopStyle() != null) {
            x0 = r.x;
            y0 = r.y;
            x1 = (r.x + r.width);
            y1 = y0;
            paint(getTopStyle(), g, x0, y0, x1, y1);
        }

        if (getRightStyle() != null) {
            x0 = r.x + r.width;
            y0 = r.y;
            x1 = r.x + r.width;
            y1 = (r.y + r.height);
            paint(getRightStyle(), g, x0, y0, x1, y1);
        }

        if (getBottomStyle() != null) {
            x0 = r.x;
            y0 = (r.y + r.height);
            x1 = r.x + r.width;
            y1 = (r.y + r.height);
            paint(getBottomStyle(), g, x0, y0, x1, y1);
        }

        if (getLeftStyle() != null) {
            x0 = r.x;
            y0 = r.y;
            x1 = r.x;
            y1 = r.y + r.height;
            paint(getLeftStyle(), g, x0, y0, x1, y1);
        }
    }

    private void paint(final BorderStyle edge, final Graphics graphics,
        final int xStart, final int yStart, final int xEnd, final int yEnd) {
        final String style = edge.getStyle();

        if (graphics instanceof Graphics2D) {
            final Graphics2D g2d = (Graphics2D) graphics;
            final Stroke stroke;

            if (BorderStyle.BORDER_STYLE_SOLID.equals(style)) {
                stroke = getSolidStroke(edge.getThickness());
            } else if (BorderStyle.BORDER_STYLE_DASHED.equals(style)) {
                stroke = getDashedStroke(edge.getThickness());
            } else if (BorderStyle.BORDER_STYLE_DOTTED.equals(style)) {
                stroke = getDottedStroke(edge.getThickness());
            } else {
                throw new IllegalStateException(App.messages.getString(
                        "res.630") + style);
            }

            g2d.setStroke(stroke);
        }

        graphics.setColor(edge.getColor());
        graphics.drawLine(xStart, yStart, xEnd, yEnd);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Stroke getSolidStroke(final float width) {
        return new BasicStroke(width);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Stroke getDashedStroke(final float width) {
        return new BasicStroke(width, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, new float[] { 5 }, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Stroke getDottedStroke(final float width) {
        return new BasicStroke(width, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, new float[] { 2 }, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BorderStyle getTopStyle() {
        validate();

        return topStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BorderStyle getBottomStyle() {
        validate();

        return bottomStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BorderStyle getLeftStyle() {
        validate();

        return leftStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BorderStyle getRightStyle() {
        validate();

        return rightStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getInsets() {
        return NULL_INSETS;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (this.styleText == null) {
            return "";
        } else {
            if (this.styleText.endsWith(";")) {
                return this.styleText;
            } else {
                return this.styleText + ";";
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor("StyleText", String.class)
        };
    }

    private void validate() {
        if (!valid) {
            this.leftStyle = null;
            this.rightStyle = null;
            this.bottomStyle = null;
            this.topStyle = null;

            if (DEFAULT_STYLE_TEXT.equals(this.styleText)) {
                this.leftStyle = DEFAULT_STYLE;
                this.rightStyle = DEFAULT_STYLE;
                this.bottomStyle = DEFAULT_STYLE;
                this.topStyle = DEFAULT_STYLE;
            } else if (this.styleText != null) {
                StyleAttributes parser = new StyleAttributes(this.styleText);
                String style = parser.getString("border", null);

                if (style != null) {
                    this.leftStyle = BorderStyle.parse(style);
                    this.topStyle = leftStyle;
                    this.bottomStyle = leftStyle;
                    this.rightStyle = leftStyle;
                } else {
                    style = parser.getString("border-left", null);

                    if (style != null) {
                        this.leftStyle = BorderStyle.parse(style);
                    }

                    style = parser.getString("border-right", null);

                    if (style != null) {
                        this.rightStyle = BorderStyle.parse(style);
                    }

                    style = parser.getString("border-top", null);

                    if (style != null) {
                        this.topStyle = BorderStyle.parse(style);
                    }

                    style = parser.getString("border-bottom", null);

                    if (style != null) {
                        this.bottomStyle = BorderStyle.parse(style);
                    }
                }
            }

            valid = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStyleText() {
        return styleText;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styleText DOCUMENT ME!
     */
    public void setStyleText(String styleText) {
        this.styleText = styleText;

        if (this.styleText != null) {
            this.styleText = normalStyle(this.styleText);
        }

        this.valid = false;
    }

    private static String normalStyle(String aText) {
        StringBuffer result = new StringBuffer();
        Matcher matcher = ASSURE_PX_PATTERN.matcher(aText);

        while (matcher.find()) {
            String repl = matcher.group(0).replaceAll("\\s+$", "") + "px ";

            matcher.appendReplacement(result, repl);
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
