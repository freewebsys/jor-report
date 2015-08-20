/*
 * Created on 2004-7-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.engine.export;

import jatools.core.view.View;

/**
 * @author java
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TableCell {

	/**
	 *  
	 */
	public View element = null;
	public int width = 0;
	public int height = 0;
	public int colSpan = 0;
	public int rowSpan = 0;

	/**
	 *  
	 */
	public TableCell(View element, int width, int height, int colSpan, int rowSpan) {
		this.element = element;
		this.width = width;
		this.height = height;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;

	}
}
