package jatools.designer;

import jatools.component.ComponentConstants;

import jatools.core.view.Border;

import jatools.designer.data.HypelinkPropertyEditor;
import jatools.designer.data.Hyperlink;
import jatools.designer.data.VariableChooser;

import jatools.designer.property.PropertyTableCellRenderer;
import jatools.designer.property.SimplePropertyEditor;
import jatools.designer.property.editor.BackgroundImagePropertyEditor;
import jatools.designer.property.editor.BorderPropertyEditor;
import jatools.designer.property.editor.CellEditorWithCustomEditor;
import jatools.designer.property.editor.CheckBoxCellEditor;
import jatools.designer.property.editor.ColorCellEditor;
import jatools.designer.property.editor.ComboBoxEditor;
import jatools.designer.property.editor.FormatChooser;
import jatools.designer.property.editor.IconListCellEditorRX;
import jatools.designer.property.editor.ImageSrcPropertyEditor;
import jatools.designer.property.editor.NodePathPropertyEditor;
import jatools.designer.property.editor.RadioButtonsCellEditorRX;
import jatools.designer.property.editor.ReaderCellEditor;
import jatools.designer.property.editor.printstyle.PrintStylePropertyEditor;

import jatools.formatter.Format2;

import jatools.swingx.AbstractIcon;
import jatools.swingx.ColorIcon;
import jatools.swingx.FontChooser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class PropertyEditorAndRendererSupport {
    static final Object[] codes = {
            "codabar", "code39", "code128", "2of5", "postnet", "ean-128", "ean-13", "ean-8", "upc-a",
            "upc-e", "royal-mail-cbc", "pdf417", "datamatrix"
        };
    static final Object[] imageTypeText = new Object[] { "png", "gif", "jpeg", "flash" };
    static final Object[] imageType = new Object[] {
            new Integer(0), new Integer(1), new Integer(2), new Integer(3)
        };

    /**
     * Creates a new PropertyEditorAndRendererSupport object.
     *
     * @param editor DOCUMENT ME!
     * @param pe DOCUMENT ME!
     */
    public PropertyEditorAndRendererSupport(JComponent editor, SimplePropertyEditor pe) {
        DefaultTableCellRenderer colorRenderer;

        CheckBoxCellEditor booleanEditorRX;

        TableCellRenderer fontRenderer;

        CellEditorWithCustomEditor fontEditor;

        TableCellRenderer formatRenderer;

        CellEditorWithCustomEditor formatEditor;

        CellEditorWithCustomEditor borderEditor;

        TableCellRenderer borderRenderer;

        IconListCellEditorRX lineSize;

        IconListCellEditorRX linePattern;

        RadioButtonsCellEditorRX alignX;
        RadioButtonsCellEditorRX alignY;

        Icon icon = new ColorIcon(10, 10);

        colorRenderer = new PropertyTableCellRenderer() {
                    public void setValue(Object value) {
                        ColorIcon icon = (ColorIcon) getIcon();
                        icon.setColor((Color) value);

                        if (value == null) {
                            super.setValue(App.messages.getString("res.13"));
                        } else {
                            Color color = (Color) value;
                            super.setValue("RGB[" + color.getRed() + "," + color.getGreen() + "," +
                                color.getBlue() + "]");
                        }
                    }
                };

        colorRenderer.setIcon(icon);

        pe.registerPropertyRenderer(Color.class, colorRenderer);

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_BACK_COLOR, new ColorCellEditor(true));

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_FORE_COLOR, new ColorCellEditor(false));

        booleanEditorRX = new CheckBoxCellEditor();
        pe.registerPropertyEditor(Boolean.TYPE, booleanEditorRX);
        booleanEditorRX = new CheckBoxCellEditor();
        pe.registerPropertyRenderer(Boolean.TYPE, booleanEditorRX);

        fontRenderer = new PropertyTableCellRenderer() {
                    public void setValue(Object value) {
                        if (value != null) {
                            Font font = (Font) value;
                            super.setValue(font.getName() + "," + font.getStyle() + "," +
                                font.getSize());
                        }
                    }
                };

        pe.registerPropertyRenderer(Font.class, fontRenderer);

        fontEditor = new CellEditorWithCustomEditor(editor, FontChooser.getDefault());
        pe.registerPropertyEditor(Font.class, fontEditor);

        pe.registerPropertyEditor(Hyperlink.class,
            new CellEditorWithCustomEditor(editor, new HypelinkPropertyEditor()));

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_TOOLTIP_TEXT,
            new CellEditorWithCustomEditor(editor, new TooltipPropertyEditor()));

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_PRINT_STYLE,
            new CellEditorWithCustomEditor(editor, new PrintStylePropertyEditor()));

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_BACKGROUND_IMAGE,
            new CellEditorWithCustomEditor(editor, new BackgroundImagePropertyEditor()));

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_IMAGE_SRC,
            new CellEditorWithCustomEditor(editor, new ImageSrcPropertyEditor(true)));

        formatRenderer = new PropertyTableCellRenderer() {
                    public void setValue(Object value) {
                        if (value != null) {
                            super.setValue(value.toString());
                        } else {
                            super.setValue(null);
                        }
                    }
                };

        pe.registerPropertyRenderer(Format2.class, formatRenderer);

        formatEditor = new CellEditorWithCustomEditor(editor,
                new FormatChooser((Frame) editor.getTopLevelAncestor()));
        pe.registerPropertyEditor(Format2.class, formatEditor);

        ReaderCellEditor rce = new ReaderCellEditor(new JComboBox());

        ComboBoxEditor bareditor = new ComboBoxEditor(codes, codes);
        pe.registerPropertyEditor(ComponentConstants.PROPERTY_CODE_TYPE, bareditor);
        pe.registerPropertyRenderer(ComponentConstants.PROPERTY_CODE_TYPE, bareditor);

        ComboBoxEditor imageTypeEditor = new ComboBoxEditor(imageTypeText, imageType);
        pe.registerPropertyEditor(ComponentConstants.PROPERTY_REQUIRED_HTML_IMAGE_FORMAT,
            imageTypeEditor);

        pe.registerPropertyRenderer(ComponentConstants.PROPERTY_REQUIRED_HTML_IMAGE_FORMAT,
            imageTypeEditor);

        borderEditor = new CellEditorWithCustomEditor(editor, new BorderPropertyEditor());
        pe.registerPropertyEditor(Border.class, borderEditor);

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_NODE_PATH,
            new CellEditorWithCustomEditor(editor, new NodePathPropertyEditor()));

        borderRenderer = new PropertyTableCellRenderer() {
                    public void setValue(Object value) {
                        super.setValue((value == null) ? null : value.toString());
                    }
                };

        pe.registerPropertyRenderer(Border.class, borderRenderer);

        icon = new AbstractIcon(30, 20) {
                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        Float width = (Float) ((JComponent) c).getClientProperty(IconListCellEditorRX.PROPERTY_VALUE);

                        if (width == null) {
                            return;
                        }

                        BasicStroke stroke = new BasicStroke(width.floatValue());

                        g.setColor(c.getBackground());
                        g.fillRect(0, y, 500, 20);
                        ((Graphics2D) g).setStroke(stroke);
                        g.setColor(c.getForeground());

                        g.drawLine(4, y + (this.getIconHeight() / 2), 80,
                            y + (this.getIconHeight() / 2));

                        g.drawString(width.floatValue() + "", 86, (y + this.getIconHeight()) - 5);
                    }
                };

        Float[] floatObjects = new Float[15];
        float size = 0.25f;

        for (int i = 0; i < floatObjects.length; i++) {
            floatObjects[i] = new Float(size);
            size += 0.25f;
        }

        lineSize = new IconListCellEditorRX(floatObjects, icon);
        pe.registerPropertyEditor(ComponentConstants.PROPERTY_LINE_SIZE, lineSize);
        pe.registerPropertyRenderer(ComponentConstants.PROPERTY_LINE_SIZE, lineSize);

        CellEditorWithCustomEditor variableEditor = new CellEditorWithCustomEditor(editor,
                new VariableChooser());

        pe.registerPropertyEditor(ComponentConstants.PROPERTY_URL, variableEditor);

        //     pe.registerPropertyEditor(ComponentConstants.PROPERTY_BACK_IMAGE, variableEditor);
        Object[] values = new Object[] { new Integer(0), new Integer(1), new Integer(2) };
        Object[] texts = new Object[] {
                App.messages.getString("res.157"), App.messages.getString("res.161"),
                App.messages.getString("res.159")
            };
        alignX = new RadioButtonsCellEditorRX(values, texts);
        pe.registerPropertyEditor(ComponentConstants.PROPERTY_HORIZONTAL_ALIGNMENT, alignX);
        pe.registerPropertyRenderer(ComponentConstants.PROPERTY_HORIZONTAL_ALIGNMENT, alignX);

        texts = new Object[] {
                App.messages.getString("res.156"), App.messages.getString("res.161"),
                App.messages.getString("res.158")
            };
        alignY = new RadioButtonsCellEditorRX(values, texts);
        pe.registerPropertyEditor(ComponentConstants.PROPERTY_VERTICAL_ALIGNMENT, alignY);
        pe.registerPropertyRenderer(ComponentConstants.PROPERTY_VERTICAL_ALIGNMENT, alignY);

        //        texts = new Object[] {

        //            };

        //        RadioButtonsCellEditorRX imageModeX = new RadioButtonsCellEditorRX(values, texts);
        //        pe.registerPropertyEditor(ComponentConstants.PROPERTY_SIZE_MODE, imageModeX);
        //        pe.registerPropertyRenderer(ComponentConstants.PROPERTY_SIZE_MODE, imageModeX);
    }
}
