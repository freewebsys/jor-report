package jatools.designer.property.editor.printstyle;

import jatools.designer.App;
import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.VisibleRule;
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
public class VisibleRulePanel extends JPanel {
    private String[] printMode = {
            "",
            App.messages.getString("res.400"),
            App.messages.getString("res.401")
        };
    private String[] visible = {
            "",
            App.messages.getString("res.402"),
            App.messages.getString("res.403")
        };
    private TemplateComboBox printModeTempCombo = null;
    private TemplateComboBox visibleTempCombo = null;

    /**
     * Creates a new RepeatRulePanel object.
     */
    public VisibleRulePanel() {
        // TODO Auto-generated constructor stub
        printModeTempCombo = new TemplateComboBox(printMode);
        visibleTempCombo = new TemplateComboBox(visible);

        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        //    gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(App.messages.getString("res.404"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;
        add(printModeTempCombo, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.405"), JLabel.LEFT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(visibleTempCombo, gbc);

        gbc.weighty = 100;
        add(Box.createVerticalGlue(), gbc);
        SwingUtil.setBorder6(this);
    }

    void setRule(VisibleRule rule) {
        clear();

        if ((rule != null) && (!rule.isNull())) {
            if (rule.printMode != null) {
                if (rule.printMode.is("everypage")) {
                    this.printModeTempCombo.setText(printMode[1]);
                } else if (rule.printMode.is("followed")) {
                    this.printModeTempCombo.setText(printMode[2]);
                } else {
                    this.printModeTempCombo.setText(rule.printMode.toString());
                }
            }

            if (rule.visible != null) {
                if (rule.visible.is("true")) {
                    this.visibleTempCombo.setText(visible[1]);
                } else if (rule.visible.is("false")) {
                    this.visibleTempCombo.setText(visible[2]);
                } else {
                    this.visibleTempCombo.setText(rule.visible.toString());
                }
            }
        }
    }

    void clear() {
        this.printModeTempCombo.setText(null);
        this.visibleTempCombo.setText(null);
    }

    CSSValue getCSSValue(String csstext) {
        if ((csstext != null) && (csstext.trim().length() > 0)) {
            return new CSSValue(csstext.trim());
        } else {
            return null;
        }
    }

    VisibleRule getRule() {
        VisibleRule rule = new VisibleRule();

        String text = printModeTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            int i = ArrayUtils.indexOf(this.printMode, text);

            if (i == 1) {
                text = "everypage";
            } else if (i == 2) {
                text = "followed";
            }

            rule.printMode = new CSSValue(text);
        }

        text = visibleTempCombo.getText();

        if ((text != null) && (text.trim().length() > 0)) {
            text = text.trim();

            int i = ArrayUtils.indexOf(this.visible, text);

            if (i == 1) {
                text = "true";
            } else if (i == 2) {
                text = "false";
            }

            rule.visible = new CSSValue(text);
        }

        return rule.isNull() ? null : rule;
    }
}
