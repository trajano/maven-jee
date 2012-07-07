package net.trajano.blueprint.mongo.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import net.trajano.blueprint.consumer.internal.ServiceUserBean;
import net.trajano.hello.osgi.IHello;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * This tests the {@link ServiceUserBean}.
 * 
 * @author Archimedes Trajano
 */
public class ServiceUserBeanTest {
	/**
	 * Tests using injected mock objects.
	 */
	@Test
	public void testMock() {
		final Executor executor = createMock(Executor.class);
		@SuppressWarnings("unchecked")
		final BlockingQueue<String> queue = EasyMock
				.createMock(BlockingQueue.class);
		final MongoDbFactory mongoDbFactory = EasyMock
				.createMock(MongoDbFactory.class);

		final IHello hello = createMock(IHello.class);
		expect(hello.echo("hello")).andReturn("hello");
		replay(hello);

		final ServiceUserBean bean = new ServiceUserBean(executor, hello,
				mongoDbFactory, queue);
		assertEquals("olleh", bean.reverse("hello"));
		assertNotNull(bean.servicesToString());
	}
}
