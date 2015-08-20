package jatools.xml.serializer;

import jatools.engine.ProtectClass;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.2 $
 *  @author $author$
 * 
 */

public class ClassResolvable implements ProtectClass{
	Object object;

	/**
	 * Creates a new ZAutoObject object.
	 *
	 * @param object DOCUMENT ME!
	 */
	public ClassResolvable(Object object) {
		this.object = object;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Object getObject() {
		return object;
	}
}
