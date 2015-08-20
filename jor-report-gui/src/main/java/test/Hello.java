package test;

import jatools.accessor.ProtectPublic;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;


/**
 *
 *
 * @author
 * @version
  */
public class Hello implements ProtectPublic{
	
	public static void main(String[] args) {
		 try {
			Statement stat = new CCJSqlParserManager().parse(new StringReader(
			 "select xx1,xx2,xx3 from Xtable where (xx1 like 'abc%' escape '\\' or xx1 like 'abc%' or xx1 = 'abc' ) and stime between 1234 and 4567"));
Select select = (Select) stat;
Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
System.out.println();
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     *
     *
     * @param user
     *
     * @return
     */
    public static String hello(String user) {
        return "您好," + user;
    }
    
    public static String myhello(String yy)
    {
    	return "aaaaaaaaaaaaaaaaaaa";
    }
}
