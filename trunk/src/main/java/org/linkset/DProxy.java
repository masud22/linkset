// -----------------------------------------------------------------------------
//    LinkSet
//    Copyright: 2011 Lukasz Bownik (lukasz.bownik@gmail.com)
//
//    This file is part of LinkSet.
//
//    LinkSet is free software; you can redistribute it and/or modify
//    it under the terms of the Lesser GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    LinkSet is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    Lesser GNU General Public License for more details.
//
//    You should have received a copy of the Lesser GNU General Public License
//    along with LinkSet; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
// -----------------------------------------------------------------------------
package org.linkset;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * A dynamic proxy generator.
 * 
 * @author Lukasz Bownik
 ******************************************************************************/
public final class DProxy {

	/***************************************************************************
	 * Creates a proxy that enables calling supplied object's methods through
	 * supplied interface.
	 * 
	 * @param interfaceClass
	 *            an interface class
	 * @param target
	 *            a target object
	 * @return created proxy object
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if target is not compatible with the supplied interface
	 **************************************************************************/
	@SuppressWarnings("unchecked")
	public static <T> T make(final Class<T> interfaceClass, final Object target) {

		if (target == null) {
			throw new NullPointerException("Null target.");
		}

		if (interfaceClass == null) {
			throw new NullPointerException("Null interface class object.");

		}

		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class<?>[] { interfaceClass }, new MultiHandler(target,
						constructMapping(interfaceClass, target)));
	}

	/***************************************************************************
	 * Construct a mapping
	 **************************************************************************/
	private static Map<Method, Method> constructMapping(
			final Class<?> interfaceClass, final Object target) {

		final Map<Method, Method> map = new HashMap<Method, Method>();
		final Method[] interfaceMethods = interfaceClass.getMethods();

		for (final Method interfaceMethod : interfaceMethods) {
			for (final Method targetMethod : target.getClass().getMethods()) {
				if (match(interfaceMethod, targetMethod)) {
					map.put(interfaceMethod, targetMethod);
				}
			}
		}
		// no or not all methods found
		if (map.size() < interfaceMethods.length) {
			throw new IllegalArgumentException("Object of class "
					+ target.getClass().getName()
					+ " is not compatible with interface "
					+ interfaceClass.getName());
		}
		return map;
	}

	/***************************************************************************
	 * Check if methods match
	 **************************************************************************/
	private static boolean match(final Method method1, final Method method2) {

		return method1.getName().equals(method2.getName())
				&& method1.getReturnType().equals(method2.getReturnType())
				&& Arrays.equals(method1.getParameterTypes(),
						method2.getParameterTypes());
	}

	/***************************************************************************
	 * We don't wan ti to be instantiable
	 **************************************************************************/
	private DProxy() {

	}
}
