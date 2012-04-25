package org.apertium.lttoolbox.process;

/*
 * Copyright (C) 2005 Universitat d'Alacant / Universidad de Alicante
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Pool of T objects
 * Re-written by Jacob Nordfalk Apr 19 2010
 */
public class Pool<T> {

  public interface ObjectFactory<E> {
    E next();
    void reset(E e);
 }

    /**
     * Free pointers to objects
     */
    ArrayList<T> free  = new ArrayList<T>(200);    

    ObjectFactory<T> objectfactory;


    /**
     * Parametrized constructor
     * @param nelems initial size of the pool
     * @param objectfactory factory for creating additional objects in the pool
     */
    Pool(ObjectFactory<T> objectfactory) {
        this.objectfactory = objectfactory;
    }

    /**
     * Allocate a pointer to a free 'new' object.
     * @return pointer to the object
     */
    T get() {
      /*
      free.clear();
      T tmp = objectfactory.next();
      return tmp;*/

      int size = free.size();
        if (size != 0) {
            T item = free.remove(size-1);

            //System.err.println("item = " + item);
            objectfactory.reset(item);

            //System.err.println("item2 = " + item);
            return item;
        } else {
            T tmp = objectfactory.next();
            return tmp;
        }/**/
    }

    /**
     * Release a no more needed instance of a pooled object
     * @param item the no more needed instance of the object
     */
    void release(T item) {
        free.add(item);
        objectfactory.reset(item);
    }
}
