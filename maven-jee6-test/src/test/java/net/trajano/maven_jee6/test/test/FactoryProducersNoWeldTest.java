package net.trajano.maven_jee6.test.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.trajano.maven_jee6.test.FactoryProducersUtil;

import org.junit.Assert;
import org.junit.Test;

public class FactoryProducersNoWeldTest {
	@Test
	public void testCreateEntityManager() throws Exception {
		final EntityManagerFactory emf = FactoryProducersUtil
				.createEntityManagerFactory();
		final EntityManager em = FactoryProducersUtil.createEntityManager(emf);
		Assert.assertNotNull(em.getTransaction());
		FactoryProducersUtil.disposeEntityManager(em);
		FactoryProducersUtil.disposeEntityManagerFactory(emf);
	}
}
