package jatools.tags;

public class ReportTagUtil {

	public static String getJavaScriptReportId(String reportId) {
		
		return "_jatools_report_"+reportId;
	}

	public static String getNoReturnString(String str) {
    	
    	return str.replaceAll( "\n","").replaceAll("\r","");
    	
	}
	
	public static String getJavaScriptNavigatorId(String reportId)
	{
		return "_"+getJavaScriptReportId(reportId)+"_navigator";
	}

}
