package jatools.data.reader.sql;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;

import jatools.engine.script.GlobalScripts;
import jatools.engine.script.ReportContext;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ModelsQuery {
    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws IOException
     * @throws DatasetException
     */
    public static void service(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        //	http://localhost:8099/jor/jatoolsreport?file=d:/models.xml&as=xls&models=%s&quans=%s
        String models = request.getParameter("models");
        String quans = request.getParameter("quans");
        Map qmap = GlobalScripts.toMap(models, quans);

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=GB2312");

        PrintWriter out = response.getWriter();
        Connection conn = new Connection();
        conn.setDriver("com.mysql.jdbc.Driver");
        conn.setUrl("jdbc:mysql://localhost:3306/iprint?useUnicode=true&amp;characterEncoding=GBK");
        conn.setUser("root");
        conn.setPassword("admin");

        String sql = String.format("select * from models where model in (%s) order by model",
                GlobalScripts.quoted(models));

        SqlReader reader = new SqlReader(conn, sql);
        Dataset dataset = reader.read(ReportContext.getDefaultContext(), -1);

        double total = 0.0;
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < dataset.getRowCount(); i++) {
            //        	产品编码	产品名称	规格		生产商	使用数量		单价	金额
            //
            String model = (String) dataset.getValueAt(i, "MODEL");
            int quan = ((Integer) qmap.get(model)).intValue();
            double price = ((Number) dataset.getValueAt(i, "PRICE")).doubleValue();

            String rowstr = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                    dataset.getValueAt(i, "MODEL"), dataset.getValueAt(i, "NAME"),
                    normal(dataset.getValueAt(i, "TEXTURE")) +
                    normal(dataset.getValueAt(i, "DIMENSION")),
                    normal(dataset.getValueAt(i, "MANUFACTURE")), quan + "",
                    GlobalScripts.format(price, "#,##0.00"),
                    GlobalScripts.format(price * quan, "#,##0.00"));

            buffer.append(rowstr);
            total += (price * quan);
        }

        buffer.append(String.format(" \t \t \t \t \t合计\t%s", GlobalScripts.format(total, "#,##0.00")));

        System.out.println(buffer.toString());

        out.write(buffer.toString());
    }

    private static String normal(Object a) {
        if (a == null) {
            return "";
        } else {
            return a.toString();
        }
    }
}
