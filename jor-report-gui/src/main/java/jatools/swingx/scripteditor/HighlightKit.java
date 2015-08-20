package jatools.swingx.scripteditor;

import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;

public  class HighlightKit extends StyledEditorKit {
    private boolean asTemplate;

	public HighlightKit(boolean asTemplate) {
		this.asTemplate = asTemplate;
	}

	public Document createDefaultDocument() {
        return new HighlightDocument(asTemplate);
    }
}
