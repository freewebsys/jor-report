package jatools.designer.layer.drop;

import jatools.component.Component;
import jatools.component.Text;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DropTextFactory implements DropComponentFactory {
    String[] variables;

    /**
         * @param variables
         */
    public DropTextFactory(String[] variables) {
        this.variables = variables;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Component[] create() {
        Component[] result = new Component[variables.length];

        for (int i = 0; i < result.length; i++) {
            Text text = new Text();
            text.setVariable(variables[i]);

            result[i] = text;
        }

        return result;
    }
}
