package net.trajano.maven_jee6.test.test;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.xpath.XPathExpressionException;

import net.trajano.maven_jee6.test.FactoryProducersUtil;

public class FactoryProducers {
	@Produces
	public Connection createConnection(final ConnectionFactory connectionFactory)
			throws JMSException {
		return FactoryProducersUtil.createConnection(connectionFactory);
	}

	@Produces
	@Singleton
	public ConnectionFactory createConnectionFactory() {
		return FactoryProducersUtil.createConnectionFactory();
	}

	@Produces
	public EntityManager createEntityManager(final EntityManagerFactory emf) {
		return FactoryProducersUtil.createEntityManager(emf);
	}

	@Produces
	@Singleton
	public EntityManagerFactory createEntityManagerFactory()
			throws XPathExpressionException {
		return FactoryProducersUtil.createEntityManagerFactory();
	}

	@Produces
	public Session createSession(final Connection connection)
			throws JMSException {
		return FactoryProducersUtil.createSession(connection);
	}

	public void disposeConnection(@Disposes final Connection connection)
			throws JMSException {
		FactoryProducersUtil.disposeConnection(connection);
	}

	public void disposeEntityManager(@Disposes final EntityManager em) {
		FactoryProducersUtil.disposeEntityManager(em);
	}

	public void disposeEntityManagerFactory(
			@Disposes final EntityManagerFactory emf) {
		FactoryProducersUtil.disposeEntityManagerFactory(emf);
	}

	public void disposeSession(@Disposes final Session session)
			throws JMSException {
		FactoryProducersUtil.disposeSession(session);
	}

}
