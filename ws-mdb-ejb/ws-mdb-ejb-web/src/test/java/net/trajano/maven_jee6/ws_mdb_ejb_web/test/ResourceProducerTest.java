package net.trajano.maven_jee6.ws_mdb_ejb_web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import net.trajano.maven_jee6.test.FactoryProducersUtil;
import net.trajano.maven_jee6.ws_mdb_ejb_web.ResourceProducer;
import net.trajano.maven_jee6.ws_mdb_ejb_web.TextMessage;
import net.trajano.maven_jee6.ws_mdb_ejb_web.TextMessages;

import org.junit.Test;

public class ResourceProducerTest {
	@Test
	public void testBuildJMSManually() throws Exception {
		final ResourceProducer producer = new ResourceProducer();

		producer.setConnectionFactory(FactoryProducersUtil
				.createConnectionFactory());

		final Connection connection = producer.createConnection();
		final Session session = producer.createSession(connection);

		producer.setWorkQueue(session.createQueue("workQueue"));
		final MessageProducer p = producer.createWorkMessageProducer(session);

		producer.disposeWorkQueueMessageProducer(p);
		producer.disposeSession(session);
		producer.disposeConnection(connection);
	}

	@Test
	public void testBuildJPAManually() throws Exception {
		final TextMessages textMessages = new TextMessages(
				FactoryProducersUtil.createEntityManager(FactoryProducersUtil
						.createEntityManagerFactory()));
		assertEquals(0, textMessages.listText().size());
	}

	@Test
	public void testBuildJPAManuallyWithInsert() throws Exception {
		final EntityManager em = FactoryProducersUtil
				.createEntityManager(FactoryProducersUtil
						.createEntityManagerFactory());
		final TextMessages textMessages = new TextMessages(em);
		{
			final EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			assertEquals(0, textMessages.listText().size());
			transaction.commit();
		}
		{
			final EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			textMessages.putText("hello");
			transaction.commit();
		}
		{
			final EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			assertEquals(1, textMessages.listText().size());
			final TextMessage tm = (TextMessage) em.createQuery(
					"select t from TextMessage t").getSingleResult();
			assertEquals("hello", tm.getText());
			assertNotNull(tm.getUuid());
			transaction.commit();
		}
	}
}
