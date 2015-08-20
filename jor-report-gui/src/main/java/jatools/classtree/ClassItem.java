package jatools.classtree;


import java.util.ArrayList;
import java.util.Collections;


public class ClassItem implements Comparable{

	private String className;
	private String fullPath;
	private String jarName;
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
		
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public ClassItem() {
		// TODO Auto-generated constructor stub
	}
	public ClassItem(String className,String fullPath,String jarName) {
		// TODO Auto-generated constructor stub
		this.className=className;
		this.fullPath=fullPath;
		this.jarName=jarName;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ClassItem a=new ClassItem("student","test","mytest.jar");
		ClassItem b=new ClassItem("Sdudent","test","mytest.jar");
		ClassItem c=new ClassItem("soudent","test","mytest.jar");
		ArrayList al=new ArrayList();
		al.add(a);
		al.add(b);
		al.add(c);
		System.out.println("排序前");
		System.out.println(al);
		System.out.println("排序后");
		Collections.sort(al);
		System.out.println(al);
//		ClassItem[] d=new ClassItem[]{a,b,c};
//		System.out.println("排序前");
//		System.out.println(Arrays.asList(d));
//		System.out.println("排序后");
//		Arrays.sort(d); 
//		System.out.println(Arrays.asList(d));
		
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof ClassItem)
		{
			ClassItem compared=(ClassItem)o;
			String name=compared.getClassName();
			int result=this.className.toLowerCase().compareTo(name.toLowerCase());
			return result	;		
		}
		else
		{
			throw new ClassCastException("Can't compare");
		}
		
	}
	public String toString() {
//		return this.getClassName()+"    "+this.getFullPath()+"     "+this.getJarName();
		return this.getFullPath()+" - "+this.getJarName();
	}
}
