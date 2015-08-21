package jatools.designer.config;


import jatools.accessor.AutoAccessor;
import jatools.data.reader.DatasetReader;
import jatools.designer.App;
import jatools.util.Util;
import jatools.xml.XmlReader;
import jatools.xml.XmlWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Configuration extends AutoAccessor implements ChangeListener, DatasetReaderList {
    private static Logger logger = Logger.getLogger("ZDesignerConfiguration");
    public static final int ROWS_READER = 16;
    static String XML_PATH = "mru.xml";
    static Configuration instance;
    ArrayList readers = new ArrayList();
    ArrayList mru = new ArrayList();
    transient ArrayList proxyListeners = new ArrayList();

    /**
     * Creates a new Configuration object.
     */
    public Configuration() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Configuration getInstance() {
        if (instance == null) {
            instance = load(XML_PATH);
        }

        return instance;
    }

    private static Configuration load(String filePath) {
        Configuration configure = null;

        try {
            FileInputStream is = new FileInputStream(filePath);
            configure = (Configuration) XmlReader.read(is);
            is.close();
        } catch (FileNotFoundException e) {
            Util.debug(logger, App.messages.getString("res.558") + filePath);
        } catch (Exception e) {
            Util.debug(logger, App.messages.getString("res.559") + Util.toString(e));
        }

        if (configure == null) {
            configure = new Configuration();
        }

        return configure;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public static void write(Configuration d) {
        d.write(XML_PATH);
    }

    private void write(String filePath) {
        try {
            FileOutputStream fo = new FileOutputStream(filePath);
            XmlWriter.write(this, fo);
            fo.close();
        } catch (Exception ex) {
            Util.debug(logger, App.messages.getString("res.560") + filePath + "," + Util.toString(ex));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param readers DOCUMENT ME!
     */
    public void setReaders(ArrayList readers) {
        this.readers = readers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getReaders() {
        return readers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void add(DatasetReader reader) {
        readers.add(reader);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void remove(DatasetReader reader) {
        readers.remove(reader);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getMru() {
        return mru;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mru DOCUMENT ME!
     */
    public void setMru(ArrayList mru) {
        this.mru = mru;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof MruManager) {
            MruManager mm = (MruManager) e.getSource();
            mru.clear();

            for (int i = 0; i < mm.getSize(); i++) {
                mru.add(mm.get(i));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        return readers.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader getDatasetReader(int index) {
        return (DatasetReader) readers.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addDatasetChangeListener(ChangeListener lst) {
        proxyListeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeDatasetChangeListener(ChangeListener lst) {
        proxyListeners.remove(lst);
    }
}
