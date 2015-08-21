package jatools.data.reader.sql;

import jatools.dataset.RowMeta;


public class SqlRowInfo extends RowMeta {
    public SqlRowInfo(SqlColumnInfo[] columnInfos) {
        super(columnInfos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnType(int i) {
        return ((SqlColumnInfo) getColumnInfo(i)).getType();
    }
}
