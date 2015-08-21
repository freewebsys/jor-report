package jatools.dataset;

import jatools.accessor.PropertyDescriptor;
import jatools.data.reader.AbstractDatasetReader;
import jatools.designer.App;
import jatools.engine.script.Script;

import java.util.ArrayList;



public class SimpleReader extends AbstractDatasetReader {
    String[] columns;
    ArrayList data = new ArrayList();
    int currentRow;
    private Class[] classes;

    public SimpleReader(String[] cols) {
        this.columns = cols;
    }

    public SimpleReader(String[] cols, Class[] classes) {
        this(cols);
        this.classes = classes;
    }

    public void add(Object[] val) {
        data.add(val);
    }

    public Object clone() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jatools.data.rs.ZAbstractRowsReader#getType()
     */
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jatools.data.rs.ZRowsReader#readStart(com.jatools.data.rs.ZRowInfo,
     *      com.jatools.core.ZDataProvider)
     */
    public RowMeta readStart( Script paraProvider,boolean withdata)
        throws DatasetException {
        currentRow = 0;

        try {
            Column[] columnInfos = new Column[columns.length];

            for (int i = 0; i < columnInfos.length; i++) {
                Class klass = (classes != null) ? classes[i] : Object.class;
                columnInfos[i] = new Column(columns[i], klass);
            }

            return new RowMeta(columnInfos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new jatools.dataset.DatasetException(App.messages.getString("res.611"), e); //
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jatools.data.rs.ZRowsReader#readRow(com.jatools.data.rs.ZRowInfo)
     */
    public Row readRow(RowMeta rowInfo) throws DatasetException {
        try {
            if (rowInfo != null) {
                if (currentRow < data.size()) {
                    Object[] values;
                    values = new Object[rowInfo.getColumnCount()];

                    for (int i = 0; i < values.length; i++) {
                        values[i] = ((Object[]) data.get(currentRow))[i];
                    }

                    currentRow++;

                    return new Row(values);
                } else {
                    // System.out.println("over!");
                    return Row.NO_MORE_ROWS;
                }
            } else {
                throw new jatools.dataset.DatasetException(App.messages.getString("res.612")); //
            }
        } catch (Exception e) {
            throw new jatools.dataset.DatasetException(App.messages.getString("res.613"), //
                e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jatools.data.rs.ZRowsReader#readEnd()
     */
    public int readEnd() throws DatasetException {
        return data.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jatools.core.accessor.ZPropertyAccessor#getRegistrableProperties()
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        // TODO Auto-generated method stub
        return null;
    }

	public Dataset load(Script script) {
		// TODO Auto-generated method stub
		return null;
	}

	public RowMeta getRowMeta(Script script) {
		// TODO Auto-generated method stub
		return null;
	}
}
