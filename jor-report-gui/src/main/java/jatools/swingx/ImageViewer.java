package jatools.swingx;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author zhou
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ImageViewer extends JLabel {
	ImageIcon icon = null;
	ImageIcon rawIcon ;

	public void setImageIcon(ImageIcon rawIcon) {
		if (rawIcon != null) {

			if (rawIcon.getIconWidth() <= getWidth()
				&& rawIcon.getIconHeight() <= getHeight()) {
				icon = rawIcon;
			} else {
				if (getWidth() * 1.0 / rawIcon.getIconWidth()
					> getHeight() * 1.0 / rawIcon.getIconHeight())
					icon =
						new ImageIcon(
							rawIcon.getImage().getScaledInstance(
								-1,
								this.getHeight(),
								Image.SCALE_DEFAULT));
				else
					icon =
						new ImageIcon(
							rawIcon.getImage().getScaledInstance(
								this.getWidth(),
								-1,
								Image.SCALE_DEFAULT));

			}

			
			this.rawIcon = rawIcon;

		}
	}
	
	public ImageIcon getRawIcon()
	{
		return rawIcon;
	}

	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground() );
		g.fillRect( 0,0,getWidth(),getHeight()); 
	
		if (icon != null) {
			int x = (getWidth() / 2) - (icon.getIconWidth() / 2);
			int y = (getHeight() / 2) - (icon.getIconHeight() / 2);

			if (y < 0) {
				y = 0;
			}

			if (x < 5) {
				x = 5;
			}

			icon.paintIcon(this, g, x, y);
		}
	}
}
