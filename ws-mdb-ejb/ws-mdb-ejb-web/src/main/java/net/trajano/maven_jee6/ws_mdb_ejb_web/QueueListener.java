package net.trajano.maven_jee6.ws_mdb_ejb_web;

import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * This takes messages from queue and executes the process.
 * 
 * @author Archimedes Trajano
 */
@MessageDriven(mappedName = "jms/workQueue")
public class QueueListener implements MessageListener {

	/**
	 * Injected {@link TextMessages}.
	 */
	private TextMessages textMessages;

	/**
	 * Puts the message text into the database table. {@inheritDoc}
	 */
	@Override
	public void onMessage(final Message message) {
		try {
			textMessages.putText(((TextMessage) message).getText());
		} catch (final JMSException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Injects the {@link TextMessages} object.
	 * 
	 * @param textMessages
	 *            {@link TextMessages} object
	 */
	@Inject
	public void setTextMessages(final TextMessages textMessages) {
		this.textMessages = textMessages;
	}
}
