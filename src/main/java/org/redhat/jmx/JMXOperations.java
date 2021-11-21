package org.redhat.jmx;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.*;

public class JMXOperations {
    public static void main(String[] args) throws Exception {
        MBeanServerConnection server = makeJMXConnection();
        listObjects(server);
        refreshBundles(server);
    }

    private static void refreshBundles(MBeanServerConnection server) throws Exception {

        ObjectName objectName = new ObjectName("org.apache.karaf:type=bundle,*");
        List<ObjectName> cacheList = new LinkedList<>(server.queryNames(objectName, null));
        System.out.println(cacheList);
        for (Iterator<ObjectName> itr = cacheList.listIterator(); itr.hasNext();){
            objectName = itr.next();
            /*Object[] params = {"236"};
            String[] sig = {String.class.getName()};
            Object obj = server.invoke(objectName, "getStatus", params, sig);*/
            /*Object[] params = {"mvn:org.redhat.fusesource.example/karaf-camel-rest-configuration/1.0-SNAPSHOT"};
            String[] sig = {String.class.getName()};
            Object obj = server.invoke(objectName, "install", params, sig);*/
            Object[] params = {"239 "};
            String[] sig = {String.class.getName()};
            Object obj = server.invoke(objectName, "refresh", params, sig);
            System.out.println(obj);
        }
    }

    private static void listObjects(MBeanServerConnection server) throws Exception {
        ObjectName objectName = new ObjectName("osgi.core:type=bundleState,*");
        List<ObjectName> cacheList = new LinkedList<>(server.queryNames(objectName, null));
        System.out.println(cacheList);
        for (Iterator<ObjectName> itr = cacheList.listIterator(); itr.hasNext();){
            objectName = itr.next();
            Object[] params = {236};
            String[] sig = {long.class.getName()};
            Object obj = server.invoke(objectName, "getBundle", params, sig);
            System.out.println(obj);
        }
    }

    public static MBeanServerConnection makeJMXConnection() throws IOException {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://127.0.0.1:44444/jndi/rmi://127.0.0.1:1099/karaf-root");
        Map<String, String[]> env = new HashMap<>();
        String[] credentials = {"admin", "admin"};
        env.put(JMXConnector.CREDENTIALS, credentials);
        JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
        MBeanServerConnection server = jmxc.getMBeanServerConnection();
        return server;
    }
}
