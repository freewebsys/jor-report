package jatools.data.reader.sql;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class SqlUtil {

	private static EqualsTo NEQ;

	public static String toNodataSql(String sql) {
		try {
			Statement stat = new CCJSqlParserManager().parse(new StringReader(
					sql));
			if (stat instanceof Select
					&& ((Select) stat).getSelectBody() instanceof PlainSelect) {
				PlainSelect select = (PlainSelect) ((Select) stat)
						.getSelectBody();
				select.setWhere(getNEQ());
				sql = stat.toString();
			}

		} catch (JSQLParserException e) {
		
			e.printStackTrace();

		}

		return sql;

	}

	private static EqualsTo getNEQ() {
		if (NEQ == null)
		{
			NEQ = new EqualsTo();
			NEQ.setLeftExpression(new LongValue("1"));
			NEQ.setRightExpression(new LongValue("0"));
		}
		return NEQ;

	}
	
	  public static void main(String[] args) {
			try {
				Statement stat = new CCJSqlParserManager().parse(new StringReader("select * from a where amount >1 and id='2' order by 年份,月份") );
				System.out.println(SqlUtil.toNodataSql("select * from (select  年份,月份,sum(金额)  from 订单查询  group by 年份,月份  order by 年份,月份) a"));
				
			} catch (JSQLParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

}
