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

import com.example.HandlerTestClass2;
import com.example.HandlerTestClass1;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/*******************************************************************************
 * The test class
 * 
 * @author Łukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public class TestDefaultListenerManager {

    /***************************************************************************
     *
     **************************************************************************/
    @Before
    public void setUp() throws Exception {

        this.listenerSet = new DefaultListenerManager();
    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_add() {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();

        Assert.assertEquals(0, this.listenerSet.size());
        this.listenerSet.add(handler1, "method1");
        this.listenerSet.add(handler1.getClass(), "staticMethod1");
        Assert.assertEquals(2, this.listenerSet.size());

    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_add_twice() {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();

        Assert.assertEquals(0, this.listenerSet.size());
        this.listenerSet.add(handler1, "method1");
        Assert.assertEquals(1, this.listenerSet.size());
        this.listenerSet.add(handler1, "method1");
        Assert.assertEquals(1, this.listenerSet.size());
    }
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_remove() {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();
        final HandlerTestClass2 handler2 = new HandlerTestClass2();

        this.listenerSet.add(handler1, "method1");
        this.listenerSet.add(handler2, "method2");
        this.listenerSet.add(handler2.getClass(), "staticMethod2");

        Assert.assertEquals(3, this.listenerSet.size());
        this.listenerSet.remove(handler1, "method1");
        Assert.assertEquals(2, this.listenerSet.size());
        this.listenerSet.remove(handler2.getClass(), "staticMethod2");
        Assert.assertEquals(1, this.listenerSet.size());

    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_removeAll() {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();
        final HandlerTestClass2 handler2 = new HandlerTestClass2();

        this.listenerSet.add(handler1, "method1");
        this.listenerSet.add(handler1, "method12");
        this.listenerSet.add(handler1.getClass(), "staticMethod1");
        this.listenerSet.add(handler1.getClass(), "staticMethod12");
        this.listenerSet.add(handler2, "method2");
        Assert.assertEquals(5, this.listenerSet.size());

        this.listenerSet.removeAll(handler1);
        Assert.assertEquals(3, this.listenerSet.size());
        this.listenerSet.removeAll(handler1.getClass());
        Assert.assertEquals(1, this.listenerSet.size());

    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_invokeAll() throws Exception {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();
        HandlerTestClass1.staticMethod12Invoked = false;
        HandlerTestClass1.staticMethod12Invoked = false;

        this.listenerSet.add(handler1, "method1");
        this.listenerSet.add(handler1, "method12");
        this.listenerSet.add(handler1.getClass(), "staticMethod1");
        this.listenerSet.add(handler1.getClass(), "staticMethod12");

        this.listenerSet.invokeAll();
        Assert.assertTrue(handler1.method1Invoked);
        Assert.assertTrue(handler1.method12Invoked);
        Assert.assertTrue(HandlerTestClass1.staticMethod1Invoked);
        Assert.assertTrue(HandlerTestClass1.staticMethod12Invoked);

    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_invokeAllWithParameters() throws Exception {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();

        this.listenerSet.add(handler1, "parameterMethod");
        this.listenerSet.invokeAll("123", 123);
        Assert.assertEquals("123", handler1.parameter1);
        Assert.assertEquals(123, handler1.parameter2);

    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_invokeAllStaticWithParameters() throws Exception {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();
        HandlerTestClass1.staticParameter1 = null;
        HandlerTestClass1.staticParameter2 = 0;

        this.listenerSet.add(handler1.getClass(), "staticParameterMethod");
        this.listenerSet.invokeAll("123", 123);
        Assert.assertEquals("123", HandlerTestClass1.staticParameter1);
        Assert.assertEquals(123, HandlerTestClass1.staticParameter2);

    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_invokeReturnValueMethod() throws Exception {

        final HandlerTestClass1 handler1 = new HandlerTestClass1();
        final MethodPointer pointer = new MethodPointer(handler1, "returnValueMethod");

        Assert.assertEquals("123", pointer.invoke());
    }
    /***************************************************************************
     *
     **************************************************************************/
    @Test
    public void test_ensureParameterTypes() throws Exception {

        final DefaultListenerManager set = new DefaultListenerManager(String.class, Boolean.class);
        final HandlerTestClass1 handler1 = new HandlerTestClass1();

        try {
            set.add(handler1.getClass(), "staticParameterMethod");
            Assert.fail("Parameters not verified.");
        } catch (final IllegalArgumentException e) {
            Assert.assertEquals("Nonconformant parameter types.", e.getMessage());
        }
    }
    /***************************************************************************
     *
     **************************************************************************/
    private DefaultListenerManager listenerSet;
}
