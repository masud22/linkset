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
import java.util.ArrayList;

/*******************************************************************************
 * The class that handles listener collections
 * 
 * @author Lukasz Bownik (lukasz.bownik@gmail.com)
 ******************************************************************************/
public final class DefaultListenerManager implements ListenerManager {

    /***************************************************************************
     * A default constructor. This construcotr is equivalnet to DefaultListenerManager(null)
     * @see DefaultListenerManager#DefaultListenerManager(java.lang.Class<?>[]) 
     **************************************************************************/
    public DefaultListenerManager() {

        this((Class<?>[]) null);
    }
    /***************************************************************************
     * A construtor
     * @param parameterTypes the array of parameter types of event handler methods,
     * if null then the parameter types are not checked for consistency
     **************************************************************************/
    public DefaultListenerManager(final Class<?>... parameterTypes) {

        this.parameterTypes = parameterTypes;
    }
    /***************************************************************************
     * @see ListenerManager#add(java.lang.Object, java.lang.String) 
     **************************************************************************/
    public void add(final Object target, final String methodId) {

        final MethodPointer pointer = new MethodPointer(target, methodId);
        if (!this.pointers.contains(pointer)) {

            if (this.parameterTypes != null) {
                pointer.ensureParameterTypes(this.parameterTypes);
            }
            this.pointers.add(pointer);
        }
    }
    /***************************************************************************
     * @see ListenerManager#remove(java.lang.Object, java.lang.String)
     **************************************************************************/
    public void remove(final Object target, final String methodId) {

        this.pointers.remove(new MethodPointer(target, methodId));
    }
    /***************************************************************************
     * @see ListenerManager#removeAll(java.lang.Object) 
     **************************************************************************/
    public void removeAll(final Object target) {

        if (target == null) {
            throw new NullPointerException("Null target.");
        }

        int index = 0;
        while (index < this.pointers.size()) {
            if (this.pointers.get(index).target == target) {
                this.pointers.remove(index);
            } else {
                ++index;
            }
        }
    }
    /***************************************************************************
     * @return true if a set is empty
     **************************************************************************/
    public boolean isEmpty() {

        return this.pointers.isEmpty();
    }
    /***************************************************************************
     * @return the amount of listeners in this set
     **************************************************************************/
    public int size() {

        return this.pointers.size();
    }
    /***************************************************************************
     * Clears the set
     **************************************************************************/
    public void clear() {

        this.pointers.clear();
    }
    /***************************************************************************
     * Invokes all the listeners in this set. The order of invocation is not
     * specified. Throws an exception if something goes wrong. Invocation of all
     * listeners is not guaranteed if an axception is thrown.
     *
     * @param args
     *            method arguments or null for no argument listeners
     * @throws ExceptionInInitializerError  if the initialization provoked by any method fails.
     * @throws InvocationTargetException if any underlying method throws an exception.
     **************************************************************************/
    public void invokeAll(final Object... args) throws InvocationTargetException,
            ExceptionInInitializerError {

        for (final MethodPointer pointer : this.pointers) {
            pointer.invoke(args);
        }
    }
    /***************************************************************************
     * This method is equivalent to invokeAll(null)
     * @see ListenerSet#invokeAll(java.lang.Object[]) 
     **************************************************************************/
    public void invokeAll() throws InvocationTargetException,
            ExceptionInInitializerError {

        invokeAll((Object[]) null);
    }
    /***************************************************************************
     *
     **************************************************************************/
    private final ArrayList<MethodPointer> pointers = new ArrayList<MethodPointer>();
    private final Class<?>[] parameterTypes;
}
