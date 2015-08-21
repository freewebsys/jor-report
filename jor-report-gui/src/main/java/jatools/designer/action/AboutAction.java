package jatools.designer.action;

import jatools.designer.App;
import jatools.designer.Main;
import jatools.swingx.CommandPanel;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision$
 */
public class AboutAction extends ReportAction {
	/**
	 * Creates a new AboutAction object.
	 */
	public AboutAction() {
		super(App.messages.getString("res.569"));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
	public void actionPerformed(ActionEvent e) {
		new _AboutDialog();
	}

	class _AboutDialog extends JDialog {
		_AboutDialog() {
			super(Main.getInstance(), App.messages.getString("res.570"), true);

			JPanel panel = new JPanel(new GridBagLayout());
			JLabel label = new JLabel(Util.getIcon("/jatools/icons/about.jpg"));
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setBackground(Color.white);
			label.setOpaque(true);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;

			gbc.gridwidth = GridBagConstraints.REMAINDER;
			panel.add(label, gbc);

			gbc.gridwidth = 1;
			panel.add(Box.createHorizontalStrut(10), gbc);

			gbc.weightx = 100.0;

			String html = App.messages.getString("res.571");

//www.jatools.com'>www.jatools.com</a><br>销售咨询:0571-88254255/57-18<br>技术支持:0571-88254255/57-13&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MSN: <a href='#' >jatools0571@hotmail.com</a><br>北京办事处:010-51297501";
			panel.add(new JLabel(html), gbc);

			CommandPanel commandPanel = CommandPanel.createPanel();
			JButton doneCommand = new JButton(App.messages.getString("res.3"));
			doneCommand.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hide();
					
				}
			});

			commandPanel.addComponent(doneCommand);

			getContentPane().add(panel, BorderLayout.CENTER);
			getContentPane().add(commandPanel, BorderLayout.SOUTH);
			setSize(new Dimension(440, 370));

			setLocationRelativeTo(Main.getInstance());
			show();
		}
	}
}
