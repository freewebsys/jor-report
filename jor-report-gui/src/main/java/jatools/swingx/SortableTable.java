package jatools.swingx;

import jatools.designer.Global;
import jatools.util.Util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class SortableTable extends JPanel implements ListSelectionListener {
    /**
     * DOCUMENT ME!
     */
    public static final int NEW = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int DELETE = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int UP = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int DOWN = 8;

    /**
     * DOCUMENT ME!
     */
    public static final int NO_NEW = DELETE | UP | DOWN;

    /**
     * DOCUMENT ME!
     */
    public static final int ALL = NEW | NO_NEW;
    public static final int UP_DOWN = UP | DOWN;

    /**
     * DOCUMENT ME!
     */
    public static final int ICON_SIZE = 24;
    private JButton newButton;
    private JButton deleteButton;
    private JButton upButton;
    private JButton downButton;
    private JTable table;

    /**
     * Creates a new SortableTable object.
     *
     * @param t DOCUMENT ME!
     * @param buttons DOCUMENT ME!
     */
    public SortableTable(JTable t, int buttons) {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Global.GBC_INSETS;

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        //gbc.weightx = 1.0;
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);

        if ((buttons & NEW) != 0) {
            bar.add(newButton = new FixedSizeButton(Util.getIcon("/jatools/icons/new.gif"), null,
                        ICON_SIZE, ICON_SIZE));
        }

        if ((buttons & DELETE) != 0) {
            deleteButton = new FixedSizeButton(Util.getIcon("/jatools/icons/delete.gif"), null,
                    ICON_SIZE, ICON_SIZE);
            deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        delete();
                    }
                });
            bar.add(deleteButton);
        }

        if ((buttons & UP) != 0) {
            upButton = new FixedSizeButton(Util.getIcon("/jatools/icons/up.gif"), null, ICON_SIZE,
                    ICON_SIZE);
            upButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        up();
                    }
                });
            bar.add(upButton);
        }

        if ((buttons & DOWN) != 0) {
            downButton = new FixedSizeButton(Util.getIcon("/jatools/icons/down.gif"), null,
                    ICON_SIZE, ICON_SIZE);
            downButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        down();
                    }
                });
            bar.add(downButton);
        }

        if(buttons !=0)
        add(bar, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        add(new JScrollPane(t), gbc);

        t.getSelectionModel().addListSelectionListener(this);

        this.table = t;
        valueChanged(null);
    }

    /**
     * DOCUMENT ME!
     */
    public void delete() {
        int i = this.table.getSelectedRow();

        if (i > -1) {
            this.getModel().removeRow(i);

            if (i == this.table.getRowCount()) {
                
                i--;
            }

            if (i >= 0) 
             {
                this.setSelectedRow(i);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setSelectedRow(int i) {
        this.table.getSelectionModel().setSelectionInterval(i, i);
    }

    private DefaultTableModel getModel() {
        return (DefaultTableModel) this.table.getModel();
    }

    /**
     * DOCUMENT ME!
     */
    public void up() {
        if (canUp()) {
            int row = table.getSelectedRow();
            this.getModel().moveRow(row, row, row - 1);

            setSelectedRow(row - 1);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void down() {
        if (canDown()) {
            int row = table.getSelectedRow();
            getModel().moveRow(row, row, row + 1);
            setSelectedRow(row + 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void valueChanged(ListSelectionEvent e) {
        if (upButton != null) {
            upButton.setEnabled(canUp());
        }

        if (downButton != null) {
            downButton.setEnabled(canDown());
        }

        if (deleteButton != null) {
            deleteButton.setEnabled(canDelete());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addNewActionListener(ActionListener lst) {
        if (newButton != null) {
            newButton.addActionListener(lst);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canUp() {
        return table.getSelectedRow() > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canDelete() {
        return table.getSelectedRow() > -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canDown() {
        return (table.getRowCount() > 1) && (table.getSelectedRow() < (table.getRowCount() - 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JTable getTable() {
        return table;
    }
}
