package net.trajano.maven_jee6.test;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Disables SSL certificate checks. This is used for performing integration test
 * against servers that may use self-signed certificates.
 * 
 * @author Archimedes Trajano
 * 
 */
public final class DisableSSLCertificateCheckUtil {
	/**
	 * Host name verifier that does not perform any checks.
	 */
	private static class NullHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(final String hostname, final SSLSession session) {
			return true;
		}
	}

	/**
	 * Trust manager that does not perform any checks.
	 */
	private static class NullX509TrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(final X509Certificate[] chain,
				final String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] chain,
				final String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	/**
	 * Disable trust checks for SSL connections.
	 */
	public static void disableChecks() throws NoSuchAlgorithmException,
			KeyManagementException {
		// CHECKSTYLE:OFF
		// This invocation will always fail, but it will register the
		// default SSL provider to the URL class.
		try {
			new URL("https://0.0.0.0/").getContent();
		} catch (final IOException e) {
		}
		// CHECKSTYLE:ON

		final SSLContext context = SSLContext.getInstance("SSLv3");
		final TrustManager[] trustManagerArray = { new NullX509TrustManager() };
		context.init(null, trustManagerArray, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(context
				.getSocketFactory());
		HttpsURLConnection
				.setDefaultHostnameVerifier(new NullHostnameVerifier());
	}

	/**
	 * Prevent instantiation of utility class.
	 */
	private DisableSSLCertificateCheckUtil() {
	}
}
