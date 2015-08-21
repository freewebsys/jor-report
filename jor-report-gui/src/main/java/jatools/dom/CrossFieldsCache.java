package jatools.dom;

import bsh.Primitive;

import jatools.dataset.CrossIndexField;
import jatools.dataset.CrossIndexView;

import jatools.engine.script.KeyStack;

import java.util.HashMap;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CrossFieldsCache {
    private HashMap<String, Object> fieldsCache = new HashMap<String, Object>();
    private DatasetBasedNode base;
    private CrossIndexView indexView;

    /**
         * @param base
         * @param indexView
         */
    public CrossFieldsCache(DatasetBasedNode base, CrossIndexView indexView) {
        this.base = base;
        this.indexView = indexView;
    }

    /**
    * DOCUMENT ME!
    *
    * @param prop DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Object getProperty(String prop) {
        Object result = this.fieldsCache.get(prop);

        if (result == null) {
            int col = base.getDataset().getColumnIndex(prop);

            if (col > -1) {
                KeyStack key = base.getRoot().getScript().getKeyStack(0);
                KeyStack key2 = base.getRoot().getScript().getKeyStack(1);

                result = new CrossIndexField(this.indexView, col, key, key2);
            } else {
                result = Primitive.VOID;
            }

            this.fieldsCache.put(prop, result);
        }

        return result;
    }
}
