package jatools.swingx;

import jatools.designer.App;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class ColorChooser implements Chooser {
	Color color;

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean showChooser(
		JComponent owner,
		Object value) {
		color = (Color) value;
		Color selectedColor =
			JColorChooser.showDialog(owner, App.messages.getString("res.12"), color); 

		if ((selectedColor != null) && !selectedColor.equals(color)) {
			color = selectedColor;

			return true;
		} else {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue() {
		return color;
	}

}
