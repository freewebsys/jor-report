package jatools.designer.chooser;



import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.ReportBuilder;
import jatools.designer.wizard.ReportStyler;
import jatools.designer.wizard.StylerManager;
import jatools.swingx.ButtonCellRenderer;
import jatools.swingx.CommandPanel;
import jatools.swingx.ListView;
import jatools.swingx.SwingUtil;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NewPanel extends ReportProvider {
    public final static String STYLER = "styler";
    static ReportDocument document;
    static JDialog d;
    ReportDocument doc;
    ListView listView;
    private JLabel tooltip;

    /**
     * Creates a new NewPanel object.
     */
    public NewPanel() {
        setLayout(new BorderLayout());

        ReportStyler[] stylers = StylerManager.getStylers();

        JLabel[] styleLabels = new JLabel[stylers.length];

        for (int i = 0; i < stylers.length; i++) {
            styleLabels[i] = new JLabel(stylers[i].getName(), stylers[i].getIcon(), JLabel.LEFT);
            styleLabels[i].putClientProperty(STYLER, stylers[i]);
        }

        listView = new ListView(styleLabels, ListView.HORIZONTAL_WRAP);
        listView.getList().setCellRenderer(new ButtonCellRenderer());

        ActionListener ok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireActionListener(ReportProvider.DONE);
                }
            };

        ActionListener cancel = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireActionListener(ReportProvider.CANCEL);
                }
            };

        JPanel commands = CommandPanel.createPanel(App.messages.getString("res.561"), ok, App.messages.getString("res.4"), cancel);

        tooltip = new JLabel();
        tooltip.setVerticalAlignment(SwingConstants.TOP);
        tooltip.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        tooltip.setPreferredSize(new Dimension(0, 80));
        tooltip.setIcon(Util.getIcon("/jatools/icons/help.gif"));
        tooltip.setVerticalTextPosition(SwingConstants.TOP);

        JPanel center = new JPanel(new BorderLayout());
        center.add(tooltip, BorderLayout.SOUTH);
        center.add(listView, BorderLayout.CENTER);

        listView.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    showTooltip();
                }
            });

        JLabel commentsLabel = new JLabel(App.messages.getString("res.562"));
        add(commentsLabel, BorderLayout.NORTH);

        add(center, BorderLayout.CENTER);
        add(commands, BorderLayout.SOUTH);

        listView.getList().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        fireActionListener(ReportProvider.DONE);
                    }
                }
            });

        SwingUtil.setBorder6(this);
        this.setPreferredSize(new Dimension(530, 490));

        listView.setSelectedIndex(0);
    }

    private void showTooltip() {
        ReportStyler styler = getSelectedStyler();

        if (styler != null) {
            tooltip.setText("<html>" + styler.getDescription() + "</html>");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        ReportDocument doc = null;
        ReportStyler styler = this.getSelectedStyler();

        if (styler != null) {
            ReportBuilder builder = styler.getBuilder();
            Component c = this.getTopLevelAncestor();
            Frame owner = (Frame) ((c instanceof JDialog) ? c.getParent() : null);
            doc = builder.build(owner, new BuilderContext());
        }

        return doc;
    }

    ReportStyler getSelectedStyler() {
        JLabel label = (JLabel) listView.getList().getSelectedValue();

        return (label != null) ? ((ReportStyler) label.getClientProperty(STYLER)) : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frame DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ReportDocument newDocument(Frame frame) {
        document = null;

        d = new JDialog(frame, App.messages.getString("res.563"), true);

        NewPanel newPanel = new NewPanel();
        newPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        newPanel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof ReportProvider) {
                        if (e.getActionCommand() == ReportProvider.DONE) {
                            document = ((ReportProvider) e.getSource()).getDocument();
                            d.hide();
                        } else if (e.getActionCommand() == ReportProvider.CANCEL) {
                            d.hide();
                        }
                    }
                }
            });
        d.getContentPane().add(newPanel, BorderLayout.CENTER);
        d.setSize(480, 380);
        d.setLocationRelativeTo(frame);
        d.show();

        return document;
    }
}
