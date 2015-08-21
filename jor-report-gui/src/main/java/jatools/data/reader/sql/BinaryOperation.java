package jatools.data.reader.sql;

import bsh.EvalError;
import bsh.ParserConstants;
import bsh.Primitive;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BinaryOperation implements ParserConstants {
    /**
     * DOCUMENT ME!
     *
     * @param callstack DOCUMENT ME!
     * @param interpreter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws EvalError DOCUMENT ME!
     */
    static Object eval(Object lhs, Object rhs, int kind) {
        return Primitive.unwrap(_eval(lhs, rhs, kind));
    }

    static Object _eval(Object lhs, Object rhs, int kind) {
        try {
            boolean isLhsWrapper = isWrapper(lhs);
            boolean isRhsWrapper = isWrapper(rhs);

            if (isLhsWrapper && isRhsWrapper) {
                //                if ((isLhsWrapper && isRhsWrapper && (kind == EQ))) {
                //                    // 延时
                //                } else {
                return Primitive.binaryOperation(lhs, rhs, kind);

                //                }
            }

            switch (kind) {
            case EQ:

                if (lhs instanceof String || lhs instanceof Date) {
                    return (lhs.equals(rhs));
                } else {
                    return (lhs == rhs);
                }

            case NE:

                if (lhs instanceof String || lhs instanceof Date) {
                    return !lhs.equals(rhs);
                } else {
                    return lhs != rhs;
                }

            case PLUS:

                if (lhs instanceof String || rhs instanceof String) {
                    return lhs.toString() + rhs.toString();
                }

            default:
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isWrapper(Object obj) {
        return (obj instanceof Boolean || obj instanceof Character || obj instanceof Number);
    }
}
