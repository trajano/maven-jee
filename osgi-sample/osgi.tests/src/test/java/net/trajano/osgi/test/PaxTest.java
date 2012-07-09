package net.trajano.osgi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import net.trajano.blueprint.consumer.IServiceUser;
import net.trajano.hello.osgi.IHello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintEvent;
import org.osgi.service.blueprint.container.BlueprintListener;
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
						new File("../obr/target/dependency/repository.xml")
								.toURI().toASCIIString()),
				bundle("mvn:org.ops4j.pax.url/pax-url-commons/1.4.2"),
				bundle("mvn:org.ops4j.pax.url/pax-url-obr/1.4.2"),
				bundle("mvn:org.ops4j.pax.swissbox/pax-swissbox-property"),
				bundle("mvn:org.ops4j.pax.swissbox/pax-swissbox-tracker"),
				bundle("mvn:org.apache.felix/org.osgi.service.obr/1.0.2"),
				bundle("mvn:org.apache.felix/org.apache.felix.bundlerepository/1.6.6"),
				junitBundles());
	}

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
	 * Deploy OBR resources to Blueprint Container.
	 * 
	 * @param filter
	 *            filter.
	 * @throws Exception
	 */
	private void deployBlueprint(final String filter) throws Exception {
		final Resolver resolver = repositoryAdmin.resolver();
		final Resource[] discoverResources = repositoryAdmin
				.discoverResources(filter);
		assertTrue(discoverResources.length > 0);
		resolver.add(discoverResources[0]);
		assertTrue(resolver.resolve());
		resolver.deploy(true);
		final Set<String> resourceSet = new HashSet<String>();
		for (final Resource resource : resolver.getAddedResources()) {
			resourceSet.add(resource.getSymbolicName() + resource.getVersion());
		}
		for (final Resource resource : discoverResources) {
			resourceSet.add(resource.getSymbolicName() + resource.getVersion());
		}
		final CountDownLatch latch = new CountDownLatch(resourceSet.size());
		bundleContext.registerService(BlueprintListener.class,
				new BlueprintListener() {
					@Override
					public void blueprintEvent(final BlueprintEvent event) {
						if (event.getType() == BlueprintEvent.CREATED) {
							resourceSet.remove(event.getBundle()
									.getSymbolicName()
									+ event.getBundle().getVersion());
							latch.countDown();
						}
					}
				}, new Hashtable<String, Object>());
		latch.await(5, TimeUnit.SECONDS);
		assertEquals("some resources didn't start " + resourceSet, 0,
				latch.getCount());
	}

	@Test
	public void testBlueprintBundle() throws Exception {
		deployBlueprint("(symbolicname=net.trajano.maven-jee6.blueprint.producer)");
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
	public void testBlueprintUserBundle() throws Exception {
		deployBlueprint("(symbolicname=net.trajano.maven-jee6.*)");
		deployBlueprint("(symbolicname=net.trajano.maven-jee6.blueprint.consumer)");

		final ServiceReference<IServiceUser> serviceReference = bundleContext
				.getServiceReference(IServiceUser.class);
		assertNotNull("unable to locate service reference for "
				+ IServiceUser.class, serviceReference);
		final IServiceUser bean = bundleContext.getService(serviceReference);
		assertNotNull(bean);
		assertEquals("olleh", bean.reverse("hello"));
	}

	/**
	 * This tests the {@link IHello#echo(String)} service call.
	 */
	@Test
	public void testFramework() throws Exception {
		assertNotNull(bundleContext
				.getServiceReference("org.osgi.service.obr.RepositoryAdmin"));
	}

	/**
	 * Puts the data directly into the hazelcast queue and pops it out using the
	 * service.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHazelcastQueue() throws Exception {
		deployBlueprint("(symbolicname=net.trajano.maven-jee6.*)");
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
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHelloBundle() throws Exception {
		bundleContext.installBundle("obr:net.trajano.maven-jee6.hello.osgi")
				.start();
		final ServiceReference<IHello> serviceReference = bundleContext
				.getServiceReference(IHello.class);
		assertNotNull(serviceReference);
		final IHello hello = bundleContext.getService(serviceReference);
		assertNotNull(hello);
		assertEquals("hello", hello.echo("hello"));
	}

	/**
	 * Tests to make sure the {@link JUnit4TestRunner} is configured correctly.
	 */
	@Test
	public void testInjectedObjects() {
		assertNotNull(bundleContext);
		assertNotNull(repositoryAdmin);
	}
}
