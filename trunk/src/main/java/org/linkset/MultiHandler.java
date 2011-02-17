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
import java.util.Map;

/*******************************************************************************
 * Proxy handler
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
final class MultiHandler implements InvocationHandler {

	/***************************************************************************
	 *
	 **************************************************************************/
	MultiHandler(final Object target, final Map<Method, Method> map) {

		this.target = target;
		this.map = map;
	}

	/***************************************************************************
	 * @see InvocationHandler#invoke(Object, Method, Object[])
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
	private final Map<Method, Method> map;
}
