package jatools.designer.property.editor;

import jatools.swingx.AbstractNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;



/**
 * boolean 值table cell 编辑器
 * 用一个JCheckBox 组件实现
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class CheckBoxCellEditor extends AbstractCellEditor
    implements TableCellEditor, TableCellRenderer {
   
    JCheckBox checkBox = new JCheckBox();

    /**
     * Creates a new ZCheckBoxCellEditor object.
     */
    public CheckBoxCellEditor() {
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	checkBox.setIcon(checkBox.isSelected() ? AbstractNode.CHECKED_ICON : AbstractNode.NO_CHECK_ICON  );
                stopCellEditing();
            }
        });
        

        checkBox.setBackground(Color.white);
        checkBox.setFocusable(false);
    }

    /**
     * JTable 要求一个cell绘制器
     * <p>TableCellRenderer 的唯一方法</p>
     *
     * @param table 是哪一个table要求绘制器
     * @param value 单元格中的现值
     * @param isSelected 单元格是否被选中
     * @param hasFocus  当前单元格是否有焦点
     * @param row 单元格所在行!
     * @param column 单元格所在列
     *
     * @return 返回编辑器部件（colorCombo）
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, 
                                                   int row, int column) {
        try {
            resetCheckBox(((Boolean) value).booleanValue());
        } catch (Exception ex) {
        	ex.printStackTrace() ;
        }

        return checkBox;
    }

    /**
     * 根据现值，重新设置外观
     *
     * @param selected 当前值是否为真
     */
    private void resetCheckBox(boolean selected) {
        checkBox.setSelected(selected);
    	checkBox.setIcon(checkBox.isSelected() ? AbstractNode.CHECKED_ICON : AbstractNode.NO_CHECK_ICON  );
//        if (selected) {
//            checkBox.setText(TRUE_TEXT);
//        } else {
//            checkBox.setText(FALSE_TEXT);
//        }
    }

    /**
     * JTable 要求一个cell编辑器
     * <p>TableCellEditor 方法</p>
     *
     * @param table 是哪一个table要求编辑器
     * @param value 单元格中的现值
     * @param isSelected 单元格是否被选中
     * @param row 单元格所在行!
     * @param column 单元格所在列
     *
     * @return 返回编辑器部件（colorCombo）
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        try {
            resetCheckBox(((Boolean) value).booleanValue());
        } catch (Exception ex) {
        	ex.printStackTrace() ;
        }

        return checkBox;
    }

    /**
     * table 得知编辑器编辑结束后，来提取现值
     *
     * <p>TableCellEditor 方法</p>
     * @return 现值
     */
    public Object getCellEditorValue() {
        return new Boolean(checkBox.isSelected());
    }
}
