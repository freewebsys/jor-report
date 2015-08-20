package jatools.designer.variable.dialog;

import jatools.data.reader.DatasetReader;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;

import jatools.designer.App;
import jatools.designer.Main;

import jatools.dom.src.DatasetNodeSource;

import jatools.engine.script.ReportContext;

import jatools.resources.Messages;

import jatools.swingx.CommandPanel;
import jatools.swingx.SwingUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class IndexEditor extends JDialog implements ActionListener {
    private JRadioButton indexRadio;
    private JRadioButton crossIndexRadio;
    private DatasetNodeSource datasetNodeSrc;
    private boolean success;
    private IndexFieldSelectPanel indexFieldSelectPanel;

    /**
    * Creates a new IndexEditor object.
    *
    * @param parentSrc DOCUMENT ME!
    */
    public IndexEditor(DatasetNodeSource parentSrc) {
        super((Frame) Main.getInstance());

        this.setModal(true);
        this.setTitle(App.messages.getString("res.278"));

        initUI();
        setSource(parentSrc);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSuccess() {
        return success;
    }

    private void initUI() {
        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;

        JPanel options = new JPanel(new FlowLayout(FlowLayout.LEFT));

        this.indexRadio = new JRadioButton("索引");
        this.crossIndexRadio = new JRadioButton("交叉索引");

        this.indexRadio.addActionListener(this);
        this.crossIndexRadio.addActionListener(this);

        this.indexRadio.setActionCommand("changetype");
        this.crossIndexRadio.setActionCommand("changetype");

        ButtonGroup group = new ButtonGroup();
        group.add(indexRadio);
        group.add(crossIndexRadio);
        this.indexRadio.setSelected(true);

        options.add(new JLabel("索引类型:"));
        options.add(this.indexRadio);
        options.add(this.crossIndexRadio);

        p.add(options, gbc);
        gbc.weighty = 1.0;
        indexFieldSelectPanel = new IndexFieldSelectPanel();
        p.add(indexFieldSelectPanel, gbc);
        this.getContentPane().add(p, BorderLayout.CENTER);

        JButton clear = new JButton("清除");
        clear.setActionCommand("clear");

        JButton ok = new JButton(Messages.getString("res.3"));
        JButton cancel = new JButton(Messages.getString("res.4"));

        ok.setActionCommand("ok");

        cancel.setActionCommand("cancel");
        clear.addActionListener(this);
        ok.addActionListener(this);
        cancel.addActionListener(this);

        CommandPanel command = CommandPanel.createPanel();
        command.addComponents(clear, ok, cancel);

        this.getContentPane().add(command, BorderLayout.SOUTH);
        SwingUtil.setBorder6((JComponent) this.getContentPane());
        this.setSize(new Dimension(430, 350));
        this.setLocationRelativeTo(Main.getInstance());
    }

    private void setSource(DatasetNodeSource datasetNodeSrc) {
        String[] fields = getFields(datasetNodeSrc.getReader());

        String[] selectedFields = datasetNodeSrc.getIndexFields();
        String[] selectedFields2 = datasetNodeSrc.getIndexFields2();

        indexFieldSelectPanel.init(fields, selectedFields, selectedFields2);

        if (this.indexFieldSelectPanel.isCrossIndex()) {
            this.crossIndexRadio.setSelected(true);
        } else {
            this.indexRadio.setSelected(true);
        }

        this.datasetNodeSrc = datasetNodeSrc;
    }

    /**
    * 取得所有字段
    * @param reader DatasetReader
    * @return String[]
    */
    private String[] getFields(DatasetReader reader) {
        if (reader != null) {
            try {
                Dataset dataSet = reader.read(ReportContext.getDefaultContext(), 0);

                return dataSet.getFieldNames(reader);
            } catch (DatasetException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentSrc DOCUMENT ME!
     * @param _indexSource DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ok")) {
            try {
                indexFieldSelectPanel.requiredCheck();
            } catch (Exception e1) {
                JOptionPane.showConfirmDialog(Main.getInstance(), e1.getMessage(), "错误",
                    JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                return;
            }

            datasetNodeSrc.setIndexFields(indexFieldSelectPanel.getIndexFields());
            datasetNodeSrc.setIndexFields2(indexFieldSelectPanel.getIndexFields2());
            success = true;
            this.dispose();
        } else if (e.getActionCommand().equals("cancel")) {
            this.dispose();
        } else if (e.getActionCommand().equals("clear")) {
            datasetNodeSrc.setIndexFields(null);
            datasetNodeSrc.setIndexFields2(null);
            success = true;
            this.dispose();
        } else if (e.getActionCommand().equals("changetype")) {
            indexFieldSelectPanel.setCrossIndex(this.crossIndexRadio.isSelected());
        }
    }
}
