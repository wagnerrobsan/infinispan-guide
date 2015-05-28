package com.packtpub.infinispan.chapter10.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ogm.util.impl.Log;
import org.hibernate.ogm.util.impl.LoggerFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import com.packtpub.infinispan.chapter10.domain.Ticket;
import com.packtpub.infinispan.chapter10.domain.TicketCategory;


public class QueryTicketsTest {

	private static final String PERSISTENCE_UNIT_NAME_WITH_JTA = "ogm-jpa-tutorial";

	private static final Log logger = LoggerFactory.make();
	EntityManagerFactory factory = null;

	//@Test
	public void queryTicketByIdWithHQL() {
		logger.info("Using Hibernate Search to find a Ticket!");

		EntityManager em = getEntityManager();
		em.getTransaction().begin();

		Session session = em.unwrap(Session.class);
		Query q = session
				.createQuery("from Ticket t where t.id = :ticketNumber");
		Ticket ticket = GenerateTicketsTest.tickets.get(1);
		q.setString("ticketNumber", ticket.getId());
		Ticket retrievedTicket = (Ticket) q.uniqueResult();
		assertEquals(retrievedTicket.getId(), ticket.getId());
		logger.infof("\nFound ticket %s ", retrievedTicket);
		em.getTransaction().commit();
		em.close();
	}

	//@Test
	public void queryTicketByCategoryWithHQL() {
		logger.info("Using Hibernate Search to find a Ticket of Category!");

		EntityManager em = getEntityManager();
		em.getTransaction().begin();

		Session session = em.unwrap(Session.class);
		Query q = session
				.createQuery("from Ticket t where t.ticketCategory.id = :ticketCategory");
		TicketCategory category = GenerateTicketsTest.tickets.get(1).getTicketCategory();
		q.setEntity("ticketCategory", category.getId());
		
		@SuppressWarnings("unchecked")
		List<Ticket> ticketList = q.list();
		assertEquals(ticketList.size(), 6);
		logger.infof("\nFound ticket %s ", ticketList.toString());
		em.getTransaction().commit();
		em.close();
	}

	//@Test
	@SuppressWarnings("unchecked")
	public void queryTicketWithQueryLuceneTest() {
		logger.info("Using QueryBuilder and FullTextQuery !!");
        
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
		Ticket ticket = GenerateTicketsTest.tickets.get(1);

		QueryBuilder b = ftem.getSearchFactory().buildQueryBuilder()
				.forEntity(Ticket.class).get();
		org.apache.lucene.search.Query lq = b.keyword().onField("id")
				.matching(ticket.getId()).createQuery();

		//  Note: Here we are converting the Lucene Query to JPA Query
		FullTextQuery ftQuery = ftem.createFullTextQuery(lq, Ticket.class);

		ftQuery.initializeObjectsWith(ObjectLookupMethod.SKIP,
				DatabaseRetrievalMethod.FIND_BY_ID);

		List<Ticket> resultList = ftQuery.getResultList();
		assertEquals(resultList.size(), 1);
		em.getTransaction().commit();
		em.close();

	}

	@Test
	public void test(){
		String country = "uk";
		String city = "London";
		String p1 = "http://api.openweathermap.org/data/2.5/weather?q=%s, %s";
		String p2 = "http://api.wunderground.com/api/449c708390bc617d/conditions/q/%s/%s.json";
		String url  = String.format(p1, country, city);
	    System.out.println(url);
	    assertEquals(url, "http://api.openweathermap.org/data/2.5/weather?q=uk, London");	    
	    url = String.format(p2, country.toUpperCase(), city);	    
	    System.out.println(url);
	    assertEquals(url, "http://api.wunderground.com/api/449c708390bc617d/conditions/q/UK/London.json");
	    
	}
	
	public synchronized EntityManager getEntityManager() {
		if (factory == null) {
			factory = Persistence
					.createEntityManagerFactory(PERSISTENCE_UNIT_NAME_WITH_JTA);
		}
		return factory.createEntityManager();
	}

}
