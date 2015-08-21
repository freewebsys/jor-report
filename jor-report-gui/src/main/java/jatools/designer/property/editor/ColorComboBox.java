package jatools.designer.property.editor;

import jatools.swingx.ColorPallette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ListCellRenderer;



/**
 * @author   java9
 */
public class ColorComboBox extends JComboBox implements ListCellRenderer/*自定义的弹出list绘制器*/
{
    public static final int NULL_BOX_VISIBLE = 1;
    public static final int OTHER_BOX_VISIBLE = 2;
    public static final String PROPERTY_POPUP_CLOSED = "popup_closed"; //

    
    private ColorPallette listRenderer;

    
    
    
    private Color color = Color.black;

    
    private DefaultComboBoxModel model = new DefaultComboBoxModel();

    /**
    * Creates a new ZColorPicker object.
    */
    public ColorComboBox() {
        this(NULL_BOX_VISIBLE);
    }

    /**
     * Creates a new ZColorComboBox object.
     *
     * @param textVisible DOCUMENT ME!
     */
    public ColorComboBox(int textVisible) {
        
        model.addElement(color);

        setModel(model);


        
        listRenderer = new ColorPallette(textVisible);


        
        setRenderer(this);

        
        FixedSizeComboBoxUI ui = new FixedSizeComboBoxUI();
        setUI(ui);


        
        listenToList(ui.getPopupList());


        
        setSelectedIndex(0);


        
        setEditable(false);

        JComponent jc = (JComponent) this.getEditor().getEditorComponent();
        jc.setBorder(BorderFactory.createLineBorder(Color.red)); // . .setBackground(Color.red)  ;
    }

    /**
	 * DOCUMENT ME!
	 * @param color   DOCUMENT ME!
	 * @uml.property   name="color"
	 */
    public void setColor(Color color) {
        this.color = color;
        setSelectedItem(this.color);
        repaint();
    }

    /**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="color"
	 */
    public Color getColor() {
        return color;
    }

    /**
    * DOCUMENT ME!
    *
    * @param list DOCUMENT ME!
    * @param value DOCUMENT ME!
    * @param index DOCUMENT ME!
    * @param isSelected DOCUMENT ME!
    * @param cellHasFocus DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, 
                                                  boolean cellHasFocus) {
        listRenderer.setColor(color);


        
        listRenderer.setAtHeader(index == -1);

        return listRenderer;
    }

    /**
    * 侦听弹出式列表
    *
    * 取到列表，以便将鼠标事件传给调色板，这样调色板可以呈现rollover风格
    *
    * @param list 弹出式列表
    */
    private void listenToList(JList list) {
        list.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                listRenderer.mousePressed(e);
                color = listRenderer.getColor();
                setSelectedIndex(0);
            }

            public void mouseReleased(MouseEvent e) {
                
                firePropertyChange(PROPERTY_POPUP_CLOSED, false, true);
            }

            public void mouseExited(MouseEvent e) {
                listRenderer.mouseExited(e);
            }
        });
        list.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                listRenderer.mouseMoved(e);
            }
        });
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        ColorComboBox content = new ColorComboBox();

        JDialog d = new JDialog((Frame) null, "Just For Test !"); //
        d.setModal(true);
        d.getContentPane().add(content, BorderLayout.CENTER);
        d.pack();
        d.show();
    }
}
