package net.trajano.maven_jee6.test.test;

import static net.trajano.maven_jee6.test.FactoryProducersUtil.createEntityManager;
import static net.trajano.maven_jee6.test.FactoryProducersUtil.createEntityManagerFactory;
import static net.trajano.maven_jee6.test.FactoryProducersUtil.disposeEntityManager;
import static net.trajano.maven_jee6.test.FactoryProducersUtil.disposeEntityManagerFactory;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;

public class FactoryProducersNoWeldTest {
	@Test
	public void testCreateEntityManager() throws Exception {
		final EntityManagerFactory emf = createEntityManagerFactory();
		final EntityManager em = createEntityManager(emf);
		assertNotNull(em.getTransaction());
		disposeEntityManager(em);
		disposeEntityManagerFactory(emf);
	}
}
