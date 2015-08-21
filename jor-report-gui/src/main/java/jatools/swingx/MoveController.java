package jatools.swingx;

import jatools.designer.App;
import jatools.swingx.dnd.Moveable;
import jatools.util.Util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class MoveController extends JPanel {
    /**
     * DOCUMENT ME!
     */
    public static final int SELECT = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int UNSELECT = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int SELECT_ALL = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int UNSELECT_ALL = 8;

    /**
     * DOCUMENT ME!
     */
    public static final int FULL_OPTIONS = SELECT | UNSELECT | SELECT_ALL | UNSELECT_ALL;

    /**
     * DOCUMENT ME!
     */
    public static final int SINGLE_OPTIONS = SELECT | UNSELECT;
    Moveable leftMoveable;
    Moveable rightMoveable;

    /**
     * Creates a new MoveController object.
     *
     * @param leftMoveable DOCUMENT ME!
     * @param rightMoveable DOCUMENT ME!
     */
    public MoveController(Moveable leftMoveable, Moveable rightMoveable) {
        this(leftMoveable, rightMoveable, FULL_OPTIONS);
    }

    /**
     * Creates a new ZMoveController object.
     */
    public MoveController(Moveable leftMoveable, Moveable rightMoveable, int options) {
        if (leftMoveable == null) {
            throw new NullPointerException(App.messages.getString("res.27")); //
        }

        if (rightMoveable == null) {
            throw new NullPointerException(App.messages.getString("res.28")); //
        }

        this.leftMoveable = leftMoveable;
        this.rightMoveable = rightMoveable;

        setLayout(new GridBagLayout());

        Dimension si = new Dimension(30, 25);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        //  gbc.weighty = 1;
        gbc.insets = new Insets(2, 0, 2, 0);

        JButton b = null;

        if ((options & SELECT) != 0) {
            b = new JButton(Util.getIcon("/jatools/icons/select.gif")); //
            b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        move();
                    }
                });

            b.setPreferredSize(si);

            add(b, gbc);
        }

        if ((options & SELECT_ALL) != 0) {
            b = new JButton(Util.getIcon("/jatools/icons/selectall.gif")); //
            b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        moveAll();
                    }
                });

            b.setPreferredSize(si);

            add(b, gbc);
        }

        if ((options & UNSELECT) != 0) {
            b = new JButton(Util.getIcon("/jatools/icons/unselect.gif")); //
            b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        moveBack();
                    }
                });

            b.setPreferredSize(si);
            add(b, gbc);
        }

        if ((options & UNSELECT_ALL) != 0) {
            b = new JButton(Util.getIcon("/jatools/icons/unselectall.gif")); //
            b.setFont(new Font("Arial", 0, 25)); //
            b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        moveAllBack();
                    }
                });
            b.setPreferredSize(si);

            add(b, gbc);
        }

        // setPreferredSize(new Dimension(250,10));
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
    }

    /**
     * DOCUMENT ME!
     */
    public void move() {
        rightMoveable.drop(leftMoveable);
    }

    /**
     * DOCUMENT ME!
     */
    public void moveAll() {
        rightMoveable.dropAll(leftMoveable);
    }

    /**
     * DOCUMENT ME!
     */
    private void moveBack() {
        leftMoveable.drop(rightMoveable);
    }

    /**
     * DOCUMENT ME!
     */
    private void moveAllBack() {
        leftMoveable.dropAll(rightMoveable);
    }
}
