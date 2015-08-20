package jatools.engine.printer;

import jatools.component.Component;
import jatools.component.Image;
import jatools.component.ImageStyle;

import jatools.core.view.ImageView;
import jatools.core.view.View;

import jatools.designer.data.Hyperlink;

import jatools.engine.PrintConstants;

import jatools.engine.imgloader.ImageLoader;

import jatools.engine.script.Context;
import jatools.engine.script.Script;

import java.awt.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ImagePrinter extends AbstractPrinter implements PropertyChangeListener {
    ImageStyle lastcss;
    boolean cssdirty = true;

    protected ImageStyle getImageStyle(Script script) {
        if (cssdirty) {
            Image image = (Image) this.getComponent();
            lastcss = image.getImageStyle();

            if (lastcss != null) {
                ImageLoader loader = (ImageLoader) script.get(PrintConstants.IMAGE_LOADER);
                loader.load(lastcss);
            }

            boolean isfield = (lastcss != null) && (lastcss.getImageFileSrc() != null) &&
                lastcss.getImageFileSrc().startsWith(Image.SOURCE_DATASET_FIELD);

            if (!isfield) {
                cssdirty = false;
            } else {
                image.invalidate();
            }
        }

        return lastcss;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void open(Component c, Script script) {
        super.open(c, script);
        c.addPropertyChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent evt) {
        this.cssdirty = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        this.getComponent().removePropertyChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public View print(Context context) throws Exception {
        doBeforePrint(context.getScript());

        Component image = this.getComponent();

        ImageView e = null;

        if (isVisible(context.getScript())) {
            e = new ImageView();

            e.setBounds(image.getBounds());

            Color color = image.getBackColor();

            if (color != null) {
                e.setBackColor(color);
            }

            e.setImageStyle(getImageStyle(context.getScript()));

            Hyperlink link = image.getHyperlink();

            if (link != null) {
                this.printHyperlink(context.getScript(), e, link);
            }

            String tooltip = image.getTooltipText();

            if (tooltip != null) {
                this.printTooltipText(context.getScript(), e, tooltip);
            }

            e.setBorder(image.getBorder());
        }

        doAfterPrint(context.getScript());
        this.done = true;

        return e;
    }
}
