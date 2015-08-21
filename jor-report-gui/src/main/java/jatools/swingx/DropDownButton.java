package jatools.swingx;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class DropDownButton extends JButton {
    private JPopupMenu popupMenu;

    public DropDownButton(String s) {
        this(s, null);
    }

    public DropDownButton(String s, Icon icon) {
        super(s, icon);
        this.popupMenu = new JPopupMenu();
        setModel(new DropDownButtonModel(popupMenu));

        popupMenu.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    getModel().setArmed(true);
                    ((DropDownButtonModel) (getModel())).setArmedAndPress(true);
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    getModel().setArmed(false);
                    ((DropDownButtonModel) (getModel())).setArmedAndPress(false);
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });

        addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    popupMenu.show(DropDownButton.this, 0, getHeight());
                }

                public void mouseReleased(MouseEvent e) {
                    fireActionPerformed(new ActionEvent(DropDownButton.this,
                            ActionEvent.ACTION_PERFORMED, ""));
                }
            });

        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("button 1 action");
                }
            });
    }

    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        this.setMaximumSize(getPreferredSize());
        this.setMinimumSize(getPreferredSize());
        this.setIconTextGap(0);
        this.setMargin(new Insets(0, 0, 0, 0));
    }

    protected void processEvent(AWTEvent e) {
        super.processEvent(e);
    }

    protected void fireActionPerformed(ActionEvent event) {
        super.fireActionPerformed(event);
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}


class DropDownButtonModel extends DefaultButtonModel {
    private JPopupMenu menu = null;
    private boolean armAndPress = true;

    DropDownButtonModel(JPopupMenu menu) {
        super();
        this.menu = menu;
    }

    void setArmedAndPress(boolean b) {
        armAndPress = b;
        setArmed(b);
        setPressed(b);
    }

    public void setArmed(boolean b) {
        super.setArmed(armAndPress);
    }

    public void setPressed(boolean b) {
        super.setPressed(armAndPress);
    }
}
