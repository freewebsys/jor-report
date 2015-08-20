package jatools.data.reader.sql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface StoredProcResultsFactory {
    /**
     * DOCUMENT ME!
     *
     * @param stmt DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public ResultSet createResultSet(CallableStatement stmt)
        throws SQLException;


}
