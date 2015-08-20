package jatools.designer.property.editor.printstyle;

import jatools.designer.App;
import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.PageRule;
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
 * @version $Revision: 1.3 $
  */
public class PageRulePanel extends JPanel {
    private static final String[] forceBreakPrompts = { "", App.messages.getString("res.232"), App.messages.getString("res.233") };
    private static final String[] forceBreakValues = { "", "true", "false" };
    private String[] newPagePos = { "", App.messages.getString("res.372"), "0" };
    private TemplateComboBox newPageXTempCombo = null;
    private TemplateComboBox forceBreakTempCombo = new TemplateComboBox(forceBreakPrompts);
    private TemplateComboBox newPageYTempCombo = null;

    /**
     * Creates a new RepeatRulePanel object.
     */
    public PageRulePanel() {
        // TODO Auto-generated constructor stub
        newPageXTempCombo = new TemplateComboBox(newPagePos);
        newPageYTempCombo = new TemplateComboBox(newPagePos);

        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        //    gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(App.messages.getString("res.373"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;
        add(newPageYTempCombo, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.374"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(newPageXTempCombo, gbc);

        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.375"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(forceBreakTempCombo, gbc);

        gbc.weighty = 100;
        add(Box.createVerticalGlue(), gbc);
        SwingUtil.setBorder6(this);
    }

    void setRule(PageRule rule) {
        clear();

        if ((rule != null) && (!rule.isNull())) {
            if (rule.newPageX != null) {
                if (rule.newPageX.is("100%")) {
                    this.newPageXTempCombo.setText(newPagePos[1]);
                } else {
                    this.newPageXTempCombo.setText(rule.newPageX.toString());
                }
            }

            if (rule.newPageY != null) {
                if (rule.newPageY.is("100%")) {
                    this.newPageYTempCombo.setText(newPagePos[1]);
                } else {
                    this.newPageYTempCombo.setText(rule.newPageY.toString());
                }
            }
            
            if (rule.forceBreak != null) {
                int i = ArrayUtils.indexOf(forceBreakValues, rule.forceBreak.toString());

                if (i != -1) {
                    this.forceBreakTempCombo.setText(forceBreakPrompts[i]);
                } else {
                    this.forceBreakTempCombo.setText(rule.forceBreak.toString());
                }
            }
            
        }
    }

    void clear() {
        this.newPageXTempCombo.setText(null);
        this.newPageYTempCombo.setText(null);
        this.forceBreakTempCombo.setText(null);
    }

    CSSValue getCSSValue(String csstext) {
        if ((csstext != null) && (csstext.trim().length() > 0)) {
            return new CSSValue(csstext.trim());
        } else {
            return null;
        }
    }

    PageRule getRule() {
        PageRule rule = new PageRule();

        String text = newPageXTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            int i = ArrayUtils.indexOf(this.newPagePos, text);

            if (i == 1) {
                text = "100%";
            }

            rule.newPageX = new CSSValue(text);
        }

        text = newPageYTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            int i = ArrayUtils.indexOf(this.newPagePos, text);

            if (i == 1) {
                text = "100%";
            }

            rule.newPageY = new CSSValue(text);
        }
        
        text = forceBreakTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
        	text = text.trim();

            int i = ArrayUtils.indexOf(forceBreakPrompts, text);

            if (i != -1) {
            	text = forceBreakValues[i];
            }

            rule.forceBreak = new CSSValue(text);
        }
        

        return rule.isNull() ? null : rule;
    }
}
