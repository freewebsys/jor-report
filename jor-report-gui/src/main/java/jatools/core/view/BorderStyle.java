package jatools.core.view;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BorderStyle implements BorderBase {
    /**
     * DOCUMENT ME!
     */
    public static final Map colors = new HashMap();

    static {
        colors.put("black", Color.black);
        colors.put("red", Color.red);
        colors.put("yellow", Color.yellow);
        colors.put("green", Color.green);
        colors.put("blue", Color.blue);
        colors.put("orange", Color.orange);
        colors.put("gray", Color.gray);
    }

    private Color color = Color.black;
    private String style = BORDER_STYLE_SOLID;
    private float thickness = 1;

    /**
     * Creates a new BorderStyle object.
     *
     * @param thickness DOCUMENT ME!
     * @param style DOCUMENT ME!
     * @param color DOCUMENT ME!
     */
    public BorderStyle(float thickness, String style, Color color) {
        this.thickness = thickness;
        this.style = style;
        this.color = color;
    }

    /**
     * Creates a new BorderStyle object.
     */
    public BorderStyle() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof BorderStyle) {
            BorderStyle an = (BorderStyle) obj;

            if (this.thickness != an.thickness) {
                return false;
            }

            if (this.style != an.style) {
                return false;
            }

            boolean eq = (this.color == null) ? (an.color == null) : this.color.equals(an.color);

            return eq;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStyle() {
        return style;
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getThickness() {
        return thickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     */
    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        // 如果是整数，则按px为单位，否则，以pt为单位，在jor设计器界面上，有0.5pt，1px-8px为宽度的边框线
        return thickness + (this.isIntegerThickness() ? "px " : "pt ") + style + " " +
        StyleAttributes.toString(color);
    }

    private boolean isIntegerThickness() {
        return this.thickness == (int) this.thickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styleText DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BorderStyle parse(String styleText) {
        IndexedStyleAttributes parser = new IndexedStyleAttributes(styleText);

        BorderStyle style = new BorderStyle();

        style.setThickness(parser.getPx(0, 1));
        style.setStyle(parser.getString(1, "solid"));
        style.setColor(parser.getColor(2, Color.black));

        return style;
    }
    
    public static void main(String[] args) {
    	  String text = "border-top:10 solid #123;border-top:10 solid 123px";
    	  
    	  
    	  
          System.out.println("Old text : " + text);
          System.out.println("New text : " + getEditedText(text));
	}
    
  

      /**
      * Replace all words starting with the letter 'a' or 'A' with
      * their uppercase forms.
      */
      private static String getEditedText(String aText){
        StringBuffer result = new StringBuffer();
        Matcher matcher = fINITIAL_A.matcher(aText);
        while ( matcher.find() ) {
        	
        	for(int i = 0;i < matcher.groupCount();i++)
        		System.out.println(i+">>"+matcher.group(i));
        	
        	
          matcher.appendReplacement(result, getReplacement(matcher));
        }
        matcher.appendTail(result);
        return result.toString();
      }

      private static final Pattern fINITIAL_A = Pattern.compile(
        "([\\s|:]+\\d+[\\s|$])",
        Pattern.CASE_INSENSITIVE
      );

      private static String getReplacement(Matcher aMatcher){
        return aMatcher.group(0).replaceAll("\\s+$","")+"px ";
      }

  

}
