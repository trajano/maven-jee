package net.trajano.maven_jee6.test.test;

import static net.trajano.maven_jee6.test.UtilityClassTestUtil.assertUtilityClassWellDefined;
import net.trajano.maven_jee6.test.FactoryProducersUtil;
import net.trajano.maven_jee6.test.UtilityClassTestUtil;
import net.trajano.maven_jee6.test.util.MultipleConstructorUtil;
import net.trajano.maven_jee6.test.util.NonFinalUtil;
import net.trajano.maven_jee6.test.util.PublicConstructorUtil;

import org.junit.Test;

/**
 * Tests {@link UtilityClassTestUtil}.
 * 
 * @author Archimedes Trajano
 * 
 */
public class UtilityClassTestUtilTest {
	@Test(expected = AssertionError.class)
	public void testBadUtil1() throws Exception {
		assertUtilityClassWellDefined(NonFinalUtil.class);
	}

	@Test(expected = AssertionError.class)
	public void testBadUtil2() throws Exception {
		assertUtilityClassWellDefined(PublicConstructorUtil.class);
	}

	@Test(expected = AssertionError.class)
	public void testBadUtil3() throws Exception {
		assertUtilityClassWellDefined(MultipleConstructorUtil.class);
	}

	@Test
	public void testOtherUtil() throws Exception {
		assertUtilityClassWellDefined(FactoryProducersUtil.class);
	}

	@Test
	public void testSelf() throws Exception {
		assertUtilityClassWellDefined(UtilityClassTestUtil.class);
	}

	@Test(expected = AssertionError.class)
	public void testTestSelf() throws Exception {
		assertUtilityClassWellDefined(UtilityClassTestUtilTest.class);
	}
}
