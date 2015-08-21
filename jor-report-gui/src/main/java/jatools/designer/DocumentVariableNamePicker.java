package jatools.designer;

import jatools.ReportDocument;


public class DocumentVariableNamePicker {
    private ReportDocument document;

    public DocumentVariableNamePicker(ReportDocument doc) {
        this.document = doc;
    }

    public String newName(String prefix) {
        String var = null;
        DocumentVariableNameChecker checker = new DocumentVariableNameChecker(document);

        for (int i = 1; true; i++) {
            try {
                checker.check(var = prefix + i);

                break;
            } catch (Exception e) {
                
            }
        }

        return var;
    }
}
