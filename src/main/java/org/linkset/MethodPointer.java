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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/*******************************************************************************
 * The pointer to a listener
 * 
 * @author Lukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
final public class MethodPointer {

	/***************************************************************************
	 * Contructor
	 * 
	 * @param methodId
	 *            an id of a handler method
	 * @throws NullPointerException
	 *             id methodId == null or target == null
	 * @throws IllegalArgumentException
	 *             if methodId is empty
	 **************************************************************************/
	public MethodPointer(final Object target, final String methodId) {

		if (target == null) {
			throw new NullPointerException("Null target");
		}

		this.target = target;

		if (methodId == null) {
			throw new NullPointerException("Null methodId");
		}
		this.methodId = methodId.trim();

		if (this.methodId.isEmpty()) {
			throw new IllegalArgumentException("Empty mehotdId.");
		}

		if (this.target instanceof Class<?>) {
			this.method = getMethod((Class<?>) target, methodId);
			if (!Modifier.isStatic(this.method.getModifiers())) {
				throw new IllegalArgumentException(
						"Non-static method not allowed");
			}
		} else {
			this.method = getMethod(target.getClass(), methodId);
			if (Modifier.isStatic(this.method.getModifiers())) {
				throw new IllegalArgumentException("Static method not allowed");
			}
		}
	}

	/***************************************************************************
	 * @see Object#toString()
	 **************************************************************************/
	@Override
	public String toString() {

		return super.toString() + " ; " + this.methodId;
	}

	/***************************************************************************
	 * @see Object#hashCode()
	 **************************************************************************/
	@Override
	public int hashCode() {

		return 17 * this.target.hashCode() + this.method.hashCode();
	}

	/***************************************************************************
	 * @see Object#equals(Object)
	 **************************************************************************/
	@Override
	public boolean equals(final Object obj) {

		if (obj != null) {
			if (obj.getClass().equals(this.getClass())) {
				final MethodPointer other = (MethodPointer) obj;
				// use identity not equality
				return this.target == other.target
						&& this.method.equals(other.method);
			}
		}
		return false;
	}

	/***************************************************************************
	 * Exerutes a method pointed by this pointer
	 * 
	 * @param args
	 *            method arguments
	 * @return value returned by a called method or null
	 * @throws ExceptionInInitializerError
	 *             if the initialization provoked by this method fails.
	 * @throws InvocationTargetException
	 *             if the underlying method throws an exception.
	 **************************************************************************/
	public Object invoke(final Object... args)
			throws InvocationTargetException, ExceptionInInitializerError {

		try {
			return this.method.invoke(this.target, args);
		} catch (final IllegalAccessException e) {
			// should never happen
			throw new RuntimeException(e);
		} catch (final IllegalArgumentException e) {
			// should never happen
			throw new RuntimeException(e);
		}
	}

	/***************************************************************************
	 * This method is equivalent to invoke((Object[])null);
	 * 
	 * @see MethodPointer#invoke(java.lang.Object[])
	 **************************************************************************/
	public Object invoke() throws InvocationTargetException,
			ExceptionInInitializerError {

		return invoke((Object[]) null);
	}

	/***************************************************************************
	 * Returns a method
	 * 
	 * @param cls
	 **************************************************************************/
	@SuppressWarnings("deprecation")
	private static Method getMethod(final Class<?> cls, final String methodId) {

		for (final Method method : cls.getDeclaredMethods()) {

			final MethodId handler = method.getAnnotation(MethodId.class);
			if (handler != null && handler.value().equals(methodId)) {
				method.setAccessible(true);
				return method;
			}
		}
		// backward compatibility code
		for (final Method method : cls.getDeclaredMethods()) {

			final HandlerMethod oldHandler = method
					.getAnnotation(HandlerMethod.class);
			if (oldHandler != null && oldHandler.id().equals(methodId)) {
				method.setAccessible(true);
				return method;
			}
		}
		// handler not found
		throw new IllegalArgumentException(methodId + " not found in " + cls);
	}

	/***************************************************************************
	 * ensures that method parameters are conformant to the specified ones.
	 * Throws exception otherwise.
	 * 
	 * @param parameterTypes
	 *            the array of parameter types of event handler methods
	 **************************************************************************/
	void ensureParameterTypes(final Class<?>[] parameterTypes) {

		if (!Arrays.equals(parameterTypes, this.method.getParameterTypes())) {
			throw new IllegalArgumentException("Nonconformant parameter types.");
		}
	}

	/***************************************************************************
     *
     **************************************************************************/
	private final Method method;
	final Object target;
	private final String methodId;
}
