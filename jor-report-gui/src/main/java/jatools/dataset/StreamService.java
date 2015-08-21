/*
 * Created on 2004-1-6
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.dataset;

import java.io.InputStream;
import java.io.Reader;

/**
 * @author zhou
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface StreamService {
	InputStream getBinaryStream(int row, int col);
	Reader getReader(int row, int col);
	
}
