/*
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.component;

import jatools.accessor.PropertyDescriptor;

import java.awt.BasicStroke;
import java.awt.Graphics2D;


/**
 * 自定义线组件
 *
 */
public class Line extends Component {
    /**
     * DOCUMENT ME!
     */
    private static Object[] stockLinePatterns = new Object[6];

    static {
        float j = 1.1f;

        for (int i = 1; i < stockLinePatterns.length; i++, j += 1.0f) {
            float[] dash = {
                    j
                };
            stockLinePatterns[i] = dash;
        }
    }

    private int linePattern;
    private float lineSize = 1.0f;

   
    /**
    * 创建一个线对象
    */
    public Line() {
    }

    /**
    * 创建一个线对象
    *
    * @param x 线组件在容器中的x坐标
    * @param y 线组件在容器中的y坐标
    * @param width 线组件宽度
    * @param height 线组件高度
    */
    public Line(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * 设置线形
     *
     * @param linePattern 线形值
     */
    public void setLinePattern(int linePattern) {
        this.linePattern = linePattern;
    }

    /**
     * 获取线形
     *
     * @return 线形值
     */
    public int getLinePattern() {
        return linePattern;
    }

    /**
     * 设置线宽
     *
     * @param lineSize 线宽值
     */
    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    /**
     * 获取线宽
     *
     * @return 线宽值
     */
    public float getLineSize() {
        return lineSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void setStroke(Graphics2D g) {
        float lineSize1 = getLineSize();

        float[] dash = (float[]) stockLinePatterns[getLinePattern()];

        BasicStroke stroke;

        if (dash == null) {
            stroke = new BasicStroke(lineSize1);
        } else {
            stroke = new BasicStroke(lineSize1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                    10.0f, dash, 0.0f);
        }

        g.setStroke(stroke);
    }

    /**
    * 线的属性集
    *
    * @return 线属性
    */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            ComponentConstants._FORE_COLOR,
            ComponentConstants._LINE_PATTERN,
            ComponentConstants._LINE_SIZE,
            ComponentConstants._X,
            ComponentConstants._Y,
            ComponentConstants._WIDTH,
            ComponentConstants._HEIGHT,
            ComponentConstants._CELL,
			ComponentConstants._INIT_PRINT,
            ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2
        };
    }

	public static Object[] getStockLinePatterns() {
		return stockLinePatterns;
	}
}
