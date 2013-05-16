package net.trajano.maven_jee6.test.test;

import java.util.logging.Logger;

import net.trajano.maven_jee6.test.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Tests {@link LogUtil}. These tests must be visually inspected.
 * 
 * @author Archimedes Trajano
 * 
 */
public class LogUtilTest {
	@Test
	public void loadConfiguration() throws Exception {
		LogUtil.loadConfiguration();
		final Logger log = Logger.getLogger(LogUtilTest.class.getName());
		final Logger apachelog = Logger.getLogger(StringUtils.class.getName());

		log.finest("should not be seen");
		log.finer("should not be seen");
		log.fine("should not be seen");
		log.config("should not be seen");
		log.info("should be seen");
		log.warning("should be seen");
		log.severe("should be seen");

		apachelog.finest("should not be seen");
		apachelog.finer("should not be seen");
		apachelog.fine("should not be seen");
		apachelog.config("should not be seen");
		apachelog.info("should not be seen");
		apachelog.warning("should be seen");
		apachelog.severe("should be seen");
	}

	@Test
	@Deprecated
	public void loadDebugConfiguration() throws Exception {
		LogUtil.loadDebugConfiguration();
		final Logger log = Logger.getLogger(LogUtilTest.class.getName());
		final Logger apachelog = Logger.getLogger(StringUtils.class.getName());

		log.finest("should be seen");
		log.finer("should be seen");
		log.fine("should be seen");
		log.config("should be seen");
		log.info("should be seen");
		log.warning("should be seen");
		log.severe("should be seen");

		apachelog.finest("should not be seen");
		apachelog.finer("should not be seen");
		apachelog.fine("should not be seen");
		apachelog.config("should not be seen");
		apachelog.info("should be seen");
		apachelog.warning("should be seen");
		apachelog.severe("should be seen");
	}
}
