package jatools.designer;

import jatools.designer.action.AboutAction;
import jatools.designer.action.AlignBottomAction;
import jatools.designer.action.AlignCenterAction;
import jatools.designer.action.AlignLeftAction;
import jatools.designer.action.AlignRightAction;
import jatools.designer.action.AlignTopAction;
import jatools.designer.action.AlignVerticalCenterAction;
import jatools.designer.action.BorderEnableAction;
import jatools.designer.action.BringToBackAction;
import jatools.designer.action.BringToFrontAction;
import jatools.designer.action.CenterToParentAction;
import jatools.designer.action.CloseAction;
import jatools.designer.action.DocumentSettingsAction;
import jatools.designer.action.EditCopyAction;
import jatools.designer.action.EditCutAction;
import jatools.designer.action.EditDeleteAction;
import jatools.designer.action.ExitAction;
import jatools.designer.action.FormatBrushAction;
import jatools.designer.action.NewAction;
import jatools.designer.action.NewBarcodeAction;
import jatools.designer.action.NewChartAction;
import jatools.designer.action.NewImageAction;
import jatools.designer.action.NewLabelAction;
import jatools.designer.action.NewPanelAction;
import jatools.designer.action.NewPowerTableAction;
import jatools.designer.action.NewTableAction;
import jatools.designer.action.NewTextAction;
import jatools.designer.action.OpenAction;
import jatools.designer.action.PageSetupAction;
import jatools.designer.action.PasteAction;
import jatools.designer.action.RedoAction;
import jatools.designer.action.ReportAction;
import jatools.designer.action.SameBothAction;
import jatools.designer.action.SameHeightAction;
import jatools.designer.action.SameWidthAction;
import jatools.designer.action.SaveAction;
import jatools.designer.action.SaveAsAction;
import jatools.designer.action.SelectAction;
import jatools.designer.action.ShowPreviewerAction;
import jatools.designer.action.UndoAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ActionManager implements EditorView {
    public static ActionManager global;
    Action newAction;
    Action openAction;
    Action saveAction;
    Action saveAsAction;
    Action closeAction;
    Action pageSetupAction;
    Action documentSettingsAction;
    Action exitAction;
    Action undoAction;
    Action redoAction;
    Action copyAction;
    Action cutAction;
    Action pasteAction;
    Action deleteAction;
    Action formatBrushAction;
    Action selectAction;
    Action editModeAction2;
    Action previewModeAction2;
    Action newLabelAction;
    Action newTextAction;
    Action newImageAction;
    Action newBarcodeAction;
    Action newChartAction;
    Action newPanelAction;
    Action newTableAction;
    Action newPowerTableAction;
    Action bringToFrontAction;
    Action bringToBackAction;
    Action leftAlignAction;
    Action rightAlignAction;
    Action centerAlignAction;
    Action topAlignAction;
    Action bottomAlignAction;
    Action verticalCenterAlignAction;
    Action centerPageAction;
    Action sameHeightAction;
    Action sameWidthAction;
    Action sameBothAction;
    Action borderEnableAction;
    ArrayList selectableActions = new ArrayList();
    Action aboutAction;
    UndoableActionEnabler undolistener = new UndoableActionEnabler();
    SelectionListener selectionlistener = new SelectionListener();
    private ReportEditor editor;
    ShowPreviewerAction showPreviewerAction2;

    /**
     * Creates a new ActionManager object.
     */
    public ActionManager() {
        initAction();
        Main.getInstance().registerEditorView(this);
        global = this;
    }

    private void initAction() {
        newAction = new NewAction();
        openAction = new OpenAction();
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();

        closeAction = new CloseAction();

        pageSetupAction = new PageSetupAction();
        documentSettingsAction = new DocumentSettingsAction();

        exitAction = new ExitAction();

        undoAction = new UndoAction();
        redoAction = new RedoAction();
        copyAction = new EditCopyAction();
        cutAction = new EditCutAction();
        pasteAction = new PasteAction();
        deleteAction = new EditDeleteAction();
        formatBrushAction = new FormatBrushAction();

        showPreviewerAction2 = new ShowPreviewerAction();

        selectAction = new SelectAction();
        newLabelAction = new NewLabelAction();
        newTextAction = new NewTextAction();

        newImageAction = new NewImageAction();
        newBarcodeAction = new NewBarcodeAction();
        newChartAction = new NewChartAction();

        newPanelAction = new NewPanelAction();
        newTableAction = new NewTableAction();

        newPowerTableAction = new NewPowerTableAction();

        bringToFrontAction = new BringToFrontAction();
        bringToBackAction = new BringToBackAction();

        leftAlignAction = new AlignLeftAction();
        rightAlignAction = new AlignRightAction();
        centerAlignAction = new AlignCenterAction();
        topAlignAction = new AlignTopAction();
        bottomAlignAction = new AlignBottomAction();
        verticalCenterAlignAction = new AlignVerticalCenterAction();

        centerPageAction = new CenterToParentAction();

        sameHeightAction = new SameHeightAction();
        sameWidthAction = new SameWidthAction();
        sameBothAction = new SameBothAction();

        borderEnableAction = new BorderEnableAction();
        aboutAction = new AboutAction();
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void enabledEditorAction(boolean b) {
        saveAction.setEnabled(b);
        saveAsAction.setEnabled(b);
        closeAction.setEnabled(b);

        pageSetupAction.setEnabled(b);
        documentSettingsAction.setEnabled(b);

        selectAction.setEnabled(b);
        newLabelAction.setEnabled(b);
        newTextAction.setEnabled(b);

        newImageAction.setEnabled(b);
        newBarcodeAction.setEnabled(b);
        newChartAction.setEnabled(b);

        newPanelAction.setEnabled(b);
        newTableAction.setEnabled(b);

        newPowerTableAction.setEnabled(b);

        showPreviewerAction2.setEnabled(b);

        pasteAction.setEnabled(b);

        if (!b) {
            copyAction.setEnabled(b);
            cutAction.setEnabled(b);

            deleteAction.setEnabled(b);
            formatBrushAction.setEnabled(b);

            bringToFrontAction.setEnabled(b);
            bringToBackAction.setEnabled(b);

            leftAlignAction.setEnabled(b);
            rightAlignAction.setEnabled(b);
            centerAlignAction.setEnabled(b);
            topAlignAction.setEnabled(b);
            bottomAlignAction.setEnabled(b);
            verticalCenterAlignAction.setEnabled(b);

            centerPageAction.setEnabled(b);

            sameHeightAction.setEnabled(b);
            sameWidthAction.setEnabled(b);
            sameBothAction.setEnabled(b);

            borderEnableAction.setEnabled(b);
        }
    }

    Action[] clipboardActions() {
        return new Action[] { copyAction, cutAction, pasteAction, deleteAction };
    }

    void collectSelectableActions() {
        selectableActions.add(copyAction);
        selectableActions.add(cutAction);

        selectableActions.add(deleteAction);
        selectableActions.add(formatBrushAction);
        selectableActions.add(bringToFrontAction);
        selectableActions.add(bringToBackAction);

        selectableActions.add(leftAlignAction);
        selectableActions.add(rightAlignAction);
        selectableActions.add(centerAlignAction);
        selectableActions.add(topAlignAction);
        selectableActions.add(bottomAlignAction);
        selectableActions.add(verticalCenterAlignAction);
        selectableActions.add(centerPageAction);

        selectableActions.add(sameHeightAction);
        selectableActions.add(sameWidthAction);
        selectableActions.add(sameBothAction);
        selectableActions.add(borderEnableAction);
    }

    Action[] getNewActions() {
        return new Action[] {
            selectAction, newLabelAction, newTextAction,
            
            newImageAction, newBarcodeAction, newChartAction,
            
            newPanelAction, newTableAction, newPowerTableAction
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setEditor(ReportEditor editor) {
        if (this.editor != null) {
            this.editor.getReportPanel().getUndoManager().removeListener(this.undolistener);
            this.editor.getReportPanel().removeSelectionListener(selectionlistener);
        }

        this.editor = editor;

        if (this.editor != null) {
            this.editor.getReportPanel().getUndoManager().addListener(this.undolistener);
            this.undolistener.actionPerformed(null);
            this.editor.getReportPanel().addSelectionListener1(selectionlistener);
            selectionlistener.stateChanged(null);
        }

        enabledEditorAction(editor != null);
    }

    class SelectionListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            SelectionState state = Main.getInstance().getActiveEditor().getReportPanel()
                                       .getPeerSelection().getState();
            Iterator it = selectableActions.iterator();

            while (it.hasNext()) {
                ReportAction a = (ReportAction) it.next();
                a.enabled(state);
            }
        }
    }

    class UndoableActionEnabler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ReportPanel kit = Main.getInstance().getActiveEditor().getReportPanel();

            if (undoAction != null) {
                undoAction.setEnabled(kit.getUndoManager().canUndo());
            }

            if (redoAction != null) {
                redoAction.setEnabled(kit.getUndoManager().canRedo());
            }

            kit.repaint();
        }
    }
}
