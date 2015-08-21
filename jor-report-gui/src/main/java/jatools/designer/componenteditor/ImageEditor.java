package jatools.designer.componenteditor;


import jatools.component.ComponentConstants;
import jatools.component.Image;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.property.editor.ImageSrcPropertyEditor;
import jatools.designer.undo.PropertyEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ImageEditor implements ComponentEditor {
    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void show(ComponentPeer peer) {
        Image image = (Image) peer.getComponent();
        Object src = image.getImageSrc();
        ImageSrcPropertyEditor editor = new ImageSrcPropertyEditor(true);

        if (editor.showChooser(peer.getOwner(), src)) {
            image.setImageSrc((String) editor.getValue());
            peer.getOwner()
                .addEdit(new PropertyEdit(peer, ComponentConstants.PROPERTY_IMAGE_SRC, src,
                    image.getImageSrc()));
        }

        peer.setEditing(false);
    }
}
