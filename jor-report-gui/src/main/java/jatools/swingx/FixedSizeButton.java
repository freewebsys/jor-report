package jatools.swingx;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;


public class FixedSizeButton extends JButton {
    public FixedSizeButton(String caption,int width,int height) {
        this(null,caption,width,height);
        
    }
    
    public FixedSizeButton(Icon icon,String caption,int width,int height) {
        super(caption,icon);
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(getPreferredSize());
        this.setMinimumSize(getPreferredSize());
        this.setIconTextGap(0);
        this.setMargin(new Insets(0, 0, 0, 0));
    }
}
