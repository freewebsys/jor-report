package jatools.designer.action;

import jatools.component.Image;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.property.editor.ImageSrcPropertyEditor;
import jatools.engine.imgloader.ImageLoader;

import java.awt.event.ActionEvent;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.7 $
 * @author $author$
 */
public class NewImageAction extends ReportAction {
    /**
     * Creates a new ZInsertImageAction object.
     *
     * @param owner DOCUMENT ME!
     */
    public NewImageAction() {
        super(App.messages.getString("res.589"), getIcon("/jatools/icons/image.gif"), getIcon("/jatools/icons/image2.gif")); // //$NON-NLS-2$
        putValue(CLASS,Image.class );
       }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ImageSrcPropertyEditor d = new ImageSrcPropertyEditor(false);

        if (d.showChooser(Main.getInstance().getEditorPanel(), null)) {
            Image image = new Image();
            image.setImageSrc((String) d.getValue());

            ImageLoader loader = Main.getInstance().getActiveEditor().getImageLoader();
            java.awt.Image _image = (java.awt.Image) loader.load(image.getImageStyle());

            int w = 100;
            int h = 100;

            if (_image != null) {
                w = _image.getWidth(null);
                h = _image.getHeight(null);
            }

            image.setWidth(w);
            image.setHeight(h);

            Main.getInstance().getActiveEditor().getReportPanel().setBaby(image);
        }
    }
}
