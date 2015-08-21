package jatools.designer.data;


import jatools.data.reader.csv.CsvReader;
import jatools.data.reader.sql.SqlReader;
import jatools.designer.data.jdbc.SqlReaderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SimpleDatasetReaderFactory {
    static {
        registerFactory(SqlReader.TYPE, new SqlReaderFactory());
        registerFactory(CsvReader.TYPE, new CsvReaderFactory());
        //registerFactory(SqlReader.TYPE, new SqlReaderFactory());
    }

    static String[] types = { SqlReader.TYPE ,CsvReader.TYPE};
    static Map factoryCache;

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param factory DOCUMENT ME!
     */
    public static void registerFactory(String type, DatasetReaderFactory factory) {
        if (factoryCache == null) {
            factoryCache = new HashMap();
        }

        factoryCache.put(type, factory);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getTypes() {
        if (factoryCache == null) {
            return new String[0];
        } else {
            ArrayList v = new ArrayList(factoryCache.keySet());

            return (String[]) v.toArray(new String[0]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DatasetReaderFactory getIntance(String type) {
        if (factoryCache == null) {
            return null;
        } else {
            return (DatasetReaderFactory) factoryCache.get(type);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DatasetReaderFactory getIntance(int type) {
        return getIntance(types[type]);
    }
}
