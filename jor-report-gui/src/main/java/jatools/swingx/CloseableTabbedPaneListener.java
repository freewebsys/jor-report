package jatools.swingx;

import jatools.engine.ProtectClass;

import java.util.EventListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public interface CloseableTabbedPaneListener extends EventListener , ProtectClass{
    /**
     * Informs all <code>CloseableTabbedPaneListener</code>s when a tab should be
     * closed
     * @param tabIndexToClose the index of the tab which should be closed
     * @return true if the tab can be closed, false otherwise
     */
    boolean closeTab(int tabIndexToClose);
}
