package jatools.swingx;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class ListEditor extends JPanel {
    JList list;
    JTextField field;
    JLabel label;
    ArrayList listeners = new ArrayList();

    /**
     * Creates a new ZListEditor object.
     *
     * @param items DOCUMENT ME!
     */
    public ListEditor(Object[] items) {
        this(items, null, false);
    }

    /**
     * Creates a new ZListEditor object.
     *
     * @param items DOCUMENT ME!
     * @param editable DOCUMENT ME!
     */
    public ListEditor(Object[] items, String title, boolean editable) {
        list = new JList(items);
        list.setSelectionMode(0);
        field = new JTextField();
        field.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fireChangeListener();
            }
        });
       
        field.setEditable(editable);
        this.setLayout(new BorderLayout());

        if (title == null) {
            add(field, BorderLayout.NORTH);
        } else {
            JPanel titleBox = new JPanel();
            titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
            titleBox.add(label = new JLabel(title));
            titleBox.add(field);
            add(titleBox, BorderLayout.NORTH);
        }

        add(new JScrollPane(list), BorderLayout.CENTER);

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                field.setText(list.getSelectedValue() + ""); //
                fireChangeListener();
            }
        });
        list.setSelectedIndex(0);
    }

    public ListEditor(Object[] items, String title, boolean editable,int index) {
        list = new JList(items);
        list.setSelectionMode(0);
        field = new JTextField();
        field.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fireChangeListener();
            }
        });
     
        field.setEditable(editable);
        this.setLayout(new BorderLayout());

        if (title == null) {
            add(field, BorderLayout.NORTH);
        } else {
            JPanel titleBox = new JPanel();
            titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
            titleBox.add(label = new JLabel(title));
            titleBox.add(field);
            add(titleBox, BorderLayout.NORTH);
        }

        add(new JScrollPane(list), BorderLayout.CENTER);

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                field.setText(list.getSelectedValue() + ""); //
                fireChangeListener();
            }
        });
        list.setSelectedIndex(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    public void setSelectable(Object[] selectable) {
        DefaultListModel model = new DefaultListModel();

        for (int i = 0; i < selectable.length; i++) {
            model.addElement(selectable[i]);
        }

        list.setModel(model);

        if (selectable.length > 0) {
            list.setSelectedIndex(0);
        }
    }

    public void setEnabled(boolean e) {
        list.setEnabled(e);
        field.setEditable(e);

        if (label != null) {
            label.setEnabled(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    /**
     * DOCUMENT ME!
     */
    protected void fireChangeListener() {
        ChangeEvent e = new ChangeEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            ChangeListener l = (ChangeListener) listeners.get(i);
            l.stateChanged(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSelectedValue() {
        return field.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void setSelectedIndex(int index) {
        list.setSelectedIndex(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setSelectedValue(Object value) {
        list.setSelectedValue(value, true);
    }

    public Object getElementAt(int i) {
        if ((i >= 0) && (i < list.getModel().getSize())) {
            return list.getModel().getElementAt(i);
        } else {
            return null;
        }
    }
}
