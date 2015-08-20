package jatools.core.view;

import jatools.engine.script.TemplateStringUtil;

import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class StyleParser {
    static StyleParser defaults;

    /**
     * DOCUMENT ME!
     *
     * @param styleText DOCUMENT ME!
     * @param styles DOCUMENT ME!
     */
    public void parse(String styleText, Properties styles) {
        if (styleText != null) {
            if (TemplateStringUtil.startIndex(styleText, 0) > -1) {
                templateParse(styleText, styles);
            } else {
                String[] stylepairs = styleText.split(";");

                for (int i = 0; i < stylepairs.length; i++) {
                    int sep = stylepairs[i].indexOf(':');

                    if (sep > -1) {
                        String prop = stylepairs[i].substring(0, sep);
                        String value = stylepairs[i].substring(sep + 1);
                        styles.put(prop, value);
                    }
                }
            }
        }
    }

    private void templateParse(String styleText, Properties styles) {
        
        
        // 
        while (true) {
            int i = styleText.indexOf(':');

            if (i > -1) {
                String name = styleText.substring(0, i).trim();
                
                styleText = styleText.substring(i + 1).trim();

                int from = 0;

                if (styleText.startsWith("${")) {
                    int end = TemplateStringUtil.endIndex(styleText, 0);

                    if (end > -1) {
                        from = end;
                    }
                }

                int end = styleText.indexOf(";", from);

                if (end == -1) {
                    styles.put(name, styleText);
//                    System.out.println(name + ":" + styleText);
                } else {
                    styles.put(name, styleText.substring(0, end));
//                    System.out.println(name + ":" + styleText.substring(0, end));
                    styleText = styleText.substring(end + 1);
                }
            } else {
                break;
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static StyleParser getDefaults() {
        if (defaults == null) {
            defaults = new StyleParser();
        }

        return defaults;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        getDefaults().parse(" nnaa: ${aaa}; fasdfa:aaa;;;", new Properties());
    }
}
