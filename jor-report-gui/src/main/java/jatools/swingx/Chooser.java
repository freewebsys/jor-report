package jatools.swingx;
import javax.swing.JComponent;

public interface Chooser {
    public boolean showChooser(JComponent owner,Object value);
    public Object getValue();

}


