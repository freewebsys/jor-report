/*
 * Created on 2003-12-27
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
package jatools.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author zhou
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code
 * and Comments
 */
public class ButtonCellRenderer extends DefaultListCellRenderer {


	JPanel comp = new JPanel(new BorderLayout());
	JLabel iconLabel = new JLabel();

	public ButtonCellRenderer() {
		iconLabel.setHorizontalAlignment(JLabel.CENTER  );
		comp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		comp.setBackground(Color.white);

	}
	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);


		if (value instanceof JLabel) {
			JLabel labelValue = (JLabel) value;
			iconLabel.setIcon(labelValue.getIcon());
			
			setText(labelValue.getText());
		    this.setHorizontalAlignment(JLabel.CENTER  );//	setAlignmentX(0.5f );
			comp.removeAll();
			comp.add(iconLabel, BorderLayout.CENTER);
			comp.add(this, BorderLayout.SOUTH);

			return comp;
		} else
			return this;
	}

}
