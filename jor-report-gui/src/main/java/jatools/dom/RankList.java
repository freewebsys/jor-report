package jatools.dom;

import jatools.accessor.ProtectPublic;

import java.util.Arrays;
import java.util.HashMap;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RankList extends HashMap implements ProtectPublic{
    static final Object NULL = new Object();

    RankList(Object[] list, boolean asc) {
        build(list, asc);
    }

    private void build(Object[] list, boolean asc) {
       
        Arrays.sort(list);

        int rank = 1;

        if (asc) {
            Object last = list[0];
            put(last, rank);

            for (int i = 1; i < list.length ; i++) {
                Object obj = list[i];
                rank++;

                if (!equals(last, obj)) {
                    put(obj, rank);
                    last = obj;
                }
            }
        } else {
            Object last = list[list.length  - 1];
            put(last, rank);

            for (int i = list.length - 2; i > -1; i--) {
                Object obj = list[i];
                rank++;

                if (!equals(last, obj)) {
                    put(obj, rank);
                    last = obj;
                }
            }
        }
    }

    private void put(Object val, int rank) {
        if (val == null) {
            val = NULL;
        }

        put(val, new Integer(rank));
    }

    boolean equals(Object o1, Object o2) {
        return (o1 == null) ? (o2 == null) : o1.equals(o2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object indexOf(Object val) {
        if (val == null) {
            val = NULL;
        }

        return  (Integer) this.get(val);

   
    }
}
