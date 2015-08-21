package jatools.designer;

import jatools.designer.wizard.BuilderContext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class OtherSelector extends javax.swing.JPanel {
    JCheckBox showPage;
    JCheckBox tableMode;
    JRadioButton onFooter;
    JRadioButton onHeader;
    JPanel jPanel3;
    JPanel jPanel2;
    JCheckBox printPage;
    JTextField titleText;
    JLabel jLabel1;
    JPanel jPanel1;

    /**
     * Creates a new OtherSelector object.
     */
    public OtherSelector() {
        initGUI();
    }

    /**
     * DOCUMENT ME!
     */
    public void initGUI() {
        try {
            jPanel1 = new JPanel();
            jLabel1 = new JLabel();
            titleText = new JTextField();
            jPanel2 = new JPanel();
            showPage = new JCheckBox();
            jPanel3 = new JPanel();

            ButtonGroup group = new ButtonGroup();
            onHeader = new JRadioButton();
            onFooter = new JRadioButton();

            group.add(onHeader);
            group.add(onFooter);

            GridBagLayout thisLayout = new GridBagLayout();
            setLayout(thisLayout);
            setPreferredSize(new java.awt.Dimension(465, 301));
            setBounds(new java.awt.Rectangle(0, 0, 465, 301));

            BorderLayout jPanel1Layout = new BorderLayout();
            jPanel1.setLayout(jPanel1Layout);
            jPanel1.setVisible(true);

            jPanel1.setBorder(new EmptyBorder(new Insets(0, 20, 0, 20)));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(10, 0, 0, 0);

            this.add(jPanel1, gbc);

            jLabel1.setText(App.messages.getString("res.113"));

            jPanel1.add(jLabel1, BorderLayout.WEST);

            titleText.setVisible(true);

            titleText.setPreferredSize(new Dimension(250, 23));
            titleText.setBounds(new java.awt.Rectangle(151, 46, 151, 77));
            jPanel1.add(titleText, BorderLayout.CENTER);

            GridLayout jPanel2Layout = new GridLayout(2, 1);
            jPanel2.setLayout(jPanel2Layout);

            jPanel2.setPreferredSize(new java.awt.Dimension(332, 109));
            jPanel2.setBorder(BorderFactory.createTitledBorder(App.messages.getString("res.136")));

            jPanel2.setBounds(new java.awt.Rectangle(66, 109, 332, 109));
            this.add(jPanel2, gbc);
            showPage.setText(App.messages.getString("res.137"));
            showPage.setSelected(true);
            showPage.setVisible(true);
            showPage.setPreferredSize(new java.awt.Dimension(302, 64));
            showPage.setBorder(new EmptyBorder(new Insets(0, 10, 0, 0)));
            showPage.setBounds(new java.awt.Rectangle(5, 5, 322, 49));
            jPanel2.add(showPage);

            tableMode = new JCheckBox(App.messages.getString("res.138"));
            tableMode.setSelected(true);
            tableMode.setVisible(false);
            tableMode.setPreferredSize(new java.awt.Dimension(302, 64));
            tableMode.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
            tableMode.setBounds(new java.awt.Rectangle(5, 5, 322, 49));
            this.add(tableMode, gbc);

            FlowLayout jPanel3Layout = new FlowLayout();
            jPanel3.setLayout(jPanel3Layout);
            jPanel3.setVisible(true);
            jPanel3.setPreferredSize(new java.awt.Dimension(309, 76));
            jPanel3.setBorder(new EmptyBorder(new Insets(0, 0, 100, 0)));
            jPanel3.setBounds(new java.awt.Rectangle(5, 5, 322, 128));
            jPanel2.add(jPanel3);
            onHeader.setText(App.messages.getString("res.139"));
            onHeader.setVisible(true);
            jPanel3.add(onHeader);
            onFooter.setText(App.messages.getString("res.140"));
            onFooter.setSelected(true);
            onFooter.setVisible(true);
            showPage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        onHeader.setEnabled(showPage.isSelected());
                        onFooter.setEnabled(showPage.isSelected());
                    }
                });

            jPanel3.add(onFooter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        showGUI();
    }

    /**
     * DOCUMENT ME!
     */
    public static void showGUI() {
        try {
            JFrame frame = new JFrame();
            OtherSelector inst = new OtherSelector();
            frame.setContentPane(inst);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void apply(BuilderContext context) {
        context.setValue(BuilderContext.TITLE, titleText.getText());

        if (showPage.isSelected()) {
            context.setValue(BuilderContext.WHERE_PAGE_NUMBER,
                Boolean.valueOf(onFooter.isSelected()));
        }

        context.setValue(BuilderContext.TABLE_MODE, new Boolean(tableMode.isSelected()));
    }
}
