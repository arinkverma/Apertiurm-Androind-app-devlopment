package org.apertium.lttoolbox.compile;

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

import org.apertium.lttoolbox.compile.Compile;
import org.apertium.lttoolbox.compile.Transducer;
import org.apertium.lttoolbox.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Raah
 */
public class testTransducer {

    /**
     * @param args the command line arguments
     */
    
    static Transducer read(InputStream input) throws IOException {
        
        
        Transducer t = new Transducer();
        
        t.initial = Compression.multibyte_read(input);
        int finals_size = Compression.multibyte_read(input);

        int base = 0;

        Set<Integer> myfinals = new TreeSet<Integer>();

        while (finals_size > 0) {
            System.out.println("finals_size : "+finals_size);
            finals_size--;
            base += Compression.multibyte_read(input);
            t.finals.add(base);
        }

        base = Compression.multibyte_read(input);

        int number_of_states = base;
        int current_state = 0;
/*
        while (number_of_states > 0) {
            //System.out.println("number of states : "+number_of_states);
            int number_of_local_transitions = Compression.multibyte_read(input);
            int tagbase = 0;
            if (!t.transitions.containsKey(current_state)) {
                    t.transitions.put(current_state,new TreeMap<Integer,Set<Integer>>());
                }

            while (number_of_local_transitions > 0) {
                //System.out.println("number of local transitions "+number_of_local_transitions);
                number_of_local_transitions--;
                tagbase += Compression.multibyte_read(input);
                int state = (current_state + Compression.multibyte_read(input)) % base;
                //int i_symbol = alphabet.decode(tagbase).firstInt;
                //int o_symbol = alphabet.decode(tagbase).second;
                //System.out.println("i : "+i_symbol+", o : "+o_symbol);
                
                if (!t.transitions.get(current_state).containsKey(tagbase)) {
                    t.transitions.get(current_state).put(tagbase, new TreeSet<Integer>());
                }
                    t.transitions.get(current_state).get(tagbase).add(state);
            }
            number_of_states--;
            current_state++;
        }
 *
 */
        return t;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
            
        
        Compile c = new Compile();
        Compile c2 = new Compile();
        c.parse("testdata/apertium-fr-es.fr.dix", Compile.COMPILER_RESTRICTION_LR_VAL);
        //c.parse("../src/test/org/apertium/lttoolbox/test3.dix", NewCompiler.COMPILER_RESTRICTION_LR_VAL);
        //c.parse("../src/test/org/apertium/lttoolbox/test4.dix", NewCompiler.COMPILER_RESTRICTION_LR_VAL);
        //c.parse("../src/test/org/apertium/lttoolbox/test5.dix", NewCompiler.COMPILER_RESTRICTION_LR_VAL);
        //c.parse("../src/test/org/apertium/lttoolbox/test6.dix", NewCompiler.COMPILER_RESTRICTION_LR_VAL);
        //c.parse("../src/test/org/apertium/lttoolbox/test7.dix", NewCompiler.COMPILER_RESTRICTION_LR_VAL);
        //c.parse("../src/test/org/apertium/lttoolbox/short.dix", NewCompiler.COMPILER_RESTRICTION_LR_VAL);
        
        
         for (String s : c.sections.keySet()) {
            System.out.println("considering transducer of section "+s);
            System.out.println("number of states : "+c.sections.get(s).transitions.size());
            int temp = 0;
            int max = 0;
            float average = 0;
            for (int i=0; i<c.sections.get(s).transitions.size(); i++) {
                temp+=c.sections.get(s).transitions.get(i).size();
                average+=temp;
                max=(temp>max)?temp:max;
                temp = 0;
            }
            System.out.println("maximal number of transitions leaving a state "+max);
            System.out.println("average number of transitions leaving a state "+average/((float)c.sections.get(s).transitions.size()));
        }
         
         //System.exit(-1);
        OutputStream output = new BufferedOutputStream (new FileOutputStream("testTransducer2.bin"));
        c.write(output);
        output.close();
        //InputStream input = new InputStream(new BufferedInputStream (new FileInputStream("testTransducer2.bin")));
        InputStream input = new BufferedInputStream (new FileInputStream("outc"));
        //c2 = c.DEBUG_read(input);
        
        //FSTProcessor fstp = new FSTProcessor();
        //fstp.load(input);
        String letters = Compression.String_read(input);
        Alphabet a = new Alphabet();
        a.read(input);
        
        Map<String, Transducer> sections = new HashMap<String, Transducer>();
        
        int len = Compression.multibyte_read(input);
        
        while (len > 0) {
            String name = Compression.String_read(input);
            
            if (!sections.containsKey(name)) {
                sections.put(name,new Transducer());
            }
            System.out.println("reading : "+name);
            //if (len ==2) {System.exit(-1);}   
            sections.put(name, read(input));
         
            len--;
            if(c.sections.get(name)!=null&&sections.get(name)!=null) {
            System.out.println(c.sections.get(name).DEBUG_compare(sections.get(name)));
            }
            //System.exit(-1);
            //throw new RuntimeException("section "+name+" was totaly DEBUG_read");
        }
        input.close();
        
        for (String s : c.sections.keySet()) {
            int count1 = 0;
            int max1 = 0;
            int count2 = 0;
            int max2 = 0;
            for (int i=0; i<c.sections.get(s).transitions.size(); i++) {
                if (i>max1) {max1=i;}
                for (Integer j : c.sections.get(s).transitions.get(i).keySet()) {
                    
                    count1 += c.sections.get(s).transitions.get(i).get(j).size();
                }
            }
            for (int i=0; i<sections.get(s).transitions.size(); i++) {
                if (i>max2) {max2=i;}
                for (Integer j : sections.get(s).transitions.get(i).keySet()) {
                    count2 += sections.get(s).transitions.get(i).get(j).size();
                }
            }
            
            System.out.println("comparing transducers of section "+s);
            //System.out.println("original transducer : "+c.sections.get(s));
            System.out.println("original transducer has "+count1+" transitions");
            System.out.println("original transducer higher state is "+max1);
            //System.out.println("DEBUG_read transducer : "+sections.get(s));
            System.out.println("read transducer has "+count2+" transitions");
            System.out.println("read transducer higher state is "+max2);
            //System.out.println(c.sections.get(s).DEBUG_compare(sections.get(s)));
        }
        
        //System.out.println(c.DEBUG_compare(c2));
        
        
    }

}
