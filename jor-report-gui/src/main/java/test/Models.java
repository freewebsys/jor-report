package test;

import jatools.data.reader.sql.Connection;
import jatools.data.reader.sql.SqlReader;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.engine.script.ReportContext;


/**
 *
 *
 * @author
 * @version
  */
public class Models {
    /**
     *
     *
     * @return
     */
    public static Dataset load() {
        Connection conn = new Connection();
        conn.setDriver("com.mysql.jdbc.Driver");
        conn.setUrl("jdbc:mysql://localhost:3306/iprint?useUnicode=true&amp;characterEncoding=GBK");
        conn.setUser("root");
        conn.setPassword("admin");

        String sql = "select * from models order by model";

        SqlReader reader = new SqlReader(conn, sql);
        Dataset dataset = null;
		try {
			dataset = reader.read(ReportContext.getDefaultContext(), -1);
		} catch (DatasetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return dataset;
    }
    
    
    public static String normal(Object a) {
        if (a == null) {
            return "";
        } else {
            return a.toString();
        }
    }
}
