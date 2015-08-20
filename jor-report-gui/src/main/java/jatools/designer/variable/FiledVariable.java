package jatools.designer.variable;

import jatools.designer.data.Variable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FiledVariable extends Variable {
    private boolean forLabel;

	/**
     * Creates a new FiledVariable object.
     *
     * @param disPalyName DOCUMENT ME!
     * @param permission DOCUMENT ME!
     * @param variableName DOCUMENT ME!
     */
    public FiledVariable(String disPalyName, boolean forLabel, String variableName) {
        super(disPalyName, 0, variableName);
        this.forLabel = forLabel;
        
    }
}
