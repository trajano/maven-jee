package net.trajano.maven_jee6.ws_mdb_ejb_web.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import net.trajano.maven_jee6.test.FactoryProducersUtil;
import net.trajano.maven_jee6.test.LatchedMessageListener;
import net.trajano.maven_jee6.ws_mdb_ejb_web.BusinessProcessImpl;
import net.trajano.maven_jee6.ws_mdb_ejb_web.QueueListener;
import net.trajano.maven_jee6.ws_mdb_ejb_web.ResourceProducer;
import net.trajano.maven_jee6.ws_mdb_ejb_web.TextMessages;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssembledTest {
	private BusinessProcessImpl business;
	private Connection connection;
	private EntityManager em;
	private MessageProducer messageProducer;
	private ResourceProducer producer;
	private LatchedMessageListener queueListener;
	private Session session;

	@Before
	public void setUp() throws Exception {
		producer = new ResourceProducer();
		final EntityManagerFactory emf = FactoryProducersUtil
				.createEntityManagerFactory();
		em = FactoryProducersUtil.createEntityManager(emf);
		final ConnectionFactory connectionFactory = FactoryProducersUtil
				.createConnectionFactory();
		producer.setConnectionFactory(connectionFactory);
		connection = producer.createConnection();
		final TextMessages texts = new TextMessages(em);
		session = producer.createSession(connection);
		final Queue workQueue = session.createQueue("workQueue");
		producer.setWorkQueue(workQueue);
		messageProducer = producer.createWorkMessageProducer(session);

		business = new BusinessProcessImpl();
		business.setTextMessages(texts);
		business.setMessageProducer(messageProducer);
		business.setSession(session);

		final QueueListener listener = new QueueListener();
		listener.setTextMessages(texts);
		queueListener = new LatchedMessageListener(listener, connectionFactory,
				workQueue);

		final MessageConsumer consumer = session.createConsumer(workQueue);
		consumer.setMessageListener(queueListener);

	}

	@After
	public void tearDown() throws Exception {
		producer.disposeWorkQueueMessageProducer(messageProducer);
		producer.disposeSession(session);
		producer.disposeConnection(connection);
	}

	@Test
	public void testAssembly() throws Exception {
	}

	@Test
	public void testEmptyQueue() throws Exception {
		queueListener.await();
	}

	@Test
	public void testWebService() throws Exception {
		final EntityTransaction txn = em.getTransaction();
		txn.begin();
		business.putTextMessage("hello");
		session.commit();
		queueListener.await();

		business.listMessages();
		txn.commit();
	}
}
