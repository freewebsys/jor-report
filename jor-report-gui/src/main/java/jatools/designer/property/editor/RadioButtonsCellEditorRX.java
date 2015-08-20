package jatools.designer.property.editor;

import jatools.swingx.XpRadioButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;



/**
 * boolean 值table cell 编辑器
 * 用一个JCheckBox 组件实现
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class RadioButtonsCellEditorRX extends AbstractCellEditor implements ActionListener, TableCellEditor,
    TableCellRenderer {
    private static final String PROPERTY_VALUE = "value"; //
    JPanel editorComponent = new JPanel();
    ButtonGroup group = new ButtonGroup();
    Object value;

    /**
    * Creates a new ZRadioButtonsCellEditorRX object.
    */
    public RadioButtonsCellEditorRX(Object[] values, Object[] texts) {
        editorComponent.setLayout(new GridLayout(1, values.length));

        for (int i = 0; i < values.length; i++) {
        	XpRadioButton b = new XpRadioButton(texts[i].toString());

            b.putClientProperty(PROPERTY_VALUE, values[i]);
            b.addActionListener(this);
            group.add(b);
            editorComponent.add(b);
            b.setBackground(Color.white);
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    */
    public void setSelectedValueIs(Object value) {
        Enumeration en = group.getElements();

        while (en.hasMoreElements()) {
            JRadioButton b = (JRadioButton) en.nextElement();

            if (b.getClientProperty(PROPERTY_VALUE).equals(value)) {
                b.setSelected(true);

                
            }else
            	b.setSelected( false);
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param evt DOCUMENT ME!
    */
    public void actionPerformed(ActionEvent evt) {
        JRadioButton b = (JRadioButton) evt.getSource();
        value = b.getClientProperty(PROPERTY_VALUE);
        stopCellEditing();
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
        setSelectedValueIs(value);
        

        return editorComponent;
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
        setSelectedValueIs(value);
      
        return editorComponent;
    }

    /**
    * table 得知编辑器编辑结束后，来提取现值
    *
    * <p>TableCellEditor 方法</p>
    * @return 现值
    */
    public Object getCellEditorValue() {
        return value;
    }
    
    public static void main(String[] args) {
    	JDialog d = new JDialog();
    	RadioButtonsCellEditorRX c = new RadioButtonsCellEditorRX(new String[]{"1","2","3"},new String[]{"1","2","3"});
    	
		d.getContentPane().add(c.editorComponent  );
		d.show() ;
		
		
	}
}
