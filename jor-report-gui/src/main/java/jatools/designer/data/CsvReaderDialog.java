package jatools.designer.data;

import jatools.data.reader.csv.CsvReader;
import jatools.swingx.Chooser;
import jatools.swingx.CommandPanel;
import jatools.swingx.GridBagConstraints2;
import jatools.swingx.MessageBox;
import jatools.swingx.MoreButton;
import jatools.swingx.SwingUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class CsvReaderDialog extends JDialog implements Chooser {
    private static JFileChooser fileChooser;
    JTextField nameText = new JTextField();
    JTextField filePathText = new JTextField();
    JTextField whereText = new JTextField();
    JTextField orderByText = new JTextField();
    boolean exitOK;
    NameChecker nameChecker;
    String oldName;
    private JRadioButton localfile;
    private JRadioButton remotefile;
    JButton chooser = new MoreButton();

    /**
     * Creates a new CsvReaderDialog object.
     *
     * @param title DOCUMENT ME!
     * @param reader DOCUMENT ME!
     * @param owner DOCUMENT ME!
     */
    public CsvReaderDialog(String title, CsvReader reader, Frame owner) {
        super(owner, title, true);
        buildUI();

        setReader(reader);
        pack();
        setSize(new Dimension(450, 250));
    }

    private void dataPreview() {
        try {
            DatasetPreviewer preview = new DatasetPreviewer((Frame) getOwner());
            preview.setLocationRelativeTo(this);
            preview.setReader(this.getReader());
            preview.show();
        } catch (Exception ex) {
            ex.printStackTrace();

            MessageBox.error(this, ex.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nameChecker DOCUMENT ME!
     */
    public void setNameChecker(NameChecker nameChecker) {
        this.nameChecker = nameChecker;
    }

    private void buildUI() {
        JPanel propertyPane = new JPanel(new GridBagLayout());

        GridBagConstraints2 gbc = new GridBagConstraints2(propertyPane);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0f;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        propertyPane.add(new JLabel("名称:"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.add(this.nameText, 150);

        gbc.weightx = 0.0f;
        gbc.gridwidth = 1;

        propertyPane.add(new JLabel("位置类型:"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100.0f;

        JPanel optionpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        localfile = new JRadioButton("本地");
        remotefile = new JRadioButton("网络");

        ButtonGroup group = new ButtonGroup();
        group.add(localfile);
        group.add(remotefile);

        optionpanel.add(localfile);
        optionpanel.add(remotefile);

        propertyPane.add(optionpanel, gbc);

        gbc.weightx = 0.0f;
        gbc.gridwidth = 1;

        propertyPane.add(new JLabel("Csv 文件位置:"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100.0f;

        JPanel chooserpanel = new JPanel(new BorderLayout());
        chooserpanel.add(filePathText, BorderLayout.CENTER);
        chooserpanel.add(chooser, BorderLayout.EAST);

        propertyPane.add(chooserpanel, gbc);
        gbc.weightx = 0.0f;
        gbc.gridwidth = 1;
        propertyPane.add(new JLabel("过滤条件(Where):"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        propertyPane.add(whereText, gbc);

        gbc.gridwidth = 1;
        propertyPane.add(new JLabel("排序(Order By):"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        propertyPane.add(orderByText, gbc);

        chooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectFile();
                }
            });

        CommandPanel commandPane = CommandPanel.createPanel();

        JButton b = new JButton("预览");
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dataPreview();
                }
            });

        commandPane.addComponent(b);

        b = new JButton("确认");
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        commandPane.addComponent(b);

        b = new JButton("取消");
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exitOK = false;
                    hide();
                }
            });

        commandPane.addComponent(b);

        getContentPane().add(propertyPane, BorderLayout.CENTER);
        getContentPane().add(commandPane, BorderLayout.SOUTH);

        SwingUtil.setBorder6(((JPanel) getContentPane()));

        ActionListener lst = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    enableChooser();
                }
            };

        localfile.addActionListener(lst);
        remotefile.addActionListener(lst);
        localfile.setSelected(true);
    }

   
    
    protected void done() {
        if (!isOriginalName() && this.nameChecker != null) {
            try {
                this.nameChecker.check(this.nameText.getText());
            } catch (Exception e) {
                MessageBox.error(CsvReaderDialog.this, e.getMessage());
                nameText.requestFocus();
                nameText.selectAll();

                return;
            }
        }

        exitOK = true;
        hide();
    }

    boolean isOriginalName() {
  
        return (nameText.getName() != null) && nameText.getName().equals(nameText.getText());
    }

    

    protected void selectFile() {
        JFileChooser fc = getFileChooser();

        if (JFileChooser.APPROVE_OPTION == fc.showDialog(this, "导入")) {
            File file = fc.getSelectedFile();

            this.filePathText.setText(file.getAbsolutePath());
        }
    }

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
        }

        return fileChooser;
    }

    protected void enableChooser() {
        chooser.setEnabled(localfile.isSelected());
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
     * @return DOCUMENT ME!
     */
    public CsvReader getReader() {
        return new CsvReader(nameText.getText(), null, filePathText.getText(),
            localfile.isSelected(), whereText.getText(), orderByText.getText());
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        exitOK = false;

        this.setReader((CsvReader) value);
        this.setLocationRelativeTo(owner);

        show();

        return exitOK;
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void setReader(CsvReader reader) {
        if (reader != null) {
            nameText.setText(reader.getName());
            nameText.setName( reader.getName() );

            oldName = nameText.getText();

            filePathText.setText(reader.getFilePath());

            if (reader.isLocalFile()) {
                localfile.setSelected(true);
            } else {
                remotefile.setSelected(true);
            }

            whereText.setText(reader.getWhere());
            orderByText.setText(reader.getOrderBy());
        } else {
            nameText.setText(null);

            oldName = nameText.getText();

            filePathText.setText(null);
            whereText.setText(null);
            orderByText.setText(null);
            this.localfile .setSelected(true);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return getReader();
    }
}
