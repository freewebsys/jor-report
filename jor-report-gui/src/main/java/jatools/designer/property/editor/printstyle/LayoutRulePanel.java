package jatools.designer.property.editor.printstyle;

import jatools.designer.App;
import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.LayoutRule;
import jatools.swingx.SwingUtil;
import jatools.swingx.TemplateComboBox;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.ArrayUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class LayoutRulePanel extends JPanel {
    private static final String[] NULL = {
            ""
        };
    private static final String[] hPosition = {
            "",
            App.messages.getString("res.30"),
            App.messages.getString("res.31"),
            App.messages.getString("res.32")
        };
    private static final String[] vPosition = {
            "",
            App.messages.getString("res.360"),
            App.messages.getString("res.31"),
            App.messages.getString("res.361")
        };
    private static final String[] frozenPointPrompts = {
            "",
            App.messages.getString("res.362"),
            App.messages.getString("res.363")
        };
    private static final String[] frozenPointValues = {
            "",
            "true",
            "false"
        };
    private static final String[] position = {
            "",
            "0%",
            "50%",
            "100%"
        };
    private TemplateComboBox hTempComb;
    private TemplateComboBox vTempComb;
    private TemplateComboBox bottomTextField;
    private TemplateComboBox rightTextField;
    private TemplateComboBox leftTextField;
    private TemplateComboBox topTextField;
    private TemplateComboBox heightTextField;
    private TemplateComboBox widthTextField;
   
    /**
     * Creates a new LayoutRulePanel object.
     */
    public LayoutRulePanel() {
        // TODO Auto-generated constructor stub
        hTempComb = new TemplateComboBox(hPosition);
        vTempComb = new TemplateComboBox(vPosition);
        widthTextField = new TemplateComboBox(NULL);
        heightTextField = new TemplateComboBox(NULL);
        leftTextField = new TemplateComboBox(NULL);
        topTextField = new TemplateComboBox(NULL);
        bottomTextField = new TemplateComboBox(NULL);
        rightTextField = new TemplateComboBox(NULL);

        
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        //        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(App.messages.getString("res.364"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;
        gbc.insets = new Insets(0, 0, 0, 0);

        add(hTempComb, gbc);

        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.365"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(vTempComb, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.366"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(widthTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.367"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(heightTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.368"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(topTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.369"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(leftTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.370"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(bottomTextField, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.371"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(rightTextField, gbc);

        
        gbc.weighty = 100;
        add(Box.createVerticalGlue(), gbc);
        SwingUtil.setBorder6(this);
    }

    void setRule(LayoutRule rule) {
        clear();

        if ((rule != null) && (!rule.isNull())) {
            if (rule.alignX != null) {
                int i = ArrayUtils.indexOf(position, rule.alignX.toString());

                if (i != -1) {
                    this.hTempComb.setText(hPosition[i]);
                } else {
                    this.hTempComb.setText(rule.alignX.toString());
                }
            }

            if (rule.alignY != null) {
                int i = ArrayUtils.indexOf(position, rule.alignY.toString());

                if (i != -1) {
                    this.vTempComb.setText(vPosition[i]);
                } else {
                    this.vTempComb.setText(rule.alignY.toString());
                }
            }

            if (rule.bottom != null) {
                this.bottomTextField.setText(rule.bottom.toString());
            }

            if (rule.right != null) {
                this.rightTextField.setText(rule.right.toString());
            }

            if (rule.left != null) {
                this.leftTextField.setText(rule.left.toString());
            }

            if (rule.top != null) {
                this.topTextField.setText(rule.top.toString());
            }

            if (rule.width != null) {
                this.widthTextField.setText(rule.width.toString());
            }

            if (rule.height != null) {
                this.heightTextField.setText(rule.height.toString());
            }

            
        }
    }

    void clear() {
        this.hTempComb.setText(null);
        this.vTempComb.setText(null);
        this.leftTextField.setText(null);
        this.topTextField.setText(null);
        this.bottomTextField.setText(null);
        this.rightTextField.setText(null);

        this.widthTextField.setText(null);
        this.heightTextField.setText(null);
        
    }

    CSSValue getCSSValue(String csstext) {
        if ((csstext != null) && (csstext.trim().length() > 0)) {
            return new CSSValue(csstext.trim());
        } else {
            return null;
        }
    }

    LayoutRule getRule() {
        LayoutRule rule = new LayoutRule();

        String ax = hTempComb.getText();

        if ((ax != null) && (ax.trim().length() > 0)) {
            ax = ax.trim();

            int i = ArrayUtils.indexOf(hPosition, ax);

            if (i != -1) {
                ax = position[i];
            }

            rule.alignX = new CSSValue(ax);
        }

        String ay = vTempComb.getText();

        if ((ay != null) && (ay.trim().length() > 0)) {
            ay = ay.trim();

            int i = ArrayUtils.indexOf(vPosition, ay);

            if (i != -1) {
                ax = position[i];
            }

            rule.alignY = new CSSValue(ax);
        }

        rule.left = this.getCSSValue(this.leftTextField.getText());
        rule.top = this.getCSSValue(this.topTextField.getText());
        rule.right = this.getCSSValue(this.rightTextField.getText());
        rule.bottom = this.getCSSValue(this.bottomTextField.getText());
        rule.width = this.getCSSValue(this.widthTextField.getText());
        rule.height = this.getCSSValue(this.heightTextField.getText());

       

        if (!rule.isNull()) {
            return rule;
        } else {
            return null;
        }
    }
}
