/*
 * Created on 2004-1-3
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jatools.designer.config;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author   java9
 */
public class MruPopupMenu implements ChangeListener {
    static final String MRU_TAG = "mru.item"; //
                                              /*
    -----------------------    startIndex;
    c:\...
    ...
    -----------------------
    exit
    */
    MruManager manager;
    JMenu parent;
    int startIndex;

    /**
     * Creates a new MruPopupMenu object.
     *
     * @param owner DOCUMENT ME!
     * @param manager DOCUMENT ME!
     */
    public MruPopupMenu( MruManager manager) {
        this.manager = manager;
        manager.addChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     */
    public void refresh() {
        stateChanged(new ChangeEvent(manager));
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void append(JMenu parent) {
        startIndex = parent.getPopupMenu().getComponentCount();
        this.parent = parent;
        refresh();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        
        clearMruMenuItem();

        MruManager mm = (MruManager) e.getSource();

        if (mm.getSize() > 0) {
            for (int i = mm.getSize() - 1; i >= 0; i--) {
                Action action = mm.getAction() ;
                
                JMenuItem mi = new JMenuItem(action);
                mi.setActionCommand(mm.get( i));
       
                mi.setText( mm.get( i));
                
                //setMnemonic((char) (i+1) );
                mi.putClientProperty(MRU_TAG, MRU_TAG);
                parent.insert(mi, startIndex);
            }

            JPopupMenu.Separator sp = new JPopupMenu.Separator();
            sp.putClientProperty(MRU_TAG, MRU_TAG);
            parent.getPopupMenu().insert(sp, startIndex);
        }
    }

    void clearMruMenuItem() {
        if (parent.getPopupMenu() != null) {
            for (int i = parent.getPopupMenu().getComponentCount() - 1; i >= 0; i--) {
                JComponent comp = (JComponent) parent.getPopupMenu().getComponent(i);

                if (comp.getClientProperty(MRU_TAG) != null) {
                    parent.getPopupMenu().remove(i);
                }
            }
        }
    }
}
