package jatools.data.reader.sql;

import bsh.ParserConstants;

import jatools.dataset.RowMeta;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;

import org.apache.commons.lang.StringUtils;

import java.io.StringReader;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class WhereExpressionVisitor implements ExpressionVisitor, ParserConstants {
    private Object result;
    private Expression expression;
    private Object[] values;
    private Map<String, Integer> columns = new HashMap<String, Integer>();

    /**
     * Creates a new MyExpressionVisitor object.
     *
     * @param rowInfo DOCUMENT ME!
     */
    public WhereExpressionVisitor(Map<String, Integer> columns, Expression expression) {
        this.columns = columns;
        this.expression = expression;
    }

    /**
    * DOCUMENT ME!
    *
    * @param exp DOCUMENT ME!
    */
    public Object eval(Object[] values) {
        this.values = values;
        expression.accept(this);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            // 准备数据集
            Object[][] data = {
                    { "崔永远", 18 },
                    { "王小鸭", 16 }
                };
            jatools.dataset.Column[] columns = new jatools.dataset.Column[2];
            columns[0] = new jatools.dataset.Column("姓名", String.class);
            columns[1] = new jatools.dataset.Column("年龄", Integer.class);

            RowMeta rowMeta = new RowMeta(columns);

            Statement stat = new CCJSqlParserManager().parse(new StringReader(
                        "select * from a where 姓名='崔永远' and 年龄=18"));
            Select select = (Select) stat;
            Expression where = ((PlainSelect) select.getSelectBody()).getWhere();

            Map<String, Integer> _columns = new HashMap<String, Integer>();
            for (int i = 0; i < rowMeta.getColumnCount(); i++) {
            	_columns.put(rowMeta.getColumnName(i).toUpperCase(), i);
            }
            
            WhereExpressionVisitor visitor = new WhereExpressionVisitor(_columns, where);

            for (int i = 0; i < data.length; i++) {
                Object result = visitor.eval(data[i]);

                if (result instanceof Boolean && ((Boolean) result).booleanValue()) {
                    System.out.print("通过=====>");
                } else {
                    System.out.print("不通过=====>");
                }

                System.out.println(StringUtils.join(data[i], ","));
            }
        } catch (JSQLParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nullValue DOCUMENT ME!
     */
    public void visit(NullValue nullValue) {
        this.result = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param function DOCUMENT ME!
     */
    public void visit(Function function) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param inverseExpression DOCUMENT ME!
     */
    public void visit(InverseExpression inverseExpression) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param jdbcParameter DOCUMENT ME!
     */
    public void visit(JdbcParameter jdbcParameter) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubleValue DOCUMENT ME!
     */
    public void visit(DoubleValue doubleValue) {
        result = doubleValue.getValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param longValue DOCUMENT ME!
     */
    public void visit(LongValue longValue) {
        this.result = longValue.getValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param dateValue DOCUMENT ME!
     */
    public void visit(DateValue dateValue) {
        result = dateValue.getValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param timeValue DOCUMENT ME!
     */
    public void visit(TimeValue timeValue) {
        result = timeValue.getValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param timestampValue DOCUMENT ME!
     */
    public void visit(TimestampValue timestampValue) {
        result = timestampValue.getValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param parenthesis DOCUMENT ME!
     */
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param stringValue DOCUMENT ME!
     */
    public void visit(StringValue stringValue) {
        result = stringValue.getValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param addition DOCUMENT ME!
     */
    public void visit(Addition equalsTo) {
        Expression left = equalsTo.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = equalsTo.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, PLUS);
    }

    /**
     * DOCUMENT ME!
     *
     * @param division DOCUMENT ME!
     */
    public void visit(Division division) {
        Expression left = division.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = division.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, SLASH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param multiplication DOCUMENT ME!
     */
    public void visit(Multiplication multiplication) {
        Expression left = multiplication.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = multiplication.getRightExpression();
        right.accept(this);

        Object rightValue = result;
        result = BinaryOperation.eval(leftValue, rightValue, STAR);
    }

    /**
     * DOCUMENT ME!
     *
     * @param subtraction DOCUMENT ME!
     */
    public void visit(Subtraction subtraction) {
        Expression left = subtraction.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = subtraction.getRightExpression();
        right.accept(this);

        Object rightValue = result;
        result = BinaryOperation.eval(leftValue, rightValue, MINUS);
    }

    /**
     * DOCUMENT ME!
     *
     * @param andExpression DOCUMENT ME!
     */
    public void visit(AndExpression andExpression) {
        Expression left = andExpression.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = andExpression.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, BOOL_AND);
    }

    /**
     * DOCUMENT ME!
     *
     * @param orExpression DOCUMENT ME!
     */
    public void visit(OrExpression orExpression) {
        Expression left = orExpression.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = orExpression.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, BOOL_OR);
    }

    /**
     * DOCUMENT ME!
     *
     * @param between DOCUMENT ME!
     */
    public void visit(Between between) {
        Expression left = between.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression start = between.getBetweenExpressionStart();
        start.accept(this);

        Object startValue = result;

        Expression end = between.getBetweenExpressionStart();
        end.accept(this);

        Object endValue = result;

        if (!between.isNot()) {
            Object ge = BinaryOperation.eval(leftValue, startValue, GE);
            Object le = BinaryOperation.eval(leftValue, endValue, LE);

            result = BinaryOperation.eval(ge, le, BOOL_AND);
        } else {
            Object ge = BinaryOperation.eval(leftValue, startValue, LT);
            Object le = BinaryOperation.eval(leftValue, endValue, GT);

            result = BinaryOperation.eval(ge, le, BOOL_OR);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param equalsTo DOCUMENT ME!
     */
    public void visit(EqualsTo equalsTo) {
        Expression left = equalsTo.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = equalsTo.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, EQ);
    }

    /**
     * DOCUMENT ME!
     *
     * @param greaterThan DOCUMENT ME!
     */
    public void visit(GreaterThan greaterThan) {
        Expression left = greaterThan.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = greaterThan.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, GT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param greaterThanEquals DOCUMENT ME!
     */
    public void visit(GreaterThanEquals greaterThanEquals) {
        Expression left = greaterThanEquals.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = greaterThanEquals.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, GE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param inExpression DOCUMENT ME!
     */
    public void visit(InExpression inExpression) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param isNullExpression DOCUMENT ME!
     */
    public void visit(IsNullExpression isNullExpression) {
        Expression left = isNullExpression.getLeftExpression();
        left.accept(this);

        boolean isnull = result == null;

        if (isNullExpression.isNot()) {
            result = !isnull;
        } else {
            result = isnull;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param likeExpression DOCUMENT ME!
     */
    public void visit(LikeExpression likeExpression) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param minorThan DOCUMENT ME!
     */
    public void visit(MinorThan minorThan) {
        Expression left = minorThan.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = minorThan.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, LT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param minorThanEquals DOCUMENT ME!
     */
    public void visit(MinorThanEquals minorThanEquals) {
        Expression left = minorThanEquals.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = minorThanEquals.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, LE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param notEqualsTo DOCUMENT ME!
     */
    public void visit(NotEqualsTo notEqualsTo) {
        Expression left = notEqualsTo.getLeftExpression();
        left.accept(this);

        Object leftValue = result;

        Expression right = notEqualsTo.getRightExpression();
        right.accept(this);

        Object rightValue = result;

        result = BinaryOperation.eval(leftValue, rightValue, NE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tableColumn DOCUMENT ME!
     */
    public void visit(Column tableColumn) {
        Integer col = this.columns.get(tableColumn.getColumnName().toUpperCase());

        if (col != null) {
            result = this.values[col];
        } else {
            result = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param subSelect DOCUMENT ME!
     */
    public void visit(SubSelect subSelect) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param caseExpression DOCUMENT ME!
     */
    public void visit(CaseExpression caseExpression) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param whenClause DOCUMENT ME!
     */
    public void visit(WhenClause whenClause) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param existsExpression DOCUMENT ME!
     */
    public void visit(ExistsExpression existsExpression) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param allComparisonExpression DOCUMENT ME!
     */
    public void visit(AllComparisonExpression allComparisonExpression) {
        throw new UnsupportedOperationException("不支持!");
    }

    /**
     * DOCUMENT ME!
     *
     * @param anyComparisonExpression DOCUMENT ME!
     */
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        throw new UnsupportedOperationException("不支持!");
    }
}
