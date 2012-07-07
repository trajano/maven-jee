package net.trajano.maven_jee6.test;

import static org.junit.Assert.assertNotNull;

import javax.xml.namespace.QName;

import net.trajano.maven_jee6.BusinessProcessService;
import net.trajano.schemas._2013.ObjectFactory;

import org.junit.Test;

/**
 * This class is primarily to improve code coverage on generated code.
 * 
 * @author Archimedes Trajano
 */
public class WebServiceClientCreationTest {
	@Test
	public void testCreatedClient() {
		final BusinessProcessService service = new BusinessProcessService();
		assertNotNull(service.getBusinessProcess());
	}

	@Test
	public void testCreatedClient2() {
		final BusinessProcessService service = new BusinessProcessService(
				Thread.currentThread().getContextClassLoader()
						.getResource("META-INF/wsdl/ws-mdb-ejb.wsdl"),
				new QName("http://maven-jee6.trajano.net/",
						"BusinessProcessService"));
		assertNotNull(service.getBusinessProcess());
	}

	@Test
	public void testGeneratedClasses() {
		final ObjectFactory of = new ObjectFactory();
		of.createTextList().getText().add(of.createText("foo").getValue());
	}
}
