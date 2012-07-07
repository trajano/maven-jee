package net.trajano.osgi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openengsb.labs.paxexam.karaf.options.KarafDistributionOption.configureConsole;
import static org.openengsb.labs.paxexam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.openengsb.labs.paxexam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.scanFeatures;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import net.trajano.blueprint.consumer.IServiceUser;
import net.trajano.hello.osgi.IHello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openengsb.labs.paxexam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.EagerSingleStagedReactorFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Tests the OSGi Bundle using Karaf and Pax Exam. This is run using
 * {@link JUnit4TestRunner} to allow injection of a {@link BundleContext} and
 * allow configuration using the {@link Configuration} annotation.
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(EagerSingleStagedReactorFactory.class)
public class KarafTest {

	/**
	 * <p>
	 * Configures PAX Exam. It configures it so it clears the caches, has a
	 * higher log level, include JUnit bundles, the dependent bundles and Apache
	 * Felix.
	 * </p>
	 * <p>
	 * In order for things to work in M2E, the use of the
	 * org.apache.servicemix:depends-maven-plugin is not used.
	 * </p>
	 * 
	 * @return OSGi container configuration.
	 */
	@Configuration
	public static Option[] configuration() throws Exception {
		return CoreOptions
				.options(
						karafDistributionConfiguration(
								"mvn:org.apache.karaf/apache-karaf/2.2.8/tar.gz",
								KarafTest.class.getName(), "2.2.8")
								.useDeployFolder(false).unpackDirectory(
										new File("target/paxexam/")),
						logLevel(LogLevelOption.LogLevel.WARN),
						configureConsole().ignoreLocalConsole()
								.ignoreRemoteShell(),
						felix(),
						scanFeatures(
								"mvn:org.apache.karaf.assemblies.features/standard/2.2.8/xml/features",
								"spring-dm", "spring-tx"),
						scanFeatures(
								"mvn:net.trajano.maven-jee6/obr/1.0.0/xml/features",
								"blueprint.consumer", "blueprint.producer",
								"hello.osgi"));
	}

	/**
	 * Injected bundle context.
	 */
	@Inject
	private BundleContext bundleContext;

	@Test
	public void testBlueprintBundle() {
		assertNotNull(bundleContext.getServiceReference(MongoDbFactory.class
				.getName()));
		assertNotNull(bundleContext.getServiceReference(BlockingQueue.class
				.getName()));
		assertNotNull(bundleContext.getServiceReference(Executor.class
				.getName()));
	}

	/**
	 * This tests the {@link IServiceUser#reverse(String)} service call.
	 */
	@Test
	public void testBlueprintUserBundle() {
		final ServiceReference serviceReference = bundleContext
				.getServiceReference(IServiceUser.class.getName());
		assertNotNull("unable to locate service reference for "
				+ IServiceUser.class, serviceReference);
		final IServiceUser bean = (IServiceUser) bundleContext
				.getService(serviceReference);
		assertNotNull(bean);
		assertEquals("olleh", bean.reverse("hello"));
	}

	/**
	 * Puts the data directly into the hazelcast queue and pops it out using the
	 * service.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHazelcastQueue() throws Exception {
		{
			@SuppressWarnings("unchecked")
			final BlockingQueue<String> queue = (BlockingQueue<String>) bundleContext
					.getService(bundleContext
							.getServiceReference(BlockingQueue.class.getName()));
			queue.put("hello");
		}
		{
			final IServiceUser serviceUser = (IServiceUser) bundleContext
					.getService(bundleContext
							.getServiceReference(IServiceUser.class.getName()));
			assertEquals("hello", serviceUser.pop());
		}
	}

	/**
	 * This tests the {@link IHello#echo(String)} service call.
	 */
	@Test
	public void testHelloBundle() {
		final ServiceReference serviceReference = bundleContext
				.getServiceReference(IHello.class.getName());
		assertNotNull(serviceReference);
		final IHello hello = (IHello) bundleContext
				.getService(serviceReference);
		assertNotNull(hello);
		assertEquals("hello", hello.echo("hello"));
	}

	/**
	 * Tests to make sure the {@link JUnit4TestRunner} is configured correctly.
	 */
	@Test
	public void testInjectedObjects() {
		assertNotNull(bundleContext);
	}
}
