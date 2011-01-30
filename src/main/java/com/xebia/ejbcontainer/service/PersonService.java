package com.xebia.ejbcontainer.service;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.xebia.ejbcontainer.domain.Person;

@Stateless
@Local
/**
 * Service permettant la gestion des entit�s Person avec la base de donn�es
 * Seule la m�thode de cr�ation est impl�ment�e.
 */
public class PersonService implements IPersonService {

    @PersistenceContext
    EntityManager em;

    /**
     * Cr�e une nouvelle Person dans la base de donn�es.
     * M�thode transactionnelle :
     *  @TransactionAttribute(TransactionAttributeType.REQUIRED) implicite
     * @param person une instance de Person
     * @return L'instance de personne persist�e, champ Id initialis�
     */
    @Override
    public Person create(Person person) {
        em.persist(person);
        return person;
    }

}
