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
package com.example;

import org.linkset.*;

/*******************************************************************************
 * The handler test class
 * 
 * @author Łukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public final class HandlerTestClass1 {

    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "method1")
    private void method1() {

        System.out.println("Method1 invoked.");
        this.method1Invoked = true;
    }
    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "method12")
    private void method12() {

        System.out.println("Method1 invoked.");
        this.method12Invoked = true;
    }
    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "returnValueMethod")
    private String returnValueMethod() {

        return "123";
    }
    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "parameterMethod")
    private void parameterMethod(final String param1, final int param2) {

        System.out.println("parameterMethod invoked.");
        this.parameter1 = param1;
        this.parameter2 = param2;
    }
    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "staticParameterMethod")
    private static void staticParameterMethod(final String param1, final int param2) {

        System.out.println("parameterMethod invoked.");
        staticParameter1 = param1;
        staticParameter2 = param2;
    }
    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "staticMethod1")
    private static void staticMethod1() {

        System.out.println("staticMethod1 invoked.");
        staticMethod1Invoked = true;
    }
    /***************************************************************************
     *
     **************************************************************************/
    @HandlerMethod(id = "staticMethod12")
    private static void staticMethod12() {

        System.out.println("staticMethod1 invoked.");
        staticMethod12Invoked = true;
    }
    /***************************************************************************
     *
     **************************************************************************/
    public boolean method1Invoked = false;
    public boolean method12Invoked = false;
    public static boolean staticMethod1Invoked = false;
    public static boolean staticMethod12Invoked = false;
    public String parameter1 = null;
    public int parameter2 = 0;
    public static String staticParameter1 = null;
    public static int staticParameter2 = 0;
}
