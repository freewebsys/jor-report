package jatools.swingx;

import java.awt.Font;


public class MoreButton extends FixedSizeButton {
    public MoreButton() {
        this(">>");
    }

    public MoreButton(String caption) {
        super(caption, 25, 25);

        this.setFont(new Font("Dialog", 0, 9));
    }
}
