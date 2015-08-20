package jatools.data.reader.sql;

import jatools.dataset.Column;


public class SqlColumnInfo extends Column {
    private int type;

    public SqlColumnInfo(String name,
                          String className,
                          int type) throws ClassNotFoundException {
        super(name, className);
        this.type = type;
    }

    public SqlColumnInfo(String name,
                          Class cls,
                          int type) {
        super(name, cls);
        this.type = type;
    }

    
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }
}
