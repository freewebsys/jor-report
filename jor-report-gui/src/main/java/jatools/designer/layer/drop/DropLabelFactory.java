package jatools.designer.layer.drop;

import jatools.component.Component;
import jatools.component.Label;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DropLabelFactory implements DropComponentFactory {
    String[] texts;

    /**
         * @param variables
         */
    public DropLabelFactory(String[] variables) {
        this.texts = variables;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Component[] create() {
        Component[] result = new Component[texts.length];

        for (int i = 0; i < result.length; i++) {
            Label label = new Label();
            label.setText(texts[i]);

            result[i] = label;
        }

        return result;
    }
}
