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
import com.example.SubEvent;

/*******************************************************************************
 * The test class
 * 
 * @author Łukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public class TestHerarchyAwareEventBus {

	/***************************************************************************
    *
    ***************************************************************************/
	@Before
	public void setUp() throws Exception {

		this.eventBus = new HierarchyAwareEventBus();
		this.eventHandlerInvoked = false;
		this.subEventHandlerInvoked = false;
	}

	/***************************************************************************
    *
    ***************************************************************************/
	@Test
	public void test_invoke_event() throws Exception {

		this.eventBus.add(Event.class, this, "eventHandler");
		this.eventBus.add(SubEvent.class, this, "subEventHandler");
		this.eventBus.fire(new Event());

		Assert.assertTrue(this.eventHandlerInvoked);
		Assert.assertFalse(this.subEventHandlerInvoked);
	}

	/***************************************************************************
    *
    ***************************************************************************/
	@Test
	public void test_invoke_sub_event() throws Exception {

		this.eventBus.add(Event.class, this, "eventHandler");
		this.eventBus.add(SubEvent.class, this, "subEventHandler");
		this.eventBus.fire(new SubEvent());

		Assert.assertTrue(this.eventHandlerInvoked);
		Assert.assertTrue(this.subEventHandlerInvoked);
	}

	/***************************************************************************
    *
    **************************************************************************/
	@HandlerMethod(id = "eventHandler")
	private void eventHandler(final Event evet) {

		this.eventHandlerInvoked = true;
	}

	/***************************************************************************
    *
    **************************************************************************/
	@HandlerMethod(id = "subEventHandler")
	private void subEventHandler(final SubEvent evet) {

		this.subEventHandlerInvoked = true;
	}

	/***************************************************************************
    *
    ***************************************************************************/
	private HierarchyAwareEventBus eventBus;
	private boolean eventHandlerInvoked = false;
	private boolean subEventHandlerInvoked = false;
}
