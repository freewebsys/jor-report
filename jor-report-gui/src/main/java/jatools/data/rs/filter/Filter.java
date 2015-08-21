/* *##%
 * Copyright (C) 2002, 2003 Code Lutin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *##%*/

/* *
 * Where.java
 *
 * Created: 26 mai 2004
 *
 * @author Benjamin Poussin <poussin@codelutin.com>
 * Copyright Code Lutin
 * @version $Revision: 1.1 $
 *
 * Mise a jour: $Date: 2007/12/25 06:03:13 $
 * par : $Author: admin $
 */

package jatools.data.rs.filter;

import jatools.engine.script.Script;

import java.io.IOException;
import java.util.Map;


public class Filter { // Where

	protected Constraint constraint;

	private Map fields;

	private Script data;

	public Filter(Map fields, Script data,String filter) throws IOException {
		this.fields = fields;
		this.data = data;
		FilterParser.parse( this,filter);
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public void visit(QueryWhereVisitor visitor) {
		visitor.visitWhere(this);
		constraint.visit(visitor);
	}

	public boolean isTrue() {
		return constraint.yes();
	}

	public Object get(String sval) {
		if (sval.startsWith(":")) {
			return data.get(sval.substring(1));

		} else
			return fields.get(sval);

	}
}
