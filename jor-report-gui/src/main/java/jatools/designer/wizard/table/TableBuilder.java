package jatools.designer.wizard.table;

import jatools.data.reader.DatasetReader;

import jatools.designer.App;
import jatools.designer.DataTreeUtil;
import jatools.designer.DisplayFieldSelector;
import jatools.designer.GroupBySelector;
import jatools.designer.SummarySelector;

import jatools.designer.data.ReaderSelector;

import jatools.designer.wizard.BuilderContext;

import jatools.swingx.CommandPanel;
import jatools.swingx.SwingUtil;

import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TableBuilder extends JDialog implements ChangeListener {
    static final String[] PROMPT_STRINGS = {
            App.messages.getString("res.216"), App.messages.getString("res.217"),
            App.messages.getString("res.218"), App.messages.getString("res.219"),
        };
    static final String PREVIEW = App.messages.getString("res.38");
    static final String NEXT = App.messages.getString("res.37");
    static final String FINISHED = App.messages.getString("res.220");
    static final String CANCEL = App.messages.getString("res.4");
    private BuilderContext context;
    private boolean exitOK;
    JButton nextCommand = new JButton(NEXT);
    JButton prevCommand = new JButton(PREVIEW);
    JButton cancelCommand = new JButton(CANCEL);
    JButton finishCommand = new JButton(FINISHED);
    private JTabbedPane steps;
    ReaderSelector readerSelector;
    DisplayFieldSelector displayFieldSelector;
    GroupBySelector groupBySelector;
    SummarySelector summarySelector;
    private DatasetReader selectedReader;
    private JLabel infoLabel;

    /**
     * Creates a new TableBuilder object.
     *
     * @param owner DOCUMENT ME!
     * @param context DOCUMENT ME!
     */
    public TableBuilder(Frame owner, BuilderContext context) {
        super(owner, App.messages.getString("res.221"), true);
        this.context = context;
        exitOK = false;
        initUI();
        setAction();
        setSize(600, 500);
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());

        infoLabel = new JLabel();
        infoLabel.setHorizontalAlignment(JLabel.LEFT);
        infoLabel.setPreferredSize(new Dimension(10, 50));
        infoLabel.setIcon(Util.getIcon("/jatools/icons/help.gif"));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        getContentPane().add(infoLabel, BorderLayout.NORTH);

        steps = new JTabbedPane();
        readerSelector = new ReaderSelector(DataTreeUtil.asTree(App.getConfiguration()));
        readerSelector.addChangeListener(this);
        displayFieldSelector = new DisplayFieldSelector();
        displayFieldSelector.addChangeListener(this);
        groupBySelector = new GroupBySelector();
        summarySelector = new SummarySelector();

        steps.add(App.messages.getString("res.107"), readerSelector);
        steps.add(App.messages.getString("res.222"), displayFieldSelector);
        steps.add(App.messages.getString("res.223"), groupBySelector);
        steps.add(App.messages.getString("res.224"), summarySelector);

        getContentPane().add(steps, BorderLayout.CENTER);
        SwingUtil.setBorder6((JComponent) getContentPane());
        steps.setModel(new DefaultSingleSelectionModel() {
                public void setSelectedIndex(int index) {
                    if ((selectedReader != null) || (index < 1)) {
                        super.setSelectedIndex(index);
                        activatePanel(index);
                    } else {
                        JOptionPane.showMessageDialog(TableBuilder.this,
                            new JLabel(App.messages.getString("res.225")));
                    }
                }
            });
        steps.setSelectedIndex(0);

        CommandPanel control = CommandPanel.createPanel(false);
        control.addComponent(prevCommand);
        control.addComponent(nextCommand);
        control.addComponent(finishCommand);
        control.addComponent(cancelCommand);

        prevCommand.setEnabled(false);
        finishCommand.setEnabled(false);

        getContentPane().add(control, BorderLayout.SOUTH);
    }

    void apply() {
        readerSelector.apply(context);
        displayFieldSelector.apply(context);
        groupBySelector.apply(context);
        summarySelector.apply2(context);
    }

    void setAction() {
        prevCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    previous();
                }
            });

        nextCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    next();
                }
            });

        cancelCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });

        finishCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    finish();
                }
            });
    }

    void activatePanel(int index) {
        switch (index) {
        case 0:
            break;

        case 1:
            displayFieldSelector.setReader(selectedReader);

            break;

        case 2:
            groupBySelector.setReader(selectedReader);

            break;

        case 3:
            summarySelector.setReader(selectedReader);

            ArrayList groupBys = groupBySelector.getSelectedRows();
            Object[] groups = new Object[groupBys.size()];

            for (int i = 0; i < groups.length; i++) {
                groups[i] = ((Vector) groupBys.get(i)).get(0);
            }

            summarySelector.setGroupBys(groups);

            break;
        }

        infoLabel.setText(PROMPT_STRINGS[index]);
        prevCommand.setEnabled(index > 0);
        nextCommand.setEnabled(index < (steps.getTabCount() - 1));

    }

    protected void finish() {
        apply();
        exitOK = true;
        dispose();
    }

    private void cancel() {
        dispose();
    }

    private void next() {
        steps.setSelectedIndex(steps.getSelectedIndex() + 1);
    }

    private void previous() {
        steps.setSelectedIndex(steps.getSelectedIndex() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExitOK() {
        return exitOK;
    }

    /**
     * DOCUMENT ME!
     *
     * @param exitOK DOCUMENT ME!
     */
    public void setExitOK(boolean exitOK) {
        this.exitOK = exitOK;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
       // if (e.getSource() == readerSelector) {
            selectedReader = readerSelector.getSelectedReader();

            finishCommand.setEnabled((selectedReader != null) &&
                this.displayFieldSelector.isSelected());
        //}
    }
}
