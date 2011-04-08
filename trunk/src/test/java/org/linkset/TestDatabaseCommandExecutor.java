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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/*******************************************************************************
 * The test class
 * 
 * @author Łukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public class TestDatabaseCommandExecutor {

	/***************************************************************************
    *
    **************************************************************************/
	@Before
	public void setUp() throws Exception {
		Class.forName("org.hsqldb.jdbc.JDBCDriver" );
		c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
		c.createStatement().execute("create table t1(char(10) name)");
		c.commit();
		c.setAutoCommit(false);
	}

	/***************************************************************************
    *
    **************************************************************************/
	@Test
	public void test_execute() throws Throwable {

		final DatabaseCommandExecutor executor = new DatabaseCommandExecutor(
				this, this.c);

		final List<Object> commands = new ArrayList<Object>();
		commands.add("string");
		commands.add(new Integer(1));
		executor.execute(commands);

		ResultSet rs = c.createStatement().executeQuery(
				"select count * from t1");
		rs.next();
		assertEquals(0, rs.getInt(1));
	}

	/***************************************************************************
    *
    **************************************************************************/
	@SuppressWarnings("unused")
	@ExecutorOf(String.class)
	private Null command1(final String comand, final Connection cn)
			throws Exception {

		cn.createStatement().execute("insert into t1 values ('text1')");
		return null;
	}

	/***************************************************************************
    *
    **************************************************************************/
	@SuppressWarnings("unused")
	@ExecutorOf(Integer.class)
	private Null command2(final Integer comand, final Connection cn)
			throws Exception {

		throw new Exception();
	}

	private Connection c;
}
