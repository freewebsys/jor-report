package jatools.component;





import jatools.ReportDocument;
import jatools.accessor.PropertyDescriptor;
import jatools.data.Formula;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.engine.script.Script;
import jatools.formatter.Format2;
import jatools.formatter.FormatUtil;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Text extends Label implements Var {
    public static int NO_SUPPRESS = 0;
    public static int SUPPRESS = 1;
    public static int SUPPRESS_AND_STRETCH = 2;
    private Format2 format;
    private String variable;
    private boolean broken;
    boolean forcedText = false;
    private Object data;

    /**
     * Creates a new Text object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public Text(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Creates a new Text object.
     */
    public Text() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME, ComponentConstants._VARIABLE, ComponentConstants._BACK_COLOR,
            ComponentConstants._FORE_COLOR, ComponentConstants._BORDER, ComponentConstants._FONT,
            ComponentConstants._HORIZONTAL_ALIGNMENT, ComponentConstants._VERTICAL_ALIGNMENT,
            ComponentConstants._FORMAT, ComponentConstants._WORDWRAP,
            ComponentConstants._BACKGROUND_IMAGE, ComponentConstants._HYPERLINK,
            ComponentConstants._TOOLTIP_TEXT, ComponentConstants._PRINT_STYLE,
            
            ComponentConstants._X, ComponentConstants._Y, ComponentConstants._WIDTH,
            ComponentConstants._HEIGHT, ComponentConstants._CELL, ComponentConstants._INIT_PRINT,
            ComponentConstants._AFTERPRINT, ComponentConstants._BEFOREPRINT2
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataProvider DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText(Script dataProvider) {
        if (!this.forcedText) {
            if ((variable != null) && variable.startsWith("=")) {
                if (dataProvider != null) {
                    data = dataProvider.eval(variable.substring(1));
                } else {
                    System.out.println(App.messages.getString("res.636") + variable);
                }
            } else {
                data = dataProvider.get(variable);
            }

            return formatText(data);
        } else {
            return " ";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String formatText(Object data) {
        if (data != null) {
            Format2 format1 = this.getFormat();

            return (format1 != null) ? format1.format(data) : data.toString();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        ReportDocument doc = Main.getInstance().getActiveEditor().getDocument();

        if (doc != null) {
            Object var = doc.getVariable(variable);

            if (var instanceof Formula) {
                return ((Formula) var).getText();
            }
        }

        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Format2 getFormat() {
        return format;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setFormat(Format2 format) {
        this.format = format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param variable DOCUMENT ME!
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBroken() {
        return broken;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pattern DOCUMENT ME!
     */
    public void setFormatString(String pattern) {
        if (pattern != null) {
            setFormat(FormatUtil.getInstance(pattern));
        } else {
            setFormat(null);
        }
    }
    
    public static void main(String[] args) {
		System.out.println(Math.pow(10,0.5));
	}
}
