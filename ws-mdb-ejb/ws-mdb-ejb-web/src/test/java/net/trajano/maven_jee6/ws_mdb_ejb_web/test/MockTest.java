package net.trajano.maven_jee6.ws_mdb_ejb_web.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.trajano.maven_jee6.ws_mdb_ejb_web.BusinessProcessImpl;
import net.trajano.maven_jee6.ws_mdb_ejb_web.QueueListener;
import net.trajano.maven_jee6.ws_mdb_ejb_web.TextMessages;

import org.junit.Test;

/**
 * This tests with mock objects to improve test coverage. This is primarily for
 * illustrative purposes, but in real applications there is no need to go to
 * this level for testing checked exceptions required by APIs.
 * 
 * @author Archimedes Trajano
 */
public class MockTest {
	@Test
	public void testJmsExceptionFromBusinessProcessImpl() throws Exception {
		try {
			final MessageProducer mockMessageProducer = createMock(MessageProducer.class);
			final Session mockSession = createMock(Session.class);

			expect(mockSession.createTextMessage("hello")).andThrow(
					new JMSException("mock"));
			replay(mockSession);
			final BusinessProcessImpl businessProcessImpl = new BusinessProcessImpl();
			businessProcessImpl.setMessageProducer(mockMessageProducer);
			businessProcessImpl.setSession(mockSession);
			businessProcessImpl.putTextMessage("hello");
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof JMSException);
			assertEquals("mock", e.getCause().getMessage());
		}
	}

	@Test
	public void testJmsExceptionFromQueueListener() throws Exception {
		try {
			final TextMessages mockTextMessages = createMock(TextMessages.class);
			final TextMessage mockTextMessage = createMock(TextMessage.class);

			expect(mockTextMessage.getText())
					.andThrow(new JMSException("mock"));
			replay(mockTextMessage);
			final QueueListener queueListener = new QueueListener();
			queueListener.setTextMessages(mockTextMessages);
			queueListener.onMessage(mockTextMessage);
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof JMSException);
			assertEquals("mock", e.getCause().getMessage());
		}
	}
}
