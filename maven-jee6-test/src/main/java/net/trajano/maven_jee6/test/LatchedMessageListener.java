package net.trajano.maven_jee6.test;

import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * This wraps an existing {@link MessageListener} so that it can provide
 * {@link org.junit.Test} with the capability to wait till all the messages that
 * have been sent are processed before moving forward with the test.
 * 
 * @author Archimedes Trajano
 */
public class LatchedMessageListener implements MessageListener {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger
			.getLogger(LatchedMessageListener.class.getName());

	/**
	 * {@link ConnectionFactory} used to create a {@link javax.jms.QueueBrowser}
	 * .
	 */
	private final ConnectionFactory connectionFactory;

	/**
	 * {@link CountDownLatch} that counts down starting from the number of
	 * messages that have been queued up.
	 */
	private CountDownLatch countDownLatch;

	/**
	 * Wrapped message listener.
	 */
	private final MessageListener listener;

	/**
	 * Queue to check.
	 */
	private final Queue queue;

	/**
	 * {@link Semaphore} to prevent queue from being processed until the
	 * {@link Semaphore} is released.
	 */
	private final Semaphore semaphore;

	/**
	 * This constructs the {@link LatchedMessageListener} by wrapping an
	 * existing {@link MessageListener}.
	 * 
	 * @param listener
	 *            listener to wrap.
	 * @param connectionFactory
	 *            {@link ConnectionFactory} @param queue {@link Queue} to check
	 *            the contents.
	 */
	public LatchedMessageListener(final MessageListener listener,
			final ConnectionFactory connectionFactory, final Queue queue)
			throws InterruptedException {
		this.listener = listener;
		this.connectionFactory = connectionFactory;
		this.queue = queue;
		semaphore = new Semaphore(1);
		semaphore.acquire();
	}

	/**
	 * This will wait for all the messages in the queue at present to be
	 * processed. It starts out by browsing the queue to determine how many
	 * elements are inside then creates a {@link CountDownLatch} that the
	 * {@link #onMessage(Message)} will countdown on.
	 * 
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	public void await() throws JMSException, InterruptedException {
		LOG.entering(LatchedMessageListener.class.getName(), "await");
		int c = 0;
		final Connection connection = connectionFactory.createConnection();
		try {
			final Session session = connection.createSession(true, 0);
			connection.start();
			@SuppressWarnings("rawtypes")
			final Enumeration e = session.createBrowser(queue).getEnumeration();
			while (e.hasMoreElements()) {
				e.nextElement();
				++c;
			}
			countDownLatch = new CountDownLatch(c);
			semaphore.release();
			LOG.fine("released semaphore and waiting for " + c
					+ " messages to be processed");
			countDownLatch.await();
			semaphore.acquire();
		} finally {
			connection.close();
		}
	}

	/**
	 * Gets the wrapped {@link MessageListener}.
	 * 
	 * @return wrapped {@link MessageListener}.
	 */
	public MessageListener getWrappedListener() {
		return listener;
	}

	/**
	 * {@link Semaphore#acquire()} and once obtained, it will process the
	 * message and then {@link CountDownLatch#countDown()} before
	 * {@link Semaphore#release()}. {@inheritDoc}
	 */
	@Override
	public void onMessage(final Message message) {
		try {
			LOG.fine("waiting on semaphore for " + message);
			semaphore.acquire();
			LOG.fine("semaphore acquired for " + message);
			listener.onMessage(message);
			LOG.fine(message + " processed");
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			LOG.fine(countDownLatch + " counting down from "
					+ countDownLatch.getCount());
			countDownLatch.countDown();
			semaphore.release();
		}
	}
}
