package jatools.core.view;

import jatools.component.BackgroundImageStyle;
import jatools.component.painter.BackgroudImagePainter;
import jatools.designer.Main;
import jatools.formatter.Format2;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TextView extends AbstractView implements Cloneable {
    static public final String POINT = "point";
    static public final String FIRST_OPTIONS = "firstOptions";
    static final TextLine[] NULL_WRAPPED_LINES = new TextLine[0];
    static final int PADDING = 4;
    static final long serialVersionUID = 20030716012L;
    private static final int AUTO_WIDTH = 1;
    private static final int AUTO_HEIGHT = 2;
    private static JLabel fontmetricSrc = new JLabel();
    public static final int TOP = 0;
    public static final int MIDDLE = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    private String name;
    String text;
    transient Object data;
    DisplayStyle displayStyle;
    transient TextLine[] wrappedLines;
    int flag;
    int maxWidth;
    int charWidth;

    /**
     * Creates a new TextView object.
     *
     * @param css DOCUMENT ME!
     */
    public TextView(DisplayStyle css) {
        this.displayStyle = css;
    }

    /**
     * Creates a new TextView object.
     */
    public TextView() {
        this.displayStyle = new DisplayStyle();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCharWidth() {
        return charWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param charWidth DOCUMENT ME!
     */
    public void setCharWidth(int charWidth) {
        this.charWidth = charWidth;
    }

    /**
     * DOCUMENT ME!
     */
    public void setAutoWidth() {
        flag |= AUTO_WIDTH;
    }

    /**
     * DOCUMENT ME!
     */
    public void setAutoHeight() {
        flag |= AUTO_HEIGHT;
    }

    private boolean isAutoWidth() {
        return (flag & AUTO_WIDTH) != 0;
    }

    private boolean isAutoHeight() {
        return (flag & AUTO_HEIGHT) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPreferedHeight(int width) {
        if (isWordwrap() || isAutoHeight()) {
            if (this.bounds == null) {
                this.bounds = new Rectangle();
            }

            this.bounds.width = width;

            TextLine[] lines = this.getWrappedLines();

            if (lines.length > 1) {
                return lines[lines.length - 1].getBottom() + PADDING;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPreferedWidth(int height) {
        if (isAutoWidth() && (this.getText() != null)) {
            FontMetrics fm = fontmetricSrc.getFontMetrics(getFont());
            int h = 5 + fm.stringWidth("." + this.getText() + ".");

            if ((this.maxWidth > 0) && (h > this.maxWidth)) {
                h = this.maxWidth;
            }

            return h;
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     */
    public void autoResize() {
        if (isAutoWidth()) {
            int w = this.getPreferedWidth(0);

            if (w > bounds.width) {
                bounds.width = w;
            }
        } else if (isAutoHeight()) {
            int h = this.getPreferedHeight(this.bounds.width);

            if (h > bounds.height) {
                bounds.height = h;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAutoSize() {
        return isAutoWidth() || isAutoHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWordwrap() {
        return displayStyle.isWordwrap();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getBackColor() {
        return displayStyle.getBackColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param textWidth DOCUMENT ME!
     * @param fm DOCUMENT ME!
     * @param alignX DOCUMENT ME!
     * @param alignY DOCUMENT ME!
     * @param view DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Point layoutText(int textWidth, FontMetrics fm, int alignX, int alignY,
        Rectangle view) {
        int asc = fm.getFont().getSize();

        Point position = new Point();

        if (alignY == TOP) {
            position.y = (view.y + asc) - 1;
        } else if (alignY == MIDDLE) {
            position.y = (view.y + ((view.height - asc) / 2) + asc) - 1;
        } else {
            position.y = (view.y + view.height) - fm.getDescent() - 1;
        }

        if (alignX == LEFT) {
            position.x = view.x + PADDING;
        } else if (alignX == RIGHT) {
            position.x = (view.x + view.width) - textWidth - PADDING;
        } else {
            position.x = view.x + ((view.width - textWidth) / 2);
        }

        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont() {
        return displayStyle.getFont();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getForeColor() {
        return displayStyle.getForeColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        Border border = displayStyle.getBorder();
        Rectangle b = (border != null)
            ? deflateRect((Rectangle) getBounds().clone(), border.getInsets()) : getBounds();

        Graphics2D gcopy = (Graphics2D) g.create();

        Shape save = gcopy.getClip();
        gcopy.clip(b);

        Color color = displayStyle.getBackColor();

        if (color != null) {
            gcopy.setColor(color);
            gcopy.fillRect(b.x, b.y, b.width, b.height);
        }

        BackgroundImageStyle imageCSS = getBackgroundImageStyle();

        if (imageCSS != null) {
            Main.getInstance().getActiveEditor().getImageLoader().load(imageCSS);
            BackgroudImagePainter.getDefaults().paint(g, imageCSS, b);
        }

        String text = getText();

        if ((text != null) && !text.equals("")) {
            gcopy.setColor(displayStyle.getForeColor());

            Font font = displayStyle.getFont();

            if (font != null) {
                  
                gcopy.setFont(font);
            }
            

            TextLine[] lines = getWrappedLines();

            if (lines.length > 1) {
                int ay = getHorizentalAdvance(lines[lines.length - 1].y);

                for (int i = 0; i < lines.length; i++) {
                    TextLine line = lines[i];
                    gcopy.drawString(line.text, line.x + b.x, line.y + b.y + ay);
                }
            } else {
                FontMetrics fm = gcopy.getFontMetrics();

                Point base = TextView.layoutText(fm.stringWidth(text), fm,
                        displayStyle.getHorizontalAlignment(), displayStyle.getVerticalAlignment(),
                        b);
                gcopy.drawString(text, base.x, base.y);
            }
        }

        gcopy.setClip(save);

        if (getBorder() != null) {
            getBorder().paint(gcopy, getBounds());
        }

        gcopy.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param linesHeight DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHorizentalAdvance(int linesHeight) {
        int alignY = this.getVerticalAlignment();
        Rectangle b = this.getBounds();

        int advance = 0;

        if (alignY == TOP) {
            advance = 0;
        } else if (alignY == MIDDLE) {
            advance = (b.height - linesHeight) / 2;
        } else {
            advance = b.height - linesHeight;
        }

        return advance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Border getBorder() {
        return displayStyle.getBorder();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHorizontalAlignment() {
        return displayStyle.getHorizontalAlignment();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVerticalAlignment() {
        return displayStyle.getVerticalAlignment();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TextLine[] getWrappedLines() {
        if (this.wrappedLines != null) {
            return this.wrappedLines;
        }

        if (!this.isWordwrap() || (text == null) || text.equals("")) {
            this.wrappedLines = NULL_WRAPPED_LINES;
        } else {
            ArrayList lines = new ArrayList();
            float y = 0;

            String[] stringlines = text.split("\n");

            for (int i = 0; i < stringlines.length; i++) {
                String string = stringlines[i];

                if (string.length() == 0) {
                    string = " ";
                }

                AttributedString at = new AttributedString(string);
                at.addAttribute(TextAttribute.FONT, this.getFont());
                at.addAttribute(TextAttribute.FOREGROUND, this.getForeColor());

                AttributedCharacterIterator paragraph = at.getIterator();
                int paragraphStart = paragraph.getBeginIndex();
                int paragraphEnd = paragraph.getEndIndex();

                LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph,
                        new FontRenderContext(null, false, false));

                lineMeasurer.setPosition(paragraphStart);

                int oldPos = paragraphStart;

                Rectangle b = this.getBounds();

                while (lineMeasurer.getPosition() < paragraphEnd) {
                    int pos = lineMeasurer.nextOffset(b.width - 10);

                    String line = string.substring(oldPos, pos);
                    oldPos = pos;

                    TextLayout tl = lineMeasurer.nextLayout(b.width - 10);

                    y += tl.getAscent();

                    lines.add(new TextLine(getTextX1(tl), (int) y, line, tl));

                    y += (tl.getDescent() + tl.getLeading());

                    lineMeasurer.setPosition(pos);
                }
            }

            this.wrappedLines = (TextLine[]) lines.toArray(new TextLine[0]);
        }

        return this.wrappedLines;
    }

    /**
     * DOCUMENT ME!
     */
    public void uncacheLines() {
        this.wrappedLines = null;
    }

    private int getTextX1(TextLayout tl) {
        int x = 0;

        int a = this.getHorizontalAlignment();

        switch (a) {
        case RIGHT:
            x = (int) (getBounds().width - tl.getAdvance() - PADDING);

            break;

        case CENTER:
            x = (int) ((getBounds().width - tl.getAdvance()) / 2);

            break;

        case LEFT:
            x = PADDING;

            break;
        }

        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param excelValue DOCUMENT ME!
     */
    public void setData(Object excelValue) {
        this.data = excelValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DisplayStyle getDisplayStyle() {
        return displayStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param backColor DOCUMENT ME!
     */
    public void setBackColor(Color backColor) {
        displayStyle.backColor = backColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setBorder(Border border) {
        displayStyle.border = border;
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setFont(Font font) {
        displayStyle.font = font;
    }

    /**
     * DOCUMENT ME!
     *
     * @param foreColor DOCUMENT ME!
     */
    public void setForeColor(Color foreColor) {
        displayStyle.foreColor = foreColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setFormat(Format2 format) {
        displayStyle.format = format;
    }

    /**
     * DOCUMENT ME!
     *
     * @param horizontalAlignment DOCUMENT ME!
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        displayStyle.horizontalAlignment = horizontalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param verticalAlignment DOCUMENT ME!
     */
    public void setVerticalAlignment(int verticalAlignment) {
        displayStyle.verticalAlignment = verticalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param wordwrap DOCUMENT ME!
     */
    public void setWordwrap(boolean wordwrap) {
        displayStyle.wordwrap = wordwrap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param maxWidth DOCUMENT ME!
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }
}
