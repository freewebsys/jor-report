package jatools.swingx;

import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileView;


public class ImageFileChooser {
    private static ImageFileChooser sharedInstance;
    static private String newline = "\n"; //
    public final static String jpeg = "jpeg"; //
    public final static String jpg = "jpg"; //
    public final static String gif = "gif"; //
    public final static String tiff = "tiff"; //
    public final static String tif = "tif"; //
    private JFileChooser fc;
    private File file;
    private int option;
    private Component owner;
    ImagePreview preview;

    public ImageFileChooser(Component owner) {
        fc = new JFileChooser();

        //  fc.addChoosableFileFilter(new ImageFilter());
        fc.setFileView(new ImageFileView());
        preview = new ImagePreview(fc);
        fc.setAccessory(preview);

        this.owner = owner;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if ((i > 0) && (i < (s.length() - 1))) {
            ext = s.substring(i + 1).toLowerCase();
        }

        return ext;
    }

    public boolean asBase64() {
        return preview.asBase64();
    }

    public void setAsBase64(boolean base64) {
        preview.setAsBase64(base64);
    }

    public static ImageFileChooser getSharedInstance(Component owner) {
        if (sharedInstance == null) {
            sharedInstance = new ImageFileChooser(owner);
        }

        return sharedInstance;
    }

    public static void main(String[] args) {
        getSharedInstance(null).show();
    }

    public boolean show() {
        boolean option = false;
        int returnVal = fc.showDialog(owner, App.messages.getString("res.3")); //

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            option = true; //JOptionPane.OK_OPTION ;
        }

        return option;
    }

    public File getSelectedFile() {
        return file;
    }
}


class ImagePreview extends JPanel implements PropertyChangeListener {
    ImageIcon thumbnail = null;
    File file = null;
    JRadioButton asBase64;
    JRadioButton asLink;
    ImageViewer viewer = new ImageViewer();

    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(150, 50));
        fc.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        buildUI();
    }

    public boolean asBase64() {
        return asBase64.isSelected();
    }

    public void setAsBase64(boolean base64) {
        asBase64.setSelected(base64);
    }

    private void buildUI() {
        add(viewer, BorderLayout.CENTER);
    }

    public void loadImage() {
        if (file == null) {
            return;
        }

        ImageIcon tmpIcon = new ImageIcon(file.getPath());
        viewer.setImageIcon(tmpIcon);
        viewer.repaint();
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            file = (File) e.getNewValue();

            if (isShowing()) {
                loadImage();
                viewer.repaint();
            }
        }
    }
}


class ImageFilter extends javax.swing.filechooser.FileFilter {
    // Accept all directories and all gif, jpg, or tiff files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = ImageFileChooser.getExtension(f);

        if (extension != null) {
            if (extension.equals(ImageFileChooser.tiff) ||
                    extension.equals(ImageFileChooser.tif) ||
                    extension.equals(ImageFileChooser.gif) ||
                    extension.equals(ImageFileChooser.jpeg) ||
                    extension.equals(ImageFileChooser.jpg)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return "Just Images"; //
    }
}


class ImageFileView extends FileView {
    ImageIcon jpgIcon = new ImageIcon("icons/jpgIcon.gif"); //
    ImageIcon gifIcon = new ImageIcon("icons/gifIcon.gif"); //
    ImageIcon tiffIcon = new ImageIcon("icons/tiffIcon.gif"); //

    public String getName(File f) {
        return null; // let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; // let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; // let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = ImageFileChooser.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(ImageFileChooser.jpeg) ||
                    extension.equals(ImageFileChooser.jpg)) {
                type = "JPEG Image"; //
            } else if (extension.equals(ImageFileChooser.gif)) {
                type = "GIF Image"; //
            } else if (extension.equals(ImageFileChooser.tiff) ||
                    extension.equals(ImageFileChooser.tif)) {
                type = "TIFF Image"; //
            }
        }

        return type;
    }

    public Icon getIcon(File f) {
        String extension = ImageFileChooser.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(ImageFileChooser.jpeg) ||
                    extension.equals(ImageFileChooser.jpg)) {
                icon = jpgIcon;
            } else if (extension.equals(ImageFileChooser.gif)) {
                icon = gifIcon;
            } else if (extension.equals(ImageFileChooser.tiff) ||
                    extension.equals(ImageFileChooser.tif)) {
                icon = tiffIcon;
            }
        }

        return icon;
    }
}
