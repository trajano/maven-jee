package net.trajano.maven_jee6.test.test;

import net.trajano.maven_jee6.test.FactoryProducersUtil;
import net.trajano.maven_jee6.test.UtilityClassTestUtil;

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
		UtilityClassTestUtil.assertUtilityClassWellDefined(NonFinalUtil.class);
	}

	@Test(expected = AssertionError.class)
	public void testBadUtil2() throws Exception {
		UtilityClassTestUtil
				.assertUtilityClassWellDefined(PublicConstructorUtil.class);
	}

	@Test(expected = AssertionError.class)
	public void testBadUtil3() throws Exception {
		UtilityClassTestUtil
				.assertUtilityClassWellDefined(MultipleConstructorUtil.class);
	}

	@Test
	public void testOtherUtil() throws Exception {
		UtilityClassTestUtil
				.assertUtilityClassWellDefined(FactoryProducersUtil.class);
	}

	@Test
	public void testSelf() throws Exception {
		UtilityClassTestUtil
				.assertUtilityClassWellDefined(UtilityClassTestUtil.class);
	}

	@Test(expected = AssertionError.class)
	public void testTestSelf() throws Exception {
		UtilityClassTestUtil
				.assertUtilityClassWellDefined(UtilityClassTestUtilTest.class);
	}
}
