package net.trajano.cdi_javase;

import javax.enterprise.inject.Produces;

/**
 * A CDI managed class produces values.
 * 
 * @author Archimedes Trajano
 */
public class DataProducer {

	/**
	 * Produces a string for injection.
	 * 
	 * @return the string "sample"
	 */
	@Produces
	@SomeQualifier
	public final String produceString() {
		return "sample";
	}
}
