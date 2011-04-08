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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
/*******************************************************************************
 * A dummy datasource returning the same connection every time.
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
final class DummyDataSource implements DataSource {

	/***************************************************************************
	 * A constructor
	 * 
	 * @param target
	 *            an objects that defines command execution methods.
	 **************************************************************************/
	public DummyDataSource(final Connection connection) {
	
		if(connection == null) {
			throw new NullPointerException("Null connection!");
		}
		this.connection = connection;
	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public PrintWriter getLogWriter() throws SQLException {
		
		return null;
	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public int getLoginTimeout() throws SQLException {
		
		return 0;
	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public void setLogWriter(PrintWriter arg0) throws SQLException {

	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public void setLoginTimeout(int arg0) throws SQLException {

	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		
		return false;
	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		
		return null;
	}
	/***************************************************************************
	 * @see DataSource#getConnection()
	 **************************************************************************/
	public Connection getConnection() throws SQLException {

		return this.connection;
	}
	/***************************************************************************
	 * Not implemented
	 **************************************************************************/
	public Connection getConnection(String arg0, String arg1)
			throws SQLException {

		return null;
	}
	/***************************************************************************
	 * 
	 **************************************************************************/
	private Connection connection;
}
