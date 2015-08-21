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
 * Constraint.java
 *
 * Created: 26 mai 2004
 *
 * @author Benjamin Poussin <poussin@codelutin.com>
 * Copyright Code Lutin
 * @version $Revision: 1.2 $
 *
 * Mise a jour: $Date: 2008/05/27 06:11:41 $
 * par : $Author: admin $
 */

package jatools.data.rs.filter;


/**
 * Pour le not, le leftOperand est null.
 */
public class Constraint { // Constraint

	protected String operator = null;

	protected Object left = null;

	protected Object right = null;

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setLeftOperand(Object leftOperand) {
		this.left = leftOperand;
	}

	public void setRightOperand(Object rightOperand) {
		this.right = rightOperand;
	}

	public Object getLeftOperand() {
		return left;
	}

	public Object getRightOperand() {
		return right;
	}

	public boolean isEqual() {
		return "=".equalsIgnoreCase(operator);
	}

	public boolean isNotEqual() {
		return "!=".equalsIgnoreCase(operator)
				|| "<>".equalsIgnoreCase(operator);
	}

	public boolean isSmaller() {
		return "<".equalsIgnoreCase(operator);
	}

	public boolean isGreater() {
		return ">".equalsIgnoreCase(operator);
	}

	public boolean isSmallerOrEqual() {
		return "<=".equalsIgnoreCase(operator);
	}

	public boolean isGreaterOrEqual() {
		return ">=".equalsIgnoreCase(operator);
	}

	// public boolean isLike(){
	// return "like".equalsIgnoreCase(operator);
	// }
	// public boolean isNotLike(){
	// return "not like".equalsIgnoreCase(operator);
	// }
	// public boolean isIn(){
	// return "in".equalsIgnoreCase(operator);
	// }
	// public boolean isNotIn(){
	// return "not in".equalsIgnoreCase(operator);
	// }
	public boolean isAnd() {
		return "and".equalsIgnoreCase(operator);
	}

	public boolean isOr() {
		return "or".equalsIgnoreCase(operator);
	}

	public boolean isNot() {
		return "not".equalsIgnoreCase(operator);
	}

	public String toString() {
		try {
//			System.out.println("fadsfasdf");
			return "(" + toStringOperand(getLeftOperand()) + " "
					+ operator.toUpperCase() + " "
					+ toStringOperand(getRightOperand()) + ")";
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected String toStringOperand(Object operand) {
		if (operand == null) {
			if (isNot()) {
				return "";
			} else {
				return "NULL";
			}
		}

		if (operand instanceof String) {
			return "\"" + operand + "\"";
		}
		return operand.toString();
	}

	public void visit(QueryWhereVisitor visitor) {
		if (isEqual()) {
			visitor.visitWhereEqual(this, getLeftOperand(), getRightOperand());
		} else if (isNotEqual()) {
			visitor.visitWhereNotEqual(this, getLeftOperand(),
					getRightOperand());
		} else if (isGreater()) {
			visitor
					.visitWhereGreater(this, getLeftOperand(),
							getRightOperand());
		} else if (isSmaller()) {
			visitor
					.visitWhereSmaller(this, getLeftOperand(),
							getRightOperand());
		} else if (isGreaterOrEqual()) {
			visitor.visitWhereGreaterOrEqual(this, getLeftOperand(),
					getRightOperand());
		} else if (isSmallerOrEqual()) {
			visitor.visitWhereSmallerOrEqual(this, getLeftOperand(),
					getRightOperand());
		}/*
			 * else if(isLike()){ visitor.visitWhereLike( this,
			 * getLeftOperand(), getRightOperand()); }else if(isNotLike()){
			 * visitor.visitWhereNotLike( this, getLeftOperand(),
			 * getRightOperand()); }else if(isIn()){ visitor.visitWhereIn( this,
			 * getLeftOperand(), getRightOperand()); }else if(isNotIn()){
			 * visitor.visitWhereNotIn( this, getLeftOperand(),
			 * getRightOperand()); }
			 */else if (isAnd()) {
			visitor.visitWhereAnd(this, getLeftOperand(), getRightOperand());
		} else if (isOr()) {
			visitor.visitWhereOr(this, getLeftOperand(), getRightOperand());
		}/*
			 * else if(isNot()){ visitor.visitWhereNot(this, getRightOperand()); }
			 */
	}

	public boolean yes() {

		Object _left = getLeft();
		Object _right = getRight();

		if (isEqual()) {
			return equals(_left, _right);

		} else if (isNotEqual()) {
			return !equals(_left, _right);
		} else if (isGreater()) {
			return compare(_left, _right) == 1;

		} else if (isSmaller()) {
			return compare(_left, _right) == -1;
		} else if (isGreaterOrEqual()) {
			return compare(_left, _right) != -1;
		} else if (isSmallerOrEqual()) {
			return compare(_left, _right) != 1;
		} else if (isAnd()) {
			return ((Boolean) _left).booleanValue()
					&& ((Boolean) _right).booleanValue();
		} else if (isOr()) {
			return ((Boolean) _left).booleanValue()
					|| ((Boolean) _right).booleanValue();
		}/*
			 * else if(isNot()){ visitor.visitWhereNot(this,
			 * get____rightOperand()); }
			 */

		return false;
	}

	public boolean equals(Object left, Object right) {

		return left == null ? right == null : left.equals(right);
	}

	public int compare(Object left, Object right) {
		if (left instanceof Comparable) {
			return ((Comparable) left).compareTo(right);
		} else
			return -2;
	}

	public Object getLeft() {
		return valueOf(left);
	}

	public Object getRight() {

		return valueOf(right);
	}

	static Object valueOf(Object value) {
//		if (value instanceof Constraint) {
//			return Boolean.valueOf(((Constraint) value).yes());
//		} else if (value instanceof ZField) {
//			value = ((ZField) value).getValue();
//			
//		}
		if (value instanceof Number) {
			return new Double(((Number) value).doubleValue());
		}
		return value;
	}

} // Constraint

