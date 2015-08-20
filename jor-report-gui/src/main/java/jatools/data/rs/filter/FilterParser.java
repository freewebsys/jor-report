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
 * QueryParser.java
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

import jatools.designer.App;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;

public class FilterParser { // QueryParser

	static final protected String AND = "and";

//	static final protected String AS = "as";
//
//	// static final protected String ASC = "asc";
//	static final protected String AVG = "avg";
//
//	// static final protected String BY = "by";
//	static final protected String COUNT = "count";

	// static final protected String DESC = "desc";
	// static final protected String DISTINCT = "distinct";
	static final protected String FALSE = "false";

	// static final protected String FROM = "from";
//	static final protected String IN = "in";
//
//	static final protected String LIKE = "like";

	// static final protected String LIMIT = "limit";
//	static final protected String MAX = "max";
//
//	static final protected String MIN = "min";
//
//	static final protected String NOT = "not";
//
//	static final protected String NULL = "null";

	static final protected String OR = "or";

	// static final protected String ORDER = "order";
	// static final protected String SELECT = "select";
	// static final protected String STRICT = "strict";
//	static final protected String SUM = "sum";

	static final protected String TRUE = "true";

	// static final protected String WHERE = "where";



	public static void parse(Filter filter,String queryString) throws IOException {
		StreamKeywordTokenizer tokens = new StreamKeywordTokenizer(
				new StringReader(queryString));

		tokens.resetSyntax();
		tokens.commentChar('#');
		tokens.eolIsSignificant(false);
		tokens.lowerCaseMode(false);
		tokens.parseNumbers();
		tokens.quoteChar('\''); // pour les chaines de caratere
		tokens.quoteCharVariable('\"'); // pour les fields
		tokens.slashSlashComments(true);
		tokens.slashStarComments(true);
		tokens.whitespaceChars(0, ' ');
		tokens.wordChars('_', '_');
		tokens.wordChars('A', 'Z');
		tokens.wordChars('a', 'z');
		tokens.wordChars(':', ':');
		tokens.wordChars('$', '$');

		tokens.lowerCaseKeyword(true);
		tokens.addKeyword(AND);
		// tokens.addKeyword(AS);
		// tokens.addKeyword(ASC);
//		tokens.addKeyword(AVG);
//		// tokens.addKeyword(BY);
//		tokens.addKeyword(COUNT);
		// tokens.addKeyword(DESC);
		// tokens.addKeyword(DISTINCT);
		tokens.addKeyword(FALSE);
		// tokens.addKeyword(FROM);
//		tokens.addKeyword(IN);
//		tokens.addKeyword(LIKE);
		// tokens.addKeyword(LIMIT);
//		tokens.addKeyword(MAX);
//		tokens.addKeyword(MIN);
//		tokens.addKeyword(NOT);
//		tokens.addKeyword(NULL);
		tokens.addKeyword(OR);
		// tokens.addKeyword(ORDER);
		// tokens.addKeyword(SELECT);
		// tokens.addKeyword(STRICT);
//		tokens.addKeyword(SUM);
		tokens.addKeyword(TRUE);
		// tokens.addKeyword(WHERE);

		// int type;
		// while ((type = tokens.nextToken()) != tokens.TT_EOF) {
		// System.out.println(type + " - " + tokens.sval);
		// }
		// return null;
		//
		parseWhere(tokens,filter);
	}

	
	// ////////////////////////////////////////////////////////////////////
	//
	// Select
	//
	// ////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////
	//
	// From
	//
	// ////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////
	//
	// Where
	//
	// ////////////////////////////////////////////////////////////////////

	protected static void parseWhere(StreamKeywordTokenizer tokens, Filter filter) throws IOException {
		filter.setConstraint(parseWhereOr(tokens,filter) );
		
		// return result;
	}

	protected static Constraint parseWhereOr(StreamKeywordTokenizer tokens, Filter filter)
			throws IOException {
		Constraint result = null;

		result = parseWhereAnd(tokens,filter);
		while (tokens.nextToken() == tokens.TT_KEYWORD
				&& OR.equals(tokens.sval)) {
			Constraint c2 = parseWhereAnd(tokens,filter);
			Constraint tmp = new Constraint();
			tmp.setOperator("or");
			tmp.setLeftOperand(result);
			tmp.setRightOperand(c2);
			result = tmp;
		}
		tokens.pushBack();
		return result;
	}

	protected static Constraint parseWhereAnd(StreamKeywordTokenizer tokens, Filter filter)
			throws IOException {
		Constraint result = null;

		result = parseWhereNot(tokens,filter);
		while (tokens.nextToken() == tokens.TT_KEYWORD
				&& AND.equals(tokens.sval)) {
			Constraint c2 = parseWhereNot(tokens,filter);
			Constraint tmp = new Constraint();
			tmp.setOperator("and");
			tmp.setLeftOperand(result);
			tmp.setRightOperand(c2);
			result = tmp;
		}
		tokens.pushBack();
		return result;
	}

	protected static Constraint parseWhereNot(StreamKeywordTokenizer tokens, Filter filter)
			throws IOException {
		Constraint result = null;
//
//		if (tokens.nextToken() == tokens.TT_KEYWORD && NOT.equals(tokens.sval)) {
//			Constraint c1 = parseWhereConstraint(tokens,filter);
//			result = new Constraint();
//			result.setOperator("not");
//			result.setRightOperand(c1);
//		} else {
			//tokens.pushBack();
			result = parseWhereConstraint(tokens,filter);
//		}
		return result;
	}

	protected static Constraint parseWhereConstraint(StreamKeywordTokenizer tokens, Filter filter)
			throws IOException {
		Constraint result = null;

		tokens.nextToken();
		if (tokens.ttype == '(') {
			result = parseWhereOr(tokens,filter);
			tokens.nextToken();
			if (tokens.ttype != ')') {
				throw FilterParserException.create(App.messages.getString("res.614"), tokens
						);
			}
		} else {
			tokens.pushBack();
			Object c1 = parseWhereValue(tokens,filter);
			String op = parseWhereOperator(tokens,filter);
			Object c2 = parseWhereValue(tokens,filter);

			result = new Constraint();
			result.setOperator(op);
			result.setLeftOperand(c1);
			result.setRightOperand(c2);
		}

		return result;
	}

	protected static String parseWhereOperator(StreamKeywordTokenizer tokens, Filter filter)
			throws IOException {
		String result = "";

		tokens.nextToken();
		if (tokens.ttype == '=') {
			return result = "=";
		} else if (tokens.ttype == '<') {
			result = "<";
			if (tokens.nextToken() == '=') {
				result += "=";
			} else {
				tokens.pushBack();
			}
			return result;
		} else if (tokens.ttype == '>') {
			result = ">";
			if (tokens.nextToken() == '=') {
				result += "=";
			} else {
				tokens.pushBack();
			}
			return result;
		} else if (tokens.ttype == '!') {
			if (tokens.nextToken() == '=') {
				return result = "!=";
			}
		} else if (tokens.ttype == tokens.TT_KEYWORD) {
//			if (NOT.equals(tokens.sval)) {
//				result = "not ";
//				tokens.nextToken();
//			}
//			if (tokens.ttype == tokens.TT_KEYWORD) {
//				if (LIKE.equals(tokens.sval)) {
//					return result += "like";
//				} else if (IN.equals(tokens.sval)) {
//					return result += "in";
//				}
//			}
		}

		throw FilterParserException.create(App.messages.getString("res.615"), tokens
				);
	}

	protected static Object parseWhereValue(StreamKeywordTokenizer tokens, Filter filter)
			throws IOException {
		tokens.nextToken();
		if (tokens.ttype == tokens.TT_VARIABLE
				|| tokens.ttype == tokens.TT_WORD) {
			return filter.get(tokens.sval);
		} else if (tokens.ttype == tokens.TT_NUMBER) {
			return new Double(tokens.nval);
		} else if (tokens.ttype == '?') {
			return new QMark();
		} else if (tokens.ttype == '\'') {
			return tokens.sval;
		} else if (tokens.ttype == tokens.TT_KEYWORD
				&& TRUE.equals(tokens.sval)) {
			return Boolean.TRUE;
		} else if (tokens.ttype == tokens.TT_KEYWORD
				&& FALSE.equals(tokens.sval)) {
			return Boolean.FALSE;
		} /*else if (tokens.ttype == tokens.TT_KEYWORD
				&& NULL.equals(tokens.sval)) {
			return null;
		} */else if (tokens.ttype == '(') { // for in operator ex: toto in
											// (1,2,3)
			tokens.nextToken();

			tokens.pushBack();
			HashSet list = new HashSet();
			do {
				Object o = parseWhereValue(tokens,filter);
				list.add(o);
				tokens.nextToken();
			} while (tokens.ttype == ',');
			if (tokens.ttype != ')') {
				throw FilterParserException.create(App.messages.getString("res.616"),
						tokens);
			}
			return list;

		}
		// TODO peut etre avoir un keyword DATE pour les dates: Date(31/12/2004)

		throw FilterParserException
				.create(App.messages.getString("res.617"), tokens);
	}

	// ////////////////////////////////////////////////////////////////////
	//
	// Order by
	//
	// ////////////////////////////////////////////////////////////////////

} // QueryParser
