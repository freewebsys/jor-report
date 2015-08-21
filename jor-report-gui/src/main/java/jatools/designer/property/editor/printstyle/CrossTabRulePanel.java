package jatools.designer.property.editor.printstyle;

import jatools.designer.App;
import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.CrossTabRule;
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
public class CrossTabRulePanel extends JPanel {
    private static final String[] headerVisiblePrompts = {
            "",
            App.messages.getString("res.230"),
            App.messages.getString("res.231")
        };
    private static final String[] headerVisibleValues = {
            "",
            "firstpage",
            "everypage"
        };
    private static final String[] pageWrapPrompts = {
            "",
            App.messages.getString("res.232"),
            App.messages.getString("res.233")
        };
    private static final String[] pageWrapValues = {
            "",
            "true",
            "false"
        };
    private TemplateComboBox topHeaderVisibleCombo = null;
    private TemplateComboBox leftHeaderVisibleCombo = null;
    private TemplateComboBox pageWrapCombo = null;

    /**
     * Creates a new CrossTabRulePanel object.
     */
    public CrossTabRulePanel() {
        // TODO Auto-generated constructor stub
        topHeaderVisibleCombo = new TemplateComboBox(headerVisiblePrompts);
        leftHeaderVisibleCombo = new TemplateComboBox(headerVisiblePrompts);
        pageWrapCombo = new TemplateComboBox(pageWrapPrompts);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(App.messages.getString("res.240")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weightx = 100;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(topHeaderVisibleCombo, gbc);

        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.239")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(leftHeaderVisibleCombo, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JLabel(App.messages.getString("res.359")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(pageWrapCombo, gbc);

        gbc.weighty = 100;
        add(Box.createVerticalGlue(), gbc);
        SwingUtil.setBorder6(this);
    }

    void setRule(CrossTabRule rule) {
        clear();

        if ((rule != null) && (!rule.isNull())) {
            if (rule.leftHeaderVisible != null) {
                int i = ArrayUtils.indexOf(headerVisibleValues, rule.leftHeaderVisible.toString());

                if (i != -1) {
                    this.leftHeaderVisibleCombo.setText(headerVisiblePrompts[i]);
                } else {
                    this.leftHeaderVisibleCombo.setText(rule.leftHeaderVisible.toString());
                }
            }

            if (rule.topHeaderVisible != null) {
                int i = ArrayUtils.indexOf(headerVisibleValues, rule.topHeaderVisible.toString());

                if (i != -1) {
                    this.topHeaderVisibleCombo.setText(headerVisiblePrompts[i]);
                } else {
                    this.topHeaderVisibleCombo.setText(rule.topHeaderVisible.toString());
                }
            }

            if (rule.pageWrap != null) {
                int i = ArrayUtils.indexOf(pageWrapValues, rule.pageWrap.toString());

                if (i != -1) {
                    this.pageWrapCombo.setText(pageWrapPrompts[i]);
                } else {
                    this.pageWrapCombo.setText(rule.pageWrap.toString());
                }
            }
        }
    }

    void clear() {
        this.topHeaderVisibleCombo.setText(null);
        this.leftHeaderVisibleCombo.setText(null);
        this.pageWrapCombo.setText(null);
    }

    CSSValue getCSSValue(String csstext) {
        if ((csstext != null) && (csstext.trim().length() > 0)) {
            return new CSSValue(csstext.trim());
        } else {
            return null;
        }
    }

    protected CrossTabRule getRule() {
        CrossTabRule rule = new CrossTabRule();

        String ax = topHeaderVisibleCombo.getText();

        if ((ax != null) && (ax.trim().length() > 0)) {
            ax = ax.trim();

            int i = ArrayUtils.indexOf(headerVisiblePrompts, ax);

            if (i != -1) {
                ax = headerVisibleValues[i];
            }

            rule.topHeaderVisible = new CSSValue(ax);
        }

        ax = leftHeaderVisibleCombo.getText();

        if ((ax != null) && (ax.trim().length() > 0)) {
            ax = ax.trim();

            int i = ArrayUtils.indexOf(headerVisiblePrompts, ax);

            if (i != -1) {
                ax = headerVisibleValues[i];
            }

            rule.leftHeaderVisible = new CSSValue(ax);
        }

        ax = pageWrapCombo.getText();

        if ((ax != null) && (ax.trim().length() > 0)) {
            ax = ax.trim();

            int i = ArrayUtils.indexOf(pageWrapPrompts, ax);

            if (i != -1) {
                ax = pageWrapValues[i];
            }

            rule.pageWrap = new CSSValue(ax);
        }

        if (!rule.isNull()) {
            return rule;
        } else {
            return null;
        }
    }
}
