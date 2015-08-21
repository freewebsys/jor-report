package jatools.swingx;

import jatools.util.Util;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;


public class XpRadioButton extends JRadioButton {

	static JLabel label = new JLabel();

	static final Icon CHECKED_ICON = Util
			.getIcon("/jatools/icons/xpcheck.gif");

	static final Icon NO_CHECKED_ICON = Util
			.getIcon("/jatools/icons/xpnocheck.gif");

	public XpRadioButton(String text) {

		super(text);
		//this.setSelectedIcon( CHECKED_ICON);

	}
	public void setSelected(boolean b) {
       super.setSelected( b);
       this.setIcon( b? CHECKED_ICON:NO_CHECKED_ICON);
    }


}
