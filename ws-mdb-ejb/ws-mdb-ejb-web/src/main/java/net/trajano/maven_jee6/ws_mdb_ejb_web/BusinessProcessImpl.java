package net.trajano.maven_jee6.ws_mdb_ejb_web;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jws.WebService;

import net.trajano.maven_jee6.BusinessProcess;
import net.trajano.schemas._2013.ObjectFactory;
import net.trajano.schemas._2013.TextList;

/**
 * Implementation of the {@link BusinessProcess} web service.
 * 
 * @author Archimedes Trajano
 */
@WebService(endpointInterface = "net.trajano.maven_jee6.BusinessProcess", serviceName = "BusinessProcessService", portName = "BusinessProcess", name = "BusinessProcess", targetNamespace = "http://maven-jee6.trajano.net/", wsdlLocation = "WEB-INF/wsdl/ws-mdb-ejb.wsdl")
@Stateless
public class BusinessProcessImpl implements BusinessProcess {
	/**
	 * Injected {@link MessageProducer}.
	 */
	private MessageProducer messageProducer;

	/**
	 * Injected {@link Session}.
	 */
	private Session session;

	/**
	 * Injected {@link TextMessages}.
	 */
	private TextMessages textMessages;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextList listMessages() {
		final ObjectFactory of = new ObjectFactory();
		final TextList ret = of.createTextList();
		for (final String text : textMessages.listText()) {
			ret.getText().add(text);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putTextMessage(final String request) {
		try {
			messageProducer.send(session.createTextMessage(request));
		} catch (final JMSException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Injects the {@link Work} {@link MessageProducer}.
	 * 
	 * @param messageProducer
	 *            message producer
	 */
	@Inject
	public void setMessageProducer(final @Work MessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}

	/**
	 * Injects the session.
	 * 
	 * @param session
	 *            session
	 */
	@Inject
	public void setSession(final Session session) {
		this.session = session;
	}

	/**
	 * Injects the {@link TextMessages}.
	 * 
	 * @param textMessages
	 *            {@link TextMessages}
	 */
	@Inject
	public void setTextMessages(final TextMessages textMessages) {
		this.textMessages = textMessages;
	}
}
