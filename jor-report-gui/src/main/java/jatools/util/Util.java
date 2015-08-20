package jatools.util;

import jatools.ReportDocument;

import jatools.accessor.PropertyDescriptor;
import jatools.accessor.ProtectPublic;

import jatools.engine.System2;

import jatools.xml.XmlReader;
import jatools.xml.XmlWriter;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Util implements ProtectPublic {
    static final Random random = new Random();
    private static Logger logger = Logger.getLogger("ZUtil");
    public static boolean LOG = false;
    private static Line2D line2d = new Line2D.Float();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static synchronized String randomId() {
        return "jt" + Integer.toHexString(random.nextInt());
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean boolValue(Object b, boolean def) {
        if (b instanceof Boolean) {
            return ((Boolean) b).booleanValue();
        } else if (b instanceof String) {
            return b.equals("true");
        } else {
            return def;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param rect DOCUMENT ME!
     * @param text DOCUMENT ME!
     */
    public static void drawString(Graphics2D g, Rectangle rect, String text) {
        Rectangle2D r = g.getFontMetrics().getStringBounds(text, g);
        int cx = (int) (rect.x + ((rect.width - r.getWidth()) / 2));
        int cy = (int) (rect.y + ((rect.height - r.getHeight()) / 2));
        g.setColor(Color.BLACK);
        g.drawString(text, cx, (int) ((cy + r.getHeight()) - 3));
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param to DOCUMENT ME!
     */
    public static void descendants(jatools.component.Component c, List to) {
        for (int i = 0; i < c.getChildCount(); i++) {
            descendants(c.getChild(i), to);
        }

        to.add(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param img DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BufferedImage asBufferedImage(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, null);

        return bi;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clsName DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public static void debug(String clsName, Exception e) {
        logger.debug(clsName + ":" + e.getMessage());
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getPreferredHeight(Font f, String text) {
        if ((f == null) || (text == null)) {
            return 0;
        }

        int lines = text.split("\n").length;
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(f);

        return lines * fm.getHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @param a1 DOCUMENT ME!
     * @param a2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] append(int[] a1, int[] a2) {
        int[] a = new int[a1.length + a2.length];
        System.arraycopy(a1, 0, a, 0, a1.length);
        System.arraycopy(a2, 0, a, a1.length, a2.length);

        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param logger2 DOCUMENT ME!
     * @param ex DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String debug(Logger logger2, Exception ex, int i) {
        return debug(logger2, ex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static PropertyDescriptor[] getRegistrableProperties(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        ArrayList properties = new ArrayList();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            try {
                int m = field.getModifiers();

                if (!Modifier.isTransient(m) && !Modifier.isStatic(m)) {
                    String f = field.getName();
                    f = f.substring(0, 1).toUpperCase() + f.substring(1);

                    properties.add(new PropertyDescriptor(f, field.getType(),
                            PropertyDescriptor.SERIALIZABLE));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @param prop DOCUMENT ME!
     * @param boolGet DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object getValue(Object obj, String prop, boolean boolGet) {
        Class c = obj.getClass();
        Method setter = null;

        try {
            String prefix = boolGet ? "is" : "get";
            setter = c.getMethod(prefix + prop, new Class[0]);
            try {
                return setter.invoke(obj, new Object[0]);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        } catch (SecurityException ex) {
        } catch (NoSuchMethodException ex) {
        }

       

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @param prop DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param valueType DOCUMENT ME!
     */
    public static void setValue(Object obj, String prop, Object newValue, Class valueType) {
        Class c = obj.getClass();
        Method setter = null;

        try {
            setter = c.getMethod("set" + prop, new Class[] { valueType });
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        try {
            setter.invoke(obj, new Object[] { newValue });
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Rectangle toRactangle(int x0, int y0, int x1, int y1) {
        int w = Math.abs(x0 - x1);
        int h = Math.abs(y0 - y1);
        int x = Math.min(x0, x1);
        int y = Math.min(y0, y1);

        return new Rectangle(x, y, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param iconUrl DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageIcon getIcon(String iconUrl) {
        return new ImageIcon(Util.class.getResource(iconUrl));
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     * @param iconUrl DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageIcon getIcon(Class cls, String iconUrl) {
        return new ImageIcon(cls.getResource(iconUrl));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean intersects(Rectangle l, Rectangle r) {
        int lw = l.width;
        int lh = l.height;
        int rw = r.width;
        int rh = r.height;
        int tx = l.x;
        int ty = l.y;
        int rx = r.x;
        int ry = r.y;
        rw += rx;
        rh += ry;
        lw += tx;
        lh += ty;

        return (((rw < rx) || (rw > tx)) && ((rh < ry) || (rh > ty)) && ((lw < tx) || (lw > rx)) &&
        ((lh < ty) || (lh > ry)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer, true));

        return e.getMessage() + "\n\n" + writer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param logger DOCUMENT ME!
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String debug(Logger logger, String message) {
        logger.debug(message);

        System.out.println(message);

        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param logger2 DOCUMENT ME!
     * @param ex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String debug(Logger logger2, Exception ex) {
        return debug(logger2, toString(ex));
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public static void debug(String message) {
        debug(logger, message);
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Frame getCDF(Component comp) {
        if (comp instanceof JDialog) {
            return (Frame) ((JDialog) comp).getOwner();
        } else if (comp instanceof Frame) {
            return (Frame) comp;
        } else {
            return getCDF(((JComponent) comp).getTopLevelAncestor());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @param encoding DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getString(byte[] user, String encoding) {
        try {
            return new String(user, encoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getProperty(String string) {
        return System2.getProperty(string);
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] decode(String string) {
        return Base64Util.decode(string);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ReportDocument cloneDocument(ReportDocument doc) {
        return (ReportDocument) clone(doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object clone(Object src) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmlWriter.write(src, baos);
            baos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            Object result = XmlReader.read(bais);
            bais.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getTypeName(Class type) {
        String name = type.getName();

        if (type.isArray()) {
            try {
                Class cl = type;
                int dimensions = 0;

                while (cl.isArray()) {
                    dimensions++;
                    cl = cl.getComponentType();
                }

                StringBuffer sb = new StringBuffer();
                sb.append(cl.getName());

                for (int i = 0; i < dimensions; i++) {
                    sb.append("[]");
                }

                name = sb.toString();
            } catch (Throwable e) {
            }
        }

        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param methods DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toScripts(ArrayList methods) {
        StringBuffer imports = new StringBuffer();
        StringBuffer bodies = new StringBuffer();

        ArrayList classes = new ArrayList();

        for (int i = 0; i < methods.size(); i++) {
            String method = (String) methods.get(i);
            String[] m = method.split(";");

            if (!classes.contains(m[1])) {
                imports.append("import " + m[1]);
                imports.append(";\n");
                classes.add(m[1]);
            }

            String cls = m[1].substring(m[1].lastIndexOf(".") + 1);

            bodies.append(toScripts(cls, m[2]));
        }

        imports.append(bodies.toString());

        return imports.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param delegateClass DOCUMENT ME!
     * @param method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toScripts(String delegateClass, String method) {
        String returnType = null;

        int r = method.indexOf(' ');

        returnType = method.substring(0, r);
        method = method.substring(r + 1);

        int n = method.indexOf('(');
        String name = method.substring(0, n);

        method = method.substring(n + 1, method.indexOf(")"));

        String[] params = new String[0];

        if (method.trim().length() > 0) {
            params = method.split(",");
        }

        StringBuffer sb = new StringBuffer();
        sb.append("public ");

        if (returnType != null) {
            sb.append(returnType + " ");
        }

        sb.append(name);
        sb.append("(");

        for (int j = 0; j < params.length; j++) {
            if (j > 0) {
                sb.append(",");
            }

            sb.append(params[j]);
            sb.append(" arg" + (j + 1));
        }

        sb.append(")\n{\n");

        if (!returnType.equals("void")) {
            sb.append("return ");
        }

        sb.append(delegateClass + ".");

        sb.append(name);
        sb.append("(");

        for (int j = 0; j < params.length; j++) {
            if (j > 0) {
                sb.append(",");
            }

            sb.append(" arg" + (j + 1));
        }

        sb.append(");\n}\n");

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public static void open(String path) {
        if (path.indexOf(' ') != -1) {
            path = path.replaceAll(" ", "%20");
        }

        String[] cmd = new String[4];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = "start";
        cmd[3] = path;

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param acceptableError DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean hitLineTest(int x0, int y0, int x1, int y1, int x, int y,
        float acceptableError) {
        line2d.setLine(x0, y0, x1, y1);

        return (line2d.ptSegDist(x, y) <= acceptableError);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param rect DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean hitLineTest(int x0, int y0, int x1, int y1, Rectangle rect) {
        line2d.setLine(x0, y0, x1, y1);

        return line2d.intersects(rect) || rect.contains(x0, y0) || rect.contains(x1, y1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param polyline DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param acceptableError DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean hitPolyLineTest(Point[] polyline, int x, int y, float acceptableError) {
        for (int i = 0; i < (polyline.length - 1); i++) {
            if (hitLineTest(polyline[i].x, polyline[i].y, polyline[i + 1].x, polyline[i + 1].y, x,
                        y, acceptableError)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cursor createCursor(Icon icon) {
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension cursorSize = tool.getBestCursorSize(32, 32);
        int colors = tool.getMaximumCursorColors();

        if (cursorSize.equals(new Dimension(0, 0)) || (colors == 0)) {
            return null;
        }

        return tool.createCustomCursor(((ImageIcon) icon).getImage(), new Point(16, 16),
            "cursor." + new Date().getTime());
    }

    /**
     * DOCUMENT ME!
     *
     * @param filePath DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static String readTextFile(String filePath)
        throws Exception {
        String result = null;
        FileInputStream fis = new FileInputStream(filePath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[512];

        while (true) {
            int i = bis.read(b);

            if (i <= 0) {
                result = sb.toString();

                break;
            } else {
                sb.append(new String(b, 0, i));
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param ins DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Rectangle inflateRect(Rectangle b, Insets ins) {
        b.x -= ins.left;
        b.y -= ins.top;
        b.width += (ins.left + ins.right);
        b.height += (ins.top + ins.bottom);

        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param ins DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Rectangle deflateRect(Rectangle b, Insets ins) {
        b.x += ins.left;
        b.y += ins.top;
        b.width -= (ins.left + ins.right);
        b.height -= (ins.top + ins.bottom);

        if (b.width < 0) {
            b.width = 0;
        }

        if (b.height < 0) {
            b.height = 0;
        }

        return b;
    }
}
