package net.trajano.cdi_javase.test;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import net.trajano.cdi_javase.DataConsumer;
import net.trajano.cdi_javase.DataProducer;
import net.trajano.cdi_javase.InjectableClass;
import net.trajano.cdi_javase.InjectableClassImpl;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ArquillianWeldTest {
	@Deployment
	public static JavaArchive createDeployment() {
		return create(JavaArchive.class).addClass(DataConsumer.class)
				.addClass(DataProducer.class)
				.addClass(InjectableClassImpl.class)
				.addClass(InjectableClass.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private DataConsumer consumer;

	@Test
	public void testHelloWorld() {
		assertEquals("sample", consumer.getInjectedString());
		assertEquals(InjectableClassImpl.class.getName() + " sample", consumer
				.getInjectedClass().getSomeString());
	}
}
