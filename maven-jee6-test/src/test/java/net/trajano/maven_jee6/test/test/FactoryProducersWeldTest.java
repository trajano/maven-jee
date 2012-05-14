package net.trajano.maven_jee6.test.test;

import javax.inject.Inject;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FactoryProducersWeldTest {
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(FactoryProducers.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private EntityManager em;
	@Inject
	private EntityManagerFactory emf;

	@Inject
	private Session session;

	@Test
	public void testCreateEntityManager() throws Exception {
		Assert.assertNotNull(em.getTransaction());
	}

	@Test
	public void testCreateEntityManagerFactory() throws Exception {
		Assert.assertNotNull(emf);
	}

	@Test
	public void testCreateJMSSession() throws Exception {
		Assert.assertNotNull(session.createTextMessage("hello"));
	}
}
