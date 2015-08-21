package jatools.core.view;

import jatools.engine.export.html.HtmlExport;

import java.awt.Color;
import java.awt.Font;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CSSBuilder {
	
    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     * @param _html_report_id DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String build(DisplayStyle style, String _html_report_id) {
        if (style.isForCell()) {
            return buildAsCell(style, _html_report_id);
        } else {
            return buildAsNormal(style, _html_report_id);
        }
    }

    private static String buildAsNormal(DisplayStyle style, String _html_report_id) {
        if (style.isMiddleMultiline()) {
            return buildAsMiddleMultiline(style, _html_report_id);
        }

        StringBuffer result = new StringBuffer();

        // div.d200{display:table-cell,position:absolute,background,display:table-cell,border,vertical-align,} 
        // > p.c200{text-align,_position:absolute,font-family,font-size,font-weight,font-style}
        result.append("." + _html_report_id + DisplayStyleManager.DIV_CLASS_PREFIX +
            ((DisplayStyle) style).getId());
        result.append("{");

        if (style.getBorder() != null) {
            result.append(style.getBorder().toString());
        }

        if (style.getBackColor() != null) {
            result.append("background-color: #");

            String backColor = Integer.toHexString(style.getBackColor().getRGB() &
                    HtmlExport.colorMask).toUpperCase();
            backColor = ("000000" + backColor).substring(backColor.length());
            result.append(backColor + ";");
        }

        result.append("}\n");

        Font font = style.getFont();
        result.append("." + _html_report_id + DisplayStyleManager.NORMAL_CLASS_PREFIX +
            ((DisplayStyle) style).getId());

        result.append("{");

        result.append("font-family: ");
        result.append(font.getName());
        result.append(";");

        result.append("font-size: ");
        result.append(String.valueOf(font.getSize()));
        result.append("px;");

        if (font.isBold()) {
            result.append("font-weight: bold;");
        }

        if (font.isItalic()) {
            result.append("font-style: italic;");
        }

        switch (style.getHorizontalAlignment()) {
        case TextView.LEFT:
            break;

        case TextView.CENTER:
            result.append("text-align:center;");

            break;

        case TextView.RIGHT:
            result.append("text-align:right;");

            break;
        }

        switch (style.getVerticalAlignment()) {
        case TextView.TOP:
            break;

        case TextView.MIDDLE:
            result.append(";height:100%;");

            break;

        case TextView.BOTTOM:
            result.append("position:absolute;bottom:0;");

            break;
        }

        if (style.getForeColor().getRGB() != Color.black.getRGB()) {
            result.append("color: #");

            String hexa = Integer.toHexString(style.getForeColor().getRGB() & HtmlExport.colorMask)
                                 .toUpperCase();
            hexa = ("000000" + hexa).substring(hexa.length());
            result.append(hexa + ";");
        }

        result.append("}\n");

        return result.toString();
    }

    private static String buildAsMiddleMultiline(DisplayStyle style, String _html_report_id) {
        StringBuffer result = new StringBuffer();

        // div.d200{display:table-cell,position:absolute,background,display:table-cell,border,vertical-align,} 
        // > p.c200{text-align,_position:absolute,font-family,font-size,font-weight,font-style}
        result.append("." + _html_report_id + DisplayStyleManager.DIV_CLASS_PREFIX +
            ((DisplayStyle) style).getId());
        result.append("{");

        if (style.getBorder() != null) {
            result.append(style.getBorder().toString());
        }

        if (style.getBackColor() != null) {
            result.append("background-color: #");

            String backColor = Integer.toHexString(style.getBackColor().getRGB() &
                    HtmlExport.colorMask).toUpperCase();
            backColor = ("000000" + backColor).substring(backColor.length());
            result.append(backColor + ";");
        }

        result.append("}\n");

        Font font = style.getFont();
        result.append("." + _html_report_id + DisplayStyleManager.NORMAL_CLASS_PREFIX +
            ((DisplayStyle) style).getId());

        result.append("{");

        result.append("font-family: ");
        result.append(font.getName());
        result.append(";");

        result.append("font-size: ");
        result.append(String.valueOf(font.getSize()));
        result.append("px;");

        if (font.isBold()) {
            result.append("font-weight: bold;");
        }

        if (font.isItalic()) {
            result.append("font-style: italic;");
        }

        switch (style.getHorizontalAlignment()) {
        case TextView.LEFT:
            break;

        case TextView.CENTER:
            result.append("text-align:center;");

            break;

        case TextView.RIGHT:
            result.append("text-align:right;");

            break;
        }

        if (style.getForeColor().getRGB() != Color.black.getRGB()) {
            result.append("color: #");

            String hexa = Integer.toHexString(style.getForeColor().getRGB() & HtmlExport.colorMask)
                                 .toUpperCase();
            hexa = ("000000" + hexa).substring(hexa.length());
            result.append(hexa + ";");
        }

        result.append("}\n");

        return result.toString();
    }

    private static String buildAsCell(DisplayStyle style, String _html_report_id) {
        StringBuffer result = new StringBuffer();

        // td.t200 , td > p.c200
        int id2 = style.getId2();

        if (id2 != -1) {
            result.append("." + _html_report_id + DisplayStyleManager.TD_CLASS_PREFIX +
                ((DisplayStyle) style).getId2());
            result.append("{");

            if (style.getBorder() != null) {
                result.append(style.getBorder().toString());
            }

            switch (style.getVerticalAlignment()) {
            case TextView.TOP:
                result.append("vertical-align:top;");

                break;

            case TextView.MIDDLE:
                break;

            case TextView.BOTTOM:
                result.append("vertical-align:bottom;");

                break;
            }

            if (style.getBackColor() != null) {
                result.append("background-color: #");

                String backColor = Integer.toHexString(style.getBackColor().getRGB() &
                        HtmlExport.colorMask).toUpperCase();
                backColor = ("000000" + backColor).substring(backColor.length());
                result.append(backColor + ";");
            }

            result.append("}\n");
        }

        Font font = style.getFont();
        result.append("." + _html_report_id + DisplayStyleManager.NORMAL_CLASS_PREFIX +
            ((DisplayStyle) style).getId());

        result.append("{");

        result.append("font-family: ");
        result.append(font.getName());
        result.append(";");

        result.append("font-size: ");
        result.append(String.valueOf(font.getSize()));
        result.append("px;");

        if (font.isBold()) {
            result.append("font-weight: bold;");
        }

        if (font.isItalic()) {
            result.append("font-style: italic;");
        }

        if (!style.isForCell()) {
            if (style.getBorder() != null) {
                result.append(style.getBorder() + ";");
            }

            if (style.getBackColor() != null) {
                result.append("background-color: #");

                String backColor = Integer.toHexString(style.getBackColor().getRGB() &
                        HtmlExport.colorMask).toUpperCase();
                backColor = ("000000" + backColor).substring(backColor.length());
                result.append(backColor + ";");
            }
        }

        switch (style.getHorizontalAlignment()) {
        case TextView.LEFT:
            break;

        case TextView.CENTER:
            result.append("text-align:center;");

            break;

        case TextView.RIGHT:
            result.append("text-align:right;");

            break;
        }

        if (style.getForeColor().getRGB() != Color.black.getRGB()) {
            result.append("color: #");

            String hexa = Integer.toHexString(style.getForeColor().getRGB() & HtmlExport.colorMask)
                                 .toUpperCase();
            hexa = ("000000" + hexa).substring(hexa.length());
            result.append(hexa + ";");
        }

        result.append("}\n");

        return result.toString();
    }
}
