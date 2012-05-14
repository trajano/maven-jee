package net.trajano.maven_jee6.ws_mdb_ejb_web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Provides operations on an injected {@link EntityManager}.
 * 
 * @author Archimedes Trajano
 */
public class TextMessages {
	/**
	 * Entity Manager.
	 */
	private final EntityManager em;

	/**
	 * Constructs the object with the {@link EntityManager} injected.
	 * 
	 * @param em
	 *            {@link EntityManager}
	 */
	@Inject
	public TextMessages(final EntityManager em) {
		this.em = em;
	}

	/**
	 * Gets the list of texts in the database table.
	 * 
	 * @return text list.
	 */
	public List<String> listText() {
		final TypedQuery<String> query = em.createNamedQuery("listText",
				String.class);
		return new ArrayList<String>(query.getResultList());
	}

	/**
	 * Puts a text record into the {@link TextMessage} table.
	 * 
	 * @param text
	 *            text to put in.
	 */
	public void putText(final String text) {
		final TextMessage textObject = new TextMessage();
		textObject.setUuid(UUID.randomUUID());
		textObject.setText(text);
		em.persist(textObject);
	}
}
