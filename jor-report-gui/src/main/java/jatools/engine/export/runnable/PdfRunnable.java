/*
 * Created on 2004-11-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.engine.export.runnable;

import jatools.core.view.PageView;
import jatools.engine.export.BasicExport;
import jatools.engine.export.pdf.PdfExport1;

import java.util.Iterator;



/**
 * @author   java9
 */
public class PdfRunnable extends ExportRunnable {
	PdfExport1 exp;

	/**
	 * @param exp
	 */
	public PdfRunnable(PdfExport1 exp) {
		this.exp = exp;
	}
	
	
	
	
    /**
     * DOCUMENT ME!
     */
    public void run() {
        
        try {
        	
        	if (!tryCache(view, index)) {
				exp.export(view, index);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }




	/* (non-Javadoc)
	 * @see com.jatools.core.ExportRunnable#clone(com.jatools.core.view.ZPageView, int)
	 */
	ExportRunnable clone(PageView page, int index) {
		PdfRunnable r = new PdfRunnable(exp);
		r.view = page;
		r.index = index;
		return r;
	}





		public void close() throws Exception {
			if(cache != null )
			{
				for (Iterator it = cache.iterator(); it.hasNext();) {
					Runner runner = (Runner) it.next();
					exp.export(runner.view ,runner.index);
					
				}
			}
			
		}




		public BasicExport getExport() {
			// MYDO Auto-generated method stub
			return exp;
		}

}
