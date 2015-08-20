package jatools.swingx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageSourceFilter {
	static private JPanel loader = new JPanel();

	public static Image getImage(Object source) {
		

		ImageIcon icon = null;

		if (source instanceof File) {
			icon = new ImageIcon(((File) source).getPath());
		} else if (source instanceof URL) {
			icon = new ImageIcon((URL) source);
		} else if (source instanceof byte[]) {
			icon = new ImageIcon((byte[]) source);
		}

		if (icon == null) {
			return null;
		}

		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon
			.getIconHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(icon.getImage(), null, null);

		return bi;
	}
}
