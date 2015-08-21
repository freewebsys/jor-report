package jatools.designer.variable.dialog;

import jatools.data.reader.DatasetReader;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;

import jatools.designer.App;
import jatools.designer.Main;

import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.IndexNodeSource;

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
 * @version $Revision$
  */
public class IndexDialog extends JDialog implements ActionListener {
    private JTextField nameText;
    private boolean success;
    private IndexFieldSelectPanel indexFieldSelectPanel;
    private IndexNodeSource indexSrc;

    /**
    * Creates a new IndexEditor object.
    *
    * @param datasetNodeSrc DOCUMENT ME!
    */
    public IndexDialog(DatasetNodeSource datasetNodeSrc, IndexNodeSource indexSrc) {
        super((Frame) Main.getInstance());

        this.setModal(true);
        this.setTitle(App.messages.getString("res.278"));

        initUI();
        setNodeSource(datasetNodeSrc, indexSrc);
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

    private void setNodeSource(DatasetNodeSource datasetNodeSrc, IndexNodeSource indexSrc) {
        String[] fields = getFields(datasetNodeSrc.getReader());

        String[] selectedFields = null;

        if (indexSrc != null) {
            selectedFields = indexSrc.getIndexFields();
            nameText.setText(indexSrc.getTagName());
        }

        indexFieldSelectPanel.init(fields, selectedFields, null);
        indexFieldSelectPanel.setCrossIndex(false);

        this.indexSrc = indexSrc;
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
            if ((this.nameText.getText() == null) ||
                    (this.nameText.getText().trim().length() == 0)) {
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

            if (indexSrc != null) {
                indexSrc.setTagName(nameText.getText());
                indexSrc.setIndexFields(indexFieldSelectPanel.getIndexFields());
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
    public IndexNodeSource createNodeSource() {
        if (this.isSuccess()) {
            return new IndexNodeSource(nameText.getText(),
                this.indexFieldSelectPanel.getIndexFields());
        } else {
            return null;
        }
    }
}
