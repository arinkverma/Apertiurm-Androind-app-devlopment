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

import java.util.Map;
import java.util.TreeMap;

public class MatchNode {


  /**
   * The outgoing transitions from this node.
   * Schema: (input symbol, destination)
   */
  private Map<Integer,MatchNode> transitions;

  public MatchNode() {
    //transitions = new HashMap<Integer,MatchNode>(4);
    transitions = new TreeMap<Integer,MatchNode>();
  }

  public MatchNode(int svsize) {
    //transitions = new HashMap<Integer,MatchNode>(svsize);
    transitions = new TreeMap<Integer,MatchNode>();
  }

  public MatchNode(MatchNode n) {
    transitions.putAll(n.transitions);
  }

  public MatchNode(Integer[] n) {
    //TODO
  }


  /*
   * Hmm ..... se: ../../lttoolbox/lttoolbox/sorted_vector.*
   */

  void addTransition(int i, MatchNode d) {
      //System.err.println(this+".addTransition("+ i+", "+d+",  "+pos);

    //  transitions[i] = d;
    MatchNode n = transitions.put(i, d);
    if (n!=null) {
      System.err.println("HMM!!! n = " + n);
    }
  }

  MatchNode transitions_get(int symbol) {
    return transitions.get(symbol);
  }
}
