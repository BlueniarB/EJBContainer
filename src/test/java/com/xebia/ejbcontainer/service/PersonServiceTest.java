package com.xebia.ejbcontainer.service;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xebia.ejbcontainer.domain.Person;

public class PersonServiceTest {

	private static EJBContainer container;
	private static IPersonService service;
	private static Context context;
	
	@BeforeClass
	public static void initJBoss() throws NamingException {
		container = EJBContainer.createEJBContainer();
		context = container.getContext();
		service = (IPersonService) context.lookup("PersonService/local");
	}
	
	public static void initGlassfish() throws NamingException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(EJBContainer.MODULES, new File("target/classes"));
		container = EJBContainer.createEJBContainer(properties);
		context = container.getContext();
		service = (IPersonService) context.lookup("java:global/classes/"+PersonService.class.getSimpleName());
	}
	
	@AfterClass
	public static void cleanup() throws NamingException {
		if (context != null) {
			context.close();
		}
		if (container != null) {
			container.close();
		}
	}
	
	@Before
	public void setUp() {
		service.deleteAll();
	}
	
	@Test
	public void should_create_a_person() {
		Person person = new Person("Erich", "Gamma");
		service.create(person);
	}

	@Test
	public void should_retrieve_all_persons_from_database() {
		String[][] persons = { 
					{"Erich", "Gamma"},
					{"Richard", "Helm"},
					{"Ralph", "Johnson"},
					{"John", "Vlissides"}
				};
		for (String[] person:persons) {
			service.create(new Person(person[0], person[1]));
		}
		List<Person> retrievedPersons = service.retrieveAll();
		assertEquals(persons.length, retrievedPersons.size());
	}

	@Test
	public void should_update_an_existing_person() {
		Person person = service.create(new Person("Erich", "Gamma"));
		person.setFirstName("Paul");
		person = service.update(person);
		
		Person updatedPerson = service.find(person.getId());
		
		assertEquals("Paul", updatedPerson.getFirstName());
	}

	@Test
	public void should_delete_an_existing_person() {
		Person person = service.create(new Person("Erich", "Gamma"));
		service.delete(person);
		service.find(person.getId());
	}

}