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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.example.Event;
import com.example.Event2;
import com.example.HandlerTestClass1;
import com.example.HandlerTestClass2;

/*******************************************************************************
 * The test class
 * 
 * @author Łukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public class TestSimpleEventBus {

	/***************************************************************************
     *
     **************************************************************************/
	@Before
	public void setUp() throws Exception {

		this.eventBus = new SimpleEventBus();
	}

	/***************************************************************************
     *
     **************************************************************************/
	@Test
	public void test_add() {

		final HandlerTestClass1 handler1 = new HandlerTestClass1();

		Assert.assertEquals(0, this.eventBus.size());
		Assert.assertTrue(this.eventBus.isEmpty());
		this.eventBus.add(Event.class, handler1, "eventMethod");
		Assert.assertEquals(1, this.eventBus.size());
		Assert.assertFalse(this.eventBus.isEmpty());

	}

	/***************************************************************************
     * 
     **************************************************************************/
	@Test
	public void test_remove() {

		final HandlerTestClass1 handler1 = new HandlerTestClass1();

		this.eventBus.add(Event.class, handler1, "eventMethod");
		this.eventBus.add(Event2.class, handler1, "event2Method");

		Assert.assertEquals(2, this.eventBus.size());
		this.eventBus.remove(Event.class, handler1, "eventMethod");
		Assert.assertEquals(1, this.eventBus.size());
		this.eventBus.remove(Event2.class, handler1, "event2Method");
		Assert.assertEquals(0, this.eventBus.size());
	}

	/***************************************************************************
    *
    **************************************************************************/
	@Test
	public void test_removeAll() {

		final HandlerTestClass1 handler1 = new HandlerTestClass1();

		this.eventBus.add(Event.class, handler1, "eventMethod");
		this.eventBus.add(Event2.class, handler1, "event2Method");

		Assert.assertEquals(2, this.eventBus.size());

		this.eventBus.removeAll(handler1);
		Assert.assertEquals(0, this.eventBus.size());
	}

	/***************************************************************************
   *
   **************************************************************************/
	@Test
	public void test_fire() throws Exception {

		final HandlerTestClass1 handler1 = new HandlerTestClass1();

		this.eventBus.add(Event.class, handler1, "eventMethod");
		this.eventBus.add(Event2.class, handler1, "event2Method");

		this.eventBus.fire(new Event());
		Assert.assertTrue(handler1.eventInvoked);
		Assert.assertFalse(handler1.event2Invoked);
	}

	/***************************************************************************
    *
    **************************************************************************/
	private SimpleEventBus eventBus;
}
