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


/*******************************************************************************
 * In interface common to all command executors.
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
public interface ICommandExecutor {
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
	Object execute(final Object command) throws Throwable;
}
