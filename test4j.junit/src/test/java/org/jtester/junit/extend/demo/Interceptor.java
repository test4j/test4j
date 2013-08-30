package org.jtester.junit.extend.demo;

/**
 * This interface is used to declare the methods for every interceptor.
 * 
 * @version $Id: Interceptor.java 201 2009-02-15 19:18:09Z paranoid12 $
 */
public interface Interceptor {
	/**
	 * This method will be called before every test - we can implement our own
	 * logic in every implementation.
	 */
	public void interceptBefore();

	/**
	 * This method will be called after every test - we can implement our own
	 * logic in every implementation.
	 */
	public void interceptAfter();
}
