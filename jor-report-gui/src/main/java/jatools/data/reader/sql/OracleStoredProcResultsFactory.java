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
public class OracleStoredProcResultsFactory implements StoredProcResultsFactory {
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
    	//System.out.println("oracle.jdbc.OracleTypes.CURSOR >>>>>>>>>>   "+oracle.jdbc.OracleTypes.CURSOR);
        stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        stmt.execute();

        return (ResultSet) ((CallableStatement) stmt).getObject(1);
    }
}
