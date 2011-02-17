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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.example.ExampleClass;
import com.example.ExampleInterface;
import com.example.SuperClass;
import com.example.SuperInterface;


/*******************************************************************************
 * The test class
 * 
 * @author Łukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public class TestDProxy {

	/***************************************************************************
    *
    ***************************************************************************/
	@Test
	public void test_null_arguments() throws Exception {

		try {
			DProxy.make(null, "examle");
			fail();
		} catch (final NullPointerException e) {
			assertTrue(true);
		}

		try {
			DProxy.make(SuperInterface.class, null);
			fail();
		} catch (final NullPointerException e) {
			assertTrue(true);
		}
	}

	/***************************************************************************
    *
    ***************************************************************************/
	@Test
	public void test_incompatible() throws Exception {

		try {
			DProxy.make(SuperInterface.class, "example");
			fail();
		} catch (final IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DProxy.make(ExampleInterface.class, new SuperClass());
			fail();
		} catch (final IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/***************************************************************************
    *
    ***************************************************************************/
	@Test
	public void test_proper() throws Exception {

		final Object arg = new Object();

		final SuperInterface superProxy = DProxy.make(
				SuperInterface.class, new ExampleClass());
		
		assertEquals(arg.toString(), superProxy.foo(arg));
		
		final ExampleInterface exampleProxy = DProxy.make(
				ExampleInterface.class, new ExampleClass());
		
		assertEquals(arg.toString(), exampleProxy.foo(arg));
		assertEquals(arg.hashCode(), exampleProxy.bar(arg));
	}
}
