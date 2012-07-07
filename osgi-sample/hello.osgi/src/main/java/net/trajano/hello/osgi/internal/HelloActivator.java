package net.trajano.hello.osgi.internal;

import java.util.Hashtable;

import net.trajano.hello.osgi.IHello;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * This is a bundle activator. It is responsible for registration of the
 * service.
 * 
 * @author Archimedes Trajano
 */
public class HelloActivator implements BundleActivator {
	/**
	 * Service registration.
	 */
	private ServiceRegistration helloServiceRegistration;

	/**
	 * Instantiates and registers the service. {@inheritDoc}
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		final IHello service = new Hello();
		helloServiceRegistration = bundleContext.registerService(
				IHello.class.getName(), service,
				new Hashtable<String, Object>());
	}

	/**
	 * Unregisters the service. {@inheritDoc}
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		helloServiceRegistration.unregister();
	}
}
