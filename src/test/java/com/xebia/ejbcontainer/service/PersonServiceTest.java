package com.xebia.ejbcontainer.service;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xebia.ejbcontainer.domain.Person;

public class PersonServiceTest {

    private static EJBContainer ec;

    private static IPersonService personService;

    /***
     * M�thode d'initialisation appel�e une seule fois lors de l'ex�cution
     * des tests de PersonServiceTest.
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
        String personServiceName = PersonService.class.getSimpleName();
        personServiceName = isJbossContainer() ? personServiceName + "/local" : "java:global/classes.ext/" + personServiceName;
        personService = (IPersonService) ctx.lookup(personServiceName);
    }

    /***
     * M�thode de test qui v�rifie la cr�ation d'un objet Person dans la
     * base de donn�es. L'instance obtient un identifiant apr�s avoir �t�
     * persist�e par l'EntityManager.
     */
    @Test
    public void should_create_a_person() {
        Person person = new Person("Erich", "Gamma");
        Person createdPerson = personService.create(person);
        assertNotNull(createdPerson.getId());
    }

    /***
     * M�thode de nettoyage appel�e une seule fois apr�s l'ex�cution de
     * l'ensemble des tests unitaires de PersonServiceTest.
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
