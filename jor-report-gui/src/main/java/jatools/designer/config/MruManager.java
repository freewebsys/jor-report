/*
 * Created on 2003-12-31
 *
 */
package jatools.designer.config;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author jlizheng
 * @version 1.0
 *
 */
public class MruManager {
    ArrayList listeners = new ArrayList();
    ArrayList elements = new ArrayList();
    int capacity ;
	private Action action;
    

    public MruManager(int capacity,Action action) {
    	this.capacity = capacity;
    	this.action = action;
    }

    /** 
     *
     *
     * @param toOpen DOCUMENT ME!
     */
    public void open(String toOpen) {
        if (elements.contains(toOpen)) {
            int containedIndex = elements.indexOf (toOpen);
			elements.remove(containedIndex);
			elements.add(0,toOpen);
        } else {
            if (elements.size() < capacity) {
				elements.add(0,toOpen); 

            } else {
				elements.remove(capacity - 1); 
				elements.add(0,toOpen); 
            }
        }

        this.fireChangeListener();
    }



    /** 
     *
     *
     * @param capcity DOCUMENT ME!
     */
    public void setCapcity(int capcity) {
        this.capacity = capcity;
    }

    /** 
     *
     *
     * @return DOCUMENT ME!
     */
    public int getCapcity() {
        return capacity;
    }

    /** 
     *
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        return elements.size();
    }

    /** 
     *
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String get(int index) {
        return elements.get(index).toString();
    }

    /** 
     *
     *
     * @param cl DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener cl) {
		listeners.add(cl); 

    }

    /** 
     *
     *
     * @param cl DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener cl) {
		listeners.remove(cl); 
    }

    public void fireChangeListener() {
        ChangeEvent e = new ChangeEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            ChangeListener lst = (ChangeListener)listeners.get(i);
            lst.stateChanged(e);
        }
    }

	public Action getAction() {
		return action;
	}
}
