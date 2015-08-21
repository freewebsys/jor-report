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
 * QueryWhereVisitor.java
 *
 * Created: 1 juin 2004
 *
 * @author Benjamin Poussin <poussin@codelutin.com>
 * Copyright Code Lutin
 * @version $Revision: 1.1 $
 *
 * Mise a jour: $Date: 2007/12/25 06:03:13 $
 * par : $Author: admin $
 */

package jatools.data.rs.filter;

public interface QueryWhereVisitor { // QueryWhereVisitor

    public void visitWhere(Filter where);
    public void visitWhereEqual(Constraint constraint, Object op1, Object op2);
    public void visitWhereNotEqual(Constraint constraint, Object op1, Object op2);
    public void visitWhereGreater(Constraint constraint, Object op1, Object op2);
    public void visitWhereSmaller(Constraint constraint, Object op1, Object op2);
    public void visitWhereGreaterOrEqual(Constraint constraint, Object op1, Object op2);
    public void visitWhereSmallerOrEqual(Constraint constraint, Object op1, Object op2);
    public void visitWhereLike(Constraint constraint, Object op1, Object op2);
    public void visitWhereNotLike(Constraint constraint, Object op1, Object op2);
    public void visitWhereIn(Constraint constraint, Object op1, Object op2);
    public void visitWhereNotIn(Constraint constraint, Object op1, Object op2);
    public void visitWhereAnd(Constraint constraint, Object op1, Object op2);
    public void visitWhereOr(Constraint constraint, Object op1, Object op2);
    public void visitWhereNot(Constraint constraint, Object op2);

} // QueryWhereVisitor

