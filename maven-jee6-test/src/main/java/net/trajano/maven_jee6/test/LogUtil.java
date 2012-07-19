package net.trajano.maven_jee6.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * This provides a method to change the logging levels used for testing.
 * 
 * @author Archimedes Trajano
 * 
 */
public final class LogUtil {
	/**
	 * Loads the standard configuration file. In the standard configuration file
	 * {@link java.util.logging.Level#INFO} and above messages are displayed for
	 * net.trajano package space and {@link java.util.logging.Level#WARNING}
	 * messages and above are displayed for anything else.
	 * 
	 * @throws IOException
	 */
	public static void loadConfiguration() throws IOException {
		InputStream configuration = null;
		try {
			configuration = LogUtil.class
					.getResourceAsStream("/META-INF/logging.properties");
			LogManager.getLogManager().readConfiguration(configuration);
		} finally {
			if (configuration != null) {
				configuration.close();
			}
		}
	}

	/**
	 * Loads the debug configuration file. In the debug configuration file
	 * {@link java.util.logging.Level#FINEST} and above messages are displayed
	 * for net.trajano package space and {@link java.util.logging.Level#INFO}
	 * messages and above are displayed for anything else.This method was
	 * intentionally marked as deprecated to flag its use in test cases as it
	 * should not be used regularly.
	 * 
	 * @throws IOException
	 * @deprecated use {@link #loadConfiguration()}
	 */
	@Deprecated
	public static void loadDebugConfiguration() throws IOException {
		InputStream configuration = null;
		try {
			configuration = LogUtil.class
					.getResourceAsStream("/META-INF/debuglogging.properties");
			LogManager.getLogManager().readConfiguration(configuration);
		} finally {
			if (configuration != null) {
				configuration.close();
			}
		}
	}

	/**
	 * Prevent instantiation of utility class.
	 */
	private LogUtil() {

	}
}
