package jatools.xml;

import jatools.accessor.AccessorBase;
import jatools.designer.App;
import jatools.util.Map;
import jatools.xml.serializer.AccessorSerializer;
import jatools.xml.serializer.ArraySerializer;
import jatools.xml.serializer.ClassResolvable;
import jatools.xml.serializer.ClassResolvableSerializer;
import jatools.xml.serializer.ColorSerializer;
import jatools.xml.serializer.FontSerializer;
import jatools.xml.serializer.IntArraySerializer;
import jatools.xml.serializer.ListSerializer;
import jatools.xml.serializer.MapSerializer;
import jatools.xml.serializer.PointSerializer;
import jatools.xml.serializer.PrimitiveSerializer;
import jatools.xml.serializer.PropertiesSerializer;
import jatools.xml.serializer.RectangleSerializer;
import jatools.xml.serializer.StringArraySerializer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



public class XmlSerializerFactory {
    static java.util.Map serializerCache = new HashMap();
    static java.util.Map superSerializerCache = new HashMap();

    static {
        registerSerializer(Boolean.TYPE, PrimitiveSerializer.class);
        registerSerializer(Character.TYPE, PrimitiveSerializer.class);
        registerSerializer(Byte.TYPE, PrimitiveSerializer.class);
        registerSerializer(Short.TYPE, PrimitiveSerializer.class);
        registerSerializer(Integer.TYPE, PrimitiveSerializer.class);
        registerSerializer(Long.TYPE, PrimitiveSerializer.class);
        registerSerializer(Float.TYPE, PrimitiveSerializer.class);
        registerSerializer(Double.TYPE, PrimitiveSerializer.class);
        registerSerializer(String.class, PrimitiveSerializer.class);
        registerSerializer(Color.class, ColorSerializer.class);
        registerSerializer(Point.class, PointSerializer.class);
        registerSerializer(Font.class, FontSerializer.class);
        registerSerializer(ClassResolvable.class, ClassResolvableSerializer.class);
        registerSerializer(Rectangle.class, RectangleSerializer.class);
        registerSerializer(int[].class, IntArraySerializer.class);
        registerSerializer(String[].class, StringArraySerializer.class);

        registerSuperSerializer(Properties.class, PropertiesSerializer.class);
        registerSuperSerializer(Map.class, MapSerializer.class);
        registerSuperSerializer(Object[].class, ArraySerializer.class);
        registerSuperSerializer(AccessorBase.class, AccessorSerializer.class);
        registerSuperSerializer(List.class, ListSerializer.class);
    }

    public static XmlSerializer createInstance(Class valueType)
        throws Exception {
        Class serializer = (Class) serializerCache.get(valueType);

        if (serializer != null) {
            return (XmlSerializer) serializer.newInstance();
        } else {
            Iterator it = superSerializerCache.keySet().iterator();

            while (it.hasNext()) {
                Class superSerializer = (Class) it.next();

                if (superSerializer.isAssignableFrom(valueType)) {
                    serializer = (Class) superSerializerCache.get(superSerializer);
                    
                    registerSerializer(valueType, serializer);

                    return (XmlSerializer) serializer.newInstance();
                }
            }

            throw new Exception(App.messages.getString("res.0") + valueType.getName() +
                "]");
        }
    }

    public static void registerSerializer(Class clazz, Class serializer) {
        serializerCache.put(clazz, serializer);
    }

    public static void registerSuperSerializer(Class clazz, Class serializer) {
        superSerializerCache.put(clazz, serializer);
    }
}
