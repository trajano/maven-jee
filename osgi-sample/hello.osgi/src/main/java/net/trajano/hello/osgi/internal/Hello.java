package net.trajano.hello.osgi.internal;

import net.trajano.hello.osgi.IHello;

/**
 * Sample service implementation. Not exposed outside the bundle.
 * 
 * @author Archimedes Trajano
 */
public class Hello implements IHello {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String echo(final String s) {
		return s;
	}
}
