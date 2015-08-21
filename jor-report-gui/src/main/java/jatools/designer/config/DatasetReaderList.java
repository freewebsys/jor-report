package jatools.designer.config;

import jatools.data.reader.DatasetReader;

import javax.swing.event.ChangeListener;


/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.1 $
 *  @author $author$
 * 
 */

public interface DatasetReaderList {
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	int getCount();

	/**
	 * DOCUMENT ME!
	 *
	 * @param index DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	DatasetReader getDatasetReader(int index);

	/**
	 * DOCUMENT ME!
	 *
	 * @param dataset DOCUMENT ME!
	 */
	void add(DatasetReader proxy);

	/**
	 * DOCUMENT ME!
	 *
	 * @param dataset DOCUMENT ME!
	 */
	void remove(DatasetReader proxy);

	/**
	 * DOCUMENT ME!
	 *
	 * @param lst DOCUMENT ME!
	 */
	void addDatasetChangeListener(ChangeListener lst);

	/**
	 * DOCUMENT ME!
	 *
	 * @param lst DOCUMENT ME!
	 */
	void removeDatasetChangeListener(ChangeListener lst);
}
