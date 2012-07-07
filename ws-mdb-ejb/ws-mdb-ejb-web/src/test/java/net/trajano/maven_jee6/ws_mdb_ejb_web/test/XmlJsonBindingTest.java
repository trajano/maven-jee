package net.trajano.maven_jee6.ws_mdb_ejb_web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.trajano.maven_jee6.ws_mdb_ejb_web.TextMessage;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;

/**
 * This tests the scenarios where we use JAXB to do marshalling and
 * unmarshalling JSON and XML objects.
 * 
 * @author trajano
 * 
 */
public class XmlJsonBindingTest {

	private TextMessage textMessage;

	@Before
	public void setUp() {
		textMessage = new TextMessage();
		textMessage.setText("hello");
		textMessage.setUuid(UUID.randomUUID());
	}

	/**
	 * Verifies that that setup is working correctly.
	 */
	@Test
	public void testCreationOfObject() {
		assertEquals("hello", textMessage.getText());
	}

	@Test
	public void testJsonMarshalling() throws Exception {
		final JAXBContext jc = JAXBContext.newInstance(TextMessage.class);
		final Marshaller m = jc.createMarshaller();
		final StringWriter w = new StringWriter();
		m.marshal(textMessage, w);
		assertTrue(w.toString().contains("<text>hello</text>"));
	}

	@Test
	public void testXmlMarshalling() throws Exception {
		// Jersey specific start
		final Providers ps = new Client().getProviders();
		// Jersey specific end

		// A multivaluedmap that does nothing.
		final MultivaluedMap<String, Object> responseHeaders = new MultivaluedMap<String, Object>() {

			@Override
			public void add(final String key, final Object value) {
			}

			@Override
			public void clear() {
			}

			@Override
			public boolean containsKey(final Object key) {
				return false;
			}

			@Override
			public boolean containsValue(final Object value) {
				return false;
			}

			@Override
			public Set<java.util.Map.Entry<String, List<Object>>> entrySet() {
				return null;
			}

			@Override
			public List<Object> get(final Object key) {
				return null;
			}

			@Override
			public Object getFirst(final String key) {
				return null;
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public Set<String> keySet() {
				return null;
			}

			@Override
			public List<Object> put(final String key, final List<Object> value) {
				return null;
			}

			@Override
			public void putAll(
					final Map<? extends String, ? extends List<Object>> m) {
			}

			@Override
			public void putSingle(final String key, final Object value) {
			}

			@Override
			public List<Object> remove(final Object key) {
				return null;
			}

			@Override
			public int size() {
				return 0;
			}

			@Override
			public Collection<List<Object>> values() {
				return null;
			}
		};

		final MessageBodyWriter<TextMessage> messageBodyWriter = ps
				.getMessageBodyWriter(TextMessage.class, TextMessage.class,
						new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		assertNotNull(messageBodyWriter);

		messageBodyWriter.writeTo(textMessage, TextMessage.class,
				TextMessage.class, new Annotation[0],
				MediaType.APPLICATION_JSON_TYPE, responseHeaders, baos);
		final String jsonString = new String(baos.toByteArray());
		assertTrue(jsonString.contains("\"text\":\"hello\""));
	}
}
