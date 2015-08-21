package jatools.designer.toolbox;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ColorButton extends JLabel /**JButton*/{
    /**
     * Creates a new ColorButton object.
     *
     * @param col DOCUMENT ME!
     */
    public ColorButton(Color col) {
        super();
        this.setText("");

        Dimension dim = new Dimension(15, 15);
        this.setSize(dim);
        this.setPreferredSize(dim);
        this.setMinimumSize(dim);
        setBorder(BorderFactory.createRaisedBevelBorder());
        this.setBackground(col);
     
        
        
    }
}
