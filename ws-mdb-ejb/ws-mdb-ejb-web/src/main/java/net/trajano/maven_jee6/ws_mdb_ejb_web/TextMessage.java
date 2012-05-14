package net.trajano.maven_jee6.ws_mdb_ejb_web;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Text message.
 * 
 * @author Archimedes Trajano
 */
@XmlRootElement
@Entity
public class TextMessage {
	/**
	 * Text message value.
	 */
	private String text;

	/**
	 * Primary key.
	 */
	@Id
	@GeneratedValue
	@XmlTransient
	private long textMessageId;

	/**
	 * {@link UUID} stored as a {@link String}.
	 */
	@Column(unique = true, nullable = false, length = 36)
	private String uuid;

	/**
	 * Gets the text value.
	 * 
	 * @return text value.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the UUID.
	 * 
	 * @return the UUID.
	 */
	public UUID getUuid() {
		return UUID.fromString(uuid);
	}

	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * Sets the {@link UUID}. It stores the string representation of the UUID.
	 * 
	 * @param uuid
	 *            {@link UUID}
	 */
	public void setUuid(final UUID uuid) {
		this.uuid = uuid.toString();
	}

}
