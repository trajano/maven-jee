package net.trajano.cdi_javase;

import javax.inject.Inject;

/**
 * A CDI managed class where a value gets injected.
 * 
 * @author Archimedes Trajano
 */
public class DataConsumer {
	/**
	 * A class to be injected.
	 */
	@Inject
	private InjectableClass injectedClass;

	/**
	 * String to be injected.
	 */
	@Inject
	@SomeQualifier
	private String someString;

	/**
	 * This gets the instance of the {@link InjectableClass}.
	 * 
	 * @return injected instance.
	 */
	public InjectableClass getInjectedClass() {
		return injectedClass;
	}

	/**
	 * Gets the string that was injected.
	 * 
	 * @return {@link #someString}
	 */
	public String getInjectedString() {
		return someString;
	}
}
