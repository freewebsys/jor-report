package jatools.swingx;

import jatools.designer.App;
import jatools.swingx.wizard.AbstractWizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class WizardTabbedPane extends JPanel implements ActionListener, ChangeListener {
    public static final String PROPERTY_WIZARD = "wizard"; //
    static final String NEXT = App.messages.getString("res.37"); //
    static final String FINISHED = App.messages.getString("res.3"); //
    static Icon padIcon;
    JTabbedPane steps = new JTabbedPane();
    JButton prevButton = new JButton(App.messages.getString("res.38")); //
    JButton nextButton = new JButton(NEXT);
    JButton cancelButton = new JButton(App.messages.getString("res.4")); //
    AbstractWizard selectedWizard;
    ActionListener finishedAction;

    /**
     * Creates a new ZWizardTabbedPane object.
     */
    public WizardTabbedPane() {
        buildUI();
    }

    /**
     * DOCUMENT ME!
     */
    public void doNext() {
        nextButton.doClick();
    }

    /**
     * DOCUMENT ME!
     *
     * @param finishedAction DOCUMENT ME!
     */
    public void setFinishedAction(ActionListener finishedAction) {
        this.finishedAction = finishedAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent evt) {
        if (evt.getSource() == steps) {
            try {
                JComponent comp = (JComponent) steps.getSelectedComponent();

                if ((selectedWizard != null) && (selectedWizard.getEditorComponent() != comp)) {
                    selectedWizard.leave();
                }

                AbstractWizard wizard = (AbstractWizard) comp.getClientProperty(WizardTabbedPane.PROPERTY_WIZARD);
                wizard.enter();
                selectedWizard = wizard;

                enableButtons();
            } catch (Exception ex) {
                MessageBox.error(this.getTopLevelAncestor(), ex.getMessage());

                steps.setSelectedComponent(selectedWizard.getEditorComponent());
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void enableButtons() {
        prevButton.setEnabled(steps.getSelectedIndex() > 0);

        String nextText = (steps.getSelectedIndex() == (steps.getTabCount() - 1)) ? FINISHED : NEXT;

        nextButton.setText(nextText);
    }

    /**
     * DOCUMENT ME!
     */
    private void buildUI() {
        setLayout(new BorderLayout());
        add(steps, BorderLayout.CENTER);

        JPanel commandsPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        prevButton.addActionListener(this);
        commandsPane.add(prevButton);
        nextButton.addActionListener(this);
        commandsPane.add(nextButton);

        commandsPane.add(cancelButton);

        add(commandsPane, BorderLayout.SOUTH);
        steps.addChangeListener(this);
        steps.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
    }

    /**
     * DOCUMENT ME!
     */
    private void _buildUI() {
        

        setLayout(new BorderLayout());
        add(steps, BorderLayout.CENTER);

        JPanel commandsPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        prevButton.addActionListener(this);
        commandsPane.add(prevButton);
        nextButton.addActionListener(this);
        commandsPane.add(nextButton);

        commandsPane.add(cancelButton);

        add(commandsPane, BorderLayout.SOUTH);
        steps.addChangeListener(this);
        steps.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
    }

    /**
     * DOCUMENT ME!
     *
     * @param wizard DOCUMENT ME!
     */
    public void addEditor(String title, AbstractWizard wizard) {
        JComponent comp = (JComponent) wizard.getEditorComponent();
        comp.putClientProperty(WizardTabbedPane.PROPERTY_WIZARD, wizard);

        if (padIcon == null) {
            padIcon = new EmptyIcon(1, 25);
        }

        steps.addTab(title, padIcon, comp);
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        
        int n = steps.getTabCount();
        int i = steps.getSelectedIndex();

        if (evt.getSource() == nextButton) {
            if ((n == (i + 1)) && (finishedAction != null)) {
                finishedAction.actionPerformed(evt);
            } else {
                i++;

                if (i >= n) {
                    return;
                }
            }
        } else if (evt.getSource() == prevButton) {
            i--;

            if (i < 0) {
                return;
            }
        }

        steps.setSelectedIndex(i);
    }

}
