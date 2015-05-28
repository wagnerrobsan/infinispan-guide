package com.packtpub.infinispan.chapter10.tests;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ogm.util.impl.Log;
import org.hibernate.ogm.util.impl.LoggerFactory;
import org.junit.Test;

import com.packtpub.infinispan.chapter10.domain.Ticket;

public class DeleteTicketsTest {

	private static final String PERSISTENCE_UNIT_NAME_WITH_JTA = "ogm-jpa-tutorial";
	private static final Log logger = LoggerFactory.make();

	private EntityManagerFactory factory = null;

	@Test
	public void testDelete() {
		logger.info("Using Hibernate Search to find a Ticket and then remove it!");
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Session session = em.unwrap(Session.class);
		for (Ticket ticket : GenerateTicketsTest.tickets) {
			Query q = session
					.createQuery("from Ticket t where t.id = :ticketNumber");
			q.setString("ticketNumber", ticket.getId());
			Ticket retrievedTicket = (Ticket) q.uniqueResult();
			assertEquals(retrievedTicket.getId(), ticket.getId());			
			em.remove(retrievedTicket);
		}

		em.getTransaction().commit();
		em.close();

	}

	public synchronized EntityManager getEntityManager() {
		if (factory == null) {
			factory = Persistence
					.createEntityManagerFactory(PERSISTENCE_UNIT_NAME_WITH_JTA);
		}
		return factory.createEntityManager();
	}

}
