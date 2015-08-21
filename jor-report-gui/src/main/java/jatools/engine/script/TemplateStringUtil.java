package jatools.engine.script;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TemplateStringUtil {
    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     * @param start DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int startIndex(String template, int start) {
        return template.indexOf("${", start);
    }

    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     * @param start DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int endIndex(String template, int start) {
        boolean header = (template.charAt(start) == '$') && (start < (template.length() - 1)) &&
            (template.charAt(start + 1) == '{');

        if (!header) {
            return -1;
        }

        int stack = 1;

        for (int j = start + 2; j < template.length(); j++) {
            char ch = template.charAt(j);

            if (ch == '{') {
                stack++;
            } else if (ch == '}') {
                stack--;

                if (stack == 0) {
                    return j;
                }
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        String temp = "${aa{aa{a{a{aa}aaaaa}aaa}fdsafasf";

        System.out.println(endIndex(temp, startIndex(temp, 0)));
    }
}
