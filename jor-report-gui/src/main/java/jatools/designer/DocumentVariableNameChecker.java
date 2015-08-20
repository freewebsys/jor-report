package jatools.designer;

import jatools.ReportDocument;
import jatools.designer.data.NameChecker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class DocumentVariableNameChecker implements NameChecker {
	
    private static final String NAME_PATTERN = "^[\\$_a-zA-Z\u4e00-\u9fa5]{1}[_a-zA-Z0-9\u4e00-\u9fa5]*$";
    private ReportDocument document;
    private ArrayList namesCache;

    /**
     * Creates a new DocumentVariableNameChecker object.
     *
     * @param doc DOCUMENT ME!
     */
    public DocumentVariableNameChecker(ReportDocument doc) {
        this.document = doc;
    }

    private void prepareNamesCache() {
        namesCache = new ArrayList();

        for (Iterator it = this.document.getVariableNames(); it.hasNext();) {
            namesCache.add(it.next());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void check(String name) throws Exception {
        String err = null;

        if ((name == null) || name.equals("")) {
            err = App.messages.getString("res.114");
        } else {
            String stringName = (String) name;

            Pattern pattern = Pattern.compile(NAME_PATTERN);

            if (!pattern.matcher(stringName).find()) {
                err = App.messages.getString("res.115");
            } else {
                if (this.namesCache == null) {
                    prepareNamesCache();
                }

                if (namesCache.contains(name)) {
                    err = App.messages.getString("res.116") + name + ".";
                }
            }
        }

        if (err != null) {
            throw new Exception(err);
        }
    }
    
   
}
