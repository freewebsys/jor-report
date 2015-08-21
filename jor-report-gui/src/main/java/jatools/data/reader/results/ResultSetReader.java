/*
 * Created on 2005-4-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.data.reader.results;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.data.reader.AbstractDatasetReader;
import jatools.dataset.Column;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.Row;
import jatools.dataset.RowMeta;
import jatools.designer.App;
import jatools.engine.script.Script;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResultSetReader extends AbstractDatasetReader {
   
    static Map resultsCache = new HashMap();
    ResultSet results;
    int rowCount;

    public ResultSetReader(){}
    
    public ResultSetReader(ResultSet results) {
        this.results = results;
    }

    public Object clone() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jatools.data.rs.ZAbstractRowsReader#getType()
     */
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jatools.data.rs.ZRowsReader#readStart(com.jatools.data.rs.ZRowInfo, com.jatools.core.ZDataProvider)
     */
    public RowMeta readStart( Script paraProvider,boolean withdata)
        throws DatasetException {
        // currentRow = 0;
        try {
            ResultSetMetaData md = results.getMetaData();

            Column[] columnInfos = new Column[md.getColumnCount()];

            for (int i = 0; i < columnInfos.length; i++) {
                columnInfos[i] = new Column((String) md.getColumnName(i + 1),
                        md.getColumnClassName(i + 1));
            }
            
      //      results.beforeFirst() ;
            

            return new RowMeta(columnInfos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new jatools.dataset.DatasetException(App.messages.getString("res.627"), e); //
        }
    }

    /* (non-Javadoc)
     * @see com.jatools.data.rs.ZRowsReader#readRow(com.jatools.data.rs.ZRowInfo)
     */
    public Row readRow(RowMeta rowInfo) throws DatasetException {
        try {
            if (rowInfo != null) {
                if (results.next()) {
                    Object[] values;
                    values = new Object[rowInfo.getColumnCount()];

                    for (int i = 1; i <= values.length; i++) {
                        values[i - 1] = results.getObject(i);
                    }

                    rowCount++;

                    return new Row(values);
                } else {
                    //System.out.println("over!");
                    return Row.NO_MORE_ROWS;
                }
            } else {
                throw new jatools.dataset.DatasetException(App.messages.getString("res.628")); //
            }
        } catch (SQLException e) {
            throw new jatools.dataset.DatasetException(App.messages.getString("res.629"), //
                e);
        }
    }

    /* (non-Javadoc)
     * @see com.jatools.data.rs.ZRowsReader#readEnd()
     */
    public int readEnd() throws DatasetException {
        // TODO Auto-generated method stub
        return rowCount;
    }

    public String getName() {
        if (name == null) {
            name = "results" + (resultsCache.size() + 1);
            if(results != null)
            	resultsCache.put( name,results);
            
        }

        return name;
    }

    public void setName(String string) {
    	
        super.setName(string);
        results = (ResultSet) resultsCache.get( name);
    }

    /* (non-Javadoc)
    * @see com.jatools.core.accessor.ZPropertyAccessor#getRegistrableProperties()
    */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] { ComponentConstants._NAME };
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
