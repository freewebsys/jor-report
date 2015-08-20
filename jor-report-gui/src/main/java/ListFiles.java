
import java.io.*;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListFiles {

	static Vector resources = new Vector();

	static String findDir = "D:\\workspace\\jor\\src\\";

	static int num = 0;

	static String result = "";
	
	static String note = "";

	public static void main(String[] args) throws Exception {
		try {
			//			File saveFile = new File("d:\\zh.txt");
			System.out.println("start parseString");
			
			File saveFile = new File("d:\\res.txt");
			PrintWriter fout = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(saveFile), "GBK"));

			ListFiles lf = new ListFiles();
			lf.listFile("D:\\workspace\\jor\\src\\");
			for (int i = 0; i < resources.size(); i++) {
				fout.println("res." + i + "=" + resources.get(i));
			}
			fout.close();
			
			System.out.println("successfull parseString");

		} catch (ArrayIndexOutOfBoundsException ea) {
			System.out.println("Usage: ListFiles &lt;source dir&gt; &lt;target file&gt;");
		} catch (IOException e) {
			System.out.println("IO error!\r\n" + e.toString());
		}

	}

	private String substring(String str, String s) {
		return str.substring(str.lastIndexOf(s) + 1, str.length());
	}

	public void parseString(String line) {
		if (isGBK(line)) {
			//			System.out.println(line);

			int start = line.indexOf("\"");
			int len = 0;
			if (start > -1) {
				for (int i = start + 1; i < line.length(); i++) {
					if ((i + 2 < line.length()) && line.substring(i, i + 2).equals("\\\"")) {
						len += 2;
						i++;

					} else if (line.charAt(i) != '\"') {
						len++;
					} else {

						addResource(line, start, start + len + 1);

						replace(line, start, start + len + 1);

						String next = line.substring(start + len + 2);
						parseString(next);
						break;

					}
				}
			}
		}
	}

	public void replace(String str, int start, int end) {

		String s = str.substring(start + 1, end).trim();

		int index = -1;

		index = resources.indexOf(s);

		if(isGBK(s)){
			result += str.substring(0,start) + "App.messages.getString(\"res."+index+"\")";
			}
			else{
				result +=str.substring(0,start) + "\"" +s + "\"";
			}
		
		if (!isGBK(str.substring(end))) {
			result += str.substring(end + 1);
		}

	}

	public void addResource(String str, int start, int end) {
		str = str.substring(start + 1, end);
		if (isGBK(str) && !resources.contains(str.trim())) {
			resources.add(str.trim());

			System.out.println("res." + num + "=" + str.trim());
			num++;

		}
	}

	private static boolean isGBK(String str) {
		for (int i = 0; i < str.length(); i++) {
			if(str.charAt(i)>128){
				if(str.indexOf("//") > -1){
					int in = str.indexOf("//");
					String s = str.substring(0,in);
					if(!s.equals("")){
						return true;
					}
					return false;
				}
				else if(str.indexOf("*") > -1){
					return false;
				}
				return true;
			}
				
		}
		return false;
	}

	private void createFile(String line, PrintWriter out) {
		if (isGBK(line)) {
			if(!note.equals("")){
				result += note;
			}
			out.println(result);
		} else {
			out.println(line);
		}
	}

	public void listFile(String path) throws Exception {
		File file = new File(path);
		File list[] = file.listFiles();

		for (int i = 0; i < list.length; i++) {
			try {
				if (list[i].isDirectory()) {
					new ListFiles().listFile(list[i].toString());
				} else {

					String filter = substring(list[i].getAbsolutePath().toString(), ".");
					String hzutil = findDir + "jatools\\util\\HZUtil.java";

					if (filter.equals("java")
							&& (!hzutil.equals(list[i].getAbsolutePath().toString()))) {

						System.out.println(list[i].getAbsolutePath());

						File srcfile = new File(list[i].getAbsolutePath());

						BufferedReader fin = new BufferedReader(new InputStreamReader(
								new FileInputStream(srcfile), "GBK"));
						int line_no = 1;

						String line = null;

						//					fout.println(list[i].getAbsolutePath());
						String newfile = "E" + list[i].getAbsolutePath().substring(1);

						PrintWriter out = new PrintWriter(new OutputStreamWriter(
								new FileOutputStream(newfile), "GBK"));

						while ((line = fin.readLine()) != null) {
							
							if(isGBK(line)){
								int start = line.indexOf("//");
								if(start > -1){
									note = line.substring(start, line.length());
									line = line.substring(0,start);
								}
								parseString(line);
							}
							
							

							//							System.out.println(result);

							createFile(line, out);

							result = "";
							note = "";

						}

						fin.close();

						out.close();

					}
				}
			} catch (Exception ex) {
				System.out.println("Access deny:" + list[i].getAbsolutePath());
			}
		}
	}

}
