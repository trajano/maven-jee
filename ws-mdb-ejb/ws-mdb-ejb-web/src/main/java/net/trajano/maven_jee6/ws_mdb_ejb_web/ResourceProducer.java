package net.trajano.maven_jee6.ws_mdb_ejb_web;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This is a CDI resource producer specifically designed to deal with creating
 * and disposing container managed resources.
 * 
 * @author Archimedes Trajano
 */
public class ResourceProducer {
	/**
	 * Injected connection factory.
	 */
	@Produces
	@Resource(mappedName = "jms/connectionFactory")
	private ConnectionFactory connectionFactory;

	/**
	 * Injected entity manager.
	 */
	@Produces
	@PersistenceContext
	private EntityManager em;

	/**
	 * Work queue.
	 */
	@Produces
	@Work
	@Resource(mappedName = "jms/workQueue")
	private Queue workQueue;

	/**
	 * Creates a JMS {@link Connection}.
	 * 
	 * @return JMS {@link Connection}.
	 * @throws JMSException
	 */
	@Produces
	@RequestScoped
	public Connection createConnection() throws JMSException {
		final Connection connection = connectionFactory.createConnection();
		connection.start();
		return connection;
	}

	/**
	 * Produces a JMS {@link Session}.
	 * 
	 * @param connection
	 *            connection.
	 * @return session
	 * @throws JMSException
	 */
	@Produces
	@RequestScoped
	public Session createSession(final Connection connection)
			throws JMSException {
		return connection.createSession(true, 0);
	}

	/**
	 * Creates a {@link MessageProducer} for the {@link #workQueue}.
	 * 
	 * @param session
	 *            JMS {@link Session}.
	 * @return a {@link MessageProducer}.
	 * @throws JMSException
	 */
	@Produces
	@RequestScoped
	@Work
	public MessageProducer createWorkMessageProducer(final Session session)
			throws JMSException {
		return session.createProducer(workQueue);
	}

	/**
	 * Closes the JMS {@link Connection}.
	 * 
	 * @param connection
	 *            connection to dispose
	 * @throws JMSException
	 */
	public void disposeConnection(@Disposes final Connection connection)
			throws JMSException {
		connection.close();
	}

	/**
	 * Closes the JMS {@link Session}.
	 * 
	 * @param session
	 *            session to dispose
	 * @throws JMSException
	 */
	public void disposeSession(@Disposes final Session session)
			throws JMSException {
		session.close();
	}

	/**
	 * Disposes the producer.
	 * 
	 * @param producer
	 *            producer to dispose.
	 * @throws JMSException
	 */
	public void disposeWorkQueueMessageProducer(
			@Disposes @Work final MessageProducer producer) throws JMSException {
		producer.close();
	}

	public void setConnectionFactory(final ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setWorkQueue(final Queue workQueue) {
		this.workQueue = workQueue;
	}
}
