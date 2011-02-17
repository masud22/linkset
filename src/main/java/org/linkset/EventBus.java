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

/*******************************************************************************
 * An event bus interface
 * 
 * @author Lukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public interface EventBus {

	/***************************************************************************
	 * Adds a listener to this set.
	 * 
	 * @param eventType
	 *            type of events to receive
	 * @param target
	 *            a target object or class
	 * @param methodId
	 *            an id of a handler method
	 * @throws NullPointerException
	 *             id methodId == null or target == null
	 * @throws IllegalArgumentException
	 *             if methodId is empty or method argument type is ont an
	 *             eventType
	 **************************************************************************/
	public void add(final Class<?> eventType, final Object target,
			final String methodId);

	/***************************************************************************
	 * Removes a listener form this set. If a set does not contain a apecified
	 * listener, then this method does nothing.
	 * 
	 * @param eventType
	 *            type of events to receive
	 * @param target
	 *            a target object or class
	 * 
	 * @param methodId
	 *            a method annotation identifier
	 **************************************************************************/
	public void remove(final Class<?> eventType, final Object target,
			final String methodId);

	/***************************************************************************
	 * Removes all Listerners targeting a specified object.
	 * 
	 * @param target
	 *            a target object or null if a listener is a static method
	 * @throws NullPointerException
	 *             if target is null
	 **************************************************************************/
	public void removeAll(final Object target);

	/***************************************************************************
	 * Fires an event
	 * 
	 * @param event
	 *            event object
	 * @throws NullPointerException
	 *             if event is null
	 * @throws ExceptionInInitializerError
	 *             if the initialization provoked by any method fails.
	 * @throws InvocationTargetException
	 *             if any underlying method throws an exception.
	 **************************************************************************/
	public void fire(final Object event) throws InvocationTargetException,
			ExceptionInInitializerError;
}
