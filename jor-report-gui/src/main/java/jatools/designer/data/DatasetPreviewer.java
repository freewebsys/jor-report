package jatools.designer.data;

import jatools.data.reader.DatasetReader;
import jatools.data.reader.sql.SqlUtil;

import jatools.designer.App;

import jatools.swingx.CommandPanel;
import jatools.swingx.JatoolsFileFilter;
import jatools.swingx.MessageBox;
import jatools.swingx.SwingUtil;

import jatools.util.Util;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.StringReader;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetPreviewer extends JDialog implements ChangeListener {
    private static JatoolsFileFilter filter;
    private static JFileChooser chooser;
    DatasetTable table;
    private JButton refreshCmd;
    private JButton cancelCmd;
    private JLabel pageNumber;
    private DatasetReader reader;

    /**
     * Creates a new DatasetPreviewer object.
     *
     * @param owner DOCUMENT ME!
     */
    public DatasetPreviewer(Frame owner) {
        super(owner, App.messages.getString("res.536"), true);

        buildUI();

        this.setLocationRelativeTo(owner);

        this.addWindowListener(new WindowListener() {
                public void windowActivated(WindowEvent e) {
                }

                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowOpened(WindowEvent e) {
                    if (reader != null) {
                        table.setDataSet(reader);
                    }
                }
            });
    }

    private void buildUI() {
        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JToolBar toolbar = new JToolBar();

        refreshCmd = new JButton(Util.getIcon("/jatools/icons/refresh.gif"));
        cancelCmd = new JButton(Util.getIcon("/jatools/icons/stop.gif"));

        cancelCmd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });

        refreshCmd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    refresh();
                }
            });
        cancelCmd.setPreferredSize(new Dimension(25, 25));
        refreshCmd.setPreferredSize(new Dimension(25, 25));

        pageNumber = new JLabel();
        pageNumber.setHorizontalAlignment(JLabel.LEFT);
        pageNumber.setPreferredSize(new Dimension(200, 25));

        toolbar.add(cancelCmd);
        toolbar.add(refreshCmd);

        JButton tocsv = new JButton(App.messages.getString("res.537"));

        toolbar.add(tocsv);
        tocsv.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportToCsv();
                }
            });

        toolbar.add(Box.createHorizontalStrut(280));
        toolbar.add(pageNumber);

        table = new DatasetTable();
        table.setChangeListener(this);

        commandPanel.add(toolbar);
        this.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        this.getContentPane().add(commandPanel, BorderLayout.NORTH);

        SwingUtil.setBorder6(((JPanel) getContentPane()));

        CommandPanel buttonPanel = CommandPanel.createPanel(false);

        JButton okButton = new JButton(App.messages.getString("res.3"));
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        buttonPanel.addComponent(okButton);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();

        setSize(new Dimension(550, 450));
    }

    protected void exportToCsv() {
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            filter = new JatoolsFileFilter(new String[] { "txt" }, App.messages.getString("res.538"));

            chooser.addChoosableFileFilter(filter);
        }

        chooser.showSaveDialog(this);

        File f = filter.normalize(chooser.getSelectedFile());

        if (f != null) {
            try {
                table.saveAsCsv(f);
                MessageBox.show(this, App.messages.getString("res.82"),
                    App.messages.getString("res.539"));
            } catch (Exception e) {
                MessageBox.error(this, e.getMessage());
            }
        }
    }

    protected void refresh() {
        table.refresh();
        cancelCmd.setEnabled(true);
    }

    protected void cancel() {
        table.cancel();
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void setReader(DatasetReader reader) {
        this.reader = reader;
    }

    /**
     * DOCUMENT ME!
     */
    public void show() {
        super.show();
        table.cancel();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e == DatasetTable.ADD_ROW) {
            pageNumber.setText(App.messages.getString("res.540") + table.getRowCount());
            repaint();
        } else if (e == DatasetTable.START) {
            refreshCmd.setEnabled(false);
            cancelCmd.setEnabled(true);
        } else {
            refreshCmd.setEnabled(true);
            cancelCmd.setEnabled(false);
        }
    }
}
