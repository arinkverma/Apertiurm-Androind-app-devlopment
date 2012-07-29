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

import org.apertium.lttoolbox.Alphabet;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;


/**
 * one state element in  the current set of states of transducer processing
 * @author Raah
 */
class TNodeState {

    /** Which node are we currently visiting */
    Node where;

    /** The list of output symbols we produced while getting to this node */
    ArrayList<Integer> sequence;


    /** caseWasChanged means input was lowercased - thus we should consider uppercasing the output symbols  before outputting */
    boolean caseWasChanged;


    public TNodeState(Node where, ArrayList<Integer> sequence, boolean caseWasChanged) {
        this.where = where;
        this.sequence = sequence;
        this.caseWasChanged = caseWasChanged;
    }

    public TNodeState() {
    }

    /** special constructor to signal that the sequence list must be initialized */
  TNodeState(boolean b) {
    sequence = new ArrayList<Integer>();
  }

  TNodeState(int sequence_size) {
    sequence = new ArrayList<Integer>(sequence_size);
  }


    @Override
    public String toString() {
       if (Alphabet.debuggingInstance==null) return "Alphabet.debuggingInstance==null";

        StringBuilder sb = new StringBuilder(sequence==null?20 : 20+sequence.size()*2);
        if (caseWasChanged) sb.append("caseWasChanged;");
        if (sequence!=null) for (int i : sequence) sb.append(Alphabet.debuggingInstance.getSymbol(i, caseWasChanged));
        sb.append('→');
        if (where!=null) {
          for (int i : where.transitions.keySet()) sb.append(Alphabet.debuggingInstance.getSymbol(i));
          return sb.toString()+"@"+Integer.toString(where.hashCode(), Character.MAX_RADIX);
        }
        return sb.toString()+"@null";
    }
}

/**
 * Class to represent the set of alive states of a transducer
 * @author Raah
 */
public class State {
  /**
   * Whether object pooling should be done.
   * Object allocation (of small objects) on modern JVMs is even so fast that making a copy of immutable 
   * objects sometimes outperforms modification of mutable (and often old) objects.
   * See also "Object pooling is now a serious performance loss": http://www.theserverside.com/news/thread.tss?thread_id=37146
   * 
   * Pooling seems to do no difference on JDK 1.6, but it might make a big difference for smaller VMs,
   * like Androids or J2ME JVMs in phones, so we turn it on for now
   */
  public static final boolean REUSE_OBJECTS=true;

  /** Pre-allocate for this amount of elements in a sequence */
  public static final int INITAL_SEQUENCE_ALLOCATION = 50;
  
  /*
  static int oprettet=0;
  static int genbrugt=0;
  public static void printStatistik() {
    System.err.println("\ni nodeStatePool = " + nodeStatePool.size());
    System.err.println("oprettet = " + oprettet);
    System.err.println("genbrugt = " + genbrugt);
  }*/


    ArrayList<TNodeState> state = new ArrayList<TNodeState>(50);
    
    /**
     * Pool of TNodeState (with their sequence list), for efficiency
     */
    private static ArrayList<TNodeState> nodeStatePool = REUSE_OBJECTS?new ArrayList<TNodeState>(50):null;
    private TNodeState nodeStatePool_get() {
      int size = nodeStatePool.size();
      if (size != 0) {
        TNodeState tn = nodeStatePool.remove(size-1);
        tn.sequence.clear();
        //genbrugt++;
        return tn;
      } else {
        TNodeState tn = new TNodeState();
        //oprettet++;
        tn.sequence = new ArrayList<Integer>(INITAL_SEQUENCE_ALLOCATION);
        return tn;
      }
    }

    private void nodeStatePool_release(TNodeState state_i) {
      nodeStatePool.add(state_i);
    }
    /*
    Pool<TNodeState> nodeStatePool = new Pool<TNodeState>(new ObjectFactory<TNodeState>() {
      @Override public TNodeState next() {
        TNodeState tn = new TNodeState();
        tn.sequence = new ArrayList<Integer>(INITAL_SEQUENCE_ALLOCATION);
        return tn;
      }
      @Override public void reset(TNodeState e) {
        e.sequence.clear();
        e.caseWasChanged = false;
        e.where=null; }
    });
    */

     public static boolean DEBUG=false;


      State copy(State other_state) {

        //System.err.println("this.state = " + this.state);
        if (REUSE_OBJECTS) for (int i = state.size(); i > 0; ) {
          nodeStatePool_release(state.get(--i));
        }
        state.clear();

        ArrayList<TNodeState> other_states = other_state.state;
        for (int i = 0,  limit = other_states.size(); i != limit; i++) {
          TNodeState tn = other_states.get(i);
          if (REUSE_OBJECTS) {
            TNodeState copy = nodeStatePool_get();
            copy.caseWasChanged = tn.caseWasChanged;
            copy.sequence.addAll(tn.sequence);
            copy.where = tn.where;
            this.state.add(copy);
          } else {
            this.state.add(new TNodeState(tn.where, new ArrayList<Integer>(tn.sequence), tn.caseWasChanged));
          }
        }
        return this;
      }

      State copy() {
        return new State().copy(this);
      }


    /**
     * Number of alive transductions
     * @return the size
     */
   int size() {
        return state.size();
    }

    /**
     * Init the state with the initial node and empty output
     * @param initial the initial node of the transducer
     */
    public void init(Node initial) {
        state.clear();
        TNodeState tn = REUSE_OBJECTS?nodeStatePool_get():new TNodeState(true);
        tn.where = initial;
        tn.caseWasChanged = false;
        state.add(tn); 
        epsilonClosure();
    }

  public String toString() {
    return this.state.toString();
  }

    /**
     * Reference to last discarded list of nodestates
     */
    private ArrayList<TNodeState> reusable_state =  REUSE_OBJECTS ? new ArrayList<TNodeState>(50):null;


    /**
     * Make a transition, version for lowercase letters and symbols
     * @param input the input symbol
     */
    private void apply(int input) {
        ArrayList<TNodeState> new_state;
        if (REUSE_OBJECTS) {
          reusable_state.clear();
          new_state =  reusable_state;
        } else {
          new_state = new ArrayList<TNodeState>(state.size()*2);
        }


        if (input==0) { // in transfer it happens an unknown symbol is translated to 0. Avoid interpreting that as an epsilon.
          if (REUSE_OBJECTS) reusable_state = state;
          state = new_state;
          return;
        }

        for (int i = 0,  limit = state.size(); i != limit; i++) {
            TNodeState state_i = state.get(i);
            Transition it = state_i.where.transitions.get(input);
            while (it != null) {
              TNodeState tn = REUSE_OBJECTS?nodeStatePool_get(): new TNodeState(state_i.sequence.size()+1);
              tn.where = it.dest;
              tn.caseWasChanged = state_i.caseWasChanged;
              tn.sequence.addAll(state_i.sequence);
              tn.sequence.add(it.output_symbol);
              new_state.add(tn);
              it = it.next;
            }
            if (REUSE_OBJECTS) nodeStatePool_release(state_i);
        }
        if (REUSE_OBJECTS) reusable_state = state;
        state = new_state;
    }

    /**
     * Make a transition, version for lowercase and uppercase letters
     * @param input the input symbol (which is actually always uppercase)
     * @param lowerCasedInput the alternative input symbol (actually its always Alphabet.toLowerCase(input))
     */
    private void apply(int input, int lowerCasedInput) {
        ArrayList<TNodeState> new_state;
        if (REUSE_OBJECTS) {
          reusable_state.clear();
          new_state =  reusable_state;
        } else {
          new_state = new ArrayList<TNodeState>(state.size()*2);
        }

        for (int i = 0,  limit = state.size(); i != limit; i++) {
            TNodeState state_i = state.get(i);
            Transition it = state_i.where.transitions.get(input);
            while (it != null) {
              TNodeState tn = REUSE_OBJECTS?nodeStatePool_get(): new TNodeState(state_i.sequence.size()+1);
              tn.where = it.dest;
              tn.caseWasChanged = state_i.caseWasChanged;
              tn.sequence.addAll(state_i.sequence);
              tn.sequence.add(it.output_symbol);
              new_state.add(tn);
              it = it.next;
            } //XXX no pool now: pool.release(state.get(i).sequence);

            // try also apply lowerCasedInput
            it = state_i.where.transitions.get(lowerCasedInput);
            while (it != null) {
              TNodeState tn = REUSE_OBJECTS?nodeStatePool_get(): new TNodeState(state_i.sequence.size()+1);
              tn.where = it.dest;
              tn.caseWasChanged = true; // lowercased version of input
              tn.sequence.addAll(state_i.sequence);
              tn.sequence.add(it.output_symbol);
              new_state.add(tn);
              it = it.next;
            }
            if (REUSE_OBJECTS) nodeStatePool_release(state_i);
        }

        if (REUSE_OBJECTS) reusable_state = state;
        state = new_state;
    }

    /**
     * Calculate the epsilon closure over the current state, replacing its content.
     * i.e. expand to all states reachable consuming θ (the empty input symbol)
     */
    private void epsilonClosure() {
        for (int i = 0; i != state.size(); i++) {
            TNodeState state_i = state.get(i);
            // get the transitions consuming θ (the empty input symbol)
            Transition epsilonTransition = state_i.where.transitions.get(0);
            while (epsilonTransition != null) {
              TNodeState tn = REUSE_OBJECTS?nodeStatePool_get(): new TNodeState(state_i.sequence.size()+1);
                tn.where = epsilonTransition.dest;
                tn.caseWasChanged = state_i.caseWasChanged;
                tn.sequence.addAll(state_i.sequence);
                if (epsilonTransition.output_symbol != 0) {
                    tn.sequence.add(epsilonTransition.output_symbol);
                }
                state.add(tn);
                epsilonTransition = epsilonTransition.next;
            }
        }
    }

    /**
     * Calculates an extended epsilon where a set of symbols are considered epsilons
     * i.e. expand to all states reachable consuming this set of symbols
     *
    private void flagClosure(Integer[] flagMatch_symbolList) {
        for (int i = 0; i != state.size(); i++) {
            TNodeState state_i = state.get(i);
            // get the transitions consuming a symbol from the list
            for (int j=0; j<flagMatch_symbolList.length; j++) {
              Transition epsilonTransition = state_i.where.transitions.get(flagMatch_symbolList[j]);
              while (epsilonTransition != null) {
                  List<Integer> tmp; // JACOB = pool.get();
                  tmp = new ArrayList<Integer>(state_i.sequence);
                  if (epsilonTransition.output_symbol != 0) {
                      tmp.add(epsilonTransition.output_symbol);
                  }
                  state.add(new TNodeState(epsilonTransition.dest, tmp, state_i.caseWasChanged));
                  epsilonTransition = epsilonTransition.next;
              }
            }
        }
    }
   */
    void tjekDubletter() {
        //System.err.println("Duble? "+ state.size());
        for (int i = 0; i != state.size(); i++) {
          TNodeState state_i = state.get(i);
          for (int j = i+1; j != state.size(); j++) {
            TNodeState state_j = state.get(j);
            if (state_i.where == state_j.where && state_i.caseWasChanged ==state_j.caseWasChanged && state_i.sequence.equals(state_j.sequence)) {
                System.err.println("Dublet!!! "+ i + " " + j);
                System.err.println("Dublet?: state_j = " + state_i + "==" + state_j);
                new Exception().printStackTrace();
                state.remove(j);
                j--;
            }
          }
        }
    }

    /**
     * step = apply + epsilonClosure
     * @param input the input symbol
     */
    public void step(int input) {
//        if (DEBUG) System.err.println();
//        if (DEBUG) System.err.println("state f = " + state  +"     - apply (" + (char) input);
//        if (DEBUG) tjekDubletter();
        apply(input);
//        if (DEBUG) tjekDubletter();
//        if (DEBUG) System.err.println("state e1= " + state);
        epsilonClosure();
//        if (DEBUG) System.err.println("state e2= " + state);
    }

    /**
     * step = apply + epsilonClosure
     * @param input the input symbol
     * @param alt the alternative input symbol (typically lowercase version of input symbol)
     */
    public void step(int input, int lowerCasedInput) {
        apply(input, lowerCasedInput);
        epsilonClosure();
    }
/*
    public void step_case(char val, boolean caseSensitive, Integer[] flagMatch_symbolList) {
        if (!Alphabet.isUpperCase(val) || caseSensitive) {
            apply(val);
            if (DEBUG) System.err.println("state e1= " + state);
            flagClosure(flagMatch_symbolList);
            epsilonClosure();
        } else {
            apply(val, Alphabet.toLowerCase(val));
            flagClosure(flagMatch_symbolList);
            epsilonClosure();
        }
    }
*/

    public void step_case(char val, boolean caseSensitive) {
        if (!Alphabet.isUpperCase(val) || caseSensitive) {
            step(val);
        } else {
            step(val, Alphabet.toLowerCase(val));
        }
    }

    public void step_case(int val, boolean caseSensitive) {
        if (Alphabet.isTag(val) || !Alphabet.isUpperCase(val) || caseSensitive) {
            step(val);
        } else {
            step(val, Alphabet.toLowerCase(val));
        }
    }
      /**
     * Return true if at least one record of the state references a
     * final node of the set
     * @param finals set of final nodes 
     * @return true if the state is final
     */
    boolean isFinal(Set<Node> finals) {
        //if (finals.isEmpty()) return false;
        for (int i = 0,  limit = state.size(); i != limit; i++) {
            if (finals.contains(state.get(i).where)) {
                return true;
            }
        }
        return false;
    }

    /** Slower version of the StringBuilder filterFinals() */
    String filterFinals(Set<Node> finals, Alphabet alphabet, SetOfCharacters escaped_chars, boolean uppercase, boolean firstupper) {
      return filterFinals(new StringBuilder(), finals, alphabet, escaped_chars, uppercase, firstupper).toString();
    }


    /**
     * Print all outputs of current parsing, preceeded by a bar '/', from the final nodes of the state. Examples:
     * /le<prn><pro><p3><nt>/le<det><def><m><sg>/le<prn><pro><p3><m><sg>
     * /domaine<n><m><sg>
     * /,<cm>
     * @param result append to this buffer
     * @param finals the set of final nodes
     * @param alphabet the alphabet to decode strings
     * @param escaped_chars the set of chars to be preceeded with one backslash
     * @param uppercase true if the word is uppercase
     * @param firstupper true if the first letter of a word is uppercase
     * @return the result of the transduction
     */
    StringBuilder filterFinals(StringBuilder result, Set<Node> finals, Alphabet alphabet, SetOfCharacters escaped_chars, boolean uppercase, boolean firstupper) {

        for (int i = 0,  limit = state.size(); i != limit; i++) {
            TNodeState state_i = state.get(i);
            if (finals.contains(state_i.where)) {
                result.append('/');
                int first_char = result.length();

                // make all uppercase if original word was uppercase && case was changed to lowercase during match
                boolean upc = uppercase &&  state_i.caseWasChanged;

                for (int j = 0,  limit2 = state_i.sequence.size(); j != limit2; j++) {
                    int symbol = ((state_i.sequence).get(j)).intValue();
                    if (escaped_chars.contains((char) symbol)) {
                        result.append('\\');
                    }
                    result.append(alphabet.getSymbol(symbol, upc));
                }


                if (firstupper && state_i.caseWasChanged ) {
                    if (result.charAt(first_char) == '~') {
                        // skip post-generation mark
                        result.setCharAt(first_char + 1, Alphabet.toUpperCase(result.charAt(first_char + 1)));
                    } else {
                        result.setCharAt(first_char, Alphabet.toUpperCase(result.charAt(first_char)));
                    }
                }
            }
        }
      //System.err.println("filterFinals RET ( " + result);
        return result;
    }

            /*
            if (DEBUG && finals.contains(state_i.where) != state_i.where.transitions.isEmpty()) {
              System.err.println("!HM finals.contains(state_i.where) = " + finals.contains(state_i.where));
              System.err.println("!HM state_i.where.transitions = " + state_i);
            }*/

    /**
     * Find final states, remove those that not has a requiredSymbol and 'restart' each of them as the set of initial states, but remembering the sequence and adding a separationSymbol
     * @param finals
     * @param requiredSymbol
     * @param restart_state
     * @param separationSymbol
     */
    void restartFinals(Set<Node> finals, int requiredSymbol, State restart_state, int separationSymbol) {

      ArrayList<TNodeState> added_states = new ArrayList<TNodeState>();

        for (int i = 0;  i<state.size(); i++) {
          TNodeState state_i = state.get(i);
          // A state can be a possible final state and still have transitions

          if (finals.contains(state_i.where)) {
            boolean restart=lastPartHasRequiredSymbol(state_i.sequence, requiredSymbol, separationSymbol);
            if (restart) {
              if (restart_state!=null) {
                if (DEBUG) System.err.println("restart state "+i+"= " + state_i);
                for (TNodeState initst : restart_state.state) {
                  TNodeState tn = REUSE_OBJECTS?nodeStatePool_get(): new TNodeState(state_i.sequence.size()+1);
                  tn.where = initst.where;
                  tn.caseWasChanged = state_i.caseWasChanged;
                  tn.sequence.addAll(state_i.sequence);
                  tn.sequence.add(separationSymbol);
                  added_states.add(tn);
                }
              }
            }
          }
        }
        state.addAll(added_states);
    }


                    /*
                    if (restart && state_i.sequence.size()-n<10) {
                      restart = false;

                      StringBuilder result = new StringBuilder();
                      for (int j = 0,  limit2 = state_i.sequence.size(); j != limit2; j++) {
                          symbol = ((state_i.sequence).get(j)).intValue();
                          result.append(Alphabet.debuggingInstance.getSymbol(symbol));
                      }

                      System.err.println("Unhammer sjusk: "+result);
                    } // Unhammer sjusk :-)
                     */

    private boolean lastPartHasRequiredSymbol(List<Integer> seq, int requiredSymbol, int separationSymbol) {
      // state is final - it should be restarted it with all elements in stateset restart_state, with old symbols conserved
      boolean restart=false;
      for (int n=seq.size()-1; n>=0; n--) {
        int symbol=seq.get(n);
        if (symbol==requiredSymbol) {
          restart=true;
          break;
        }
        if (symbol==separationSymbol) {
          break;
        }
      }
      return restart;
    }


    void pruneStatesWithForbiddenSymbol(int forbiddenSymbol) {

      Integer forbiddenSymbolInteger = forbiddenSymbol;

      // remove states containing a forbidden symbol
      for (int i = state.size()-1; i>=0; i--) {
        TNodeState state_i = state.get(i);
        // TODO optimize: search from end,
        if (state_i.sequence.contains(forbiddenSymbolInteger)) {
          state.remove(i);
          if (REUSE_OBJECTS) nodeStatePool_release(state_i);
        }
      }
    }


    void pruneCompounds(int requiredSymbol, int separationSymbol, int compound_max_elements) {
      int minNoOfCompoundElements = compound_max_elements; //Integer.MAX_VALUE-1;
      int[] noOfCompoundElements = new int[state.size()];
      for (int i = 0;  i<state.size(); i++) {
          List<Integer> seq = state.get(i).sequence;
          if (!lastPartHasRequiredSymbol(seq, requiredSymbol, separationSymbol)) {
            //
            noOfCompoundElements[i] = Integer.MAX_VALUE;
            continue;
          }
          int this_noOfCompoundElements = 0;
          for (int j = seq.size()-2; j>0; j--) if (seq.get(j)==separationSymbol) this_noOfCompoundElements++;
          noOfCompoundElements[i] = this_noOfCompoundElements;
          minNoOfCompoundElements = Math.min(minNoOfCompoundElements, this_noOfCompoundElements);
      }

      // remove states with more than minimum number of compounds
      for (int i = state.size()-1; i>=0; i--) {
        if (noOfCompoundElements[i]>minNoOfCompoundElements) {
          if (REUSE_OBJECTS) nodeStatePool_release(state.remove(i));
          else state.remove(i);
        }
      }
    }


    /**
     * Same as previous one, but  the output is adapted to the SAO system
     * @param finals the set of final nodes
     * @param alphabet the alphabet to decode strings
     * @param escaped_chars the set of chars to be preceeded with one backslash
     * @param uppercase true if the word is uppercase
     * @param firstupper true if the first letter of a word is uppercase
     * @param firstchar first character of the word
     * @return the result of the transduction
     */
    String filterFinalsSAO(Set<Node> finals,
        Alphabet alphabet,
        SetOfCharacters escaped_chars,
        boolean uppercase, boolean firstupper, int firstchar) {
        StringBuilder result = new StringBuilder("");

        for (int i = 0,  limit = state.size(); i != limit; i++) {
            TNodeState state_i = state.get(i);
            if (finals.contains(state_i.where)) {
                result.append('/');
                int first_char = result.length() + firstchar;
                for (int j = 0,  limit2 = state_i.sequence.size(); j != limit2; j++) {
                    if (escaped_chars.contains((char) (state_i.sequence).get(j).intValue())) {
                        result.append('\\');
                    }
                    if (alphabet.isTag(((state_i.sequence)).get(j))) {
                        result.append('&');
                        result.append(alphabet.getSymbol(state_i.sequence.get(j)));
                        result.setCharAt(result.length() - 1, ';');
                    } else {
                        result.append(alphabet.getSymbol(((state_i.sequence)).get(j), uppercase));
                    }
                }
                if (firstupper) {
                    if (result.charAt(first_char) == '~') {
                        // skip post-generation mark
                        result.setCharAt(first_char + 1, Alphabet.toUpperCase(result.charAt(first_char + 1)));
                    } else {
                        result.setCharAt(first_char, Alphabet.toUpperCase(result.charAt(first_char)));
                    }
                }
            }
        }

        return result.toString();
    }

    /**
     * Same as previous one, but  the output is adapted to the TM system
     * @param finals the set of final nodes
     * @param alphabet the alphabet to decode strings
     * @param escaped_chars the set of chars to be preceeded with one backslash
     * @param uppercase true if the word is uppercase
     * @param firstupper true if the first letter of a word is uppercase
     * @param firstchar first character of the word
     * @return the result of the transduction
     */
    String filterFinalsTM(Set<Node> finals, Alphabet alphabet, SetOfCharacters escaped_chars, ArrayDeque<String> blankqueue, ArrayList<String> numbers) {
        String result = "";
        for (int i = 0,  limit = state.size(); i < limit; i++) {
            TNodeState state_i = state.get(i);
            if (finals.contains(state_i.where)) {
                result += '/';
                for (int j = 0,  limit2 = state_i.sequence.size(); j < limit2; j++) {
                    if (escaped_chars.contains((char) state_i.sequence.get(j).intValue())) {
                        result += '\\';
                    }
                    result += alphabet.getSymbol(state_i.sequence.get(j));
                }
            }
        }
        String result2 = "";
        ArrayList<String> fragments = new ArrayList<String>();
        fragments.add("");
        for (int i = 0,  limit = result.length(); i < limit; i++) {
            if (result.charAt(i) == ')') {
                fragments.add("");
            } else {
                fragments.set(fragments.size() - 1, fragments.get(fragments.size() - 1) + result.charAt(i));
            }
        }

        for (int i = 0,  limit = fragments.size(); i < limit; i++) {
            if (i < limit - 1) {
                if (fragments.get(i).length() >= 2 &&
                    fragments.get(i).substring(fragments.get(i).length() - 2).equals("(#")) {
                    String whitespace = "";
                    if (blankqueue.size() != 0) {
                        whitespace = blankqueue.getFirst().substring(1);
                        blankqueue.removeFirst();
                        whitespace = whitespace.substring(0, whitespace.length() - 1);
                    }
                    fragments.set(i, fragments.get(i).substring(0, fragments.get(i).length() - 2) + whitespace);
                } else {
                    boolean replaced = false;
                    for (int j = fragments.size() - 1; j >= 0; j--) {
                        if (fragments.get(i).length() > 3 &&
                            fragments.get(i).charAt(j) == '\\' &&
                            fragments.get(i).charAt(j + 1) == '@' &&
                            fragments.get(i).charAt(j + 2) == '(') {
                            int num = 0;
                            boolean correct = true;
                            for (int k = j + 3,  limit2 = fragments.get(i).length(); k < limit2; k++) {

                                if (iswdigit(fragments.get(i).charAt(k))) {
                                    num *= 10;
                                    num += ((int) fragments.get(i).charAt(k)) - 48;
                                } else {
                                    correct = false;
                                    break;
                                }
                            }
                            if (correct) {
                                fragments.set(i, fragments.get(i).substring(0, j) + numbers.get(num - 1));
                                replaced = true;
                                break;
                            }
                        }
                    }
                    if (!replaced) {
                        fragments.set(i, fragments.get(i) + ')');
                    }
                }
            }
        }
        result = "";
        for (int i = 0,  limit = fragments.size(); i < limit; i++) {
            result += fragments.get(i);
        }
        return result;
    }

    
    /**
     * Compute if a character is a digit (gives the same results as 
     * the c++ iswdigit() function
     * @param c the character
     * @return true if the c is a digit
     */
    private boolean iswdigit(char c) {
        int i = (int)c;
        return ((i>=48&&i<=57)||i==178||i==179||i==185);
    }
}
