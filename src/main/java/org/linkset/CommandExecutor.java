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
import java.util.HashMap;

/*******************************************************************************
 * A class dispatching commands.
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
final class CommandExecutor {

	/***************************************************************************
	 * A constructor
	 * 
	 * @param target
	 *            an objects that defines command execution methods.
	 **************************************************************************/
	public CommandExecutor(final Object target) {

		this.target = target;

		for (final Method method : target.getClass().getDeclaredMethods()) {

			final Executor executor = method.getAnnotation(Executor.class);
			if (executor != null) {

				final Class<?> parameters[] = method.getParameterTypes();
				if (parameters.length != 1) {
					throw new RuntimeException("Executor " + method
							+ " must have only 1 parameter");
				}
				if (!parameters[0].equals(executor.cmdClass())) {
					throw new RuntimeException("Executor " + method
							+ " must have a parameter fo type: "
							+ executor.cmdClass());
				}

				if (method.getReturnType().equals(Void.class)) {
					throw new RuntimeException("Executor " + method
							+ " must return value 'Null' suggested.");
				}

				method.setAccessible(true);
				final Method prevoius = this.map.put(executor.cmdClass(),
						method);

				if (prevoius != null) {
					throw new RuntimeException("Multiple executors found for: "
							+ executor.cmdClass());
				}
			}
		}
	}

	/***************************************************************************
	 * A method that executes a supplied command.
	 * 
	 * @param command
	 *            a command to execute
	 * @return return value of a command execution method.
	 * @throws a
	 *             throwable thrown by command execution method.
	 **************************************************************************/
	public Object execute(final Object command) throws Throwable {

		final Method method = this.map.get(command.getClass());
		if (method != null) {
			try {
				final Object result = method.invoke(this.target, command);
				return result;
			} catch (final InvocationTargetException e) {
				throw e.getTargetException();
			}
		} else {
			throw new RuntimeException("No executor found for: "
					+ command.getClass());
		}
	}

	/***************************************************************************
	 * 
	 **************************************************************************/
	private final HashMap<Class<?>, Method> map = new HashMap<Class<?>, Method>();
	private final Object target;
}
