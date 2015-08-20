package jatools.swingx;

import jatools.designer.App;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author   java9
 */
public class ColorPallette extends JLabel {
    public static final int NULL_BOX_VISIBLE = 1;
    public static final int OTHER_BOX_VISIBLE = 2;
    final static int NULL_COLOR_INDEX = 64; 
    final static int OTHER_COLOR_INDEX = 65; 
    final static int COLUMN = 8; 
    final static int ROW = 8;
    final static String NULL_COLOR_TEXT = App.messages.getString("res.13"); // "空色框"中显示的字符 //
    final static String OTHER_COLOR_TEXT = App.messages.getString("res.14"); // "其他色框"中显示的字符 //
    final static int COLOR_DROP_SIZE = 12; 
    final static int DROP_GAP = 7; 
    final static int TEXT_BOX_HEIGHT = 25; 

    
    private static Color[] colors8x8;

    
    
    private Color color;

    
    Rectangle indexColorBox = new Rectangle();
    Rectangle nullColorBox = new Rectangle();
    Rectangle otherColorBox = new Rectangle();

    
    private boolean atHeader;

    
    private int textBoxVisible;

    
    private int selectedIndex = -1;

    
    private int hittedIndex = -1;

    
    ArrayList listeners = new ArrayList();

    
    Color backColor;

    /**
 * Creates a new ZColorPallette object.
 */
    public ColorPallette() {
        this(NULL_BOX_VISIBLE);
    }

    /**
 * Creates a new ZColorPallette object.
 */
    public ColorPallette(int textBoxVisible) {
        this.textBoxVisible = textBoxVisible;

        int w = (COLUMN * COLOR_DROP_SIZE) + ((COLUMN + 1) * DROP_GAP);
        int h = (ROW * COLOR_DROP_SIZE) + ((ROW + 2) * DROP_GAP);

        if ((textBoxVisible & NULL_BOX_VISIBLE) != 0) {
            nullColorBox.width = w;
            nullColorBox.height = TEXT_BOX_HEIGHT;
            h += TEXT_BOX_HEIGHT;
        }

        if ((textBoxVisible & OTHER_BOX_VISIBLE) != 0) {
            otherColorBox.width = w;
            otherColorBox.height = TEXT_BOX_HEIGHT;
            h += TEXT_BOX_HEIGHT;
        }

        indexColorBox.width = w;
        this.setPreferredSize(new Dimension(w, h));
        this.setMinimumSize(getPreferredSize());

        backColor = getBackground();

        listenToMouse();
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public static Color[] get8x8Colors() {
        if (colors8x8 == null) {
            int[] values = new int[] { 0, 128, 192, 255 };
            colors8x8 = new Color[64];

            int index = 0;

            
            for (int r = 0; r < values.length; r++) {
                for (int g = 0; g < values.length; g++) {
                    for (int b = 0; b < values.length; b++) {
                        colors8x8[index] = new Color(values[r], values[g], values[b]);
                        index++;
                    }
                }
            }
        }

        return colors8x8;
    }

    /**
 * 加入变化侦听器
 *
 * @param lst 变化侦听器
 */
    public void addChangeListener(ChangeListener lst) {
        listeners.add(lst);
    }

    /**
 * 移去变化侦听器
 *
 * @param lst 要移走的侦听器
 */
    public void removeChangeListener(ChangeListener lst) {
        listeners.remove(lst);
    }

    /**
 * 激发变化侦听器，通知当前色改变
 */
    public void fireChangeListener() {
        ChangeEvent evt = new ChangeEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            ChangeListener lst = (ChangeListener) listeners.get(i);
            lst.stateChanged(evt);
        }
    }

    /**
	 * 通知是否以嵌入方式绘制 如果与组合框配合使用时，经常是组合框通调用此方法
	 * @param atHeader   true/false 是/否处于嵌入方式
	 * @see #paintComponent
	 * @uml.property   name="atHeader"
	 */
    public void setAtHeader(boolean atHeader) {
        this.atHeader = atHeader;
    }

    /**
	 * 设置当前颜色值
	 * @param color   当前颜色
	 * @uml.property   name="color"
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * 取得选定的颜色
	 * @return   当前选定色
	 * @uml.property   name="color"
	 */
    public Color getColor() {
        if ((selectedIndex < 64) && (selectedIndex >= 0)) {
            Color[] colors = get8x8Colors();

            return colors[selectedIndex];
        } else if (selectedIndex == 64) {
            color = null;
        }

        
        return color;
    }

    /**
 * 鼠标离开时，击中色序恢复
 *
 * @param e 鼠标事件
 */
    public void mouseExited(MouseEvent e) {
        hittedIndex = -1; 

        
        JComponent comp = (JComponent) e.getSource();
        comp.repaint();
    }

    /**
 * 鼠标事件按下时，通知色值被改变
 *
 * @param e 鼠标事件
 */
    public void mousePressed(MouseEvent e) {
        int i = hitIndex(e.getX(), e.getY());

        boolean isChanged = false;

        if (i == ColorPallette.OTHER_COLOR_INDEX) {
            Color selectedColor = JColorChooser.showDialog(this, "Select color", color); //

            if ((selectedColor != null) && !selectedColor.equals(color)) {
                color = selectedColor;
                isChanged = true;
            }
        } else if (i != selectedIndex) {
            selectedIndex = i;
            color = getColor();
            isChanged = true;
        }

        if (isChanged) {
            selectedIndex = -1;

            JComponent comp = (JComponent) e.getSource();
            comp.repaint();
            fireChangeListener();
        }
    }

    /**
 * 鼠标移动时检测击中的色序，以呈现ROLLOVER风格
 *
 * @param e 鼠标事件
 */
    public void mouseMoved(MouseEvent e) {
        int i = hitIndex(e.getX(), e.getY());

        if (hittedIndex != i) {
            hittedIndex = i;

            JComponent comp = (JComponent) e.getSource();
            comp.repaint();
        }
    }

    /**
 * 侦听鼠标事件
 *
 * 如果是standalone方式使用时，可以直接从机板中接收鼠标事件，
 * 如果配合组合框使用，则该方法不会有任何效果
 *
 */
    private void listenToMouse() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ColorPallette.this.mousePressed(e);
            }

            public void mouseExited(MouseEvent e) {
                ColorPallette.this.mouseExited(e);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                ColorPallette.this.mouseMoved(e);
            }
        });
    }

    /**
 * 按给定座标取得被击中的颜色序号
 *
 * @param x 模座标
 * @param y 纵座标
 *
 * @return 击中的颜色序号
 */
    public int hitIndex(int x, int y) {
        //-----------------------------
        // 1 2 3 4 5 6 7 8
        // ...
        // 1 2 3 4 5 6 7 8
        // none
        // other
        // -----------------------------
        int i = -1;

        if (nullColorBox.contains(x, y)) {
            i = NULL_COLOR_INDEX;
        } else if (otherColorBox.contains(x, y)) {
            i = OTHER_COLOR_INDEX;
        } else if (indexColorBox.contains(x, y)) {
            
            int column = (x - DROP_GAP / 2) / (COLOR_DROP_SIZE + DROP_GAP);
            column = (column >= COLUMN) ? (COLUMN - 1) : column;

            int row = (y - DROP_GAP / 2) / (COLOR_DROP_SIZE + DROP_GAP);
            row = (row >= ROW) ? (ROW - 1) : row;
            i = (row * COLUMN) + column;
        }

        return i;
    }

    /**
 * 画调色板
 *
 * 调色板有两种画法，如果正被嵌入，则仅画一个色点，如果不被嵌入，则画整个色点及文字框，
 * 文字框是否画出可选，根据构造函数参数而定
 *
 * @param g_ 图形对象
 */
    public void paintComponent(Graphics g_) {
        Graphics2D g2 = (Graphics2D) g_;

        
        if (atHeader) {
            paintAtHeader(g2);

            return;
        }

        
        int x = DROP_GAP;
        int y = DROP_GAP;


        
        g2.setColor(backColor);

        Rectangle bounds = getBounds();
        g2.fill(bounds);

        Rectangle box = new Rectangle(x, y, COLOR_DROP_SIZE, COLOR_DROP_SIZE);

        
        Color[] colors = get8x8Colors();

        for (int i = 0; i < colors.length; i++) {
            Color c = colors[i];
            box.x = x;
            box.y = y;
            g2.setColor(c);
            g2.fill(box);
            g2.setColor(Color.darkGray);
            g2.draw(box);

            if ((color != null) && c.equals(color)) {
                
                draw3DBox(g2, box, DROP_GAP / 2, DROP_GAP / 2, false);
            } else if (hittedIndex == i) {
                
                draw3DBox(g2, box, DROP_GAP / 2, DROP_GAP / 2, true);
            }

            x += (COLOR_DROP_SIZE + DROP_GAP);

            if (((i + 1) % 8) == 0) {
                x = DROP_GAP;
                y += (COLOR_DROP_SIZE + DROP_GAP);
            }
        }

        indexColorBox.height = y;

        
        FontMetrics fm = g2.getFontMetrics(g2.getFont());

        if ((textBoxVisible & NULL_BOX_VISIBLE) != 0) {
            nullColorBox.y = y;
            nullColorBox.x = 0;


            
            x = (nullColorBox.width - fm.stringWidth(NULL_COLOR_TEXT)) / 2;
            y = (nullColorBox.y + nullColorBox.height) - ((nullColorBox.height - fm.getHeight()) / 2);
            g2.setColor(Color.black);
            g2.drawString(NULL_COLOR_TEXT, x, y);

            g2.setColor(Color.darkGray);
            box = (Rectangle) nullColorBox.clone(); // new Rectangle(1, 2, 18, 10);
            box.grow(-DROP_GAP, -DROP_GAP / 2);

            g2.draw(box);

            
            if (color == null) {
                draw3DBox(g2, nullColorBox, -DROP_GAP, -DROP_GAP / 2, false);
            } else if (hittedIndex == NULL_COLOR_INDEX) {
                draw3DBox(g2, nullColorBox, -DROP_GAP, -DROP_GAP / 2, true);
            }


            
            y = nullColorBox.y + nullColorBox.height;
        }

        if ((textBoxVisible & OTHER_BOX_VISIBLE) != 0) {
            
            otherColorBox.y = y;
            otherColorBox.x = 0;
            x = (otherColorBox.width - fm.stringWidth(OTHER_COLOR_TEXT)) / 2;
            y = (otherColorBox.y + otherColorBox.height) - ((otherColorBox.height - fm.getHeight()) / 2);

            g2.drawString(OTHER_COLOR_TEXT, x, y - 3);

            if (hittedIndex == OTHER_COLOR_INDEX) {
                draw3DBox(g2, otherColorBox, -DROP_GAP / 2, 0, true);
            }
        }
    }

    /**
 * 以嵌入方式画出调色板
 *
 * 与组合框一起使用时要求，此时仅画当前色一个色点
 *
 * @param g2 图形对象
 */
    private void paintAtHeader(Graphics2D g2) {
        Rectangle box = new Rectangle(1, 2, 18, 10);
        g2.setColor(color);
        g2.fill(box);
        g2.setColor(Color.DARK_GRAY);
        g2.draw(box);
    }

    /**
 * 画出一个立体的边框
 *
 * @param g2 图形对象
 * @param b 边框
 * @param gx 横座标放大量
 * @param gy 纵座标放大量
 * @param raised 是否突出显示
 */
    public void draw3DBox(Graphics2D g2, Rectangle b, int gx, int gy, boolean raised) {
        Rectangle border = (Rectangle) b.clone();

        border.grow(gx, gy);
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw3DRect(border.x, border.y, border.width, border.height, raised);
    }
}
