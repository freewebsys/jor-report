package jatools.dom;

import jatools.dom.field.FixedNodeField;
import bsh.CallStack;
import bsh.Interpreter;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class PaddingNode extends RowNode {
    /**
    * Creates a new PaddingNode object.
    *
    * @param parent DOCUMENT ME!
    * @param row DOCUMENT ME!
    */
    public PaddingNode(int row) {
        super(null, row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param callstack DOCUMENT ME!
     * @param interpreter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getProperty(String prop, CallStack callstack, Interpreter interpreter) {
    	 return new FixedNodeField(-100, this);
        
    }
    
    public Object valueAt(int col) {
        return null;
    }
    
    public boolean isNew()
    {
    	return true;
    }
}
