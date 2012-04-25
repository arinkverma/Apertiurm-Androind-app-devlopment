package org.apertium.lttoolbox;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.Comparator; //Unused
import java.util.HashMap;
//import java.util.List; //Unused
//import java.util.Map; //Unused
//import java.util.TreeMap; //Unused
import java.util.ArrayList;
//import java.util.HashSet; //Unused
import java.util.Set;


/**
 * Alphabet class.
 * Encodes pairs of symbols into an integer.
 * @author Raah
 */
public class Alphabet {

/**
 * Class to represent a pair of integers
 * @author Raah
 */
public static class IntegerPair { 

    public int first;
    public int second;

    public IntegerPair(Integer i1, Integer i2) {
      first = i1;
        second = i2;
    }

    @Override
    public int hashCode() {
    	return first + second*0x8000;
    }

    @Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		/*
		 * if (! (o instanceof IntegerPair)) { return false; }
		 */
		if (o instanceof IntegerPair) {
			IntegerPair p = (IntegerPair) o;
			return ((first == p.first) && (second == p.second));
		} else
			return false;
	}

    @Override
    public String toString() {
        return ("<" + (first>0?(char)first:"") + first + "," + (second>0?(char)second:"") + second + ">");
    }
}

    /**
     * Symbol-identifier relationship.
     */
    private final HashMap<String, Integer> slexic;
    
    /**
     * Identifier-symbol relationship.
     */
    private final ArrayList<String> slexicinv;


    private final HashMap<IntegerPair, Integer> spair;

    
    private final ArrayList<IntegerPair> spairinv;


    /** For debugging purposes ONLY */
    public static Alphabet debuggingInstance;

    public void debug_remember_removed_symbols() {
        debuggingInstance = new Alphabet();
        debuggingInstance.slexicinv.addAll(slexicinv);
    }

    /**
     * The constructor
     */
    public Alphabet() {
    	slexic = new HashMap<String, Integer>();
    	slexicinv = new ArrayList<String>();
    	spair = new HashMap<IntegerPair, Integer>();
    	spairinv = new ArrayList<IntegerPair>();
    	  cast00 = cast(0, 0);
    	
        Alphabet.debuggingInstance = this;
    }
    
    /**
     * Copy constructor, creates a new copy of Alphabet based on the passed-in
     * Alphabet object.
     * @param o - The Alphabet object to copy.
     */
    public Alphabet(Alphabet o) {
        slexic = new HashMap<String, Integer>(o.slexic);
        slexicinv = new ArrayList<String>(o.slexicinv);
        spair = new HashMap<IntegerPair, Integer>(o.spair);
        spairinv = new ArrayList<IntegerPair>(o.spairinv);
    	  cast00 = cast(0, 0);
        Alphabet.debuggingInstance = this;
    }

    public Set<String> getFlagMatchSymbols() {
    	return slexic.keySet();
    }

    /**
     * Include a symbol into the alphabet.
     * @param s the symbol to include
     */
    public void includeSymbol(String s) {
        if (!slexic.containsKey(s)) {
            int slexic_size = slexic.size();
            slexic.put(s, -(slexic_size + 1));
            slexicinv.add(s);
        }
    }


    
    /** Non-threadsafe temp variable */
    IntegerPair tmp = new IntegerPair(0,0);

    /** this lookup is needed very often and thus cached */
    public final int cast00;

    /**
     * Get an unique code for a pair of characters
     * @param c1 left symbol
     * @param c2 right symbol
     * @return the code for (c1, c2)
     */
    public int cast(int c1, int c2) {
        tmp.first=c1;
        tmp.second=c2;
        Integer res = spair.get(tmp);
        if (res==null) {
            int spair_size = spair.size();
            spair.put(tmp, spair_size);
            spairinv.add(tmp);
            // for use next time
            tmp = new IntegerPair(0,0);
            return spair_size;
        }
        return res;
    }

    /**
     * Gets the individual symbol identifier.
     * @param s symbol to be identified.
     * @return the symbol identifier.
     */
    public int cast(String s) {

      //System.err.println(this+" "+slexic+"cast s = " + s);
      Integer i = slexic.get(s);

      if (i==null) return 0; // unknown symbol - this happens in transfer

      return (int) i;
    }

    /**
     * Check wether the symbol is defined in the alphabet.
     * @param s the symbol to check.
     * @return true if the symbol is defined.
     */
    public boolean isSymbolDefined(String s) {
        return slexic.containsKey(s);
    }

    /**
     * Give the size of the alphabet.
     * @return the number of symbols of the alphabet.
     */
    public int size() {
        return slexic.size();
    }

    /**
     * Write the alphabet to a stream.
     * @param output the outputstream.
     * @throws java.io.IOException.
     */
    public void write(OutputStream output) throws IOException {
        // First, we write the taglist
        Compression.multibyte_write(slexicinv.size(), output);

        for (int i = 0,  limit = slexicinv.size(); i < limit; i++) {
            Compression.String_write(slexicinv.get(i).substring(1, 1 + slexicinv.get(i).length() - 2), output);
        }

        // Then we write the list of pairs
        // All numbers are biased + slexicinv.size() to be positive or zero
        int bias = slexicinv.size();

        Compression.multibyte_write(spairinv.size(), output);
        for (int i = 0,  limit = spairinv.size(); i != limit; i++) {
            Compression.multibyte_write(spairinv.get(i).first + bias, output);
            Compression.multibyte_write(spairinv.get(i).second + bias, output);
        }
    }

    /**
     * Compare with another instance of alphabet
     * Was written for debugging purpose
     * @param a the alphabet to DEBUG_compare to
     * @return true if both alphabets are similar
     */
    public boolean DEBUG_compare(Alphabet other) {
        System.out.println("now comparing the alphabets this with other:\n"+this+"\n"+other);
        boolean sameSlexic = true;
        for (String s : other.slexic.keySet()) {
            if (!slexic.containsKey(s)) {
                sameSlexic = false;
                break;
            }
            if (!slexic.get(s).equals(other.slexic.get(s))) {
                sameSlexic = false;
                break;
            }
            slexic.remove(s);
        }
        System.out.println("the slexic attributes are the same : " + sameSlexic);
        boolean sameSlexicinv = true;
        for (int i = 0,  limit = other.slexicinv.size(); i < limit; i++) {
            boolean temp = false;
            for (int j = i,  limit2 = slexicinv.size() + i; j < limit2; j++) {
                int k = j % slexicinv.size();
                if (other.slexicinv.get(i).equals(slexicinv.get(k))) {
                    temp = true;
                    break;
                }
            }
            if (!temp) {
                sameSlexicinv = false;
                break;
            }

        }
        boolean sameSpair = true;
        System.out.println("the slexicinv attributes are the same : " + sameSlexicinv);
        for (IntegerPair p : other.spair.keySet()) {
            if(!spair.containsKey(p)) {
            //if (!containsKey(spair, p)) {
                sameSpair = false;
                break;
            }
            //if (!(get(spair, p).equals(get(a.spair, p)))) {
            if (!spair.get(p).equals(other.spair.get(p))) {
                sameSpair = false;
                break;
            }
        }
        System.out.println("the spair attributes are the same : " + sameSpair);
        boolean sameSpairinv = true;
        for (int i = 0,  limit = other.spairinv.size(); i < limit; i++) {
            boolean temp = false;
            for (int j = i,  limit2 = spairinv.size() + i; j < limit2; j++) {
                int k = j % spairinv.size();
                if (other.spairinv.get(i).equals(spairinv.get(k))) {
                    temp = true;
                    break;
                }
            }
            if (!temp) {
                sameSpairinv = false;
                break;
            }
        }
        System.out.println("the spairinv attributes are the same : " + sameSpairinv);
        return (sameSpairinv && sameSpair && sameSlexic && sameSlexicinv);
    }

    /**
     * Write the alphabet to a file, read it again,
     * and then check that both are similar
     * Was written for debugging purpose
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void testIO() throws FileNotFoundException, IOException {
        OutputStream output = new BufferedOutputStream(new FileOutputStream("testAlphabet.bin"));
        this.write(output);
        output.close();
        InputStream input = new BufferedInputStream(new FileInputStream("testAlphabet.bin"));
        Alphabet a = new Alphabet();
        a = read(input);
        input.close();
        if (this.DEBUG_compare(a)) {
            System.out.println("the two alphabets are the same : true");
        } else {
            System.out.println("the two alphabets are the same : false");
        }
    }

    /**
     * display the alphabet
     */
    public void display() {
        System.out.println("slexicinv : \n" + slexicinv);
        System.out.println("slexic : \n" + slexic);
        System.out.println("spairinv :\n ");
        System.out.print("[");
        for (int i = 0; i < spairinv.size() - 1; i++) {
            System.out.print("<" + spairinv.get(i).first + "," + spairinv.get(i).second + ">; ");
        }
        System.out.print("<" + spairinv.get(spairinv.size() - 1).first + "," + spairinv.get(spairinv.size() - 1).second + ">");
        System.out.println("]");
        System.out.println("spair :\n ");
        System.out.print("[");
        for (IntegerPair pair : spair.keySet()) {
            System.out.print("{<" + pair.first + "," + pair.second + "> =>" + spair.get(pair) + "}, ");
        }
        System.out.println("]");

    }

    /**
     * Read an alphabet from an input stream
     * @param input the stream to read from
     * @return the alphabet read from th input
     * @throws java.io.IOException
     */
    public static Alphabet read(InputStream input) throws IOException {
        Alphabet a_new = new Alphabet();
        a_new.spairinv.clear();
        a_new.spair.clear();

        // Reading of taglist
        int tam = Compression.multibyte_read(input);
        //StringBuilder sb = new StringBuilder();
        while (tam > 0) {
            tam--;
            String mytag = "<" + Compression.String_read(input) + ">";
            a_new.slexicinv.add(mytag);
            a_new.slexic.put(mytag, -a_new.slexicinv.size());
        }

        // Reading of pairlist
        int bias = a_new.slexicinv.size();
        tam = Compression.multibyte_read(input);
        while (tam > 0) {
            tam--;
            int first = Compression.multibyte_read(input);
            int second = Compression.multibyte_read(input);
            IntegerPair tmp2 = new IntegerPair(first - bias, second - bias);
            int spair_size = a_new.spair.size();
            a_new.spair.put(tmp2, spair_size);
            a_new.spairinv.add(tmp2);
        }

        return a_new;
    }

    public String toString() {
    	return slexicinv.toString();
    	//return slexicinv+"\n/"+spairinv +"\n/"+ spair;
    }


    /**
     * @deprecated
     */
    public String getSymbol(String s, int symbol) {
        return getSymbol(s, symbol, false);
    }

    /**
     * @deprecated
     */
    public String getSymbol(String s, int symbol, boolean uppercase) {
      return s + getSymbol(symbol, uppercase);
    }

    public String getSymbol(int symbol) {
        return getSymbol(symbol, false);
    }


    private static final int MAX_CHARCACHE = 200;
    private static final String[] CHARCACHE = new String[MAX_CHARCACHE];
    private static final String[] UPCHARCACHE = new String[MAX_CHARCACHE];
    static {
      char[] ca = new char[MAX_CHARCACHE];
      for (char i=0; i<MAX_CHARCACHE; i++) ca[i] = i;
      String str = new String(ca);
      for (int i=1; i<MAX_CHARCACHE; i++) CHARCACHE[i] = str.substring(i, i+1);
      String upstr = str.toUpperCase();
      for (int i=1; i<MAX_CHARCACHE; i++) UPCHARCACHE[i] = upstr.substring(i, i+1);
      CHARCACHE[0] = UPCHARCACHE[0] = "";
    }


  public static boolean isUpperCase(int val) {
    return Character.isUpperCase(val);
  }

  public static boolean isSpaceChar(char val) {
    return Character.isSpaceChar(val);
  }

  public static boolean isWhitespace(char val) {
    return Character.isWhitespace(val);
  }

  public static boolean isLetter(char charAt) {
    return Character.isLetter(charAt);
  }

  public static char toLowerCase(char charAt) {
    return Character.toLowerCase(charAt);
  }

  public static int toLowerCase(int val) {
    return Character.toLowerCase(val);
  }

  public static char toUpperCase(char charAt) {
    return Character.toUpperCase(charAt);
  }



    /**
     * Find a symbol symbol
     * @param symbol the symbol to be added
     * @param uppercase true if we want an uppercase symbol
     * @return the symbol as a string
     */
    public String getSymbol(int symbol, boolean uppercase) {

      //System.err.println("symbol = " + symbol);
        if (symbol == 0) {
            return "";
        }

        if (symbol < 0) {
            return slexicinv.get(-symbol - 1);
        }

        // re-use strings
        if (symbol < MAX_CHARCACHE) {
            return uppercase? UPCHARCACHE[symbol] : CHARCACHE[symbol];
        }


      if (!uppercase) {
          return String.valueOf((char)symbol);
        } else {
            return String.valueOf(Character.toUpperCase((char)symbol));
        }
    }


    /**
     * Sets an already existing symbol to represent a new value
     * Used to avoid decomposition symbols in output
     */
    public void setSymbol(int symbol, String newSymbolString) {
        if (symbol >= 0) {
            throw new IllegalArgumentException("Symbol may not be a normal character:"+symbol);
        } else {
            slexicinv.set(-symbol - 1, newSymbolString);
        }
    }

    /**
     * Checks whether a symbol is a tag or not
     * @param symbol the code of the symbol
     * @return true if the symbol is a tag
     */
    public static boolean isTag(int symbol) {
        return symbol < 0;
    }

    public IntegerPair decode(int code) {
        return spairinv.get(code);
    }

}
