package net.trajano.osgi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.swissbox.framework.ServiceLookup.getService;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import net.trajano.blueprint.consumer.IServiceUser;
import net.trajano.hello.osgi.IHello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.service.obr.RepositoryAdmin;
import org.osgi.service.obr.Resolver;
import org.osgi.service.obr.Resource;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Tests the OSGi Bundle using Karaf and Pax Exam. This is run using
 * {@link JUnit4TestRunner} to allow injection of a {@link BundleContext} and
 * allow configuration using the {@link Configuration} annotation.
 */
@RunWith(JUnit4TestRunner.class)
public class PaxTest {

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
		return options(
				systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
						.value("WARN"),
				frameworkProperty("obr.repository.url").value(
						new File("target/dependency/repository.xml").toURI()
								.toASCIIString()),
				bundle("mvn:org.apache.felix/org.osgi.service.obr/1.0.2"),
				bundle("mvn:org.apache.felix/org.apache.felix.bundlerepository/1.6.6"),
				bundle("mvn:org.apache.aries/org.apache.aries.util/0.4"),
				bundle("mvn:org.apache.aries.blueprint/org.apache.aries.blueprint.core/0.4"),
				bundle("mvn:org.apache.aries.proxy/org.apache.aries.proxy/0.4"),
				junitBundles());
	}

	/**
	 * Injected blueprint container.
	 */
	@Inject
	private BlueprintContainer blueprintContainer;

	/**
	 * Injected bundle context.
	 */
	@Inject
	private BundleContext bundleContext;

	/**
	 * Injected OBR repository administrator.
	 */
	@Inject
	private RepositoryAdmin repositoryAdmin;

	/**
	 * Deploy OBR resources.
	 * 
	 * @param filter
	 *            filter.
	 * @throws Exception
	 */
	private void obrDeploy(final String filter) throws Exception {
		final Resolver resolver = repositoryAdmin.resolver();
		final Resource[] discoverResources = repositoryAdmin
				.discoverResources(filter);
		for (final Resource r : discoverResources) {
			resolver.add(r);
		}
		assertTrue(resolver.resolve());
		resolver.deploy(true);
	}

	/**
	 * Tests the Blueprint producer bundle by checking if the services it
	 * exposes are available.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBlueprintBundle() throws Exception {
		obrDeploy("(symbolicname=net.trajano.maven-jee6.blueprint.producer)");
		getService(bundleContext, MongoDbFactory.class);
		getService(bundleContext, BlockingQueue.class);
		getService(bundleContext, Executor.class);
	}

	/**
	 * This tests the {@link IServiceUser#reverse(String)} service call.
	 */
	@Test
	public void testBlueprintUserBundle() throws Exception {
		obrDeploy("(|(symbolicname=*.blueprint.consumer)(symbolicname=*.blueprint.producer)(symbolicname=*.hello.osgi))");

		final IServiceUser bean = getService(bundleContext, IServiceUser.class);

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
		obrDeploy("(|(symbolicname=*.blueprint.consumer)(symbolicname=*.blueprint.producer)(symbolicname=*.hello.osgi))");
		{
			@SuppressWarnings("unchecked")
			final BlockingQueue<String> queue = getService(bundleContext,
					BlockingQueue.class);
			queue.put("hello");
		}
		{
			final IServiceUser serviceUser = getService(bundleContext,
					IServiceUser.class);
			assertEquals("hello", serviceUser.pop());
		}
	}

	/**
	 * This tests the {@link IHello#echo(String)} service call.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHelloBundle() throws Exception {
		bundleContext.installBundle("obr:net.trajano.maven-jee6.hello.osgi")
				.start();
		final IHello hello = getService(bundleContext, IHello.class);
		assertNotNull(hello);
		assertEquals("hello", hello.echo("hello"));
	}

	/**
	 * Tests to make sure the {@link JUnit4TestRunner} is configured correctly
	 * and all the framework services are in place.
	 */
	@Test
	public void testInjectedObjects() {
		assertNotNull(blueprintContainer);
		assertNotNull(bundleContext);
		assertNotNull(repositoryAdmin);
	}
}
