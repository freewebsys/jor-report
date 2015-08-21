package jatools.designer.toolbox;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class BorderLabel extends JLabel {
	final static int SHOW_STYLE = 1;
	final static int SHOW_THICKNESS = 2;
	final static int SHOW_NOTHING = 0;
	BorderIcon icon = new BorderIcon(60, 20);
	private int showtext;

	/**
	 * Creates a new ColorLabel object.
	 */
	public BorderLabel(int showtext) {
		this.setFont(new Font("Dialog", 0, 10));
		this.setIcon(icon);
		this.showtext = showtext;
		this.setThickness2(1);
		this.setStyle("solid");
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param color
	 *            DOCUMENT ME!
	 */
	public void setColor(Color color) {
		this.icon.setColor(color);

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param style
	 *            DOCUMENT ME!
	 */
	public void setStyle(String style) {
		if (this.showtext == SHOW_STYLE) {
			this.setText(style);
		}

		this.icon.setStyle(style);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param thickness
	 *            DOCUMENT ME!
	 */
	public void setThickness2(float thickness) {
		if (this.showtext == SHOW_THICKNESS) {
			this.setText(thickness
					+ ((thickness == (int) thickness) ? "px" : "pt"));
		}

		this.icon.setThickness(thickness);
	}
}
