package jatools.data.rs.filter;

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
 * StreamKeywordTokenizer.java
 *
 * Created: 27 mai 2004
 *
 * @author Benjamin Poussin <poussin@codelutin.com>
 * Copyright Code Lutin
 * @version $Revision: 1.1 $
 *
 * Mise a jour: $Date: 2007/12/25 06:03:13 $
 * par : $Author: admin $
 */



import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashSet;

public class StreamKeywordTokenizer extends StreamTokenizer { // StreamKeywordTokenizer

    public static final int TT_KEYWORD = -101;
    public static final int TT_VARIABLE = -102;
    protected HashSet keywords = new HashSet();
    protected boolean lowerCaseKeyword = false;
    protected int quoteCharVariable = -1;

    public StreamKeywordTokenizer(Reader in){
        super(in);
        
    }

    public int nextToken() throws IOException {
        super.nextToken();
        if(ttype == TT_WORD){
            String keyword = sval;
            if(lowerCaseKeyword){
                keyword = keyword.toLowerCase();
            }
            if(keywords.contains(keyword)){
                sval = keyword;
                ttype = TT_KEYWORD;
            }
        }else if(ttype == quoteCharVariable){
            ttype = TT_VARIABLE;
        }
        return ttype;
    }

    public void addKeyword(String keyword){
        if(lowerCaseKeyword){
            keywords.add(keyword.toLowerCase());
        }else{
            keywords.add(keyword);
        }
    }

    /**
    * This method must be call before addKeyword.
    */
    public void lowerCaseKeyword(boolean fl){
        lowerCaseKeyword = fl;
    }

    public void quoteCharVariable(int c){
        quoteChar(c);
        this.quoteCharVariable = c;
    }

} // StreamKeywordTokenizer

