package jatools.designer.toolbox;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ChooserComboPopup extends JPopupMenu {
    SwingColorEditor ce;
    
    public ChooserComboPopup(SwingColorEditor c){
        super();
        this.ce = c;
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        SwatchChooserPanel s = new SwatchChooserPanel(c,this);
        s.buildChooser();
        p.add(s,BorderLayout.NORTH); 
        JButton b = new JButton("Other ...");
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color color = null;
                color = JColorChooser.showDialog(getParent(), "Color Chooser", color);
                getCE().setValue(color);
                // XXX: update the recentSwatch
                setVisible(false);
            }
        });
        p.add(b, BorderLayout.SOUTH); // , BorderLayout.SOUTH);
        add(p);
        addMouseListener(new PopupListener());
    }
    
    public SwingColorEditor getCE(){
        return this.ce;
    }
    
    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
        }
        
        public void mouseReleased(MouseEvent e){
            setVisible(false);
        }
    }
    
}
