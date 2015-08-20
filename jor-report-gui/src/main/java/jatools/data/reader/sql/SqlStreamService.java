/*
 * Created on 2004-1-6
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.data.reader.sql;


import jatools.dataset.StreamService;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;




/**
 * @author zhou
 *
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class SqlStreamService implements StreamService {
    /*
     * (non-Javadoc)
     *
     * @see com.jatools.core.data.ZStreamService#getInputStream(int, int)
     */
    ResultSet results;

    public SqlStreamService(ResultSet results) {
        this.results = results;
    }

    public InputStream getBinaryStream(int row, int col) {
        try {
            results.absolute(row + 1);

            return results.getBinaryStream(col + 1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Reader getReader(int row, int col) {
        try {
            results.absolute(row + 1);


            return results.getCharacterStream(col + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
