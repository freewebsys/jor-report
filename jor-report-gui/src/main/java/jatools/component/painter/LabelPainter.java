package jatools.component.painter;

import jatools.component.Component;
import jatools.component.Label;
import jatools.core.view.Border;
import jatools.core.view.TextLine;
import jatools.core.view.TextView;

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


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class LabelPainter extends SimplePainter {
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
	

    public void paintComponent(Graphics2D g, Component c) {
        Label label = (Label) c;

        Border border = label.getBorder();

        Rectangle b = (border != null)
            ? TextView.deflateRect((Rectangle) label.getBounds().clone(), border.getInsets())
            : label.getBounds();

        Shape save = g.getClip();
        g.clip(b);

        Color color = label.getBackColor();

        if (color != null) {
            g.setColor(label.getBackColor());
            g.fillRect(b.x, b.y, b.width, b.height);
        }

        paintBackground(g, label, b);

        String text = label.getText();

        if ((text != null) && !text.equals("")) { //
            g.setColor(label.getForeColor());

           

         

            
            
            Font font = label.getFont();

            if (font != null) {
                g.setFont(font);
            }
            
          
            

            FontMetrics fm = g.getFontMetrics();

            if (label.isWordwrap() &&
                    ((text.indexOf("\n") > -1) || (fm.stringWidth(text) > b.width))) {
                TextLine[] lines = getParagraphLines(text, label);

                if (lines.length > 0) {
                    int ay = getHorizentalAdvance(lines[lines.length - 1].y, label);

                    for (int i = 0; i < lines.length; i++) {
                        TextLine line = lines[i];
                        g.drawString(line.text, line.x + b.x, line.y + b.y + ay);
                    }
                }
            } else {
                Point base = TextView.layoutText(fm.stringWidth(text), fm,
                        label.getHorizontalAlignment(), label.getVerticalAlignment(), b);
                g.drawString(text, base.x, base.y);
            }
        }

        g.setClip(save);
    }

    /**
     * 在设计面板中,画出标签.
     *
     * @param g
     *            设计面板图形对象.
     */
    public int getHorizentalAdvance(int linesHeight, Label label) {
        int alignY = label.getVerticalAlignment();
        Rectangle b = label.getBounds();

        int advance = 0;

        if (alignY == Label.TOP) {
            advance = 0;
        } else if (alignY == Label.CENTER) {
            advance = (b.height - linesHeight) / 2;
        } else { // (alignY == BOTTOM)
            advance = b.height - linesHeight;
        }

        return advance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final TextLine[] getParagraphLines(String text, Label label) {
        ArrayList lines = new ArrayList();
        float y = 0;

        String[] stringlines = text.split("\n"); //

        for (int i = 0; i < stringlines.length; i++) {
            String string = stringlines[i];

            if (string.length() == 0) {
                string = " "; //
            }

            AttributedString at = new AttributedString(string);
            at.addAttribute(TextAttribute.FONT, label.getFont());

            AttributedCharacterIterator paragraph = at.getIterator();
            int paragraphStart = paragraph.getBeginIndex();
            int paragraphEnd = paragraph.getEndIndex();

            // Create a new LineBreakMeasurer from the paragraph.
            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph,
                    new FontRenderContext(null, false, false));

            lineMeasurer.setPosition(paragraphStart);

            int oldPos = paragraphStart;

            // Get lines from lineMeasurer until the entire
            // paragraph has been displayed.
            Rectangle b = label.getBounds();

            while (lineMeasurer.getPosition() < paragraphEnd) {
                int pos = lineMeasurer.nextOffset(b.width);

                String line = string.substring(oldPos, pos);
                oldPos = pos;

                TextLayout tl = lineMeasurer.nextLayout(b.width);

                y += tl.getAscent();

                lines.add(new TextLine(getTextX(tl, label), (int) y, line, tl));

                y += (tl.getDescent() + tl.getLeading());
                lineMeasurer.setPosition(pos);
            }
        }

        return (TextLine[]) lines.toArray(new TextLine[0]);
    }

    private int getTextX(TextLayout tl, Label label) {
        int x = 0;

        int a = label.getHorizontalAlignment();

        switch (a) {
        case Label.RIGHT:
            x = (int) (label.getBounds().width - tl.getAdvance());

            break;

        case Label.CENTER:
            x = (int) ((label.getBounds().width - tl.getAdvance()) / 2);

            break;

        default:
            break;
        }

        return x;
    }
}
