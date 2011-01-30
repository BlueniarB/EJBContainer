package com.xebia.ejbcontainer.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelloWorldServiceTest {

    private static EJBContainer ec;

    private static IHelloWorldService helloWorldService;

    /***
     * M�thode d'initialisation appel�e une seule fois lors de l'ex�cution
     * des tests de HelloServiceTest.
     * C'est l'endroit id�al pour d�marrer l'EJBContainer et r�cup�rer
     * les EJB � tester.
     * @throws NamingException
     */
    @BeforeClass
    public static void init() throws NamingException {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(EJBContainer.MODULES, new File("target/classes.ext"));
        ec = EJBContainer.createEJBContainer(properties);
        Context ctx = ec.getContext();

        // le nom JNDI d'un EJB d�pend du serveur d'application utilis�
        String helloWorldServiceName = HelloWorldService.class.getSimpleName();
        helloWorldServiceName = isJbossContainer() ? helloWorldServiceName + "/local" : "java:global/classes.ext/" + helloWorldServiceName;
        helloWorldService = (IHelloWorldService) ctx.lookup(helloWorldServiceName);

    }

    /***
     * M�thode de test qui v�rifie que nous avons bien r�cup�r� l'EJB
     * HelloWorldService et qu'il est fonctionnel
     */
    @Test
    public void should_say_hello_world() {
        Assert.assertEquals("Hello World", helloWorldService.sayHelloWorld());
    }

    /***
     * M�thode de nettoyage appel�e une seule fois apr�s l'ex�cution de
     * l'ensemble des tests unitaires de HelloServiceTest.
     * C'est l'endroit id�al pour fermer le contexte JNDI et l'EJBContainer.
     * Un bug de JBoss nous contraint � ne pas appeler les m�thodes close()
     * sur context et container.
     * @throws NamingException
     */
    @AfterClass
    public static void cleanup() throws NamingException {
        if (!isJbossContainer()) {
            ec.close();
        }
    }

    private static boolean isJbossContainer() {
        return System.getProperty("jboss.home") != null;
    }

}
