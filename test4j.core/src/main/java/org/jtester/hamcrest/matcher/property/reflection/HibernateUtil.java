package org.jtester.hamcrest.matcher.property.reflection;

import org.jtester.module.JTesterException;

/**
 * Utility class for handling Hibernate proxies during the comparison.
 * <p/>
 * Every operation is performed through reflection to avoid a direct link to
 * Hibernate. This way you do not need Hibernate in the classpath to use the
 * reflection comparator.
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class HibernateUtil {

	/**
	 * The hibernate proxy type, null if the class is not found in the classpath
	 */
	protected static Class hibernateProxyClass;

	static {
		try {
			hibernateProxyClass = Class.forName("org.hibernate.proxy.HibernateProxy");

		} catch (ClassNotFoundException e) {
			// hibernate not found in the classpath
			hibernateProxyClass = null;
		}
	}

	/**
	 * Checks whether the given ojbect is a HibernateProxy instance.
	 * 
	 * @param object
	 *            The object
	 * @return True if the object is a proxy
	 */
	public static boolean isHibernateProxy(Object object) {
		return hibernateProxyClass != null && hibernateProxyClass.isInstance(object);
	}

	/**
	 * Checks whether the given proxy object has been loaded.
	 * 
	 * @param object
	 *            The object or proxy
	 * @return True if the object is a proxy and has been loaded
	 */
	public static boolean isUninitialized(Object object) {
		if (!isHibernateProxy(object)) {
			return false;
		}
		return (Boolean) invokeLazyInitializerMethod("isUninitialized", object);
	}

	/**
	 * Gets the class name of the proxied object
	 * 
	 * @param object
	 *            The object or proxy
	 * @return The class name of the object, null if the object is null
	 */
	public static String getEntitiyName(Object object) {
		if (!isHibernateProxy(object)) {
			return object == null ? null : object.getClass().getName();
		}
		return (String) invokeLazyInitializerMethod("getEntityName", object);
	}

	/**
	 * Gets the unique identifier of the given proxy object.
	 * 
	 * @param object
	 *            The object or proxy
	 * @return The identifier or null if the object was not a proxy
	 */
	public static Object getIdentifier(Object object) {
		if (!isHibernateProxy(object)) {
			return null;
		}
		return invokeLazyInitializerMethod("getIdentifier", object);
	}

	/**
	 * Gets (and loads) the wrapped object out of a given hibernate proxy.
	 * <p/>
	 * If the given object is not a proxy or if Hibernate is not found in the
	 * classpath, this method just returns the given object. If the given object
	 * is a proxy, the proxy is initialized (loaded) and the un-wrapped object
	 * is returned.
	 * 
	 * @param object
	 *            The object or proxy
	 * @return The uproxied object or the object itself if it was no proxy
	 */
	public static Object getUnproxiedValue(Object object) {
		// check whether object is a proxy
		if (!isHibernateProxy(object)) {
			return object;
		}
		// found a proxy, load and un-wrap
		return invokeLazyInitializerMethod("getImplementation", object);
	}

	/**
	 * Invokes the given method on the LazyInitializer that is associated with
	 * the given proxy.
	 * 
	 * @param methodName
	 *            The method to invoke, not null
	 * @param proxy
	 *            The hibernate proxy instance, not null
	 * @return The result value of the method call
	 */
	@SuppressWarnings("unchecked")
	protected static Object invokeLazyInitializerMethod(String methodName, Object proxy) {
		try {
			Object lazyInitializer = hibernateProxyClass.getMethod("getHibernateLazyInitializer").invoke(proxy);
			return lazyInitializer.getClass().getMethod(methodName).invoke(lazyInitializer);
		} catch (Throwable e) {
			throw new JTesterException("Unable to invoke method on lazy initializer of Hibernate proxy. Method: "
					+ methodName + ", proxy: " + proxy, e);
		}
	}

}