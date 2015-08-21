package jatools;

import bsh.Interpreter;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;

import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.Page;

import jatools.data.Formula;

import jatools.dom.src.RootNodeSource;
import jatools.dom.src.xpath.XPath;

import jatools.xml.XmlReader;
import jatools.xml.XmlWriter;

import jatools.xml.serializer.ListenToXmlWrite;
import jatools.xml.serializer.XmlReadListener;
import jatools.xml.serializer.XmlWriteListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision$
 */
public class ReportDocument implements PropertyAccessor, ListenToXmlWrite,
		XmlWriteListener {
	static {
		Interpreter.setXPathProcessor(XPath.getDefaults());
	}

	public final static String EXPORT_FILE_NAME = "export_file_name";
	static Map cachedFile;
	private VariableContext variableContext = new VariableContext(this);
	private String title;
	boolean closeback = false;
	private Page page;
	private RootNodeSource nodeSource;
	private Properties properties;
	private boolean debugOff;

	/**
	 * Creates a new ReportDocument object.
	 */
	public ReportDocument() {
	}

	private void updateComponentVariables() {
		variableContext.removeAll(VariableContext.COMPONENT);

		collectComponentVariables(page, variableContext);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void clearComponentVariables() {
		variableContext.removeAll(VariableContext.COMPONENT);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param pages
	 *            DOCUMENT ME!
	 */
	public void setPages(ArrayList pages) {
		if ((pages != null) && !pages.isEmpty()) {
			this.page = (Page) pages.get(0);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param childName
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Component getComponent(String childName) {
		updateComponentVariables();

		if (childName != null) {
			Object result = variableContext.getVariable(childName);

			if (result instanceof Component) {
				return (Component) result;
			}
		}

		return null;
	}

	private static void collectComponentVariables(Component comp,
			VariableContext vm2) {
		if (comp.getName() != null) {
			vm2.declareVariable(comp.getName(), comp);
		}

		for (int i = 0; i < comp.getChildCount(); i++) {
			collectComponentVariables(comp.getChild(i), vm2);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 * @param value
	 *            DOCUMENT ME!
	 */
	public void declareVariable(String name, Object value) {
		if ((value != null) && (name != null)) {
			if (variableContext != null) {
				variableContext.declareVariable(name, value);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public void undeclareVariable(String name) {
		if (variableContext != null) {
			variableContext.undeclareVariable(name);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Iterator getVariableNames() {
		return variableContext.names();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param varName
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getVariable(String varName) {
		return variableContext.get(varName);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public jatools.data.Parameter[] getParameters() {
		return new jatools.data.Parameter[0];
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Formula[] getFormulae() {
		return new Formula[0];
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public PropertyDescriptor[] getRegistrableProperties() {
		return new PropertyDescriptor[] {
				ComponentConstants._VARIABLE_CONTEXT,

				new PropertyDescriptor("Page", Page.class,
						PropertyDescriptor.SERIALIZABLE),
				new PropertyDescriptor("DebugOff", Boolean.TYPE,
						PropertyDescriptor.SERIALIZABLE),

				new PropertyDescriptor("Pages", Page.class,
						PropertyDescriptor.SERIALIZABLE
								| PropertyDescriptor.READONLY),

				new PropertyDescriptor("NodeSource", RootNodeSource.class,
						PropertyDescriptor.SERIALIZABLE),
				ComponentConstants._TITLE, ComponentConstants._PROPERTIES };
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public VariableContext getVariableContext() {
		return variableContext;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param variables
	 *            DOCUMENT ME!
	 */
	public void setVariableContext(VariableContext variables) {
		this.variableContext = variables;

		if (variableContext != null) {
			variableContext.setDocument(this);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param file
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public static ReportDocument load(File file) throws Exception {
		InputStream is = new FileInputStream(file);
		ReportDocument doc = load(is);

		cacheFile(doc, file);

		return doc;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param is
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public static ReportDocument load(InputStream is) throws Exception {
		ReportDocument doc = (ReportDocument) XmlReader.read(is);
		doc.updateComponentVariables();
		is.close();

		return doc;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param doc
	 *            DOCUMENT ME!
	 * @param file
	 *            DOCUMENT ME!
	 * @param ctrlDown
	 *            DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public static void save(ReportDocument doc, File file, boolean ctrlDown)
			throws Exception {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();

			save(doc, bao);

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bao.toByteArray());
			fos.close();
			cacheFile(doc, file);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param doc
	 *            DOCUMENT ME!
	 * @param os
	 *            DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public static void save(ReportDocument doc, OutputStream os)
			throws Exception {
		XmlWriter.write(doc, os);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param doc
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static File getCachedFile(ReportDocument doc) {
		return (File) ((cachedFile != null) ? cachedFile.get(doc) : null);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param doc
	 *            DOCUMENT ME!
	 * @param f
	 *            DOCUMENT ME!
	 */
	public static void cacheFile(ReportDocument doc, File f) {
		if (cachedFile == null) {
			cachedFile = new HashMap();
		}

		cachedFile.put(doc, f);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param title
	 *            DOCUMENT ME!
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public XmlWriteListener getSaveListener() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public XmlReadListener getLoadListener() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param panel
	 *            DOCUMENT ME!
	 */
	public void setPage(Page panel) {
		this.page = panel;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Page getPage() {
		return this.page;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param rootNodeSource
	 *            DOCUMENT ME!
	 */
	public void setNodeSource(RootNodeSource rootNodeSource) {
		this.nodeSource = rootNodeSource;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public RootNodeSource getNodeSource() {
		return nodeSource;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public XmlWriteListener getXmlWriteListener() {
		return this;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void afterWrite() {
		updateComponentVariables();
	}

	/**
	 * DOCUMENT ME!
	 */
	public void beforeWrite() {
		clearComponentVariables();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param prop
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isWritable(String prop) {
		return true;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void validate() {
		if (this.page != null) {
			page.validate();
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param properties
	 *            DOCUMENT ME!
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * 设置统计图某一属性
	 * 
	 * @param prop
	 *            DOCUMENT ME!
	 * @param val
	 *            DOCUMENT ME!
	 */
	public void setProperty(String prop, String val) {
		if (properties == null) {
			this.properties = new Properties();
		}

		if (val == null) {
			this.properties.remove(prop);
		} else

			properties.setProperty(prop, val);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param prop
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getProperty(String prop) {
		if (properties != null) {
			return this.properties.getProperty(prop);
		} else {
			return null;
		}
	}

	public boolean isDebugOff() {
		return debugOff;
	}

	public void setDebugOff(boolean debugOff) {
		this.debugOff = debugOff;
	}
}
