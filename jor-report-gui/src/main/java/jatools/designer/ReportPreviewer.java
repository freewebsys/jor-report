package jatools.designer;

import jatools.ReportDocument;
import jatools.core.view.CompoundView;
import jatools.core.view.PageView;
import jatools.designer.action.ExportAction;
import jatools.engine.PrinterListener;
import jatools.engine.printer.ReportPrinter;
import jatools.swingx.MessageBox;
import jatools.swingx.SwingUtil;
import jatools.util.Util;
import jatools.viewer.rvr;
import jatools.xml.XmlReader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportPreviewer extends rvr implements ChangeListener, PropertyChangeListener,
    PageProvider {
	
    private ReportDocument document;
    private boolean dirty = true;
    private boolean breakPreview;
    private ExportAction exportAction;
    private JButton refreshCmd;
    private JButton cancelCmd;
    private JLabel pageNumber;
    private boolean cloneViewing = true;
    private ReportDocument rawDocument = null;
    ActionListener errorAction;
    private Action closeAction;
    private JMenuBar menus;

    /**
     * Creates a new ReportPreviewer object.
     */
    public ReportPreviewer() {
        this(false);
        cloneViewing = false;
    }

    /**
     * Creates a new ReportPreviewer object.
     *
     * @param showToolbar DOCUMENT ME!
     */
    public ReportPreviewer(boolean showToolbar) {
        super(showToolbar);
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        breakPreview = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param show DOCUMENT ME!
     */
    public void showToolbar(boolean show) {
        if (show && (toolpane.getParent() != this)) {
            this.add(toolpane, BorderLayout.NORTH);
        }

        if (!show && (toolpane.getParent() == this)) {
            this.remove(toolpane);
        }
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
     * @param doc DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setDocument(ReportDocument doc) throws Exception {
        if (doc != null) {
            doc = cloneDocument(doc);
            doc.validate();
        }

        this.document = doc;
        dirty = true;
        refreshPages();
    }

    private ReportDocument cloneDocument(ReportDocument doc) {
        if (!cloneViewing) {
            return doc;
        } else {
            rawDocument = doc;

            return Util.cloneDocument(doc);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param is DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setDocument(InputStream is) throws Exception {
        ReportDocument doc = (ReportDocument) XmlReader.read(is);
        setDocument(doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void refreshPages() throws Exception {
        if ((!dirty) || (document == null)) {
            return;
        }

        cancelCmd.setEnabled(false);
        refreshCmd.setEnabled(false);
        cancelCmd.setToolTipText(App.messages.getString("res.4"));
        refreshCmd.setToolTipText(App.messages.getString("res.162"));

        clearPage();

        breakPreview = false;

        Runnable runnable = new Runnable() {
                public void run() {
                    ReportPrinter printer = null;

                    try {
                        printer = new ReportPrinter(document, getPreviewParameters());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        MessageBox.error(ReportPreviewer.this, e1.getMessage());

                        return;
                    }

                    printer.addChangeListener(new PrinterListener() {
                            public void pageAdded(ReportPrinter printer, CompoundView page) {
                                addPage((PageView) page);

                                SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            view.revalidate();

                                            if (pageNumber != null) {
                                                pageNumber.setText(App.messages.getString("res.163") +
                                                    view.getComponentCount());
                                            }
                                        }
                                    });

                                if (breakPreview) {
                                    printer.cancelPrint();
                                    cancelCmd.setEnabled(false);
                                }
                            }
                        });

                    try {
                        cancelCmd.setEnabled(true);
                        refreshCmd.setEnabled(false);
                        printer.print();
                        cancelCmd.setEnabled(false);
                        refreshCmd.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dirty = false;
                }
            };

        new Thread(runnable).start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void setErrorAction(ActionListener listener) {
        this.errorAction = listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public jatools.core.view.PageView[] getPages() {
        return view.getPages();
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent evt) {
        stateChanged(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        dirty = true;
    }

    protected JPanel getToolbar() {
        JPanel toolbar = super.getToolbar();
        exportAction = new ExportAction(this);

        JButton b = new JButton(exportAction);
        b.setText(null);
        b.setPreferredSize(new Dimension(25, 25));
        toolbar.add(b);
        refreshCmd = new JButton(Util.getIcon("/jatools/icons/refresh.gif"));
        cancelCmd = new JButton(Util.getIcon("/jatools/icons/stop.gif"));

        cancelCmd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    breakPreview = true;
                }
            });

        refreshCmd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        setDocument(document);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        cancelCmd.setPreferredSize(new Dimension(25, 25));
        refreshCmd.setPreferredSize(new Dimension(25, 25));

        toolbar.add(cancelCmd);
        toolbar.add(refreshCmd);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(b);

        return toolbar;
    }

    protected JPanel getToolbar2(JPanel toolbar) {
        pageNumber = new JLabel();
        pageNumber.setHorizontalAlignment(JLabel.LEFT);
        pageNumber.setPreferredSize(new Dimension(293, 25));

        toolbar.add(pageNumber);

        this.closeAction = new AbstractAction(App.messages.getString("res.164")) {
                    public void actionPerformed(ActionEvent e) {
                        Main.getInstance().hidePreviewer();
                    }
                };

        JButton b = new JButton(this.closeAction);

        SwingUtil.setSize(b, new Dimension(60, 25));

        toolbar.add(b);

        return toolbar;
    }

    JMenuBar getMenus() {
        if (this.menus == null) {
            menus = new JMenuBar();

            JMenu fileMenu = new JMenu(App.messages.getString("res.165"), true);
            Action[] previewActions = getActions();

            for (int i = 0; i < previewActions.length; i++) {
                if (previewActions[i] == null) {
                    fileMenu.addSeparator();
                } else {
                    fileMenu.add(new JMenuItem(previewActions[i]));
                }
            }

            menus.add(fileMenu);
        }

        return menus;
    }

    protected void exportJgo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "ok");

        File dir = chooser.getSelectedFile();

        if (dir == null) {
            return;
        }

        jatools.core.view.PageView[] pps = getPages();

        for (int i = 0; i < pps.length; i++) {
            File file = new File(dir.getAbsolutePath(), "j" + i + ".jgo");

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(pps[i]);
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getRawDocument() {
        return (rawDocument == null) ? getDocument() : rawDocument;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Map getPreviewParameters() {
        HashMap previewParameters = new HashMap();
        previewParameters.put("as", "awt");

        return previewParameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action[] getActions() {
        return new Action[] { printAction, exportAction, null, closeAction };
    }
}
