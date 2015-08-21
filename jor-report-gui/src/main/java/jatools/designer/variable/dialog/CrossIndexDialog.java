package jatools.designer.variable.dialog;

import jatools.data.reader.DatasetReader;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;

import jatools.designer.Main;

import jatools.dom.src.CrossIndexNodeSource;
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class CrossIndexDialog extends JDialog implements ActionListener {
    private JTextField nameText;
    private boolean success;
    private IndexFieldSelectPanel indexFieldSelectPanel;
    private CrossIndexNodeSource crossIndexSrc;
    private boolean allowWithDatasetSrc;

    /**
    * Creates a new IndexEditor object.
    *
    * @param datasetNodeSrc DOCUMENT ME!
    */
    public CrossIndexDialog(DatasetNodeSource datasetNodeSrc, CrossIndexNodeSource crossIndexSrc) {
        super((Frame) Main.getInstance(), "交叉索引定义", true);

        initUI();
        setNodeSource(datasetNodeSrc, crossIndexSrc);
        success = false;
    }

    private void initUI() {
        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;

        JPanel options = new JPanel(new FlowLayout(FlowLayout.LEFT));
        options.add(new JLabel("名称:"));
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(350, 25));
        options.add(nameText);

        p.add(options, gbc);
        gbc.weighty = 1.0;
        indexFieldSelectPanel = new IndexFieldSelectPanel();
        p.add(indexFieldSelectPanel, gbc);
        this.getContentPane().add(p, BorderLayout.CENTER);

        JButton ok = new JButton(Messages.getString("res.3"));
        JButton cancel = new JButton(Messages.getString("res.4"));

        ok.setActionCommand("ok");

        cancel.setActionCommand("cancel");

        ok.addActionListener(this);
        cancel.addActionListener(this);

        CommandPanel command = CommandPanel.createPanel();
        command.addComponents(ok, cancel);

        this.getContentPane().add(command, BorderLayout.SOUTH);
        SwingUtil.setBorder6((JComponent) this.getContentPane());
        this.setSize(new Dimension(430, 350));
        this.setLocationRelativeTo(Main.getInstance());
    }

    private void setNodeSource(DatasetNodeSource datasetNodeSrc, CrossIndexNodeSource crossIndexSrc) {
        String[] fields = getFields(datasetNodeSrc.getReader());

        String[] selectedFields = null;
        String[] selectedFields2 = null;

        if (crossIndexSrc != null) {
            selectedFields = crossIndexSrc.getIndexFields();
            selectedFields2 = crossIndexSrc.getIndexFields2();
            nameText.setText(crossIndexSrc.getTagName());
        }

        indexFieldSelectPanel.init(fields, selectedFields, selectedFields2);
        indexFieldSelectPanel.setCrossIndex(true);

        this.crossIndexSrc = crossIndexSrc;

        // 如果是在datasetSource上，则名称可以为空
        // 如果正在新增，且 datasetSrc还没有定义index，则可以将索引定义装配到datasetSrc上
        allowWithDatasetSrc = (crossIndexSrc == null) && !datasetNodeSrc.hasIndex();
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
    private boolean emptyName() {
        return (this.nameText.getText() == null) || (this.nameText.getText().trim().length() == 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ok")) {
            // 如果是允许加到datasetNodeSrc上,则不允许名称为空
            if (!this.allowWithDatasetSrc && emptyName()) {
                JOptionPane.showConfirmDialog(Main.getInstance(), "名称不能为空", "错误",
                    JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                nameText.requestFocus();

                return;
            }

            try {
                indexFieldSelectPanel.requiredCheck();
            } catch (Exception e1) {
                JOptionPane.showConfirmDialog(Main.getInstance(), e1.getMessage(), "错误",
                    JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                return;
            }

            if (crossIndexSrc != null) {
                crossIndexSrc.setTagName(nameText.getText());
                crossIndexSrc.setIndexFields(indexFieldSelectPanel.getIndexFields());
                crossIndexSrc.setIndexFields2(indexFieldSelectPanel.getIndexFields2());
            }

            success = true;

            this.setVisible(false);
        } else if (e.getActionCommand().equals("cancel")) {
            this.setVisible(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CrossIndexNodeSource createNodeSource() {
        if (this.isSuccess()) {
            return new CrossIndexNodeSource(emptyName() ? null : nameText.getText(),
                this.indexFieldSelectPanel.getIndexFields(),
                this.indexFieldSelectPanel.getIndexFields2());
        } else {
            return null;
        }
    }
}
