import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TestOracle {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.30.100.191:1521:hzwgc",
                    "HZWGC", "HZWGC");
            CallableStatement stmt = conn.prepareCall("{call HZWGC.SP_COLLECTION_PRINT(27,?)}");

            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();

            ResultSet xx = (ResultSet) ((CallableStatement) stmt).getObject(1);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
