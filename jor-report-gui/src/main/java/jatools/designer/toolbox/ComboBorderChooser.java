package jatools.designer.toolbox;

import jatools.core.view.StyleAttributes;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.action.ReportAction;
import jatools.swingx.Icon25x25Button;
import jatools.swingx.SwingUtil;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ComboBorderChooser extends DropDownComponent {
    final static String ACTION_COMMAND = "action.command";
    private Action lastAction;
    private Action lastTableAction;
    private BorderSelectionPanel selectionPanel;

    /**
     * Creates a new ComboBorderChooser object.
     *
     * @param enabler DOCUMENT ME!
     */
    public ComboBorderChooser(Action enabler) {
        super(new Icon25x25Button(Util.getIcon("/jatools/icons/borderall.gif")));

        enabler.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getPropertyName().equals("enabled") ||
                            e.getPropertyName().equals("selection")) {
                        setEnabled(((Action) e.getSource()).isEnabled());
                    }
                }
            });

        final Icon25x25Button b = (Icon25x25Button) this.active_comp;

        b.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getPropertyName().equals("enabled")) {
                        if (b.isEnabled()) {
                            b.setBackground(Color.WHITE);
                        } else {
                            b.setBackground(UIManager.getDefaults().getColor("Button.background"));
                        }
                    }
                }
            });

        lastAction = new ReportAction(null, Util.getIcon("/jatools/icons/borderall2.gif")) {
                    public void actionPerformed(ActionEvent e) {
                        hidePopup();
                        doCommand(getValue(ComboBorderChooser.ACTION_COMMAND));
                    }
                };

        lastAction.putValue(ComboBorderChooser.ACTION_COMMAND, BorderWorker.ALL);

        lastTableAction = new ReportAction(null, Util.getIcon("/jatools/icons/borderall.gif")) {
                    public void actionPerformed(ActionEvent e) {
                        hidePopup();
                        doCommand(getValue(ComboBorderChooser.ACTION_COMMAND));
                    }
                };

        lastTableAction.putValue(ComboBorderChooser.ACTION_COMMAND, BorderWorker.ALL);
        setAction((ReportAction) lastAction);
    }

    protected void doCommand(Object command) {
     
        
        
        String csstext = getBorderCssText();

        if (isTableEditting()) {
            TableBorderWorker.getInstance().action((String) command, csstext);
        } else {
            SimpleBorderWorker.getInstance().action((String) command, csstext);
        }
    }

    protected JComponent createPopupComponent() {
        selectionPanel = new BorderSelectionPanel();
        selectionPanel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if ("lastAction".equals(e.getPropertyName())) {
                        setAction((ReportAction) selectionPanel.getLastAction());
                    } else if ("lastCommand".equals(e.getPropertyName())) {
                        hidePopup();
                        doCommand(e.getNewValue());
                    }
                }
            });

        selectionPanel.setTableButtonsVisible(this.isTableEditting());

        return selectionPanel;
    }

    private String getBorderCssText() {
        return ((BorderSelectionPanel) getPopupComponent()).getBorderCssText();
    }

    private void setAction(ReportAction a) {
        Icon25x25Button b = (Icon25x25Button) this.active_comp;
        b.setAction(a);
        b.setText(null);
        b.setActionCommand((String) a.getValue(ACTION_COMMAND));

        if (isTableEditting()) {
            this.lastTableAction = a;
        } else {
            this.lastAction = a;
        }
    }

    private boolean isTableEditting() {
        if (Main.getInstance().getActiveEditor() != null) {
            return Main.getInstance().getActiveEditor().getReportPanel().getTableEditKit() != null;
        } else {
            return false;
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param b DOCUMENT ME!
    */
    public void setEnabled(boolean b) {
        arrow.setEnabled(b);
        active_comp.setEnabled(b);

        if (b) {
            Icon25x25Button b1 = (Icon25x25Button) this.active_comp;

            if (isTableEditting()) {
                b1.setAction(this.lastTableAction);
                b1.setActionCommand((String) lastTableAction.getValue(ACTION_COMMAND));
            } else {
                b1.setAction(this.lastAction);
                b1.setActionCommand((String) lastAction.getValue(ACTION_COMMAND));
            }

            b1.setText(null);

            if (this.selectionPanel != null) {
                selectionPanel.setTableButtonsVisible(isTableEditting());
            }
        }
    }
}


class BorderSelectionPanel extends JPanel implements BorderWorker {
    private static final long serialVersionUID = 1L;
    Action lastAction;
    ComboStyleChooser styleChooser;
    ComboColorChooser colorChooser;
    ComboThicknessChooser thicknessChooser;
    JPanel borderButtonsPanel;
    JPanel tableBorderButtonsPanel; // for table

    /**
     * Creates a new BorderSelectionPanel object.
     */
    public BorderSelectionPanel() {
        super(new GridBagLayout());
        build();
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setTableButtonsVisible(boolean b) {
        this.borderButtonsPanel.setVisible(!b);
        this.tableBorderButtonsPanel.setVisible(b);
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Action getLastTableAction() {
        // TODO Auto-generated method stub
        return null;
    }

    void build() {
        borderButtonsPanel = createBorderButtonsPanel();
        tableBorderButtonsPanel = createTableBorderButtonsPanel();

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(this.tableBorderButtonsPanel, c);
        add(this.borderButtonsPanel, c);
        c.fill = GridBagConstraints.HORIZONTAL;

        add(new JSeparator(), c);

        JPanel commandPanel = new JPanel(new GridBagLayout());
        add(commandPanel, c);

        
        c.gridwidth = 1;
        c.insets = new Insets(1, 1, 1, 1);
        commandPanel.add(new JLabel(App.messages.getString("res.288")), c);

        c.gridwidth = GridBagConstraints.REMAINDER;

        c.weightx = 100;

        commandPanel.add(getThicknessChooser(), c);

        c.gridwidth = 1;
        commandPanel.add(new JLabel(App.messages.getString("res.289")), c);
        c.weightx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;

        commandPanel.add(getStyleChooser(), c);

        c.gridwidth = 1;

        commandPanel.add(new JLabel(App.messages.getString("res.290")), c);

        c.gridwidth = GridBagConstraints.REMAINDER;

        commandPanel.add(getColorChooser(), c);
        
        SwingUtil.setSize( this.colorChooser ,new Dimension(120,20));
        SwingUtil.setSize( this.styleChooser  ,new Dimension(120,20));
        SwingUtil.setSize( this.thicknessChooser  ,new Dimension(120,20));
        
    }

    private JPanel createTableBorderButtonsPanel() {
        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(1, 1, 1, 1);

        javax.swing.border.Border b = BorderFactory.createEtchedBorder();

        Icon25x25Button button = new Icon25x25Button(new BorderAction(App.messages.getString("res.291"),
                    Util.getIcon("/jatools/icons/borderclear.gif"), CLEAR));

        button.setActionCommand(CLEAR);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(lastAction = new BorderAction(App.messages.getString("res.292"),
                        Util.getIcon("/jatools/icons/borderall.gif"), ALL));
        button.setActionCommand(ALL);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.293"),
                    Util.getIcon("/jatools/icons/borderleft.gif"), LEFT));
        button.setActionCommand(LEFT);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.294"),
                    Util.getIcon("/jatools/icons/borderhcenter.gif"), HCENTER));
        button.setActionCommand(HCENTER);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.295"),
                    Util.getIcon("/jatools/icons/borderright.gif"), RIGHT));
        button.setActionCommand(RIGHT);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.296"),
                    Util.getIcon("/jatools/icons/borderframe.gif"), FRAME));
        button.setActionCommand(FRAME);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.297"),
                    Util.getIcon("/jatools/icons/bordertop.gif"), TOP));
        button.setActionCommand(TOP);
        c.gridwidth = 1;
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.294"),
                    Util.getIcon("/jatools/icons/bordervcenter.gif"), VCENTER));
        button.setActionCommand(VCENTER);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.298"),
                    Util.getIcon("/jatools/icons/borderbottom.gif"), BOTTOM));
        button.setActionCommand(BOTTOM);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.299"),
                    Util.getIcon("/jatools/icons/bordercross.gif"), CROSS));
        button.setActionCommand(CROSS);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        return p;
    }

    private JPanel createBorderButtonsPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(1, 1, 1, 1);

        javax.swing.border.Border b = BorderFactory.createEtchedBorder();

        Icon25x25Button button = new Icon25x25Button(new BorderAction(App.messages.getString("res.291"),
                    Util.getIcon("/jatools/icons/borderclear2.gif"), CLEAR));

        button.setActionCommand(CLEAR);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(lastAction = new BorderAction(App.messages.getString("res.292"),
                        Util.getIcon("/jatools/icons/borderall2.gif"), ALL));
        button.setActionCommand(ALL);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.293"),
                    Util.getIcon("/jatools/icons/borderleft2.gif"), LEFT));
        button.setActionCommand(LEFT);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.295"),
                    Util.getIcon("/jatools/icons/borderright2.gif"), RIGHT));
        button.setActionCommand(RIGHT);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.297"),
                    Util.getIcon("/jatools/icons/bordertop2.gif"), TOP));
        button.setActionCommand(TOP);
        c.gridwidth = 1;
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        button = new Icon25x25Button(new BorderAction(App.messages.getString("res.298"),
                    Util.getIcon("/jatools/icons/borderbottom2.gif"), BOTTOM));
        button.setActionCommand(BOTTOM);
        button.setBackground(Color.WHITE);
        button.setBorder(b);
        p.add(button, c);

        return p;
    }

    String getBorderCssText() {
        return getThicknessChooser().getThickness() + " " + getStyleChooser().getStyle() + " " +
        StyleAttributes.toString(getColorChooser().getColor());
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setEnabled(boolean b) {
        super.setEnabled(b);

        for (int i = 0; i < this.getComponentCount(); i++) {
            this.getComponent(i).setEnabled(b);
        }
    }

    void setLastAction(Action newAction) {
        Action old = this.lastAction;
        this.lastAction = newAction;
        this.firePropertyChange("lastAction", old, this.lastAction);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action getLastAction() {
        return lastAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param command DOCUMENT ME!
     */
    public void setLastCommand(String command) {
        this.firePropertyChange("lastCommand", null, command);
    }

    ComboColorChooser getColorChooser() {
        if (this.colorChooser == null) {
            this.colorChooser = new ComboColorChooser(false);
            this.colorChooser.setColor(Color.black);
        }

        return colorChooser;
    }

    ComboStyleChooser getStyleChooser() {
        if (this.styleChooser == null) {
            this.styleChooser = new ComboStyleChooser();
            this.styleChooser.setStyle("solid");
        }

        return styleChooser;
    }

    ComboThicknessChooser getThicknessChooser() {
        if (this.thicknessChooser == null) {
            this.thicknessChooser = new ComboThicknessChooser();
            this.thicknessChooser.setThickness(1);
        }

        return thicknessChooser;
    }

    class BorderAction extends ReportAction {
        public BorderAction(String name, Icon icon, String command) {
            super(name, icon);

            this.putValue(ComboBorderChooser.ACTION_COMMAND, command);
        }

        public void actionPerformed(ActionEvent e) {
            setLastAction(this);
            setLastCommand(e.getActionCommand());
        }
    }
}
