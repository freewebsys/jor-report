package jatools.swingx;

import jatools.data.Formula;
import jatools.designer.Main;
import jatools.designer.data.CustomFormulaDialog;
import jatools.util.Util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class TemplateTextField extends JPanel {
    static final Icon icon = Util.getIcon("/jatools/icons/templatetext.gif");
    static final Icon formulaicon = Util.getIcon("/jatools/icons/formula.gif");
    JTextField t;
    
    boolean astemp ;

    /**
     * Creates a new TemplateTextField object.
     */
    public TemplateTextField() {
        this(true);
    }

    /**
     * Creates a new TemplateTextField object.
     *
     * @param temp DOCUMENT ME!
     */
    public TemplateTextField(boolean temp) {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        t = new JTextField(12);
        add(t, gbc);
        gbc.weightx = 0;

        JButton b = new JButton(temp ? icon : formulaicon);
        b.setFocusPainted(false);
        b.setMargin(null);
        SwingUtil.setSize(b, new Dimension(23, 23));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showTemplateTextEditor();
                }
            });
        add(b, gbc);
        this.astemp = temp;
    }

    protected void showTemplateTextEditor() {
        CustomFormulaDialog d = new CustomFormulaDialog(Main.getInstance(),astemp);

        Formula formula = d.start(t.getText());

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
        this.t.setText(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return this.t.getText();
    }

    /**
     * DOCUMENT ME!
     */
    public void requestFocus() {
        this.t.requestFocus();
    }
}
