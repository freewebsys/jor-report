package jatools.designer.chooser;



import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.config.UserX;
import jatools.swingx.JatoolsFileFilter;
import jatools.swingx.MessageBox;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportChooser implements ActionListener {
    public static final int SHOW_NEW = 1;
    public static final int SHOW_OPEN = 2;
    public static final int SHOW_ALL = SHOW_NEW | SHOW_OPEN;
    private static ReportChooser shared;
    JatoolsFileFilter filter = new JatoolsFileFilter(new String[] { "xml" }, App.messages.getString("res.564"));
    JFileChooser fileChooser;
    NewPanel newPanel;
    boolean startup;
    JDialog dialog;
    boolean done;
    File selectedFile;
    ReportDocument document;
    private boolean safeSave;

    protected ReportChooser() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ReportChooser getInstance() {
        if (shared == null) {
            shared = new ReportChooser();
        }

        return shared;
    }

    /**
     * DOCUMENT ME!
     *
     * @param caption DOCUMENT ME!
     * @param showOption DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showDialog(String caption, int showOption) {
        return showDialog(caption, showOption, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param caption DOCUMENT ME!
     * @param showOption DOCUMENT ME!
     * @param withStillShow DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showDialog(String caption, int showOption, boolean withStillShow) {
        done = false;

        dialog = createDialog(caption, showOption, withStillShow);
        dialog.show();

        return done;
    }

    private JDialog createDialog(String caption, int showOption, boolean withStillShow) {
        JDialog d = new JDialog(Main.getInstance(), caption, true);

        JPanel contentPane = (JPanel) d.getContentPane();

        if ((showOption == SHOW_NEW) || (showOption == SHOW_OPEN)) {
            if (showOption == SHOW_NEW) {
                contentPane.add(this.getNewPanel(), BorderLayout.CENTER);
            } else if (showOption == SHOW_OPEN) {
                contentPane.add(this.getOpenPanel(), BorderLayout.CENTER);
            }
        } else {
            JTabbedPane tabbed = new JTabbedPane();

            if ((showOption & SHOW_NEW) != 0) {
                tabbed.add(this.getNewPanel(), App.messages.getString("res.563"));
            }

            if ((showOption & SHOW_OPEN) != 0) {
                tabbed.add(this.getOpenPanel(), App.messages.getString("res.92"));
            }

            contentPane.add(tabbed);
        }

        if (selectedFile != null) {
            fileChooser.setSelectedFile(selectedFile);
        }

        if (withStillShow) {
            JCheckBox box = new JCheckBox(App.messages.getString("res.565"));
            box.setSelected(true);

            box.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        UserX.getInstance().setShowOpenDialogOnStartup(false);
                        UserX.write(UserX.getInstance());
                    }
                });

            contentPane.add(box, BorderLayout.SOUTH);
        }

        d.setSize(530, 390);
        d.setLocationRelativeTo(Main.getInstance());

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param caption DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showDialog(String caption) {
        return showDialog(caption, SHOW_ALL);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showSaveDialog() {
        JFileChooser fc = getOpenPanel();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);

        return this.showDialog(App.messages.getString("res.566"), SHOW_OPEN);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showSaveAsDialog() {
        JFileChooser fc = getOpenPanel();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        safeSave = true;

        boolean ret = this.showDialog(App.messages.getString("res.566"), SHOW_OPEN);
        safeSave = false;

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JPanel getNewPanel() {
        if (newPanel == null) {
            newPanel = new NewPanel();

            newPanel.addActionListener(this);
        }

        return newPanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof ReportProvider) {
            if (e.getActionCommand() == ReportProvider.DONE) {
                document = ((ReportProvider) e.getSource()).getDocument();
                done = true;
                dialog.hide();
            } else if (e.getActionCommand() == ReportProvider.CANCEL) {
                dialog.hide();
            }
        } else if (e.getSource() == fileChooser) {
            if (e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
                if (fileChooser.getDialogType() == JFileChooser.OPEN_DIALOG) {
                    openDocument(fileChooser.getSelectedFile());
                } else {
                    saveDocument(fileChooser.getSelectedFile());
                }

                done = true;
                dialog.hide();
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            } else if (e.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
                dialog.hide();
            }
        }
    }

    private void saveDocument(File file) {
        if (document != null) {
            try {
                file = filter.normalize(file);

                if (safeSave && file.exists() && !file.equals(selectedFile)) {
                    if (JOptionPane.showConfirmDialog(dialog,
                                new JLabel(App.messages.getString("res.165") + file.getName() + App.messages.getString("res.567")), App.messages.getString("res.82"),
                                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }

                ReportDocument.save(document, file, false);
                Main.getInstance().getActiveEditor().setDirty(false);

                cacheDocfile(document, file);
            } catch (Exception e) {
                MessageBox.error(dialog, App.messages.getString("res.568") + Util.toString(e));
                Util.debug(Logger.getLogger("ZReportChooser.saveDocument"), e.getMessage());
            }
        }
    }

    protected void cacheDocfile(ReportDocument doc, File file) {
        App.getMruManager().open(file.getAbsolutePath());
    }

    private void openDocument(File file) {
        if (file != null) {
            try {
                document = ReportDocument.load(file);

                cacheDocfile(document, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JFileChooser getOpenPanel() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File("../example"));
            fileChooser.setFileFilter(filter);

            if (UIManager.getLookAndFeel().getID().equals("Windows")) {
                if (fileChooser.getComponent(1).getClass().getName()
                                   .indexOf("WindowsFileChooserUI$ShortCutPanel") > -1) {
                    fileChooser.remove(1);
                    ((JComponent) ((JComponent) fileChooser.getComponent(0)).getComponent(3)).setPreferredSize(new Dimension(
                            100, 22));
                }
            }

            fileChooser.addActionListener(this);
        }

        fileChooser.rescanCurrentDirectory();

        return fileChooser;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        return document;
    }

    /**
     * DOCUMENT ME!
     *
     * @param document DOCUMENT ME!
     */
    public void setDocument(ReportDocument document) {
        this.document = document;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedFile DOCUMENT ME!
     */
    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     * @param show_all2 DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JDialog getStartupChooser(String string, int show_all2, boolean b) {
        startup = true;
        done = false;
        dialog = createDialog(string, show_all2, b);

        return dialog;
    }
}
