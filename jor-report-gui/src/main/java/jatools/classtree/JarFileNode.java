package jatools.classtree;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.Icon;

public class JarFileNode extends AbstractNode {

	/*
	 * public JarFileNode(JarFile jar) { super(jar); }
	 */

	public JarFileNode(String string) {
		super(string);
		
	}

	public void explore() {
		if (!isExplored()) {
			JarFile jar = null;
			try {
				jar = new JarFile((String) getUserObject());
			} catch (IOException e1) {
				// TODO 自动生成 catch 块
				e1.printStackTrace();
			}
			if (jar == null) {
				return;
			}
			Enumeration e = jar.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = (JarEntry) e.nextElement();
				if (entry.getName().endsWith(".class")
						&& entry.getName().indexOf("$") == -1) {
					ClassNode cn = new ClassNode(entry.getName());
				//	cn.explore();
					
					if (!cn.isLeaf())
						add(cn);
				}
			}
			explored = true;
		}
	}

	public Icon getIcon() {
		
		return JAR_ICON;
	}

	public String toString() {
		
		return (String) getUserObject();
//		JarFile jar = null;
//		try {
//			jar = new JarFile((String) getUserObject());
//		} catch (IOException e1) {
//			// TODO 自动生成 catch 块
//			e1.printStackTrace();
//		}
//		if (jar == null) {
//			return null;
//		}
//		int index = jar.getName().lastIndexOf("\\");
//
//		return jar.getName().substring(index + 1, jar.getName().length());
	}
}
