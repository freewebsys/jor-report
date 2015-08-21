package jatools.designer.data;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.2 $
 * @author $author$
 */
class SQLDocument extends DefaultStyledDocument implements SQLParserConstants {
    private DefaultStyledDocument doc;
    private Element rootElement;
    private MutableAttributeSet normal;
    private MutableAttributeSet keyword;
    private MutableAttributeSet delimiter;
    private MutableAttributeSet quote;
    private Hashtable keywords;

    /**
     * Creates a new ZSQLDocument object.
     */
    public SQLDocument() {
        doc = this;
        rootElement = doc.getDefaultRootElement();
        putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n"); //
        normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.black);
        StyleConstants.setFontFamily(normal, "Dialog"); //
        StyleConstants.setFontSize(normal, 12);

        keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, Color.blue);
        StyleConstants.setBold(keyword, true);

        delimiter = new SimpleAttributeSet();
        StyleConstants.setForeground(delimiter, new Color(83, 109, 165));
     //   StyleConstants.setBold(delimiter, true);

        quote = new SimpleAttributeSet();
        StyleConstants.setForeground(quote, Color.red);

        Object dummyObject = new Object();
        keywords = new Hashtable();

        for (int i = 0; i < SQLParserConstants.tokenImage1.length; i++) {
            keywords.put(tokenImage1[i], dummyObject);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @param str DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void insertString(int offset, String str, AttributeSet a)
                      throws BadLocationException {
        super.insertString(offset, str, a);
        processChangedLines(offset, str.length());
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
        processChangedLines(offset, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    private void processChangedLines(int offset, int length)
                              throws BadLocationException {
        String content = doc.getText(0, doc.getLength());

        //  The lines affected by the latest document update
        int startLine = rootElement.getElementIndex(offset);
        int endLine = rootElement.getElementIndex(offset + length);

        //  Do the actual highlighting
        for (int i = startLine; i <= endLine; i++) {
            applyHighlighting(content, i);
        }


        //  Resolve highlighting to the next end multi line delimiter
        highlightLinesAfter(content, endLine);
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param line DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    private void highlightLinesAfter(String content, int line)
                              throws BadLocationException {
        int offset = rootElement.getElement(line).getEndOffset();

        //  Start/End delimiter not found, nothing to do
        int startDelimiter = indexOf(content, getStartDelimiter(), offset);
        int endDelimiter = indexOf(content, getEndDelimiter(), offset);

        if (startDelimiter < 0) {
            startDelimiter = content.length();
        }

        if (endDelimiter < 0) {
            endDelimiter = content.length();
        }

        int delimiter = Math.min(startDelimiter, endDelimiter);

        if (delimiter < offset) {
            return;
        }

        //	Start/End delimiter found, reapply highlighting
        int endLine = rootElement.getElementIndex(delimiter);

        for (int i = line + 1; i < endLine; i++) {
           
            //      if (as.isEqual(comment)) {
            applyHighlighting(content, i);

            //  }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param line DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    private void applyHighlighting(String content, int line)
                            throws BadLocationException {
        int startOffset = rootElement.getElement(line).getStartOffset();
        int endOffset = rootElement.getElement(line).getEndOffset() - 1;

        int lineLength = endOffset - startOffset;
        int contentLength = content.length();

        if (endOffset >= contentLength) {
            endOffset = contentLength - 1;
        }


        //  set normal attributes for the line
        doc.setCharacterAttributes(startOffset, lineLength, normal, true);


        //  check for tokens
        checkForTokens(content, startOffset, endOffset);
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param startOffset DOCUMENT ME!
     * @param endOffset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param startOffset DOCUMENT ME!
     * @param endOffset DOCUMENT ME!
     */
    private void checkForTokens(String content, int startOffset, int endOffset) {
        while (startOffset <= endOffset) {
            //  skip the delimiters to find the start of a new token
            while (isDelimiter(content.substring(startOffset, startOffset + 1))) {
                doc.setCharacterAttributes(startOffset, 1, delimiter, false);

                if (startOffset < endOffset) {
                    startOffset++;
                } else {
                    return;
                }
            }

            //  Extract and process the entire token
            if (isQuoteDelimiter(content.substring(startOffset, startOffset + 1))) {
                startOffset = getQuoteToken(content, startOffset, endOffset);
            } else {
                startOffset = getOtherToken(content, startOffset, endOffset);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param startOffset DOCUMENT ME!
     * @param endOffset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getQuoteToken(String content, int startOffset, int endOffset) {
        String quoteDelimiter = content.substring(startOffset, startOffset + 1);
        String escapeString = getEscapeString(quoteDelimiter);

        int index;
        int endOfQuote = startOffset;


        //  skip over the escape quotes in this quote
        index = content.indexOf(escapeString, endOfQuote + 1);

        while ((index > -1) && (index < endOffset)) {
            endOfQuote = index + 1;
            index = content.indexOf(escapeString, endOfQuote);
        }


        // now find the matching delimiter
        index = content.indexOf(quoteDelimiter, endOfQuote + 1);

        if ((index < 0) || (index > endOffset)) {
            endOfQuote = endOffset;
        } else {
            endOfQuote = index;
        }

        doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote,
                                   false);

        return endOfQuote + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param startOffset DOCUMENT ME!
     * @param endOffset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getOtherToken(String content, int startOffset, int endOffset) {
        int endOfToken = startOffset + 1;

        while (endOfToken <= endOffset) {
            if (isDelimiter(content.substring(endOfToken, endOfToken + 1))) {
                doc.setCharacterAttributes(endOfToken, 1, delimiter, false);

                break;
            }

            endOfToken++;
        }

        String token = content.substring(startOffset, endOfToken);

        if (isKeyword(token)) {
            doc.setCharacterAttributes(startOffset, endOfToken - startOffset, keyword,
                                       false);
        }

        return endOfToken + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param needle DOCUMENT ME!
     * @param offset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int indexOf(String content, String needle, int offset) {
        int index;

        while ((index = content.indexOf(needle, offset)) != -1) {
            String text = getLine(content, index).trim();

            if (text.startsWith(needle) || text.endsWith(needle)) {
                break;
            } else {
                offset = index + 1;
            }
        }

        return index;
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param needle DOCUMENT ME!
     * @param offset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int lastIndexOf(String content, String needle, int offset) {
        int index;

        while ((index = content.lastIndexOf(needle, offset)) != -1) {
            String text = getLine(content, index).trim();

            if (text.startsWith(needle) || text.endsWith(needle)) {
                break;
            } else {
                offset = index - 1;
            }
        }

        return index;
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     * @param offset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getLine(String content, int offset) {
        int line = rootElement.getElementIndex(offset);
        Element lineElement = rootElement.getElement(line);
        int start = lineElement.getStartOffset();
        int end = lineElement.getEndOffset();

        return content.substring(start, end - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param character DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isDelimiter(String character) {
        String operands = ";:.{}()[]+-/%<=>!&|^~*,"; //

        if (Character.isWhitespace(character.charAt(0)) ||
                (operands.indexOf(character) != -1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param character DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isQuoteDelimiter(String character) {
        String quoteDelimiters = "\"'"; //

        if (quoteDelimiters.indexOf(character) < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isKeyword(String token) {
        Object o = keywords.get(token.toLowerCase());

        return (o == null) ? false : true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getStartDelimiter() {
        return "/*"; //
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getEndDelimiter() {
        return "*/"; //
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSingleLineDelimiter() {
        return "//"; //
    }

    /**
     * DOCUMENT ME!
     *
     * @param quoteDelimiter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getEscapeString(String quoteDelimiter) {
        return "\\" + quoteDelimiter; //
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public static void main(String[] a) {
        JFrame frame = new JFrame("Syntax Highlighting"); //
        JEditorPane edit = new JEditorPane();
        edit.setEditorKit(new StyledEditorKit());
        edit.setDocument(new SQLDocument());

        JScrollPane scroll = new JScrollPane(edit);


        //   edit.setFont(new Font("Arial",0,12));
        frame.getContentPane().add(scroll);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setVisible(true);
    }
}
