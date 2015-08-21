package jatools.designer.variable;

import jatools.designer.data.Variable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FiledsVariable extends Variable {
    private String[] variables;
    boolean forLabel;

    /**
    * Creates a new FiledVariable object.
    *
    * @param disPalyName DOCUMENT ME!
    * @param permission DOCUMENT ME!
    * @param variableName DOCUMENT ME!
    */
    public FiledsVariable(String[] vars, boolean forLabel) {
        super(null, 0, null);
        this.variables = vars;
        this.forLabel = forLabel;
    }
}
