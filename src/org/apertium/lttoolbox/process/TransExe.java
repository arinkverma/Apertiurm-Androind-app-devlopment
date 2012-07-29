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

import org.apertium.lttoolbox.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apertium.lttoolbox.Alphabet.IntegerPair;

/**
 * Transducer class for execution of lexical processing algorithms
 * @author Raah
 */
public class TransExe {
    
    /**
     * Initial state
     */
    private int initial_id;
    /**
     * Node list
     */
    private Node[] node_list2;
    /**
     * Set of final nodes
     */
    private ArrayList<Node> finals2 = new ArrayList<Node>();

    //Index of the last node added to node_list
    // private Integer index =0;
    
    TransExe() {
    }

    void read(InputStream input, Alphabet alphabet) throws IOException {
        //index = 0;
        initial_id = Compression.multibyte_read(input);
        final int finals_size = Compression.multibyte_read(input);
        //System.out.println("finals_size : "+finals_size);


        int base = 0;

        // first comes the list of all final nodes
        int[] myfinals2 = new int[finals_size];
        for (int i=0; i<finals_size; i++) {
            base += Compression.multibyte_read(input);
            myfinals2[i] = base;
        }


        final int number_of_states = Compression.multibyte_read(input);
        base = number_of_states;

        //System.out.println("number of states : "+number_of_states);
        //int maxmax = 0;
        //int ant1 = 0;

        node_list2 = new Node[number_of_states];
        for (int current_state = 0; current_state<number_of_states; current_state++) {
          node_list2[current_state] = new Node();
        }

        for (int current_state = 0; current_state<number_of_states; current_state++) {
          Node sourceNode = node_list2[current_state];
            
          int number_of_local_transitions = Compression.multibyte_read(input);
          sourceNode.initTransitions(number_of_local_transitions);
          int tagbase = 0;

          while (number_of_local_transitions > 0) {
              number_of_local_transitions--;
              tagbase += Compression.multibyte_read(input);
              int state = (current_state + Compression.multibyte_read(input)) % base;
              IntegerPair pair = alphabet.decode(tagbase);
              int i_symbol = pair.first;
              int o_symbol = pair.second;
              Node targetNode = node_list2[state];
              sourceNode.addTransition(i_symbol, o_symbol, targetNode);
          }

          //int max = 0;
          //for (Transition x : sourceNode.transitions.values()) max = Math.max(max, x.size);
          //maxmax = Math.max(max, maxmax);
          //if (max>1) ;//System.err.println("sourceNode.transitions = " + max+ " "+maxmax);
          //else ant1++;
        }


        //System.err.println(ant1 + " ettere ud af  " + number_of_states);

        for (int i=0; i<finals_size; i++) {
            base = myfinals2[i];
            finals2.add(node_list2[base]);
        }

    }
/*
    private void unifyFinals() {

        index++;
        while (node_list.containsKey(index)) {
            index++;
        }
        node_list.put(index,new Node());

        Node newfinal = node_list.get(index);

        for (Node it : finals) {

            it.addTransition(0, 0, newfinal);
        }

        finals.clear();
        finals.add(newfinal);
    }
*/
    public Node getInitial() {
        return node_list2[initial_id];
    }

    ArrayList<Node> getFinals() {
        return finals2;
    }
/*
    public String toString() {
      return "index="+this.index +"/initial_id="+ this.initial_id +"/finals="+  this.finals;
    }*/
}
