package jatools.xml.serializer;

import java.awt.Color;

import org.w3c.dom.Element;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.1 $
 *  @author $author$
 * 
 */

public class ColorSerializer extends AbstractSerializer {
	private Color obj;

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Object read(Element e, Class type) {
		return new Color(intValue(e));
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 * @param object DOCUMENT ME!
	 */
	public void write(Element e, Object object) {
		setText(e, String.valueOf(((Color) object).getRGB()));
	}
}
