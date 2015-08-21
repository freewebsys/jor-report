package jatools.dataset;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FilterSet {
    private ArrayList filters;



    /**
     * Creates a new FilterSet object.
     */
    public FilterSet() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param strVals DOCUMENT ME!
     */
    public void select(String field, String[] strVals,boolean select) {
        if (filters == null) {
            filters = new ArrayList();

            filters.add(new DatasetFilter(field, strVals,select));
        }
    }
    
  
    
    public void initDataset(Dataset ds)
    {
    	 for (int j = 0; j < this.filters.size(); j++) {
             DatasetFilter filter = (DatasetFilter) filters.get(j);

            filter.initDataset(ds);
         }
    }

    /**
     * DOCUMENT ME!
     */
    public void doFilter(Dataset data) {
    	
    	
        if ((this.filters != null) && !this.filters.isEmpty()) {
        	initDataset(data);
        	
        	
            for (int i = data.getRowCount() - 1; i > -1; i--) {
                Row r = data.getReferenceToRow(i);

                for (int j = 0; j < this.filters.size(); j++) {
                    DatasetFilter filter = (DatasetFilter) filters.get(j);

                    if (!filter.accept(r)) {
                        data.removeRow(i);

                        break;
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(Row row) {
        if ((this.filters != null) && !this.filters.isEmpty()) {
            for (int j = 0; j < this.filters.size(); j++) {
                DatasetFilter filter = (DatasetFilter) filters.get(j);

                if (!filter.accept(row)) {
                    return false;
                }
            }
        }

        return true;
    }
}
