package jatools.swingx;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class HighlightDocument extends DefaultStyledDocument{
    private Element rootElement;
    private HashMap keywords;
    private MutableAttributeSet style = new SimpleAttributeSet();
    private Color commentColor = new Color(63, 127, 95);
    private Color quoteColor = Color.blue;
    private Color keywordColor = new Color(127, 0, 85); // Color.blue;
    private Pattern singleLineCommentDelimter = Pattern.compile("//");
    private Pattern multiLineCommentDelimiterStart = Pattern.compile("/\\*");
    private Pattern multiLineCommentDelimiterEnd = Pattern.compile("\\*/");
    private Pattern quoteDelimiter = Pattern.compile("\'.*?[^\\\\]\'");
    private Pattern tag = Pattern.compile("<[/a-zA-Z0-9\\s]+>");
    private Pattern scripttag = Pattern.compile("<script>(.*?)</script>",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    /**
     * Creates a new HighlightDocument object.
     */
    public HighlightDocument() {
        putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

        rootElement = getDefaultRootElement();

        keywords = new HashMap();

        keywords.put("function", keywordColor);
        keywords.put("abstract", keywordColor);
        keywords.put("interface", keywordColor);
        keywords.put("class", keywordColor);
        keywords.put("extends", keywordColor);
        keywords.put("implements", keywordColor);

        keywords.put("package", new Color(0, 200, 0));
        keywords.put("import", new Color(0, 200, 0));
        keywords.put("private", new Color(0, 200, 0));
        keywords.put("protected", new Color(0, 200, 0));
        keywords.put("public", new Color(0, 200, 0));

        keywords.put("void", Color.orange);
        keywords.put("boolean", Color.orange);
        keywords.put("char", Color.orange);
        keywords.put("byte", Color.orange);
        keywords.put("float", Color.orange);
        keywords.put("double", Color.orange);
        keywords.put("long", Color.orange);
        keywords.put("short", Color.orange);
        keywords.put("int", Color.orange);

        keywords.put("true", Color.red);
        keywords.put("false", Color.red);
        keywords.put("const", Color.red);
        keywords.put("null", Color.red);

        keywords.put("break", keywordColor);
        keywords.put("case", keywordColor);
        keywords.put("catch", keywordColor);
        keywords.put("operator", keywordColor);
        keywords.put("continue", keywordColor);
        keywords.put("default", keywordColor);
        keywords.put("do", keywordColor);
        keywords.put("else", keywordColor);
        keywords.put("final", keywordColor);
        keywords.put("finally", keywordColor);
        keywords.put("for", keywordColor);
        keywords.put("if", keywordColor);
        keywords.put("instanceof", keywordColor);
        keywords.put("native", keywordColor);
        keywords.put("new", keywordColor);
        keywords.put("return", keywordColor);
        keywords.put("static", keywordColor);
        keywords.put("super", keywordColor);
        keywords.put("switch", keywordColor);
        keywords.put("synchronized", keywordColor);
        keywords.put("this", keywordColor);
        keywords.put("throw", keywordColor);
        keywords.put("throws", keywordColor);
        keywords.put("transient", keywordColor);
        keywords.put("try", keywordColor);
        keywords.put("volatile", keywordColor);
        keywords.put("while", keywordColor);

        
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @param str DOCUMENT ME!
     * @param attr DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void insertString(int offset, String str, AttributeSet attr)
        throws BadLocationException {
        super.insertString(offset, str, attr);
        syntaxColor(getText(0, getLength()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        syntaxColor(getText(0, getLength()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @param text DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void highlight(int offset, String text) throws BadLocationException {
        highlightString(Color.black, offset, text.length(), true, false);

        Iterator keyw = keywords.keySet().iterator();

        while (keyw.hasNext()) {
            String keyword = (String) keyw.next();
            Color col = keywordColor; //(Color) keywords.get(keyword);

            Pattern p = Pattern.compile("\\b" + keyword + "\\b");
            Matcher m = p.matcher(text);

            while (m.find()) {
                highlightString(col, m.start() + offset, keyword.length(), true, true);
            }
        }

        Matcher mlcStart = multiLineCommentDelimiterStart.matcher(text);
        Matcher mlcEnd = multiLineCommentDelimiterEnd.matcher(text);

        while (mlcStart.find()) {
            if (mlcEnd.find(mlcStart.end())) {
                highlightString(commentColor, mlcStart.start() + offset,
                    (mlcEnd.end() - mlcStart.start()), true, true);
            } else {
                highlightString(commentColor, mlcStart.start() + offset, text.length(), true, true);
            }
        }

        Matcher quote = quoteDelimiter.matcher(text);

        while (quote.find()) {
            int start = quote.start();

            if (quote.find(start)) {
                highlightString(quoteColor, start + offset, (quote.end() - start), true, false);
            }
        }

        // Single Line Comment Highligher (below)			
        Matcher slc = singleLineCommentDelimter.matcher(text);

        while (slc.find()) {
            int line = rootElement.getElementIndex(slc.start());
            int endOffset = rootElement.getElement(line).getEndOffset() - 1;

            highlightString(commentColor, slc.start() + offset, (endOffset - slc.start()), true,
                false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param begin DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param flag DOCUMENT ME!
     * @param bold DOCUMENT ME!
     */
    public void highlightString(Color col, int begin, int length, boolean flag, boolean bold) {
        StyleConstants.setForeground(style, col);
        StyleConstants.setBold(style, bold);
        setCharacterAttributes(begin, length, style, flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param line DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLineString(String content, int line) {
        Element lineElement = rootElement.getElement(line);

        return content.substring(lineElement.getStartOffset(), lineElement.getEndOffset() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void syntaxColor(String template) throws BadLocationException {
        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setBold(style, false);
        StyleConstants.setFontFamily(style, "DialogInput");
        setCharacterAttributes(0, template.length(), style, true);

        Matcher m = tag.matcher(template);

        while (m.find()) {
            //  	System.out.println(m.group() );
            highlightString(this.keywordColor, m.start(), (m.end() - m.start()), true, true);
        }

        m = scripttag.matcher(template);

        while (m.find()) {
            //     	System.out.println(m.group(1)+","+m.start(1)+","+m.start());
            highlight(m.start(1), m.group(1));
        }

        // highlight(0, template);
    }
}
