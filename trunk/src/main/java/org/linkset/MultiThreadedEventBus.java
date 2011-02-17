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
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/*******************************************************************************
 * A multithreaded event bus with exact matching
 * 
 * @author Lukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public final class MultiThreadedEventBus implements EventBus {

	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	public MultiThreadedEventBus() {

		this.thread = new Thread() {
			@Override
			public void run() {
				runInBackgrount();
			}
		};
		this.thread.start();
	}

	/***************************************************************************
	 * @see EventBus#add(Class, Object, String)
	 **************************************************************************/
	public void add(final Class<?> eventType, final Object target,
			final String methodId) {

		final DefaultListenerManager manager = ensureManagerOf(eventType);
		manager.add(target, methodId);
	}

	/***************************************************************************
	 * @see EventBus#remove(Class, Object, String)
	 **************************************************************************/
	public void remove(final Class<?> eventType, final Object target,
			final String methodId) {

		final DefaultListenerManager manager = ensureManagerOf(eventType);
		manager.remove(target, methodId);
	}

	/***************************************************************************
	 * @see EventBus#removeAll(Object)
	 **************************************************************************/
	public void removeAll(final Object target) {

		for (final DefaultListenerManager manager : this.map.values()) {
			manager.removeAll(target);
		}
	}

	/***************************************************************************
	 * @see EventBus#fire(Object)
	 **************************************************************************/
	public void fire(final Object event) throws InvocationTargetException,
			ExceptionInInitializerError {

		this.eventQueue.add(event);
	}

	/***************************************************************************
	 * @return true if a set is empty
	 **************************************************************************/
	public boolean isEmpty() {

		if (!this.map.isEmpty()) {
			for (final DefaultListenerManager manager : this.map.values()) {
				if (!manager.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	/***************************************************************************
	 * @return the amount of listeners in this set
	 **************************************************************************/
	public int size() {

		int size = 0;
		for (final DefaultListenerManager manager : this.map.values()) {
			size += manager.size();
		}
		return size;
	}

	/***************************************************************************
	 * Clears the set
	 **************************************************************************/
	public void clear() {

		this.map.clear();
	}

	/***************************************************************************
	 * Getr a listener manager of a givent event type
	 * 
	 * @param eventType
	 *            type of event
	 * @return listener manager
	 ***************************************************************************/
	private DefaultListenerManager ensureManagerOf(final Class<?> eventType) {

		DefaultListenerManager manager = this.map.get(eventType);
		if (manager == null) {
			manager = new DefaultListenerManager(eventType);
			this.map.put(eventType, manager);
		}
		return manager;
	}

	/***************************************************************************
	 * Fires events in backgraound
	 ***************************************************************************/
	private void runInBackgrount() {

		while (true) {
			try {
				final Object event = this.eventQueue.poll();
				if (event != null) {
					final DefaultListenerManager manager = this.map.get(event
							.getClass());
					if (manager != null) {
						manager.invokeAll(event);
					}
				} else {
					Thread.yield();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***************************************************************************
    *
    ***************************************************************************/
	private final Map<Class<?>, DefaultListenerManager> map = new HashMap<Class<?>, DefaultListenerManager>();
	private final Queue<Object> eventQueue = new ArrayBlockingQueue<Object>(50);
	private final Thread thread;
}
