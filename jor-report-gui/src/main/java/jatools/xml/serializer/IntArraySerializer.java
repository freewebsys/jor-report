package jatools.xml.serializer;

import java.util.StringTokenizer;

import org.w3c.dom.Element;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.2 $
 *  @author $author$
 * 
 */

public class IntArraySerializer extends AbstractSerializer {
	static final String TOKEN = ","; //

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 * @param object DOCUMENT ME!
	 */
	public void write(Element e, Object object) {
		int[] ints = (int[]) object;

		if (ints.length == 0) {
			return;
		}

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < ints.length; i++) {
			buffer.append(ints[i]);

			if (i < (ints.length - 1)) {
				buffer.append(TOKEN);
			}
		}

		this.appendText(e, buffer.toString());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Object read(Element e, Class type) {
		String text = this.getText(e);

		StringTokenizer parser;
		parser = new StringTokenizer(text, TOKEN);

		int[] ints = new int[parser.countTokens()];

		int i = 0;
		while (parser.hasMoreTokens()) {
			ints[i] = Integer.parseInt(parser.nextToken());
			i++;
		}

		return ints;
	}
}
