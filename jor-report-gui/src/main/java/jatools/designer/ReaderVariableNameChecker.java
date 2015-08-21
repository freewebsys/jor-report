package jatools.designer;

import jatools.data.reader.DatasetReader;
import jatools.designer.data.NameChecker;

import java.util.ArrayList;
import java.util.regex.Pattern;



public class ReaderVariableNameChecker implements NameChecker {
    private static final String NAME_PATTERN = "^[\\$_a-zA-Z\u4e00-\u9fa5]{1}[_a-zA-Z0-9\u4e00-\u9fa5]*$";
    private DatasetReader reader;
    private ArrayList namesCache;

    /**
     * Creates a new DocumentVariableNameChecker object.
     *
     * @param reader DOCUMENT ME!
     */
    public ReaderVariableNameChecker(DatasetReader reader) {
        this.reader = reader;
    }

    private void prepareNamesCache() {
        namesCache = new ArrayList();

//        for (Iterator it = this.document.getVariableNames(); it.hasNext();) {
//            namesCache.add(it.next());
//        }
//
//        DatasetReader reader = (DatasetReader) this.document.getVariable(ZPrintConstants.MASTER_DATASET);
//
//        if (reader != null) {
//            String[] columns = Dataset.getFieldNames(reader);
//
//            for (int i = 0; i < columns.length; i++) {
//                namesCache.add(ReportPrinterUtil.FIELD_NAME_PREFIX + columns[i]);
//            }
//        }
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
