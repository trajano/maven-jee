package net.trajano.hello.osgi.test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.Hashtable;

import net.trajano.hello.osgi.IHello;
import net.trajano.hello.osgi.internal.Hello;
import net.trajano.hello.osgi.internal.HelloActivator;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Since Pax Exam tests do not trigger code coverage. It is best we have an
 * actual unit test created for the code base.
 */
public class HelloTest {
	@Test
	public void testHello() throws Exception {
		final IHello hello = new Hello();
		assertEquals("s", hello.echo("s"));
	}

	@Test
	public void testHelloActivator() throws Exception {
		final BundleContext bundleContext = createMock(BundleContext.class);

		final ServiceRegistration serviceRegistration = createMock(ServiceRegistration.class);
		expect(
				bundleContext.registerService(eq(IHello.class.getName()),
						anyObject(IHello.class), anyObject(Hashtable.class)))
				.andReturn(serviceRegistration);
		serviceRegistration.unregister();
		expectLastCall();
		replay(bundleContext, serviceRegistration);
		final HelloActivator activator = new HelloActivator();
		activator.start(bundleContext);
		activator.stop(bundleContext);
	}
}
