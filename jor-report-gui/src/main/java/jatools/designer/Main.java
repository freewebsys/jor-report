package jatools.designer;

import jatools.ReportDocument;

import jatools.component.ComponentConstants;

import jatools.designer.action.ExitAction;
import jatools.designer.action.ReportAction;

import jatools.designer.config.Configuration;
import jatools.designer.config.MruPopupMenu;

import jatools.designer.property.SimplePropertyEditor;

import jatools.designer.toolbox.ComboBorderChooser;
import jatools.designer.toolbox.Icon25x25ToggleButton;

import jatools.swingx.ComboButton;
import jatools.swingx.Icon25x25Button;
import jatools.swingx.SwingUtil;

import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Position;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Main extends JFrame {
    private static Main instance;
    private static String EDITOR_PANEL = "editor";
    private static String PREVIEW_PANEL = "preview";
    private static String titlePrefix = App.messages.getString("res.63");
    private ActionManager am;
    private _IconToggleButton boldButton;
    private _IconToggleButton italicButton;
    private JComboBox fontNames;
    private JComboBox fontSizes;
    private Action[] newableActions;
    private JMenu ppmAlignMenu;
    private JMenu ppmSizeMenu;
    private JMenu alignMenu;
    private JMenu sizeMenu;
    private boolean updateFontUI = false;
    private ArrayList editorViews = new ArrayList();
    private JPanel card;
    private ReportPreviewer previewer;
    private EditorPanel editorPanel;
    private JPopupMenu ppm;
    private JMenuBar editorMenus;

    /**
     * Creates a new Main object.
     */
    public Main() {
        super(titlePrefix);

        instance = this;

        Container editor = new JPanel(new BorderLayout());

        card = (JPanel) getContentPane();
        card.setLayout(new CardLayout());

        editorPanel = new EditorPanel();
        editorPanel.addEditorChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    fireEditorChanged();
                }
            });
        previewer = new ReportPreviewer(true);
        editor.add(editorPanel, BorderLayout.CENTER);

        card.add(editor, EDITOR_PANEL);
        card.add(previewer, PREVIEW_PANEL);

        am = new ActionManager();

        setJMenuBar(this.editorMenus = createMenuBar());

        JPanel p = new JPanel(new GridLayout(2, 1));
        JPanel secondPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JToolBar toolBar = createFileAndFormatToolBar();
        JToolBar tb = new JToolBar();

        JToolBar gridToolBar = createBorderToolBar();

        newableActions = am.getNewActions();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] names = ge.getAvailableFontFamilyNames();
        String[] sizes = new String[] {
                "6", "8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48",
                "72"
            };

        fontNames = new JComboBox(names);
        fontNames.setPreferredSize(new Dimension(150, 25));
        fontSizes = new JComboBox(sizes);
        fontSizes.setPreferredSize(new Dimension(45, 25));

        boldButton = new _IconToggleButton(Util.getIcon("/jatools/icons/_bold.gif"));
        italicButton = new _IconToggleButton(Util.getIcon("/jatools/icons/_italic.gif"));

        boldButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!updateFontUI) {
                        boolean selected = boldButton.isSelected();

                        getEditorPanel().getPropertTable().getTable()
                            .setFontAt(new Boolean(selected), ComponentConstants.PROPERTY_FONT_BOLD);
                    }
                }
            });

        italicButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!updateFontUI) {
                        boolean selected = italicButton.isSelected();

                        getEditorPanel().getPropertTable().getTable()
                            .setFontAt(new Boolean(selected),
                            ComponentConstants.PROPERTY_FONT_ITALIC);
                    }
                }
            });

        fontNames.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!updateFontUI) {
                        String fontName = (String) fontNames.getSelectedItem();

                        getEditorPanel().getPropertTable().getTable()
                            .setFontAt(fontName, ComponentConstants.PROPERTY_FONT_NAME);
                    }
                }
            });

        fontSizes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!updateFontUI) {
                        Integer fontSize = Integer.valueOf((String) fontSizes.getSelectedItem());

                        getEditorPanel().getPropertTable().getTable()
                            .setFontAt(fontSize, ComponentConstants.PROPERTY_FONT_SIZE);
                    }
                }
            });
        tb.add(fontNames);
        tb.add(fontSizes);

        tb.addSeparator();

        tb.add(boldButton);
        tb.add(italicButton);

        fontNames.setMaximumSize(fontNames.getPreferredSize());
        fontSizes.setMaximumSize(fontSizes.getPreferredSize());

        boldButton.setAlignmentY(0.5f);
        italicButton.setAlignmentY(0.5f);
        boldButton.setAlignmentX(0.5f);
        italicButton.setAlignmentX(0.5f);

        JToolBar toolbox = createNewToolbar();

        secondPanel.add(toolbox);
        secondPanel.add(tb);
        secondPanel.add(gridToolBar);

        JPanel s = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        s.add(secondPanel);
        p.add(toolBar);
        p.add(s);

        editor.add(p, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        am.enabledEditorAction(false);
        am.collectSelectableActions();

        addWindowListener(new WindowAdapter() {
                public void windowActivated(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    if (new ExitAction().closeQuery()) {
                        dispose();
                        Configuration.write(App.getConfiguration());
                        System.exit(0);
                    }
                }
            });

        this.registerEditorView(new SelectionChangeListener());
        this.fireEditorChanged();
        this.setIconImage(Util.getIcon("/jatools/icons/logo.png").getImage());

        ((JComponent) getContentPane()).setTransferHandler(new ReportDocumentFileDropHandler());
    }

    private JToolBar createNewToolbar() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(new Icon25x25Button(am.selectAction));
        toolbar.add(new Icon25x25Button(am.newLabelAction));

        toolbar.add(new Icon25x25Button(am.newTextAction));
        toolbar.add(new Icon25x25Button(am.newImageAction));
        toolbar.add(new Icon25x25Button(am.newBarcodeAction));
        toolbar.add(new Icon25x25Button(am.newChartAction));

        toolbar.add(new Icon25x25Button(am.newPanelAction));
        toolbar.add(new Icon25x25Button(am.newTableAction));
        toolbar.add(new Icon25x25Button(am.newPowerTableAction));

        return toolbar;
    }

    protected void fireEditorChanged() {
        Iterator it = this.editorViews.iterator();

        ReportEditor editor = this.getActiveEditor();

        if ((editor == null) && this.editorPanel.isVisible()) {
            editorPanel.setVisible(true);
        } else if ((editor != null) && !this.editorPanel.isVisible()) {
            editorPanel.setVisible(true);
            editorPanel.revalidate();
            this.validate();
        }

        while (it.hasNext()) {
            EditorView view = (EditorView) it.next();
            view.setEditor(getActiveEditor());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     */
    public void registerEditorView(EditorView view) {
        if (this.editorViews == null) {
            this.editorViews = new ArrayList();
        }

        this.editorViews.add(view);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JPopupMenu getPopupMenu() {
        if (ppm == null) {
            ppm = new JPopupMenu();

            ppm.add(new JMenuItem(am.copyAction));
            ppm.add(new JMenuItem(am.cutAction));
            ppm.add(new JMenuItem(am.pasteAction));
            ppm.add(new JMenuItem(am.deleteAction));
            ppm.addSeparator();

            ppm.add(new JMenuItem(am.bringToFrontAction));
            ppm.add(new JMenuItem(am.bringToBackAction));

            ppmAlignMenu = new JMenu(App.messages.getString("res.130"));
            ppmAlignMenu.setIcon(ReportAction.EMPTY_ICON);

            ppmAlignMenu.add(new JMenuItem(am.leftAlignAction));
            ppmAlignMenu.add(new JMenuItem(am.rightAlignAction));
            ppmAlignMenu.add(new JMenuItem(am.centerAlignAction));
            ppmAlignMenu.add(new JMenuItem(am.topAlignAction));
            ppmAlignMenu.add(new JMenuItem(am.bottomAlignAction));
            ppmAlignMenu.add(new JMenuItem(am.verticalCenterAlignAction));
            ppm.add(ppmAlignMenu);
            ppm.add(new JMenuItem(am.centerPageAction));

            ppmSizeMenu = new JMenu(App.messages.getString("res.15"));
            ppmSizeMenu.setIcon(ReportAction.EMPTY_ICON);
            ppmSizeMenu.add(new JMenuItem(am.sameHeightAction));
            ppmSizeMenu.add(new JMenuItem(am.sameWidthAction));
            ppmSizeMenu.add(new JMenuItem(am.sameBothAction));
            ppm.add(ppmSizeMenu);
        }

        return ppm;
    }

    private JMenuBar createMenuBar() {
        JMenuBar mbar = new JMenuBar();

        JMenu fileMenu = new JMenu(App.messages.getString("res.131"), true);
        fileMenu.setMnemonic('F');
        fileMenu.add(new JMenuItem(am.newAction));
        fileMenu.add(new JMenuItem(am.openAction));
        fileMenu.add(new JMenuItem(am.saveAction));
        fileMenu.add(new JMenuItem(am.saveAsAction));

        fileMenu.add(new JMenuItem(am.closeAction));

        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(am.pageSetupAction));

        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(am.documentSettingsAction));

        MruPopupMenu reOpenMenu = new MruPopupMenu(App.getMruManager());

        reOpenMenu.append(fileMenu);
        reOpenMenu.refresh();

        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(am.exitAction));

        JMenu editMenu = new JMenu(App.messages.getString("res.132"), true);
        editMenu.setMnemonic('E');
        editMenu.add(new JMenuItem(am.undoAction));
        editMenu.add(new JMenuItem(am.redoAction));
        editMenu.addSeparator();

        editMenu.add(new JMenuItem(am.copyAction));
        editMenu.add(new JMenuItem(am.cutAction));
        editMenu.add(new JMenuItem(am.pasteAction));
        editMenu.add(new JMenuItem(am.deleteAction));

        JMenu newMenu = new JMenu(App.messages.getString("res.133"), true);
        newMenu.setMnemonic('I');
        newMenu.add(new JMenuItem(am.newLabelAction));
        newMenu.add(new JMenuItem(am.newTextAction));

        newMenu.add(new JMenuItem(am.newImageAction));
        newMenu.add(new JMenuItem(am.newBarcodeAction));

        newMenu.add(new JMenuItem(am.newChartAction));

        newMenu.add(new JMenuItem(am.newPanelAction));

        newMenu.add(new JMenuItem(am.newTableAction));

        JMenu formatMenu = new JMenu(App.messages.getString("res.134"), true);
        formatMenu.setMnemonic('O');
        formatMenu.add(new JMenuItem(am.bringToFrontAction));
        formatMenu.add(new JMenuItem(am.bringToBackAction));

        alignMenu = new JMenu(App.messages.getString("res.130"));
        alignMenu.setIcon(ReportAction.EMPTY_ICON);
        alignMenu.add(new JMenuItem(am.leftAlignAction));
        alignMenu.add(new JMenuItem(am.rightAlignAction));
        alignMenu.add(new JMenuItem(am.centerAlignAction));
        alignMenu.add(new JMenuItem(am.topAlignAction));
        alignMenu.add(new JMenuItem(am.bottomAlignAction));
        alignMenu.add(new JMenuItem(am.verticalCenterAlignAction));
        formatMenu.add(alignMenu);
        formatMenu.add(new JMenuItem(am.centerPageAction));

        sizeMenu = new JMenu(App.messages.getString("res.15"));

        sizeMenu.setIcon(ReportAction.EMPTY_ICON);
        sizeMenu.add(new JMenuItem(am.sameHeightAction));
        sizeMenu.add(new JMenuItem(am.sameWidthAction));
        sizeMenu.add(new JMenuItem(am.sameBothAction));
        formatMenu.add(sizeMenu);

        JMenu helpMenu = new JMenu(App.messages.getString("res.135"));
        helpMenu.setMnemonic('H');

        helpMenu.add(am.aboutAction);

        mbar.add(fileMenu);
        mbar.add(editMenu);
        mbar.add(newMenu);

        mbar.add(formatMenu);

        mbar.add(helpMenu);

        return mbar;
    }

    private JToolBar createFileAndFormatToolBar() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(new Icon25x25Button(am.newAction));
        toolbar.add(new Icon25x25Button(am.openAction));
        toolbar.add(new Icon25x25Button(am.saveAction));
        toolbar.addSeparator();
        toolbar.add(new Icon25x25Button(am.undoAction));
        toolbar.add(new Icon25x25Button(am.redoAction));
        toolbar.addSeparator();
        toolbar.add(new Icon25x25Button(am.copyAction));
        toolbar.add(new Icon25x25Button(am.cutAction));
        toolbar.add(new Icon25x25Button(am.pasteAction));
        toolbar.add(new Icon25x25Button(am.deleteAction));

        toolbar.add(new Icon25x25ToggleButton(am.formatBrushAction));
        toolbar.addSeparator();
        toolbar.add(new ComboButton(new Action[] { am.bringToFrontAction, am.bringToBackAction }));
        toolbar.add(Box.createHorizontalStrut(4));
        toolbar.add(new ComboButton(
                new Action[] {
                    am.leftAlignAction, am.rightAlignAction, am.centerAlignAction, am.topAlignAction,
                    am.bottomAlignAction, am.verticalCenterAlignAction
                }));
        toolbar.add(Box.createHorizontalStrut(4));
        toolbar.add(new Icon25x25Button(am.centerPageAction));
        toolbar.add(Box.createHorizontalStrut(4));
        toolbar.add(new ComboButton(
                new Action[] { am.sameHeightAction, am.sameWidthAction, am.sameBothAction }));

        toolbar.add(Box.createHorizontalStrut(180));

        JButton b = new JButton(am.showPreviewerAction2);
        SwingUtil.setSize(b, new Dimension(60, 25));
        toolbar.add(b);
        toolbar.add(Box.createHorizontalStrut(10));

        return toolbar;
    }

    private JToolBar createBorderToolBar() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(new ComboBorderChooser(am.borderEnableAction));

        return toolbar;
    }

    private JComponent initEditor(ReportEditor editor) {
        editor.registerKeyAction('Z', Event.CTRL_MASK, am.undoAction);
        editor.registerKeyAction('Y', Event.CTRL_MASK, am.redoAction);

        return editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lf DOCUMENT ME!
     * @param owner DOCUMENT ME!
     */
    public static void setLookAndFeel(String lf, Frame owner) {
        try {
            UIManager.setLookAndFeel(lf);

            SwingUtilities.updateComponentTreeUI(owner);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportEditor getActiveEditor() {
        return this.editorPanel.getActiveEditor();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Frame getFrame() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * DOCUMENT ME!
     */
    public void showPreviewer() {
        CardLayout layout = (CardLayout) card.getLayout();

        layout.last(card);

        try {
            this.setJMenuBar(this.previewer.getMenus());

            previewer.setDocument(getActiveEditor().getDocument());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void hidePreviewer() {
        previewer.stop();

        CardLayout layout = (CardLayout) card.getLayout();

        this.setJMenuBar(this.editorMenus);
        layout.first(card);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param tip DOCUMENT ME!
     */
    public void createEditor(ReportDocument doc, String name, String tip, boolean selectProperyPanel) {
        ReportEditor editor = this.editorPanel.createEditor(name, tip, doc);
        editor.setDocument(doc);
        this.initEditor(editor);
        this.editorPanel.setActiveEditor(editor);
        this.editorPanel.resetTitle();

        if (selectProperyPanel) {
            this.editorPanel.selectPropertyPanel();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     * @param docFile DOCUMENT ME!
     */
    public void save(ReportDocument doc, File docFile) {
        try {
            getActiveEditor().setDirty(false);
            this.editorPanel.setPrompt(this.getActiveEditor(), docFile.getName(),
                docFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditorPanel getEditorPanel() {
        return editorPanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action[] getNewableActions() {
        return newableActions;
    }

    class SelectionChangeListener implements ChangeListener, EditorView {
        ReportEditor editor;

        SelectionChangeListener() {
            this.stateChanged(null);
        }

        public void stateChanged(ChangeEvent e) {
            if (ppmAlignMenu != null) {
                ppmAlignMenu.setEnabled(am.bottomAlignAction.isEnabled());
                ppmSizeMenu.setEnabled(am.sameHeightAction.isEnabled());
            }

            alignMenu.setEnabled(am.bottomAlignAction.isEnabled());
            sizeMenu.setEnabled(am.sameHeightAction.isEnabled());

            SimplePropertyEditor propEditor = editorPanel.propPanel.getTable();

            if (propEditor.existProperty("Font")) {
                Font font = (Font) propEditor.getPropertyValue("Font", Font.class);

                if (font != null) {
                    updateFontUI = true;
                    fontNames.setSelectedItem(font.getName());
                    fontSizes.setSelectedItem(font.getSize() + "");
                    boldButton.setSelected(font.isBold());
                    italicButton.setSelected(font.isItalic());
                    updateFontUI = false;
                }

                fontNames.setEnabled(true);
                fontSizes.setEnabled(true);
                boldButton.setEnabled(true);
                italicButton.setEnabled(true);
            } else {
                fontNames.setEnabled(false);
                fontSizes.setEnabled(false);
                boldButton.setEnabled(false);
                italicButton.setEnabled(false);
            }

            boolean border = propEditor.existProperty("Border");

            am.borderEnableAction.setEnabled(border);
        }

        public void setEditor(ReportEditor editor) {
            if (this.editor != null) {
                this.editor.removeSelectionChangeListener(this);
            }

            this.editor = editor;

            if (this.editor != null) {
                this.editor.addSelectionChangeListener(this);
            }
        }
    }
}


class _IconToggleButton extends JToggleButton {
    _IconToggleButton(Icon icon) {
        super(icon);

        this.setPreferredSize(new Dimension(25, 25));
        this.setMinimumSize(getPreferredSize());
        this.setMaximumSize(getPreferredSize());
    }
}
