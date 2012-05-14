package net.trajano.cdi_javase.test;

import javax.inject.Inject;

import net.trajano.cdi_javase.DataConsumer;
import net.trajano.cdi_javase.DataProducer;
import net.trajano.cdi_javase.InjectableClass;
import net.trajano.cdi_javase.InjectableClassImpl;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ArquillianWeldTest {
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(DataConsumer.class).addClass(DataProducer.class)
				.addClass(InjectableClassImpl.class)
				.addClass(InjectableClass.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private DataConsumer consumer;

	@Test
	public void testHelloWorld() {
		Assert.assertEquals("sample", consumer.getInjectedString());
		Assert.assertEquals(InjectableClassImpl.class.getName() + " sample",
				consumer.getInjectedClass().getSomeString());
	}
}
