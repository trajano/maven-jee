package net.trajano.blueprint.consumer.internal;

import java.util.Dictionary;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import net.trajano.blueprint.consumer.IServiceUser;
import net.trajano.hello.osgi.IHello;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Implementation of an OSGi service managed by OSGi Blueprint.
 * 
 * @author Archimedes Trajano
 */
public class ServiceUserBean implements IServiceUser, ManagedService {
	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(ServiceUserBean.class
			.getName());

	/**
	 * Configured value.
	 */
	private String configuredValue;

	/**
	 * Injected executor.
	 */
	private final Executor executor;

	/**
	 * OSGi service instance.
	 */
	private final IHello hello;

	/**
	 * MongoDbFactory. This is used instead of the MongoTemplate because the
	 * current 1.0.1 release causes IllegalAccessExceptions.
	 */
	private final MongoDbFactory mongoDbFactory;

	/**
	 * Injected queue.
	 */
	private final BlockingQueue<String> queue;

	/**
	 * Instantiates the service bean.
	 * 
	 * @param executor
	 *            executor
	 * @param hello
	 *            OSGi service instance.
	 * @param mongoDbFactory
	 *            Spring Mongo DB Factory
	 * @param queue
	 *            queue
	 */
	public ServiceUserBean(final Executor executor, final IHello hello,
			final MongoDbFactory mongoDbFactory,
			final BlockingQueue<String> queue) {
		this.executor = executor;
		this.hello = hello;
		this.mongoDbFactory = mongoDbFactory;
		this.queue = queue;

		log.info(servicesToString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String getConfiguredValue() {
		return configuredValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String pop() {
		return queue.remove();
	}

	/**
	 * calls the IHello service to echo the string to reverse.
	 * 
	 * @param s
	 *            string to reverse.
	 * @return reversed string
	 */
	@Override
	public String reverse(final String s) {
		return StringUtils.reverse(hello.echo(s));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String servicesToString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("executor", executor).append("hello", hello)
				.append("mongoDbFactory", mongoDbFactory)
				.append("queue", queue)
				.append("configuredValue", configuredValue).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(
			@SuppressWarnings("rawtypes") final Dictionary properties)
			throws ConfigurationException {
		if (properties != null) {
			configuredValue = (String) properties.get("configuredvalue");
		}
	}
}
