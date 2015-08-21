package jatools.designer.property.editor.printstyle;

import jatools.designer.App;
import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.RepeatRule;
import jatools.swingx.SwingUtil;
import jatools.swingx.TemplateComboBox;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.commons.lang.ArrayUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class RepeatRulePanel extends JPanel {
	  private static final String[] NULL = {
          ""
      };
    final static int ROW = 1;
    final static int COLUMN = 2;
    private static final String[] vNumber = {
    		"",
            "auto",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10"
        };
    private static final String[] flowWay = {
    		"",
            App.messages.getString("res.381"),
            App.messages.getString("res.382")
        };
    private static final String[] overflow = {
    		"",
            App.messages.getString("res.383"),
            App.messages.getString("res.384")
        };
    private TemplateComboBox vGapTextField;
    private TemplateComboBox hGapTextField;
    private TemplateComboBox vTempCombo = null;
    private TemplateComboBox hTempCombo = null;
    private TemplateComboBox printTempCombo = null;
    private TemplateComboBox overflowTempCombo = null;
    private TemplateComboBox modCountTempCombo = null;


    /**
     * Creates a new RepeatRulePanel object.
     */
    public RepeatRulePanel() {
        // TODO Auto-generated constructor stub
        vTempCombo = new TemplateComboBox(vNumber);
        hTempCombo = new TemplateComboBox(vNumber);
        printTempCombo = new TemplateComboBox(flowWay);
        overflowTempCombo = new TemplateComboBox(overflow);
        modCountTempCombo= new TemplateComboBox(NULL);
        vGapTextField =  new TemplateComboBox(NULL);
        hGapTextField =  new TemplateComboBox(NULL);

        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        //    gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(App.messages.getString("res.385"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;
        add(vTempCombo, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.386"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(hTempCombo, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.387"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(vGapTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.388"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(hGapTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.389")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(printTempCombo, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.390")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(overflowTempCombo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.391")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(modCountTempCombo, gbc);
        

        gbc.weighty = 100;
        add(Box.createVerticalGlue(), gbc);
        SwingUtil.setBorder6(this);
    }

    void setRule(RepeatRule rule) {
        clear();

        if ((rule != null) && (!rule.isNull())) {
            if (rule.maxCountX != null) {
                this.hTempCombo.setText(rule.maxCountX.toString());
            }

            if (rule.maxCountY != null) {
                this.vTempCombo.setText(rule.maxCountY.toString());
            }

            if (rule.gapX != null) {
                this.hGapTextField.setText(rule.gapX.toString());
            }

            if (rule.gapY != null) {
                this.vGapTextField.setText(rule.gapY.toString());
            }

            if (rule.flowFirst != null) {
                if (rule.flowFirst.is("row")) {
                    this.printTempCombo.setText(flowWay[ROW]);
                } else if (rule.flowFirst.is("column")) {
                    this.printTempCombo.setText(flowWay[COLUMN]);
                } else {
                    this.printTempCombo.setText(rule.flowFirst.toString());
                }
            }

            if (rule.overflow != null) {
                if (rule.overflow.is("hidden")) {
                    this.overflowTempCombo.setText(overflow[1]);
                } else if (rule.overflow.is("newpage")) {
                    this.overflowTempCombo.setText(overflow[2]);
                } else {
                    this.overflowTempCombo.setText(rule.overflow.toString());
                }
            }
            
            if (rule.modCount != null) {
                this.modCountTempCombo.setText(rule.modCount.toString());
            }
        }
    }

    void clear() {
        this.vTempCombo.setText(null);
        this.hTempCombo.setText(null);
        this.hGapTextField.setText(null);
        this.vGapTextField.setText(null);
        this.modCountTempCombo.setText( null);
    }

    CSSValue getCSSValue(String csstext) {
        if ((csstext != null) && (csstext.trim().length() > 0)) {
            return new CSSValue(csstext.trim());
        } else {
            return null;
        }
    }

    RepeatRule getRule() {
        RepeatRule rule = new RepeatRule();

        rule.maxCountX = this.getCSSValue(hTempCombo.getText());
        rule.maxCountY = this.getCSSValue(vTempCombo.getText());
        rule.gapX = this.getCSSValue(hGapTextField.getText());
        rule.gapY = this.getCSSValue(vGapTextField.getText());
        rule.modCount = this.getCSSValue(this.modCountTempCombo .getText());
        String text = printTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            int i = ArrayUtils.indexOf(this.flowWay, text);

            if (i == ROW) {
                text = "row";
            } else if (i == COLUMN) {
                text = "column";
            }

            rule.flowFirst = new CSSValue(text);
        }

        text = overflowTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            int i = ArrayUtils.indexOf(this.overflow, text);

            if (i == 1) {
                text = "hidden";
            } else if (i == 2) {
                text = "newpage";
            }

            rule.overflow = new CSSValue(text);
        }

        return rule;
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

        RepeatRulePanel lp = new RepeatRulePanel();
        //frame.setLocation(250,250);
        frame.setSize(new Dimension(350, 250)); //new Dimension(200,100)

        Container contentPane = frame.getContentPane();

        contentPane.add(lp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
