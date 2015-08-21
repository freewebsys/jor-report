package jatools.designer.export;

import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.PageProvider;
import jatools.designer.ReportPreviewer;
import jatools.swingx.JatoolsFileFilter;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ExportPanel extends JPanel implements ActionListener {
    public static final int DHTML2 = 0;
    public static final int MHT = 1;
    public static final int PDF = 2;
    public static final int XLS = 3;
    public static final int XLS1 = 4;
    public static final int RTF = 5;
    public static final int CVS = 6;
    public static final int PS = 7;
    public static final int PNG = 8;
    private static ExportPanel instance;
    JatoolsFileFilter htmlFilter = new JatoolsFileFilter(new String[] { "html", "htm" },
            "html文件 (*.htm,*.html)");
    JatoolsFileFilter mhtFilter = new JatoolsFileFilter(new String[] { "mht" }, "html 单一文档(*.mht)");
    JatoolsFileFilter pdfFilter = new JatoolsFileFilter(new String[] { "pdf" }, "pdf文件 (*.pdf)");
    JatoolsFileFilter xlsFilter = new JatoolsFileFilter(new String[] { "xls" }, "Excel文件 (*.xls)");
    JatoolsFileFilter xls1Filter = new JatoolsFileFilter(new String[] { "xls" }, "Excel文件-分页(*.xls)");
    JatoolsFileFilter rtfFilter = new JatoolsFileFilter(new String[] { "rtf" }, "Word rtf 文件(*.rtf)");
    JatoolsFileFilter csvFilter = new JatoolsFileFilter(new String[] { "txt" }, "csv文件 (*.txt)");
    JatoolsFileFilter psFilter = new JatoolsFileFilter(new String[] { "ps" }, "PostScript 文件(*.ps)");
    JatoolsFileFilter pngFilter = new JatoolsFileFilter(new String[] { "png" }, "图片文件(*.png)");
    private JFileChooser chooser;
    private JDialog dialog;
    private PageProvider provider;
    int type;
    File f;
    ProgressDialog pd;
    boolean exportDone = false;

    private ExportPanel() {
        super(new BorderLayout());
        chooser = new JFileChooser();

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(htmlFilter);

        chooser.addChoosableFileFilter(pdfFilter);
        chooser.addChoosableFileFilter(xlsFilter);
        chooser.addChoosableFileFilter(xls1Filter);
        chooser.addChoosableFileFilter(rtfFilter);

        if (UIManager.getLookAndFeel().getID().equals("Windows")) {
            if (chooser.getComponent(1).getClass().getName()
                           .indexOf("WindowsFileChooserUI$ShortCutPanel") > -1) {
                chooser.remove(1);
                ((JComponent) ((JComponent) chooser.getComponent(0)).getComponent(3)).setPreferredSize(new Dimension(
                        100, 22));
            }
        }

        chooser.setBorder((BorderFactory.createEmptyBorder(25, 20, 25, 20)));
        chooser.addActionListener(this);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        add(chooser, BorderLayout.CENTER);
    }

    private FileFilter findFilter(String ext) {
        FileFilter[] ffa = chooser.getChoosableFileFilters();

        for (int i = 0; i < ffa.length; i++) {
            if (ffa[i] instanceof JatoolsFileFilter && ((JatoolsFileFilter) ffa[i]).accept(ext)) {
                return ffa[i];
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param provider DOCUMENT ME!
     */
    public void showDialog(Frame owner, PageProvider provider) {
        this.provider = provider;
        dialog = new JDialog(owner, App.messages.getString("res.434"), true);
        dialog.getContentPane().add(this, BorderLayout.CENTER);
        dialog.setSize(480, 380);
        dialog.setLocationRelativeTo(owner);

        if (!exportDone) {
            chooser.setFileFilter(htmlFilter);
        }

        chooser.rescanCurrentDirectory();

        dialog.show();
        dialog.getContentPane().remove(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
            dialog.hide();
            export();
        } else if (e.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
            dialog.hide();
        }
    }

    private void export() {
        exportDone = true;
        f = chooser.getSelectedFile();

        if (f != null) {
            Object filter = chooser.getFileFilter();

            type = 0;

            if (filter == htmlFilter) {
                type = DHTML2;
            } else if (filter == mhtFilter) {
                type = MHT;
            } else if (filter == pdfFilter) {
                type = PDF;
            } else if (filter == xlsFilter) {
                type = XLS;
            } else if (filter == xls1Filter) {
                type = XLS1;
            } else if (filter == rtfFilter) {
                type = RTF;
            } else if (filter == csvFilter) {
                type = CVS;
            } else if (filter == psFilter) {
                type = PS;
            } else if (filter == pngFilter) {
                type = PNG;
            }

            f = ((JatoolsFileFilter) chooser.getFileFilter()).normalize(f);

            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(dialog,
                            new JLabel(App.messages.getString("res.435") + f.getName() + App.messages.getString("res.436")), App.messages.getString("res.437"),
                            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            pd = new ProgressDialog((Frame) dialog.getOwner(), App.messages.getString("res.438"), f);

            Thread exportTask = new Thread(new Runnable() {
                        public void run() {
                            ReportDocument doc = Util.cloneDocument(((ReportPreviewer) provider).getRawDocument());

                            Map parameters = new HashMap();

                            LocalReportJob job = new LocalReportJob(doc, parameters, f);

                            try {
                                if (type == DHTML2) {
                                    job.printAsDHTML();
                                } else if (type == PDF) {
                                    job.printAsPDF();
                                } else if (type == XLS) {
                                    job.printAsXLS();
                                } else if (type == XLS1) {
                                    job.printAsXLS1();
                                } else if (type == RTF) {
                                    job.printAsRTF();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            pd.done();
                        }
                    });

            exportTask.start();

            pd.show();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExportPanel getInstance() {
        if (instance == null) {
            instance = new ExportPanel();
        }

        return instance;
    }
}
