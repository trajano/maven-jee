package net.trajano.cdi_javase;

import javax.inject.Inject;

/**
 * Implementation of an interface that will be injected.
 * 
 * @author Archimedes Trajano
 */
public class InjectableClassImpl implements InjectableClass {
	/**
	 * String to be injected.
	 */
	@Inject
	@SomeQualifier
	private String someString;

	/**
	 * It will return an injected string along with the class name.
	 * {@inheritDoc}
	 */
	@Override
	public String getSomeString() {
		return getClass().getName() + " " + someString;
	}
}
