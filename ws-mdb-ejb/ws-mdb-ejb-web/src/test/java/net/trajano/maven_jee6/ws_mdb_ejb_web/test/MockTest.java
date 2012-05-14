package net.trajano.maven_jee6.ws_mdb_ejb_web.test;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.trajano.maven_jee6.ws_mdb_ejb_web.BusinessProcessImpl;
import net.trajano.maven_jee6.ws_mdb_ejb_web.QueueListener;
import net.trajano.maven_jee6.ws_mdb_ejb_web.TextMessages;

import org.easymock.EasyMock;
import org.junit.Assert;
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
			final MessageProducer mockMessageProducer = EasyMock
					.createMock(MessageProducer.class);
			final Session mockSession = EasyMock.createMock(Session.class);

			EasyMock.expect(mockSession.createTextMessage("hello")).andThrow(
					new JMSException("mock"));
			EasyMock.replay(mockSession);
			final BusinessProcessImpl businessProcessImpl = new BusinessProcessImpl();
			businessProcessImpl.setMessageProducer(mockMessageProducer);
			businessProcessImpl.setSession(mockSession);
			businessProcessImpl.putTextMessage("hello");
		} catch (final RuntimeException e) {
			Assert.assertTrue(e.getCause() instanceof JMSException);
			Assert.assertEquals("mock", e.getCause().getMessage());
		}
	}

	@Test
	public void testJmsExceptionFromQueueListener() throws Exception {
		try {
			final TextMessages mockTextMessages = EasyMock
					.createMock(TextMessages.class);
			final TextMessage mockTextMessage = EasyMock
					.createMock(TextMessage.class);

			EasyMock.expect(mockTextMessage.getText()).andThrow(
					new JMSException("mock"));
			EasyMock.replay(mockTextMessage);
			final QueueListener queueListener = new QueueListener();
			queueListener.setTextMessages(mockTextMessages);
			queueListener.onMessage(mockTextMessage);
		} catch (final RuntimeException e) {
			Assert.assertTrue(e.getCause() instanceof JMSException);
			Assert.assertEquals("mock", e.getCause().getMessage());
		}
	}
}
