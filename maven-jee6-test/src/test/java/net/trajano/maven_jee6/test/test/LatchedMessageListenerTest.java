package net.trajano.maven_jee6.test.test;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.trajano.maven_jee6.test.LatchedMessageListener;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LatchedMessageListenerTest {
	private static class StringBuilderListener implements MessageListener {
		private final StringBuilder b = new StringBuilder();

		public String getBuiltString() {
			return b.toString();
		}

		@Override
		public void onMessage(final Message message) {
			try {
				b.append(((TextMessage) message).getText());
			} catch (final JMSException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Deployment
	public static JavaArchive createDeployment() {
		return create(JavaArchive.class).addClass(StringBuilderListener.class)
				.addClass(FactoryProducers.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	private ConnectionFactory connectionFactory;

	@Inject
	private Session session;

	@Test
	public void testLatch() throws Exception {
		final Queue sampleQueue = session.createQueue("sample");

		final MessageConsumer consumer = session.createConsumer(sampleQueue);
		final StringBuilderListener listener = new StringBuilderListener();
		final LatchedMessageListener latchedMessageListener = new LatchedMessageListener(
				listener, connectionFactory, sampleQueue);
		assertEquals(listener, latchedMessageListener.getWrappedListener());
		consumer.setMessageListener(latchedMessageListener);

		final MessageProducer producer = session.createProducer(sampleQueue);
		producer.send(session.createTextMessage("ABC"));
		producer.send(session.createTextMessage("123"));
		producer.send(session.createTextMessage("do-re-mi"));
		session.commit();

		latchedMessageListener.await();
		assertEquals("ABC123do-re-mi", listener.getBuiltString());
	}
}
