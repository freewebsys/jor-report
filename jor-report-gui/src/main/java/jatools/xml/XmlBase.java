package jatools.xml;

import jatools.accessor.ProtectPublic;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;


/*
 *
 <?xml version="1.0"?>
 <Field CLASS="Player"
 <Field NAME="Number"
 <Field NAME="HighSchool">Eaton</Field>
 <Field NAME="Name" CLASS="PersonName">           OBJECT_FIELD.LOAD
 <Field NAME="First">Jonas</Field>   SIMPLE_FIELD_PRINT
 <Field NAME="Last">Grumby</Field>
 </Field>
 <Field NAME="Children" ITEM_CLASS="java.util.Integer">
 <Field NAME="Item1">1997</Field>
 <Field NAME="Item2">69</Field>
 <Field NAME="Item3">31</Field>
 <Field NAME="Item4">30</Field>
 <Field NAME="Item5">2</Field>
 <Field NAME="Item6">15</Field>
 </Fields>
 <Field NAME="Shapes" >
 <Field NAME="Item1" Class="java.util.Line2D" >

 <>

 <Field NAME="Item2">69</Field>
 <Field NAME="Item3">31</Field>
 </Fields>

 </Object>
 </Field>
 <Field NAME="Stats">
 <Object CLASS="Statistics">
 <Fields>
 <Field NAME="Year">1997</Field>
 <Field NAME="AtBats">69</Field>
 <Field NAME="Runs">31</Field>
 <Field NAME="Hits">30</Field>
 <Field NAME="HomeRuns">2</Field>
 <Field NAME="RunsBattedIn">15</Field>
 </Fields>
 </Object>
 </Field>
 <Field NAME="Border"
 <Object CLASS="ZBorder"
 <Fields>
 <Field NAME="x"
 <Field NAME="y"
 <Field NAME="width">31</Field>
 <Field NAME="height">30</Field>
 <Field NAME="color"

 </Fields>
 </Object>
 </Field>
 </Fields>
 </Object>

 */

/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class XmlBase implements ProtectPublic {
	public static final String REPORT_TITLE = "jatools report template"; //
	static Map descriptorsCache = new HashMap();
	public static final int OBJECT_NODE = 0;
	public static final int FIELDS_NODE = 1;
	public static final int FIELD_NODE = 2;
	public static final String OBJECT_NODE_TAG = "Object"; //
	public static final String FIELDS_NODE_TAG = "Fields"; //
	public static final String FIELD_NODE_TAG = "Field"; //
	public static final String CLASS_ATTRIBUTE_TAG = "Class"; //
	public static final String NAME_ATTRIBUTE_TAG = "Name"; //
	public static final int SIMPLE_FIELD = 0;
	public static final int OBJECT_FIELD = 1;

	/**
	 * DOCUMENT ME!
	 *
	 * @param type DOCUMENT ME!
	 * @param propertyName DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static PropertyDescriptor getPropertyDescriptor(
		Class type,
		String propertyName) {
		PropertyDescriptor descriptor = (PropertyDescriptor) descriptorsCache
			.get(propertyName + "." + //
				type);

		if (descriptor == null) {
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(type);
				PropertyDescriptor[] descriptors = beanInfo
					.getPropertyDescriptors();

				for (int i = 0; i < descriptors.length; i++) {
					descriptorsCache.put(descriptors[i].getName() + "." + //
						type, descriptors[i]);
				}

				descriptor = (PropertyDescriptor) descriptorsCache.get(type
					+ "." + //
					propertyName);
			} catch (IntrospectionException ex) {
			}
		}

		return descriptor;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param type DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class type) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);

			return beanInfo.getPropertyDescriptors();
		} catch (Exception ex) {
			return new PropertyDescriptor[0];
		}
	}


}
