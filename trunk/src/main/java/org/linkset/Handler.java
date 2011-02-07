// -----------------------------------------------------------------------------
//    LinkSet
//    Copyright: 2010 Lukasz Bownik (lukasz.bownik@gmail.com)
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * Proxy handler
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
final class Handler implements InvocationHandler {

	/***************************************************************************
	 *
	 **************************************************************************/
	Handler(final Class<?> iCls, final Object target) {

		if (target == null) {
			throw new NullPointerException("Null target.");
		}

		if (iCls == null) {
			throw new NullPointerException("Null interface class object.");

		}
		this.target = target;

		System.out.println("Interface: " + iCls.getName());
		System.out.println("Class: " + target.getClass().getName());
		initMap(iCls);
	}

	/***************************************************************************
	 *
	 **************************************************************************/
	private void initMap(final Class<?> iCls) {

		for (final Method interfaceMethod : iCls.getDeclaredMethods()) {
			for (final Method targetMethod : this.target.getClass()
					.getDeclaredMethods()) {
				if (match(interfaceMethod, targetMethod)) {
					this.map.put(interfaceMethod, targetMethod);
				}
			}
		}
		// no methods found
		if (this.map.isEmpty()) {
			throw new IllegalArgumentException("Object of class "
					+ this.target.getClass().getName()
					+ " is not assignable to interface " + iCls.getName());
		}
	}

	/***************************************************************************
	 *
	 **************************************************************************/
	private static boolean match(final Method method1, final Method method2) {

		System.out.println("Mathing " + method1.toGenericString() + " and "
				+ method2.toGenericString());
		return method1.getName().equals(method2.getName())
				&& method1.getReturnType().equals(method2.getReturnType())
				&& Arrays.equals(method1.getParameterTypes(),
						method2.getParameterTypes());
	}

	/***************************************************************************
	 *
	 **************************************************************************/
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {

		final Method targetMethod = this.map.get(method);
		return targetMethod.invoke(this.target, args);
	}

	/***************************************************************************
	 *
	 **************************************************************************/
	private final Object target;
	private final Map<Method, Method> map = new HashMap<Method, Method>();
}
