package jatools.swingx;

import jatools.data.reader.DatasetReader;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.data.ReaderChooser;
import jatools.util.Util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class DatasetReaderTextField extends JPanel {
    static final Icon icon = Util.getIcon("/jatools/icons/dataset.gif");
    JTextField t;
    private DatasetReader reader;

    /**
     * Creates a new TemplateTextField object.
     */
    public DatasetReaderTextField() {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        t = new JTextField(12);
        add(t, gbc);
        gbc.weightx = 0;

        JButton b = new JButton(icon);
        SwingUtil.setSize(b, new Dimension(25, 25));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showDatasetReaderEditor();
                }
            });
        add(b, gbc);
    }

    protected void showDatasetReaderEditor() {
        ReaderChooser readerChooser = new ReaderChooser(Main.getInstance(), App.getConfiguration());
        readerChooser.setModal(true);
        readerChooser.show();

        if (!readerChooser.isCancel()) {
            setDatasetReader(readerChooser.getReader());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setDatasetReader(DatasetReader reader) {
        this.t.setText((reader != null) ? reader.getName() : null);

        if (reader != this.reader) {
            DatasetReader tmp = this.reader;
            this.reader = reader;

            this.firePropertyChange("reader", tmp, this.reader);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader getDatasetReader() {
        return this.reader;
    }
}
