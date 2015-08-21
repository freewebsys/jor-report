package jatools.designer.variable.dialog;

import jatools.data.interval.formula.IntervalFormula;
import jatools.designer.Global;
import jatools.designer.JatoolsException;
import jatools.swingx.CommandPanel;
import jatools.swingx.MessageBox;
import jatools.swingx.TemplateTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class IntervalFormulaDialog extends JDialog {
    private TemplateTextField formulaText = new TemplateTextField(false);
    private TemplateTextField asText = new TemplateTextField(false);
    private IntervalFormula dateColumn;
    private boolean done;

    IntervalFormulaDialog(Frame owner, String title) {
        super(owner, title, true);

        JPanel topPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Global.GBC_INSETS;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;

        topPanel.add(new JLabel("区间条件:"), gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        //     gbc.add( nameText,100);
        topPanel.add(this.formulaText, gbc);

        gbc.gridwidth = 1;
        topPanel.add(new JLabel("区间值:"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        topPanel.add(this.asText, gbc);

        //    topPanel.add(funcCombo, gbc);
        ActionListener lok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            };

        ActionListener cok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    canel();
                }
            };

        CommandPanel cp = CommandPanel.createPanel(CommandPanel.OK, lok, CommandPanel.CANCEL, cok);
        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(cp, BorderLayout.SOUTH);

        pack();
        setSize(new Dimension(340, 160));
        this.setLocationRelativeTo(owner);
    }

    protected void canel() {
        hide();
    }

    private void done() {
        try {
            dateColumn = this.createIntervalFormula();
            done = true;
            hide();
        } catch (JatoolsException e) {
            MessageBox.error(this, e.getMessage());

            JComponent c = (JComponent) e.getSource();

            if (c != null) {
                c.requestFocus();
            }
        }
    }

    IntervalFormula getIntervalFormula() {
        return this.dateColumn;
    }

    void setIntervalFormula(IntervalFormula f) {
        this.formulaText.setText(f.getExpression());
        this.asText.setText(f.getAs());
    }

    IntervalFormula createIntervalFormula() throws JatoolsException {
        String expression = this.formulaText.getText();
        String as = this.asText.getText();

        if ((as == null) || (as.trim().length() == 0)) {
            throw new JatoolsException("区间值不能为空.", asText);
        }

        return new IntervalFormula(expression, as);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone() {
        return done;
    }
}
