package net.trajano.maven_jee6.test;

import java.util.Properties;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * This is a utility class that provides producer implementations that can be
 * used in CDI.
 * 
 * @author Archimedes Trajano
 */
public final class FactoryProducersUtil {
	/**
	 * Creates a JMS {@link Connection}. It will also start the connection as
	 * well.
	 * 
	 * @param connectionFactory
	 *            {@link ConnectionFactory} to create the connections from.
	 * @return a started JMS {@link Connection}
	 * @throws JMSException
	 */
	public static Connection createConnection(
			final ConnectionFactory connectionFactory) throws JMSException {
		final Connection connection = connectionFactory.createConnection();
		connection.start();
		return connection;
	}

	/**
	 * Creates an {@link ActiveMQConnectionFactory}. It uses a randomized tag
	 * and is made non-persistent.
	 * 
	 * @return an {@link ActiveMQConnectionFactory}.
	 */
	public static ConnectionFactory createConnectionFactory() {
		return new ActiveMQConnectionFactory("vm://" + UUID.randomUUID()
				+ "?broker.persistent=false");
	}

	/**
	 * Creates an {@link EntityManager}.
	 * 
	 * @param emf
	 *            {@link EntityManagerFactory}
	 * @return {@link EntityManager}
	 */
	public static EntityManager createEntityManager(
			final EntityManagerFactory emf) {
		return emf.createEntityManager();
	}

	/**
	 * Creates an {@link EntityManagerFactory} based on the name in the
	 * persistence.xml that is in the thread context. It creates an in-memory
	 * Derby database and creates the tables as needed.
	 * 
	 * @return an {@link EntityManagerFactory}.
	 */
	public static EntityManagerFactory createEntityManagerFactory()
			throws XPathExpressionException {
		final XPath xp = XPathFactory.newInstance().newXPath();
		final Node persistenceUnitNode = (Node) xp
				.compile(
						"//*[local-name() = 'persistence-unit' and namespace-uri() = 'http://java.sun.com/xml/ns/persistence']")
				.evaluate(
						new InputSource(
								Thread.currentThread()
										.getContextClassLoader()
										.getResourceAsStream(
												"META-INF/persistence.xml")),
						XPathConstants.NODE);
		final String persistenceUnitName = persistenceUnitNode.getAttributes()
				.getNamedItem("name").getTextContent();
		final Properties props = new Properties();
		props.put("javax.persistence.jdbc.driver",
				EmbeddedDriver.class.getName());
		props.put("javax.persistence.jdbc.url", "jdbc:derby:memory:"
				+ UUID.randomUUID().toString() + ";create=true");
		props.put("hibernate.hbm2ddl.auto", "create-drop");
		props.put("hibernate.archive.autodetection", "class");
		return Persistence.createEntityManagerFactory(persistenceUnitName,
				props);
	}

	/**
	 * Creates a JMS {@link Session}.
	 * 
	 * @param connection
	 *            JMS {@link Connection}.
	 * @return the created JMS {@link Session}
	 * @throws JMSException
	 */
	public static Session createSession(final Connection connection)
			throws JMSException {
		return connection.createSession(true, 0);
	}

	/**
	 * Disposes the JMS {@link Connection}. It stops it first before closing.
	 * 
	 * @param connection
	 *            JMS {@link Connection} to dispose.
	 */
	public static void disposeConnection(final Connection connection)
			throws JMSException {
		connection.stop();
		connection.close();
	}

	/**
	 * Disposes the {@link EntityManager}.
	 * 
	 * @param em
	 *            {@link EntityManager} to dispose.
	 */
	public static void disposeEntityManager(final EntityManager em) {
		em.close();
	}

	/**
	 * Disposes the {@link EntityManagerFactory}.
	 * 
	 * @param emf
	 *            {@link EntityManagerFactory} to dispose.
	 */
	public static void disposeEntityManagerFactory(
			final EntityManagerFactory emf) {
		emf.close();
	}

	/**
	 * Disposes the JMS {@link Session}.
	 * 
	 * @param session
	 *            {@link Session} to dispose.
	 * @throws JMSException
	 */
	public static void disposeSession(final Session session)
			throws JMSException {
		session.close();
	}

	/**
	 * Prevent instantiation of utility class.
	 */
	private FactoryProducersUtil() {
	}

}
