package net.trajano.hello.osgi;

/**
 * Service interface.
 * 
 * @author Archimedes Trajano
 */
public interface IHello {
	/**
	 * Returns the parameter that was passed in.
	 * 
	 * @param s
	 *            string
	 * @return the parameter that was passed in.
	 */
	String echo(String s);
}
