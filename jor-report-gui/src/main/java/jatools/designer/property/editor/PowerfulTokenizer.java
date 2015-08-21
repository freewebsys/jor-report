package jatools.designer.property.editor;

import java.util.Enumeration;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class PowerfulTokenizer implements Enumeration {
    private String sInput;
    private String sDelim;
    private boolean bIncludeDelim = false;
    private StringTokenizer oTokenizer;
    private int iEndQuote = 0;
    private String sPrevToken = "";
    private int iTokenNo = 0;
    private int iTotalTokens = 0;
    private int iTokens = 0;
    private int iLen = 0;

    /**
     *        Constructor
     *
     *        @param                str                                the input string
     *        @param                sep                                the delimiter string
     */
    public PowerfulTokenizer(String str, String sep) {
        sInput = str;
        sDelim = sep;
        iLen = sDelim.length();
        oTokenizer = new StringTokenizer(str, sep, true);
    }

    /**
     *        Constructor
     *
     *        @param                str                                the input string
     *        @param                sep                                the delimiter string
     *        @param                bIncludeDelim        if true, include delimiters as tokens
     */
    public PowerfulTokenizer(String str, String sep, boolean bIncludeDelim) {
        sInput = str;
        sDelim = sep;
        this.bIncludeDelim = bIncludeDelim;
        iLen = sDelim.length();
        oTokenizer = new StringTokenizer(str, sep, true);
    }

    /**
     *        Returns the next token from the input string.
     *
     *        @return                        String                the current token from the input string.
     */
    public String nextToken() {
        String sToken = oTokenizer.nextToken();

        // return "" as token if consecutive delimiters are found.
        if ((sPrevToken.equals(sDelim)) && (sToken.equals(sDelim))) {
            sPrevToken = sToken;
            iTokenNo++;

            return "";
        }

    

        sPrevToken = sToken;

        // remove any unnecessary double quote still present.
        if (sToken.length() > 0) {
            sToken = sToken.trim();

            // remove double quote marks from beginning and end of the string
            if ((sToken.charAt(0) == '"') && (sToken.charAt(sToken.length() - 1) == '"')) {
                sToken = sToken.substring(1, sToken.length() - 1);
            }

            String sTemp = "";
            int iPrevDblQuote = 0;
            int iDblQuote = sToken.indexOf("\"\"");

            // change "\"\""'s to "\"" if any of them are present
            if (iDblQuote != -1) {
                String sDummy = sToken;

                while (iDblQuote != -1) {
                    sTemp = sDummy.substring(0, iDblQuote + 1);
                    sTemp += sDummy.substring(iDblQuote + 2);
                    iPrevDblQuote = iDblQuote;
                    sDummy = sTemp;
                    iDblQuote = sDummy.indexOf("\"\"", iPrevDblQuote + 1);
                }

                sToken = sTemp;
            }
        }

        // call next token again, if delimeters are not to be included
        // as tokens.
        if ((!bIncludeDelim) && (sToken.equals(sDelim))) {
            sToken = nextToken();
        } else {
            iTokenNo++;
        }

        //System.out.println("idx = " + iTokenNo + ", token = " + sToken);
        return sToken;
    }

    /**
     *        Checks whether any token is left in the input string
     *
     *        @return                        boolean                true, if any token is left
     */
    public boolean hasMoreTokens() {
        if (iTotalTokens == 0) {
            iTotalTokens = countTokens();
        }

        return (iTokenNo < iTotalTokens);
    }

    /**
     *        Checks whether any token is left in the input string
     *
     *        @return                        boolean                true, if any token is left
     */
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    /**
     *        Returns the next token from the input string.
     *
     *        @return                        Object                the current token from the input string.
     */
    public Object nextElement() {
        return nextToken();
    }

    /**
     *        Total number of tokens present in the input string
     *
     *        @return                        int                total number of tokens
     */
    public int countTokens() {
        //int iTokens = super.countTokens();
        iTokens = oTokenizer.countTokens();

        int iActualTokens = iTokens;
        System.out.println("original tokens = " + iTokens);

        int[] aiIndex = new int[iTokens];
        aiIndex[0] = 0;

        int iIndex = 0;
        int iNextIndex = 0;

        // check whether the delimiter is within a substring
        for (int i = 1; i < aiIndex.length; i++) {
            iIndex = sInput.indexOf(sDelim, iIndex + 1);

            if (iIndex == -1) {
                break;
            }

            // if the delimiter is within a substring, then parse upto the
            // end of the substring.
            while (sInput.substring(iIndex - iLen, iIndex).equals(sDelim)) {
                iNextIndex = sInput.indexOf(sDelim, iIndex + 1);

                if (iNextIndex == -1) {
                    break;
                }

                iIndex = iNextIndex;
            }

            aiIndex[i] = iIndex;

            //System.out.println("aiIndex[" + i + "] = " + iIndex);
            if (isWithinQuotes(iIndex)) {
                if (bIncludeDelim) {
                    iTokens -= 2;
                } else {
                    iTokens -= 1;
                }
            }
        }

        if (bIncludeDelim) {
            return iTokens;
        } else if ((!bIncludeDelim) || (iTokens == iActualTokens)) {
            // remove the number of actual delimeters from 
            // the string as this a case with bIncludeDelim=false
            int iIdx = 0;
            iIdx = sInput.indexOf(sDelim, iIdx + 1);

            while (iIdx != -1) {
                if (!((sInput.charAt(iIdx - 1) == '"')  &&
                        (((iIdx + 1 + iLen) <= sInput.length()) && (sInput.charAt(iIdx + 1) == '"')&&
                        (sInput.substring(iIdx + 1, iIdx + 1 + iLen).equals(sDelim))))) {
                    iTokens--;
                }

                // don't decrement the token count if consecutive tokens 
                // are found.
                while (((iIdx + 1) < sInput.length()) &&
                        (sInput.substring(iIdx + 1, iIdx + 1 + iLen).equals(sDelim))) {
                    iIdx += iLen;
                }

                iIdx = sInput.indexOf(sDelim, iIdx + 1);
            }
        }

        return iTokens;
    }

    /**
     *        Checks whether the particular index (at which the delimiter is found
     *        is within double quotes (i.e. in a substring). This also checks
     *        whether the token is equal to the delimeter.
     *
     *        @return                        boolean                true, if the index is within a substring
     */
    private boolean isWithinQuotes(int k) {
        int iStartQuote = sInput.indexOf("\"", 0);

        System.out.println("quote = " + iStartQuote);
        if (k < iStartQuote) {
            return false;
        }

        if (!bIncludeDelim) {
            // check whether token is equal to delimiter
            if ((sInput.charAt(k - 1) == '"') &&
                    (((k + 1 + iLen) <= sInput.length()) &&  (sInput.charAt(k + 1) == '"') &&
                    (sInput.substring(k + 1, k + 1 + iLen).equals(sDelim)))) {
                iTokens -= 2;

                return false;
            }
        }

        while (iStartQuote != -1) {
            iEndQuote = sInput.indexOf("\"", iStartQuote + 1);

            if ((k > iStartQuote) && (k < iEndQuote)) {
                // delimiter is within a substring
                return true;
            }

            iStartQuote = sInput.indexOf("\"", iEndQuote + 1);
        }

        return false;
    }

    // Test Harness
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        //String sIn = "hi, hello,, \"how, are, qrt, u\", good, "
        //					+ "\"fine, hty, great\", data";
        //String sIn = "hi, hello,,, \"how, are, qrt, u\", \"good, man\", "
        //					+ "\"hello \"\"buddy\"\", hi how\", "
        //					+ "\"fine, hty, great\", \"hello\"";
        //String sIn = "3015,http://www.hello.com/hello.html,"
        //					+ "\"Bhabani, Padhi\",\"Details \"\"News\"\".\"";
        //String sIn = "hi, hello,,, \"how, are, qrt, u\",\",\", "
        //					+ "\"good, man\", \"hello \"\"buddy\"\", "
        //					+ "hi how\", \"fine, hty, great\", \"hello\"";
        //String sIn = "hi, how, \"are, u\", hello, \"how, are\", u";
        String sIn = "hello, Today, \"I, am \", going to, \"buy, a, book\"";
        PowerfulTokenizer oMT = new PowerfulTokenizer(sIn, " ");
        int iTokens = oMT.countTokens();
        System.out.println("no of tokens = " + iTokens);

        /*for (int i=0; i<iTokens; i++)
        {
                System.out.println("token" + i + " = " + oMT.nextToken());
        }
        
        oMT = new PowerfulTokenizer(sIn, ",");*/
        int i = 0;

        while (oMT.hasMoreTokens())
            System.out.println("token" + i++ + " = " + oMT.nextToken());

        oMT = new PowerfulTokenizer(sIn, ",", true);
        iTokens = oMT.countTokens();
        System.out.println("no of tokens = " + iTokens);
        i = 0;

        while (oMT.hasMoreTokens())
            System.out.println("token" + i++ + " = " + oMT.nextToken());
    }
}
