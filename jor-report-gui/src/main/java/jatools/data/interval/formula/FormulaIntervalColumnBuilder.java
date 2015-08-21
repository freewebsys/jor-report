package jatools.data.interval.formula;

import jatools.data.reader.DatasetCursor;
import jatools.data.reader.ScrollableField;
import jatools.data.reader.udc.UserColumnBuilder;
import jatools.dataset.Dataset;
import jatools.dataset.UserColumn;
import jatools.engine.script.Script;

import java.util.ArrayList;

import bsh.BSHPrimaryExpression;
import bsh.NameSpace;
import bsh.SimpleNode;
import bsh.UtilEvalError;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class FormulaIntervalColumnBuilder implements UserColumnBuilder {
    static Object NULL = new Object();

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param script DOCUMENT ME!
     */
    public void build(Dataset data, Object src, Script script) {
        FormulaIntervalColumn intervalCol = (FormulaIntervalColumn) src;
        data.getRowInfo().addColumn(new UserColumn(intervalCol.getName()));

        NameSpace localSpace = script.createNameSpace();
        script.pushNameSpace(localSpace);

        DatasetCursor cursor = new DatasetCursor(data);

        for (int i = 0; i < data.getColumnCount(); i++) {
            ScrollableField field = new ScrollableField(i, cursor);

            try {
                localSpace.setLocalVariable(data.getColumnName(i), field);
            } catch (UtilEvalError e) {
                e.printStackTrace();
            }
        }

        Object[][] expres = this.parseExpression(intervalCol.getFormulas(), script);

        while (cursor.hasNext()) {
            cursor.next();

            Object val = null;

            for (int i = 0; i < expres.length; i++) {
                SimpleNode expNode = (SimpleNode) expres[i][0];
                boolean hit = false;

                if (expNode != null) {
                    Object o = script.eval(expNode);
                    hit = o instanceof Boolean && ((Boolean) o).booleanValue();
                } else {
                    hit = true;
                }

                if (hit) {
                    if (expres[i][2] != NULL) {
                        val = expres[i][2];
                    } else {
                        val = script.eval((SimpleNode) expres[i][1]);
                    }

                    break;
                }
            }

            data.getReferenceToRow(cursor.getRow()).addColumn(val);
        }

        script.popNameSpace();
    }

    Object[][] parseExpression(ArrayList items, Script script) {
        Object[][] result = new Object[items.size()][3];

        for (int i = 0; i < result.length; i++) {
            IntervalFormula item = (IntervalFormula) items.get(i);
            String expr = item.getExpression();

            if ((expr != null) && (expr.trim().length() > 0)) {
                result[i][0] = script.parse(expr);
            }

            String as = item.getAs();
            SimpleNode asNode = script.parse(as);
            result[i][1] = asNode;

            if (isConstant(asNode)) {
                result[i][2] = getConstantValue(asNode);
            } else {
                result[i][2] = NULL;
            }
        }

        return result;
    }

    boolean isConstant(SimpleNode node) {
        return node instanceof BSHPrimaryExpression && ((BSHPrimaryExpression) node).isConstant();
    }

    Object getConstantValue(SimpleNode node) {
        return ((BSHPrimaryExpression) node).getConstantValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
   
}
