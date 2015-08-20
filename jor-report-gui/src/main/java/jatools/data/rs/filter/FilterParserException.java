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
 * QueryParserException.java
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
import java.io.StreamTokenizer;

public class FilterParserException extends IOException { // QueryParserException

    public FilterParserException(String msg){
        super(msg);
    }

    static public FilterParserException create(String msg, StreamTokenizer tokens){
//        String state = "";
//        if(tokens.ttype == StreamTokenizer.TT_EOF){
//            state = "End of file";
//        }else if(tokens.ttype == StreamTokenizer.TT_NUMBER){
//            state = "find number " + tokens.nval;
//        }else if(tokens.ttype == StreamTokenizer.TT_WORD){
//            state = "find word '"+tokens.sval+"'";
//        }else if(tokens.ttype > 0){
//            state = "find special char '"+(char)tokens.ttype+"'";
//        }

        return new FilterParserException(msg + App.messages.getString("res.618") + tokens + " in " );
    }

} // QueryParserException

