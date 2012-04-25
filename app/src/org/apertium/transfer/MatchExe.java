package org.apertium.transfer;
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


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import org.apertium.lttoolbox.Compression;
import org.apertium.lttoolbox.compile.Transducer;
import org.apertium.lttoolbox.collections.IntSet;

/**
 * The container object that contains all states (and transitions betweem them)
 * @author Jacob Nordfalk
 */
public class MatchExe {

  /**
   * Initial state
   */
  private int initial_id;

  /**
   * MatchNode list
   * Schema:
   * - node_list[node_id] gives transitions.for node_id
   * - node_list[node_id][0] gives 1st input symbol
   * - node_list[node_id][1] gives 1st input symbol's target node_id
   * - node_list[node_id][2] gives 2nd input symbol
   * - node_list[node_id][3] gives 2nd input symbol's target node_id
   * ...
   * if there is an UNEVEN number of elements in node_list[node_id], then its a FINAL node, and
   * - node_list[node_id][node_list[node_id].length-1] gives the finalnumber=output symbol=rule number
   *
   * SO: Set of final nodes is those which 2nd index of node_list is uneven
   */
  int[][] node_list;

  public MatchExe(MatchExe te) {
    _copy(te);
  }


  public MatchExe(InputStream in, int decalage) throws IOException {

    //reading the initial state - set up initial node
    initial_id = Compression.multibyte_read(in);

    //reading the list of final states - not used
    int number_of_finals = Compression.multibyte_read(in);

      /* Discard the bytes
       * for the list of final states, since this implementation doesn't actually
       * use that list. But we need to advance the read pointer past the finals
       * list.
       */
      for (int i = number_of_finals; i > 0; i--) {
    	  /* No need for an assignment as we're discarding the data anyway.
    	   */
    	  Compression.multibyte_read(in);
          // its not needed to keep track of them, just skip them
          //base += read;
          //t.finals.add(base);
          //System.err.println("t_finals.add( base = " + base);
      }


    //reading the transitions
    int number_of_states = Compression.multibyte_read(in);

    // memory allocation
    node_list = new int[number_of_states][];
    _readAndAllocateTransitions(number_of_states, in, decalage);


    // set up finals
   //  values for en_ca_t1x: {14=1, 41=2, 48=2, 55=2, 62=2, 69=2, 76=2, 83=2, 90=2, 97=2, 103=90, 106=90, 109=90, ...
   // ... 420739=211, 420741=213, 420743=215, 420745=215, 420747=215, 420749=216}
   // noOfFinals == number of rules


    _readAndAddFinals(in, number_of_finals);
  }

  private void _readAndAllocateTransitions(int number_of_states, InputStream in, int decalage) throws IOException {
    int current_state=0;
    for (int i=number_of_states; i>0; i--) {
      int number_of_local_transitions=Compression.multibyte_read(in);
      if (number_of_local_transitions>0) {
        int[] mynode=node_list[current_state]=new int[number_of_local_transitions*2];
        int symbol=0;
        int n=0;
        for (int j=number_of_local_transitions; j>0; j--) {
          symbol+=Compression.multibyte_read(in)-decalage;
          int target_state=(current_state+Compression.multibyte_read(in))%number_of_states;
          mynode[n++]=symbol;
          mynode[n++]=target_state;
          //              System.err.println(current_state+ "( "+symbol+" "+(char)symbol+")  -> "+target_state);
        }
      } else {
        // then it must be a final state - we handle that below
      }
      current_state++;
    }
  }

  private void _readAndAddFinals(InputStream in, int number_of_finals) throws IllegalStateException, IOException {
    // was int[number_of_finals+1][] here, but it seems that a (postchunk) file
    // with just one rule match (apertium-eo-en.en-eo.t3x) needs a +1 here
    int[][] singleFinalNodeCache = new int[number_of_finals+1][]; // TODO share instances

    int noOfFinals2=Compression.multibyte_read(in);
    if (noOfFinals2!=number_of_finals) {
      System.err.println("INCONSISTENCY WARNING");
      System.err.println("1st number_of_finals = "+number_of_finals);
      System.err.println("2nd number_of_finals = "+noOfFinals2);
      throw new IllegalStateException("INCONSISTENCY on number_of_finals");
    }
    for (int i=0; i!=number_of_finals; i++) {
      int key=Compression.multibyte_read(in);
      int value=Compression.multibyte_read(in); // value == rule number (method nomber)
      int[] node=node_list[key];
      if (node==null) {
        // final states have an uneven number of ints (mostly 1)
        int[] mynode=singleFinalNodeCache[value];
        if (mynode==null) {
          mynode=singleFinalNodeCache[value]=new int[1];
          mynode[0]=value;
        }
        node_list[key]=mynode;
        ;
      } else {
        // final states have an uneven number of ints (mostly 1)
        // make an array 1 longer
        int[] mynode=new int[node.length+1];
        System.arraycopy(node, 0, mynode, 0, node.length);
        mynode[mynode.length-1]=value;
        node_list[key]=mynode;
      }
      //System.err.println("node_list["+key+"] = " + Arrays.toString(node_list[key]));
      //System.err.println("node_list["+key+"] = " + Arrays.toString(node_list[key]));
    }
  }




  @Deprecated
  public MatchExe(Transducer t, Map<Integer, Integer> final_type) {
    // System.err.println("final_type = " + new TreeMap<Integer, Integer>(final_type));
    // approx every 7th value is set. For en-ca (big pair)
    // final_type = {14=1, 41=2, 48=2, 55=2, 62=2, 69=2, 76=2, 83=2, 90=2, 97=2, 103=90, 106=90, 109=90,
    // ...
    // 420739=211, 420741=213, 420743=215, 420745=215, 420747=215, 420749=216}

    // set up initial node
    try {
        initial_id = t.getInitial();

        int limit=t.transitions.size();

        // memory allocation
        node_list = new int[limit][];

        // set up the transitions
        for (int node_id = 0; node_id < limit; node_id++) { //Loop through node_id's
            /* Each entry in the ArrayList Transducer.transitions, is a state.
             * These states correspond to the node_id's which make up the first
             * level of the node_list array.
             * 
             * Now, for the Maps that are the entries in the ArrayList.
             * The key is the input symbol, and the value is a list of target node_id's.
             * 
             */
            final Map<Integer, IntSet> second=t.transitions.get(node_id);

            /* Using an ArrayList because we don't know how many elements we'll have
             * up front, and it's not worth trying to calculate ahead of time.
             */
            ArrayList<Integer> currArray = new ArrayList<Integer>();
            for (Integer it2First : second.keySet()) { //Loop through input symbols
                IntSet it2Second=second.get(it2First);
                for (Integer integer : it2Second) { //Loop through targets
                    //mynode.addTransition(it2First, my_node_list[integer]);
                    currArray.add(it2First);
                    currArray.add(integer);
                }
            }
            /* Check if there is a final for this node_id, if so, add it.
             * Since we're iterating through all possible nodes, no need to worry 
             * about missing any of the finals.
             */
            Integer final_symbol;
            if((final_symbol = final_type.get(node_id)) != null) {
                currArray.add(final_symbol);
            }
            //Add temporary array to node_list
            int[] curr_node_list = node_list[node_id] = new int[currArray.size()];
            Integer[] currArrayArray = currArray.toArray(new Integer[1]);
            //Can't directly cast from Integer[] to int[]
            for(int i = 0; i < currArray.size(); i++) {
                curr_node_list[i] = currArrayArray[i];
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

  }

  private void _copy(MatchExe te) {
    initial_id = te.initial_id;
    node_list = te.node_list;
  }


  public int getInitial() {
    return initial_id;
  }


}