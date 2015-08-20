package jatools.swingx;

import jatools.data.Formula;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.data.CustomFormulaDialog;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class TemplateSpinEditor extends JPanel {
    static final Icon icon = Util.getIcon("/jatools/icons/templatetext.gif");
    SpinEditor spinLevel = new SpinEditor(new Integer(1), new Integer(Integer.MAX_VALUE));

    /**
     * Creates a new TemplateSpinEditor object.
     */
    public TemplateSpinEditor() {
        // TODO Auto-generated constructor stub
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        spinLevel.setPreferredSize(new Dimension(100, 23));
        add(spinLevel, gbc);
        gbc.weightx = 0;

        JButton b = new JButton(icon);
        b.setFocusPainted(false);
        b.setMargin(null);
        SwingUtil.setSize(b, new Dimension(23, 23));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showTemplateTextEditor();
                }
            });
        add(b, gbc);
    }

    protected void showTemplateTextEditor() {
        CustomFormulaDialog d = new CustomFormulaDialog(Main.getInstance(),false);

        Formula formula = d.start();

        if (formula != null) {
            this.setText(formula.getText());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text) {
        spinLevel.setValue(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return (String) this.spinLevel.getValue();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        JFrame frame = new JFrame(App.messages.getString("res.29"));
        frame.setResizable(true);

        TemplateSpinEditor lp = new TemplateSpinEditor();
        //frame.setLocation(250,250);
        frame.setSize(new Dimension(350, 250)); //new Dimension(200,100)

        Container contentPane = frame.getContentPane();

        contentPane.add(lp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
