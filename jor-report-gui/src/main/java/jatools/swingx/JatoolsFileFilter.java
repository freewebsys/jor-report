package jatools.swingx;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class JatoolsFileFilter extends FileFilter {
	private String[] extensions;
	private String description;

	/**
	 * Creates a new ZFileFilter object.
	 * 
	 * @param extensions
	 *            DOCUMENT ME!
	 * @param description
	 *            DOCUMENT ME!
	 */
	public JatoolsFileFilter(String[] extensions, String description) {
		this.extensions = extensions;
		this.description = description;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param file
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static String getExtension(File file) {
		String extension = null;
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');

		if ((i > 0) && (i < (fileName.length() - 1))) {
			extension = fileName.substring(i + 1).toLowerCase();
		}

		return extension;
	}

	public File normalize(File file) {
		if (accept(file))
			return file;
		else
			return new File(file.getAbsolutePath() + "." + extensions[0]); //

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param file
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}

		try {

			return accept(getExtension(file));

		} catch (Exception ex) {

		}

		return false;
	}

	public boolean accept(String ext) {

		for (int i = 0; i < extensions.length; i++)
			if (ext.equals(extensions[i])) {
				return true;
			}

		return false;
	}

}
