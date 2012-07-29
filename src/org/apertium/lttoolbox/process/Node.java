package org.apertium.lttoolbox.process;

/*
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

import java.util.Map;
import java.util.HashMap;

/**
 * Node class of TransExe.
 * @author Raah
 */
public class Node {

    /**
     * The outgoing transitions of this node.
     * Schema: (input symbol, (output symbol, destination))
     */
    Map<Integer, Transition> transitions;

    /**
     * The constructor
     */
    Node() {
    }
   
/*
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 47
number_of_local_transitions = 14
number_of_local_transitions = 16
number_of_local_transitions = 15
number_of_local_transitions = 15
number_of_local_transitions = 28
number_of_local_transitions = 28
number_of_local_transitions = 29
number_of_local_transitions = 28
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 0
number_of_local_transitions = 10
number_of_local_transitions = 1
number_of_local_transitions = 14
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 3
number_of_local_transitions = 2
number_of_local_transitions = 27
number_of_local_transitions = 28
number_of_local_transitions = 28
number_of_local_transitions = 12
number_of_local_transitions = 1
number_of_local_transitions = 2
number_of_local_transitions = 2
number_of_local_transitions = 1
number_of_local_transitions = 1
number_of_local_transitions = 1


 number_of_local_transitions = 2
addTransition(i = 116
addTransition(i = 115
number_of_local_transitions = 1
addTransition(i = 101
number_of_local_transitions = 2
addTransition(i = 105
addTransition(i = 32
number_of_local_transitions = 14
addTransition(i = 114
addTransition(i = 100
addTransition(i = 116
addTransition(i = 112
addTransition(i = 98
addTransition(i = 99
addTransition(i = 102
addTransition(i = 103
addTransition(i = 106
addTransition(i = 108
addTransition(i = 109
addTransition(i = 110
addTransition(i = 115
addTransition(i = 118
number_of_local_transitions = 4
addTransition(i = 114
addTransition(i = 99
addTransition(i = 110
addTransition(i = 117

 */
    void initTransitions(int number_of_local_transitions) {
        transitions = new HashMap<Integer, Transition>(number_of_local_transitions);
    }

    public static final boolean FAST_BUT_REVERSE_ORDER = false;
    /**
     * Making a link between this node and another
     * @param i input symbol
     * @param o output symbol
     * @param d destination
     */
    void addTransition(int i, int o, Node d) {

        Transition newTransition = new Transition();
        newTransition.output_symbol = o;
        newTransition.dest = d;

        if (FAST_BUT_REVERSE_ORDER) {
          Transition oldTransition = transitions.put(i, newTransition);
          // if there was already a transition it is putted behind the new one in a linked list structure
          newTransition.next = oldTransition;
        } else {
          Transition oldTransition = transitions.get(i);
          if (oldTransition==null) transitions.put(i, newTransition);
          else {
            while (oldTransition.next!=null) oldTransition=oldTransition.next;
            oldTransition.next =newTransition;
          }
        }
    }

   public String toString() {
    return "Node{"+this.transitions+"}@"+hashCode();
  }

}
