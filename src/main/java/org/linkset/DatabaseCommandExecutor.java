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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

/*******************************************************************************
 * A class dispatching commands. If data surce or connection is passed to a
 * constructor then it processes commands transactionally.
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
public final class DatabaseCommandExecutor implements ICommandExecutor {

	/***************************************************************************
	 * A constructor
	 * 
	 * @param target
	 *            an objects that defines command execution methods.
	 * @param connection
	 *            a jdbc connection
	 **************************************************************************/
	public DatabaseCommandExecutor(final Object target,
			final Connection connection) {

		this(target, new DummyDataSource(connection));
	}

	/***************************************************************************
	 * A constructor
	 * 
	 * @param target
	 *            an objects that defines command execution methods.
	 * @param dataSource
	 *            a jdbc data source
	 **************************************************************************/
	public DatabaseCommandExecutor(final Object target,
			final DataSource dataSource) {

		this.target = target;
		this.dataSource = dataSource;
		if (dataSource == null) {
			throw new NullPointerException("Null data source.");
		}

		for (final Method method : target.getClass().getDeclaredMethods()) {

			final ExecutorOf annotation = method
					.getAnnotation(ExecutorOf.class);
			if (annotation != null) {
				verifySignature(method, annotation.value());
				method.setAccessible(true);
				final Method prevoius = this.map
						.put(annotation.value(), method);

				if (prevoius != null) {
					throw new RuntimeException("Multiple executors found for: "
							+ annotation.value());
				}
			}
		}
	}

	/***************************************************************************
	 * Varifies signature of a given method.
	 * 
	 * @param method
	 *            an executor method.
	 * @param cmdCls
	 *            a class of command.
	 **************************************************************************/
	private static void verifySignature(final Method method,
			final Class<?> cmdCls) {

		final Class<?> parameters[] = method.getParameterTypes();
		if (parameters.length != 2) {
			throw new RuntimeException("Executor " + method
					+ " must have exactly 2 parameters");
		}
		if (!parameters[0].equals(cmdCls)) {
			throw new RuntimeException("Executor " + method
					+ " must have the first parameter of type: " + cmdCls);
		}
		if (!parameters[1].equals(Connection.class)) {
			throw new RuntimeException("Executor " + method
					+ " must have the second parameter of type: "
					+ "java.sql.Connection");
		}
		if (method.getReturnType().equals(Void.class)) {
			throw new RuntimeException("Executor " + method
					+ " must return null of type 'Null'.");
		}
	}

	/***************************************************************************
	 * A method that executes a supplied command.
	 * 
	 * @param command
	 *            a command to execute - if it implements the java.util.List
	 *            interface than a bach bommand is assumend
	 * @return return value of a command execution method, or a list containing
	 *         results of bach commands in the same order
	 * @throws a
	 *             throwable thrown by command execution method.
	 **************************************************************************/
	@SuppressWarnings("unchecked")
	public Object execute(final Object command) throws Throwable {

		final Connection connection = this.dataSource.getConnection();
		try {
			Object returnValue;
			if (command instanceof List) {
				returnValue = executeBatch((List<Object>) command, connection);
			} else {
				returnValue = executeSingle(command, connection);
			}
			connection.commit();
			return returnValue;
		} catch (final Exception e) {
			connection.rollback();
			throw e;
		}
	}

	/***************************************************************************
	 * A method that executes a supplied command.
	 * 
	 * @param command
	 *            a command to execute
	 * @param connection
	 *            a database connection
	 * @return return value of a command execution method.
	 * @throws a
	 *             throwable thrown by command execution method.
	 **************************************************************************/
	private Object executeSingle(final Object command,
			final Connection connection) throws Throwable {

		final Method method = this.map.get(command.getClass());
		if (method != null) {
			try {
				final Object result = method.invoke(this.target, command,
						connection);
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
	 * A method that executes a list of supplied commands
	 * 
	 * @param commands
	 *            a list of commands to execute
	 * @param connection
	 *            a database connection
	 * @return a list of return values of commands execution methods.
	 * @throws a
	 *             throwable thrown by command execution method.
	 **************************************************************************/
	private List<Object> executeBatch(final List<Object> commands,
			final Connection connection) throws Throwable {

		final ArrayList<Object> results = new ArrayList<Object>(commands.size());

		for (final Object command : commands) {
			results.add(executeSingle(command, connection));
		}

		return results;
	}

	/***************************************************************************
	 * 
	 **************************************************************************/
	private final HashMap<Class<?>, Method> map = new HashMap<Class<?>, Method>();
	private final Object target;
	private final DataSource dataSource;
}
