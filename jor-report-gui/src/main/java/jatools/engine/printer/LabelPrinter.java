package jatools.engine.printer;



import jatools.component.Label;
import jatools.component.table.Cell;
import jatools.core.view.DisplayStyle;
import jatools.core.view.DisplayStyleManager;
import jatools.core.view.TextLine;
import jatools.core.view.TextView;
import jatools.core.view.View;
import jatools.designer.data.Hyperlink;
import jatools.engine.PrintConstants;
import jatools.engine.css.rule.TextRule;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.layout.TablePrinterLayout;
import jatools.engine.layout.UnitedLevelManager;
import jatools.engine.script.Context;
import jatools.engine.script.Script;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class LabelPrinter extends AbstractPrinter {
    TextView lastView;
    DisplayStyle lastStyle;
    DisplayStyle lastLocalStyle;
    DisplayStyleManager styleManager;
    boolean dirty = true;
    final ArrayList csses = new ArrayList();
    int lastStyleId = -1;
    String unprinted;

    protected DisplayStyle getDisplayStyle(Script script) {
        Label label = (Label) this.getComponent();

        if (dirty) {
            lastLocalStyle = new DisplayStyle(label.getBackColor(), label.getForeColor(),
                    label.getFont(), label.getBorder(), label.getHorizontalAlignment(),
                    label.getVerticalAlignment(), label.getFormat(), label.isWordwrap());
        }

        DisplayStyle classStyle = null;

        if (dirty) {
            try {
                DisplayStyle newStyle = (DisplayStyle) this.lastLocalStyle.clone();

                if (classStyle != null) {
                    newStyle.apply(classStyle, classStyle);
                }

                DisplayStyle matched = matches(newStyle);

                if (matched == null) {
                    lastStyle = newStyle;

                    getStyleManager(script).add(lastStyle);
                } else {
                    lastStyle = matched;
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            dirty = false;
        }

        return lastStyle;
    }

    private DisplayStyle matches(DisplayStyle tmp) {
        Iterator it = csses.iterator();

        while (it.hasNext()) {
            DisplayStyle css = (DisplayStyle) it.next();

            if (css.equals(tmp)) {
                return css;
            }
        }

        csses.add(tmp);

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View print(Context context) {
        Script script = context.getScript();
        doBeforePrint(script);

        Label label = (Label) this.getComponent();
        TextView e = null;

        if (isVisible(script)) {
            DisplayStyle css = getDisplayStyle(script);

            String caption = getText(script);

            e = createView(script, caption, css);

            Hyperlink link = label.getHyperlink();

            if (link != null) {
                this.printHyperlink(script, e, link);
            }

            String tooltip = label.getTooltipText();

            if (tooltip != null) {
                this.printTooltipText(script, e, tooltip);
            }

            setAutoSize(e, script);

            this.setLayoutRule(e, script);
            e.setData(this.getLastValue());
        }

        doAfterPrint(script);

        this.done = this.unprinted == null;

        return e;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getLastValue() {
        return null;
    }

    protected Object getUpdatePoint(Script script) {
        return null;
    }

    private void setAutoSize(TextView e, Script script) {
        TextRule rule = this.getPrintStyle().getTextRule();

        if ((rule != null) && (rule.autoSize != null)) {
            rule.reset(script);

            if (rule.autoSize.is("width")) {
                e.setAutoWidth();
            } else if (rule.autoSize.is("height")) {
                e.setAutoHeight();
            } else if (rule.autoSize.is("breakable")) {
                this.unprinted = this.getNextText(e);
            }

            if (rule.maxWidth != null) {
                e.setMaxWidth(rule.maxWidth.intValue());
            }
        }
    }

    protected String getText(Script script) {
        if (this.unprinted != null) {
            return this.unprinted;
        } else {
            return ((Label) this.getComponent()).getText();
        }
    }

    private TextView createView(Script script, String text, DisplayStyle css) {
        TextView e = new TextView(css);

        Cell cell = this.getComponent().getCell();

        if (cell == null) {
            e.setBounds(this.getComponent().getBounds());
        }

        e.setText(text);
        setBackgroundImageStyle(script, e);
        setLayoutRule(e, script);

       

        return e;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     * @param view DOCUMENT ME!
     * @param layout DOCUMENT ME!
     * @param down DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View unitView(Script script, View view, PrinterLayout layout, boolean down) {
        TextRule rule = this.getPrintStyle().getTextRule();

        if ((rule != null) && (rule.unitedLevel != null)) {
            rule.reset(script);

            UnitedLevelManager levelManager = ((TablePrinterLayout) layout).getUnitedLevelManager();

            if (levelManager.isNew(rule.unitedLevel.intValue())) {
                lastView = null;
            }

            levelManager.useLevel(rule.unitedLevel.intValue());

            TextView e = (TextView) view;

            if (lastView != null) {
                if (down) {
                    if ((lastView.getCell().row + lastView.getCell().rowSpan) == e.getCell().row) {
                        String lastText = lastView.getText();
                        String thisText = e.getText();
                        boolean eq = (lastText != null) ? lastText.equals(thisText) : (thisText == null);

                        if (eq) {
                            lastView.getCell().rowSpan += e.getCell().rowSpan;

                            return null;
                        }
                    }
                } else if ((lastView.getCell().column2() + 1) == e.getCell().column) {
                    String lastText = lastView.getText();
                    String thisText = e.getText();
                    boolean eq = (lastText != null) ? lastText.equals(thisText) : (thisText == null);

                    if (eq) {
                        lastView.getCell().colSpan += e.getCell().colSpan;

                        return null;
                    }
                }
            }

            lastView = e;
            levelManager.newNext(rule.unitedLevel.intValue());

            return view;
        } else {
            return view;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);

        this.dirty = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DisplayStyleManager getStyleManager(Script script) {
        if (styleManager == null) {
            styleManager = (DisplayStyleManager) script.get(PrintConstants.DISPLAY_STYLE_MANAGER);
        }

        return styleManager;
    }

    private String getNextText(TextView v) {
        TextLine[] lines = v.getWrappedLines();
        v.uncacheLines();

        if (lines == null) {
            return null;
        }

        String next = null;

        if (lines.length > 1) {
            next = null;

            String caption = lines[0].getText();

            for (int i = 1; i < lines.length; i++) {
                if ((lines[i].y + 4) < v.getBounds().height) {
                    caption += ("\n" + lines[i].getText());
                } else {
                    if (next == null) {
                        next = lines[i].getText();
                    } else {
                        next += ("\n" + lines[i].getText());
                    }
                }
            }

            if (next != null) {
                v.setText(caption);
            }
        }

        return next;
    }
}
