package jatools.data.reader.sql;


import jatools.dataset.DatasetException;
import jatools.designer.App;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;


public class SqlRowsService {
    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object createObjectFromSQLTypeNoException(int type) {
        try {
            return createObjectFromSQLType(type);
        } catch (DatasetException a_DataSetException) {
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ZDataSetException DOCUMENT ME!
     */
    public static Object createObjectFromSQLType(int type)
                                          throws DatasetException {
        switch (type) {
        case Types.BIT:
            return new Boolean(false);

        case Types.BIGINT:
            return new BigInteger("0"); //

        case Types.DECIMAL:
            return new BigDecimal("0"); //

        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return new String(""); //

        case Types.INTEGER:
            return new Integer(0);

        case Types.SMALLINT:
            return new Short((short) 0);

        case Types.TINYINT:
            return new Byte((byte) 0);

        case Types.DOUBLE:
            return new Double(0);

        case Types.FLOAT:
        case Types.REAL:
            return new Float(0);

        case Types.TIME:
            return new Time(0);

        case Types.TIMESTAMP:
            return new Timestamp(0);

        case Types.DATE:
            return new java.sql.Date(0);

        default:
            throw new DatasetException(App.messages.getString("res.625") + type + App.messages.getString("res.626")); // //$NON-NLS-2$
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isValidSQLType(int type) {
        switch (type) {
        case Types.BIT:
        case Types.BIGINT:
        case Types.DECIMAL:
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.INTEGER:
        case Types.SMALLINT:
        case Types.TINYINT:
        case Types.DOUBLE:
        case Types.FLOAT:
        case Types.REAL:
        case Types.TIME:
        case Types.TIMESTAMP:
        case Types.DATE:
            return true;

        default:
            return false;
        }
    }
}
