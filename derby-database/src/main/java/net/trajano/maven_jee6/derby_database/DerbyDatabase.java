package net.trajano.maven_jee6.derby_database;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.derby.impl.drda.NetworkServerControlImpl;

/**
 * This starts up the Derby network server when the application is initialized.
 * 
 * @author Archimedes Trajano
 */
@Singleton
@Startup
public class DerbyDatabase {
	/**
	 * Network server implementation.
	 */
	private NetworkServerControlImpl networkServerControlImpl;

	/**
	 * Shuts down the network server.
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void destroy() throws Exception {
		networkServerControlImpl.shutdown();
	}

	/**
	 * Starts the Derby network server and "eats" the derby logging output. It
	 * checks if there is something listenening on the default Derby port first
	 * if not it will use another open port as a backup (primarily for testing
	 * purposes).
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		int port = 1527;
		try {
			final ServerSocket socket = new ServerSocket(port);
			socket.close();
		} catch (final IOException i) {
			final ServerSocket socket = new ServerSocket(0);
			port = socket.getLocalPort();
			socket.close();
		}
		networkServerControlImpl = new NetworkServerControlImpl(
				InetAddress.getLocalHost(), port);
		networkServerControlImpl.start(new PrintWriter(new OutputStreamWriter(
				new OutputStream() {
					/**
					 * Does nothing. {@inheritDoc}
					 */
					@Override
					public void write(final int c) throws IOException {
					}
				}, "utf8")));
		for (int i = 0; i < 5 && !networkServerControlImpl.isServerStarted(); ++i) {
			Thread.sleep(500);
		}
		if (!networkServerControlImpl.isServerStarted()) {
			throw new RuntimeException("server not started");
		}
	}
}
