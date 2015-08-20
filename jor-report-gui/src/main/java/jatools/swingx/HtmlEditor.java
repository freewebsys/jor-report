package jatools.swingx;

import java.awt.Component;

import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class HtmlEditor extends JTextPane {
    int step = 30;

    /**
     * Creates a new ScriptEdtor object.
     *
     * @param template DOCUMENT ME!
     */
    public HtmlEditor() {
        setEditorKit(new highlightKit());

        TabStop[] tabs = new TabStop[4];
        tabs[0] = new TabStop(step, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        tabs[1] = new TabStop(step * 2, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        tabs[2] = new TabStop(step * 3, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        tabs[3] = new TabStop(step * 4, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);

        TabSet ts = new TabSet(tabs);

        // Add the tab set to the logical style;
        // the logical style is inherited by all paragraphs
        Style style = getLogicalStyle();
        StyleConstants.setTabSet(style, ts);
        setLogicalStyle(style);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI ui = getUI();

        return (parent != null) ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
    }
}
