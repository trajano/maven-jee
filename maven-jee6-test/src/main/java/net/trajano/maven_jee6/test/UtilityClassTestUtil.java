package net.trajano.maven_jee6.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This provides a test to check certain properties of a utility class.
 * Primarily that it has only one private constructor and no non-static methods
 * and it is final.
 * 
 * @author Archimedes Trajano
 */
public final class UtilityClassTestUtil {
	/**
	 * Verifies that a utility class is well defined.
	 * 
	 * @param clazz
	 *            utility class to verify.
	 */
	public static void assertUtilityClassWellDefined(final Class<?> clazz)
			throws NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		assertTrue("class must be final",
				Modifier.isFinal(clazz.getModifiers()));
		assertEquals("There must be only one constructor", 1,
				clazz.getDeclaredConstructors().length);
		final Constructor<?> constructor = clazz.getDeclaredConstructor();
		if (constructor.isAccessible()
				|| !Modifier.isPrivate(constructor.getModifiers())) {
			fail("constructor is not private");
		}
		constructor.setAccessible(true);
		constructor.newInstance();
		constructor.setAccessible(false);
		for (final Method method : clazz.getMethods()) {
			if (!Modifier.isStatic(method.getModifiers())
					&& method.getDeclaringClass().equals(clazz)) {
				fail("there exists a non-static method:" + method);
			}
		}
	}

	/**
	 * Prevent instantiation of utility class.
	 */
	private UtilityClassTestUtil() {
	}
}
