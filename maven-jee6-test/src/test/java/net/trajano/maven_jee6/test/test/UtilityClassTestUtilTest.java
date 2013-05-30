package net.trajano.maven_jee6.test.test;

import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import net.trajano.commons.testing.UtilityClassTestUtil;
import net.trajano.maven_jee6.test.FactoryProducersUtil;

import org.junit.Test;

/**
 * Tests {@link UtilityClassTestUtil}.
 * 
 * @author Archimedes Trajano
 * 
 */
public class UtilityClassTestUtilTest {
	@Test
	public void testOtherUtil() throws Exception {
		assertUtilityClassWellDefined(FactoryProducersUtil.class);
	}
}
