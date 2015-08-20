package jatools.engine.imgloader;

import jatools.component.Image;
import jatools.dataset.Dataset;
import jatools.dom.DatasetBasedNode;
import jatools.dom.field.NodeField;
import jatools.dom.field.RowField;
import jatools.engine.script.Script;
import jatools.util.Base64Util;
import jatools.util.Util;

import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public abstract class AbstractImageLoader implements ImageLoader {
    static ImageLoader defaults;
    static JLabel label = new JLabel();
    Map images;

    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param imagePath
    //     *            DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public void load(ImageSource src) {
    //        if (images == null) {
    //            images = new HashMap();
    //        }
    //
    //        java.awt.Image result = (java.awt.Image) this.images.get(src);
    //
    //        if (result == null) {
    //            String path = src.getImageSrc();
    //
    //            if (path != null) {
    //                //            result = loadImage(path);
    //            }
    //
    
    //            if ((result != null) && !path.startsWith(Image.SOURCE_DATASET_FIELD)) {
    //                this.images.put(src, result);
    //            }
    //        }
    //
    //       
    //    }
    protected abstract Script getScript();

    final protected java.awt.Image loadImage(String src) {
        java.awt.Image result = null;

        if (src.startsWith(Image.SOURCE_URL)) {
            result = loadUrl(src.substring(Image.SOURCE_URL.length() + 1));
        } else if (src.startsWith(Image.SOURCE_DATASET_FIELD)) {
            result = loadDatasetField(src.substring(Image.SOURCE_DATASET_FIELD.length() + 1));
        } else if (src.startsWith(Image.SOURCE_CLASSPATH)) {
            result = loadClassPath(src.substring(Image.SOURCE_CLASSPATH.length() + 1));
        } else if (src.startsWith(Image.SOURCE_FILE)) {
            result = loadFile(src.substring(Image.SOURCE_FILE.length() + 1));
        } else if (src.startsWith(Image.SOURCE_BUILT_IN)) {
            result = loadBase64(src.substring(Image.SOURCE_BUILT_IN.length() + 1));
        }

        return result;
    }

    protected java.awt.Image loadFile(String src) {
        InputStream is;

        try {
            is = new FileInputStream(src);

            return loadInputStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected java.awt.Image loadInputStream(InputStream is) {
        //     return javax.imageio.ImageIO.read(is);
        java.awt.Image result = null;

        //        	 
        try {
            byte[] bb = new byte[is.available()];
            is.read(bb);

            result = Toolkit.getDefaultToolkit().createImage(bb);

            MediaTracker mt = new MediaTracker(label);
            mt.addImage(result, 0);

            mt.waitForID(0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    protected java.awt.Image loadClassPath(String src) {
        return loadInputStream(Util.class.getResourceAsStream(src));
    }

    protected java.awt.Image loadDatasetField(String src) {
        Script script = getScript();

        if (script != null) {
            script.clearValue2();
            script.eval(src);

     
            Object o = script.getValue2();

            if (o instanceof RowField) {
            	RowField field = (RowField) o;

                if (field.getNode() instanceof DatasetBasedNode) {
                    DatasetBasedNode node = (DatasetBasedNode) field.getNode();
                    Dataset rows = node.getDataset();
                    int col = field.getColumn();

                    if (col == -1) {
                        return null;
                    }

                    InputStream is = rows.getBinaryStream(rows.getRow(node.getRowSet().rowAt(0)).index,
                            col);

                    return loadInputStream(is);
                }
            }
        }

        return null;
    }

    protected java.awt.Image loadUrl(String src) {
        try {
            return new ImageIcon(new URL(src)).getImage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected java.awt.Image loadBase64(String src) {
        return new ImageIcon(Base64Util.decode(src)).getImage();
    }
}
