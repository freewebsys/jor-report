package jatools.swingx;

import jatools.designer.action.ReportAction;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class TwoActionButton extends JButton {
    private Action action;
    private Action action2;

    /**
     * Creates a new TwoActionButton object.
     *
     * @param action DOCUMENT ME!
     * @param action2 DOCUMENT ME!
     */
    public TwoActionButton(Action action, Action action2) {
        this(action, action2, 70, 25);
    }

    /**
     * Creates a new TwoActionButton object.
     *
     * @param action DOCUMENT ME!
     * @param action2 DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    public TwoActionButton(Action action, Action action2, int w, int h) {
        super(action);
        this.action = action;
        this.action2 = action2;
        this.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (getAction() == TwoActionButton.this.action) {
                        setAction(TwoActionButton.this.action2);
                        TwoActionButton.this.setDisabledIcon((Icon) TwoActionButton.this.action2.getValue(ReportAction.ICON2  ) );
                    } else {
                        setAction(TwoActionButton.this.action);
                        TwoActionButton.this.setDisabledIcon((Icon) TwoActionButton.this.action.getValue(ReportAction.ICON2  ) );
                    }
                }
            });

        this.setPreferredSize(new Dimension(w, h));
        this.setMinimumSize(getPreferredSize());
        this.setMaximumSize(getPreferredSize());
        this.setMargin(new Insets(0, 2, 0, 2));

//        addPropertyChangeListener(new PropertyChangeListener() {
//                public void propertyChange(PropertyChangeEvent evt) {
//                    if (evt.getPropertyName().equals("enabled")) {
//                        System.out.println("aa");
//                    }
//                }
//            });
        
        
        setDisabledIcon((Icon) TwoActionButton.this.action.getValue(ReportAction.ICON2  ) );
    }
}
