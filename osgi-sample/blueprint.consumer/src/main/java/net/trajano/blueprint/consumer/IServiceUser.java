package net.trajano.blueprint.consumer;

/**
 * OSGi service interface managed by Blueprint.
 * 
 * @author Archimedes Trajano
 */
public interface IServiceUser {
	/**
	 * Returns the currently configured value.
	 * 
	 * @return configured value.
	 */
	String getConfiguredValue();

	/**
	 * Pops a string from the blocking queue.
	 * 
	 * @return the string at the top of the blocking queue.
	 */
	String pop();

	/**
	 * Reverses the string.
	 * 
	 * @param s
	 *            string to reverse.
	 * @return reversed string.
	 */
	String reverse(String s);

	/**
	 * Outputs the toString values of the injected objects.
	 * 
	 * @return toString values of the injected objects.
	 */
	String servicesToString();
}
