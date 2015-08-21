package jatools.swingx;

import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;

public class highlightKit extends StyledEditorKit {
    public Document createDefaultDocument() {
        return new HighlightDocument();
    }
}
