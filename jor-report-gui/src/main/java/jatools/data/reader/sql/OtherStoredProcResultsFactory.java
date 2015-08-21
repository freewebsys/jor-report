package jatools.data.reader.sql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class OtherStoredProcResultsFactory implements StoredProcResultsFactory {
    /**
     * DOCUMENT ME!
     *
     * @param stmt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public ResultSet createResultSet(CallableStatement stmt)
        throws SQLException {
        stmt.registerOutParameter(1, Types.OTHER);
        stmt.execute();

        return (ResultSet) ((CallableStatement) stmt).getObject(1);
    }
}
