package jatools.classtree;

import javax.swing.Icon;

public class LibFileNode extends AbstractNode {

	// private static String[] filterName = new String[]{
	// "commons-beanutils.jar", "db2.jar", "log4j-1.2.8.jar",
	// "MSBASE.JAR", "MSUTIL.JAR", "mysql.jar", "tools.jar",
	// "xercesImpl.jar"
	// };
	private static String[] filterName = new String[0];

	public LibFileNode(String lib) {
		super(lib);
	}

	public void explore() {
		if (!isExplored()) {
			String classPath = System.getProperty("java.class.path");
			String[] jarNames = classPath.split(";");
			for (int i = 1; i < jarNames.length; i++) {
				String string = jarNames[i];
				if (!isEnabled(string))
					continue;
				/*
				 * JarFile jar = null; try { jar = new JarFile(string); } catch
				 * (IOException e) { // TODO 自动生成 catch 块 e.printStackTrace(); }
				 */
				JarFileNode j = new JarFileNode(string);

				add(j);
			}

			explored = true;
		}
	}

	public boolean isEnabled(String jarName) {
		for (int i = 0; i < filterName.length; i++) {
			if (jarName.endsWith(filterName[i]))
				return false;
		}
		return true;
	}

	public Icon getIcon() {

		return LIB_ICON;
	}
}
