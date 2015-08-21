package jatools.dataset;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CrossRowSet extends RandomRowSet {
    private Map keysCache;
    private int[] cols2;

    /**
     * Creates a new CrossRowSet object.
     *
     * @param data DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param rows DOCUMENT ME!
     */
    public CrossRowSet(Dataset data, Key key, int[] rows, int[] cols2) {
        super(data, key, rows);
        this.cols2 = cols2;
    }

    Iterator keys() {
        validate();

        return this.keysCache.keySet().iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public RowSet locate(Key key) throws DatasetException {
        validate();

        RowSet rs = (RowSet) keysCache.get(key);

        if ((rs == null) && key.hasAll()) {
            int[] tempcols2 = new int[cols2.length];
            Object[] anykey = new Object[cols2.length];
            int t = 0;

            for (int i = 0; i < tempcols2.length; i++) {
                if (key.elements[i] == Key.ANY) {
                    anykey[i] = Key.ANY;
                } else {
                    anykey[i] = Key.UNKNOWN;
                    tempcols2[t] = cols2[i];
                    t++;
                }
            }

            Key ak = new Key(anykey);

            // [1,2,any,3] 检查是否以前已经计算过 [UNKNOWN,UNKONW,ANY,UNKNOWN] 分组
            if ((t > 0) && !this.keysCache.containsKey(ak)) {
                int[] newcols2 = new int[t];
                System.arraycopy(newcols2, 0, tempcols2, 0, t);

                Map newmap = new IndexBuilder(this, newcols2).build();

                for (Object k : newmap.keySet()) {
                    if (k != Dataset.ANY) {
                        Key k2 = (Key) k;
                        Object[] tempkey = new Object[cols2.length];
                        Arrays.fill(tempkey, Key.ANY);

                        int n = 0;

                        for (int i = 0; i < tempkey.length; i++) {
                            if (key.elements[i] != Key.ANY) {
                                tempkey[i] = k2.elements[n];
                                n++;
                            }
                        }

                        Object newrs = newmap.get(k2);

                        k2.elements = tempkey;
                        this.keysCache.put(k2, newrs);
                    }
                }

                // [UNKNOWN,UNKONW,ANY,UNKNOWN] 打上此分组已计算标志
                this.keysCache.put(ak, ak);
            }

            rs = (RowSet) this.keysCache.get(key);
        }

        return rs;
    }

    private void validate() {
        if (keysCache == null) {
            this.keysCache = new IndexBuilder(this, cols2).build();
        }
    }
}
