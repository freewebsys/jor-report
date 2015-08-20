/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */


package jatools.component;
import jatools.accessor.PropertyDescriptor;
import jatools.core.view.CompoundView;
import jatools.core.view.View;
import jatools.engine.script.Script;

import java.util.ArrayList;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public final class ComponentContainer extends Component {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] { ComponentConstants._CHILDREN, //
            ComponentConstants._NAME,
            ComponentConstants._INIT_PRINT, // 
			ComponentConstants._AFTERPRINT,
			ComponentConstants._BEFOREPRINT2
			};
    }

    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     */
    public void setChildren(ArrayList children) {
        super.setChildren(children);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
//    public ArrayList getChildren() {
//        return super.getChildren();
//    }



	public View print(CompoundView c, Script dataProvider, int x, int y) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
