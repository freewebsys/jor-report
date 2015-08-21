package jatools.component;

import jatools.accessor.PropertyDescriptor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Image extends Component implements ImageFileSrc {
    public static final int TOP = 0;
    public static final int BOTTOM = 2;
    public static final int CENTER = 1;
    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    private String imageSrc;
    private int horizontalAlignment;
    private int verticalAlignment;
    private boolean stretches;
    private ImageStyle imageStyle;
    private int exportImageFormat;

    /**
     * Creates a new Image object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public Image(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Creates a new Image object.
     */
    public Image() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME, ComponentConstants._IMAGE_SRC, ComponentConstants._BACK_COLOR,
            ComponentConstants._BORDER, ComponentConstants._HORIZONTAL_ALIGNMENT,
            ComponentConstants._VERTICAL_ALIGNMENT, ComponentConstants._STRECTHES,
            ComponentConstants._REQUIRED_HTML_IMAGE_FORMAT2, ComponentConstants._HYPERLINK,
            ComponentConstants._TOOLTIP_TEXT, ComponentConstants._X, ComponentConstants._Y,
            ComponentConstants._WIDTH, ComponentConstants._HEIGHT, ComponentConstants._PRINT_STYLE,
            ComponentConstants._NO_PRINT,
            
            ComponentConstants._CELL, ComponentConstants._CONSTRAINTS,
            ComponentConstants._INIT_PRINT, ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2,
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageStyle getImageStyle() {
        if (imageStyle == null) {
            imageStyle = new ImageStyle(this.imageSrc, this.horizontalAlignment,
                    this.verticalAlignment, this.stretches, this.exportImageFormat);
        }

        return imageStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param horizontalAlignment DOCUMENT ME!
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        if (this.horizontalAlignment != horizontalAlignment) {
            int _horizontalAlignment = this.horizontalAlignment;
            this.horizontalAlignment = horizontalAlignment;
            invalidate();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_HORIZONTAL_ALIGNMENT,
                    new Integer(_horizontalAlignment), new Integer(horizontalAlignment));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getImageSrc() {
        return imageSrc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param imageSrc DOCUMENT ME!
     */
    public void setImageSrc(String imageSrc) {
        if (this.imageSrc != imageSrc) {
            String _imageSrc = this.imageSrc;
            this.imageSrc = imageSrc;
            invalidate();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_IMAGE_SRC, _imageSrc, imageSrc);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isStretches() {
        return stretches;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stretches DOCUMENT ME!
     */
    public void setStretches(boolean stretches) {
        if (this.stretches != stretches) {
            boolean _stretches = this.stretches;
            this.stretches = stretches;
            invalidate();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_STRECTHES,
                    new Boolean(_stretches), new Boolean(stretches));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param verticalAlignment DOCUMENT ME!
     */
    public void setVerticalAlignment(int verticalAlignment) {
        if (this.verticalAlignment != verticalAlignment) {
            int _verticalAlignment = this.verticalAlignment;
            this.verticalAlignment = verticalAlignment;
            invalidate();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_VERTICAL_ALIGNMENT,
                    new Integer(_verticalAlignment), new Integer(verticalAlignment));
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void invalidate() {
        this.imageStyle = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getExportImageFormat() {
        return exportImageFormat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param requiredHtmlImageFormat DOCUMENT ME!
     */
    public void setExportImageFormat(int requiredHtmlImageFormat) {
        this.exportImageFormat = requiredHtmlImageFormat;
    }
}
