package jatools.tags;

import jatools.accessor.ProtectPublic;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;


public class AbstractTEI  extends TagExtraInfo implements ProtectPublic {
	
	VariableInfo createVariableInfo(String var,String klass)
	{
		return new VariableInfo(var,klass,true,VariableInfo.AT_BEGIN );
		
	}
	
	VariableInfo createVariableInfo(String var,String klass,int where)
	{
		return new VariableInfo(var,klass,true,where );
		
	}
}
