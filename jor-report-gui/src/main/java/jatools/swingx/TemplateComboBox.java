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

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class TemplateComboBox extends JPanel {
    static final Icon icon = Util.getIcon("/jatools/icons/formula.gif");
    TemplateComboBoxModel model = null;
    JComboBox combo = null;

    /**
     * Creates a new TemplateComboBox object.
     *
     * @param data DOCUMENT ME!
     */
    public TemplateComboBox(String[] data) {
        // TODO Auto-generated constructor stub
        super(new GridBagLayout());
        model = new TemplateComboBoxModel(data);
        combo = new JComboBox(model);
        // combo= new JComboBox(data);
        combo.setEditable(true);
        combo.setSelectedIndex(-1);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        add(combo, gbc);
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
        this.setPreferredSize( new Dimension(150,25));
    }

    protected void showTemplateTextEditor() {
        CustomFormulaDialog d = new CustomFormulaDialog(Main.getInstance(),false);
        String text = this.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            if (text.startsWith("${") && text.endsWith("}")) {
                text = text.substring(2, text.length() - 1);
            }
        }

        Formula formula = d.start(text);

        if (formula != null) {
            this.setText("${" + formula.getText() + "}");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text) {
        this.combo.setSelectedItem(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return (String) this.combo.getSelectedItem();
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

        String[] hPosition = {
                App.messages.getString("res.30"),
                App.messages.getString("res.31"),
                App.messages.getString("res.32")
            };
        TemplateComboBox lp = new TemplateComboBox(hPosition);
        frame.setSize(new Dimension(350, 250)); //new Dimension(200,100)

        Container contentPane = frame.getContentPane();

        contentPane.add(lp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}


class TemplateComboBoxModel extends DefaultComboBoxModel {
    /**
     * Creates a new TemplateComboBoxModel object.
     *
     * @param items DOCUMENT ME!
     */
    public TemplateComboBoxModel(String[] items) {
        for (int i = 0; i < items.length; i++) {
            this.addElement(items[i]);
        }
    }
}
