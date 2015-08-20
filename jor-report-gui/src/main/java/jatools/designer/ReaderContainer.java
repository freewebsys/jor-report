package jatools.designer;

import jatools.data.reader.DatasetReader;

import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface ReaderContainer {
    int getReaderCount();

    DatasetReader getReader(int index);

    void add(DatasetReader reader);

    void remove(DatasetReader reader);

    void addDatasetReaderChangeListener(ChangeListener lst);

    void removeDatasetReaderChangeListener(ChangeListener lst);
}
