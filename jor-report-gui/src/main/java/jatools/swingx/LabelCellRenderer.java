/*
 * Created on 2003-12-27
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
package jatools.swingx;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * @author zhou
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code
 * and Comments
 */
public class LabelCellRenderer extends DefaultListCellRenderer {

	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value instanceof JLabel) {
			JLabel labelValue = (JLabel) value;
			setIcon(labelValue.getIcon());
			setText(labelValue.getText());
		}
		return this;
	}
}
