/*
 * Created on 2004-3-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.swingx;

import jatools.designer.action.ReportAction;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class Icon25x25Button extends JButton {
	/**
	 * Creates a new Icon25x25Button object.
	 * 
	 * @param action
	 *            DOCUMENT ME!
	 */
	public Icon25x25Button(Action action) {
		super(action);
		
		this.setPreferredSize(new Dimension(25, 25));
		this.setMinimumSize(getPreferredSize());
		this.setMaximumSize(getPreferredSize());

		if (action != null) {
			Icon icon2 = (Icon) action.getValue(ReportAction.ICON2);

			if (icon2 != null) {
				this.setDisabledIcon(icon2);
			}
			this.setToolTipText((String) action	.getValue(Action.SHORT_DESCRIPTION));
		}
		setText(null);
			
	}

	/**
	 * Creates a new Icon25x25Button object.
	 * 
	 * @param icon
	 *            DOCUMENT ME!
	 */
	public Icon25x25Button(Icon icon) {
		this((Action) null);
		this.setIcon(icon);
	}

	/**
	 * Creates a new Icon25x25Button object.
	 */
	public Icon25x25Button() {
		this((Action) null);
	}
}
