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

/**
 * A linked list of transitions.
 * Experiments show that 95 % nodes have only 1 transition, and the rest have less than 8 transitions
 * @author Jacob Nordfalk
 */
public class Transition {

  /** The output symbol (character/tag) sent when making this transition */
  int output_symbol;

  /** Destination node when makine this transition */
  Node dest;

  /** Next transition in the linked list */
  Transition next;

}
