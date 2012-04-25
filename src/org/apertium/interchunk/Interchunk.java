/*
 * Copyright (C) 2010 Stephen Tigner
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

package org.apertium.interchunk;

import static org.apertium.utils.IOUtils.openInFileStream;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.apertium.lttoolbox.Alphabet;
import org.apertium.transfer.BufferT;
import org.apertium.transfer.MatchExe;
import org.apertium.transfer.MatchState;
import org.apertium.transfer.TransferToken;
import org.apertium.transfer.development.Timing;
import org.apertium.transfer.generated.GeneratedTransferBase;

/**
 * @author Stephen Tigner
 * 
 */
public class Interchunk {
    
    /* Yes, this increases the linkage and entanglement of the classes, but
     * but it's better than duplicating so much code just for a change of a
     * couple of lines between Interchunk and Postchunk.
     * Defines the mode certain parts of code operates in, either in
     * Interchunk or Postchunk mode.
     */
    
    protected enum InterchunkMode {
        INTERCHUNK, POSTCHUNK
    }
    
    protected InterchunkMode icMode;

    /*
     * This class is very similar to the Transfer class, so some code was just
     * copied straight over, with alot of commented-out code removed.
     */

    // Private members and methods
    private Alphabet alphabet;
    private String TRXReader__ANY_CHAR = "<ANY_CHAR>";
    private String TRXReader__ANY_TAG = "<ANY_TAG>";

    private MatchExe me; // Pointer in C++ version
    private MatchState ms;

    private Method[] rule_map = null; // vector<xmlNode *> rule_map;

    private BufferT<TransferToken> input_buffer=new BufferT<TransferToken>(); // Buffer<TransferToken> input_buffer;

    private int any_char;
    private int any_tag;

    public GeneratedTransferBase transferObject;

    private boolean inword;
    private boolean null_flush;
    private boolean internal_null_flush;

    public static boolean DEBUG = false;

    // Moved these up here, in contrast to Transfer, where they're stuck down
    // between some functions.
    public Timing timing;
    public static final boolean DO_TIMING = false;


    /**
     * Constructor
     */
    public Interchunk() {
        me = null;
        inword = false;
        null_flush = false;
        internal_null_flush = false;

        icMode = InterchunkMode.INTERCHUNK;
    }
    
    /**
     * Copied from {@link org.apertium.transfer.Transfer#readData(InputStream)}
     * 
     * @param in
     * @throws IOException
     */
    public void readData(InputStream in) throws IOException {
        // symbols
        alphabet = Alphabet.read(in);
        any_char = alphabet.cast(TRXReader__ANY_CHAR);
        any_tag = alphabet.cast(TRXReader__ANY_TAG);

        // faster - let it read itself, thus no need to make a big hashmap
        me = new MatchExe(in, alphabet.size());
        ms = new MatchState(me);
    }

    /**
     * Copied from
     * {@link org.apertium.transfer.Transfer#read(Class, String, String)}
     * 
     * @param transferClass
     * @param datafile
     * @param fstfile
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void read(Class transferClass, String datafile) 
            throws FileNotFoundException, IOException, IllegalAccessException,
            InstantiationException {

        InputStream is = openInFileStream(datafile);
        readData(is);
        is.close();

        Method[] mets = transferClass.getMethods();
        rule_map = new Method[mets.length];

        for (Method method : mets) {
            String name = method.getName();

            if (DEBUG) System.err.println("n = " + name);
            if (!name.startsWith("rule"))
                continue;

            int number = Integer.parseInt(name.substring(4, name
                    .indexOf('_', 5)));
            rule_map[number] = method;

            if (DEBUG)
                System.err.println(method.getName() + "  - #words="
                        + method.getParameterTypes().length / 2);
        }

        
        transferObject = (GeneratedTransferBase) transferClass.newInstance();
        transferObject.debug = DEBUG;
        transferObject.init();

    }

    /**
     * Much of this code originally copied from {@link org.apertium.transfer.Transfer#readToken(Reader)}.
     * Modified to be in-line with the differences between transfer.cc and interchunk.cc
     * @param in
     * @return
     * @throws IOException
     */
    private TransferToken readToken(Reader in) throws IOException {
        //TODO: Make sure this isn't broken XD The read-ahead might be a failure.
        if (!input_buffer.isEmpty()) {
            return input_buffer.next();
        }

        /* This is a flag that is used if we need to skip a read in the case of a
         * single character look-ahead.
         */
        boolean skipRead = false;
        String content = "";
        int val = -1; //declare and initialize val outside of while statement.
        while (true) {
            if(skipRead) { //Already read ahead to the next character 
                skipRead = false; //unset flag
            } 
            else {
                val = in.read();
                //if (DEBUG) System.err.println("val = " + (char) val);
            }
            if (val == -1 || (val == 0 && internal_null_flush)) {
                return input_buffer.add(new TransferToken(content,
                        TransferToken.TransferTokenType.tt_eof));
            }
            if (val == '\\') {
                content += '\\';
                content += (char) in.read();
            } else if (val == '[') {
                content += '[';
                while (true) {
                    int val2 = in.read();
                    if (val2 == '\\') {
                        content += '\\';
                        content += (char) in.read();
                    } else if (val2 == ']') {
                        content += ']';
                        break;
                    } else {
                        content += (char) val2;
                    }
                }
            } else if (inword && val == '{') {
                content += '{';
                int val2 = -1; //declare and initialize val2 outside of while statement.
                while (true) {
                    if(skipRead) { //Already read ahead to the next character
                        skipRead = false; //unset flag
                    } else {
                        val2 = in.read();
                        //if (DEBUG) System.err.println("val2 = " + (char) val2);
                    }
                    if (val2 == '\\') {
                        content += '\\';
                        content += (char) in.read();
                    } else if (val2 == '}') {
                        /*
                         * Here's where we peek ahead to see if the next char is
                         * a '$' If it's not, we want that character to still be
                         * processed normally. Unfortunately, we can't "unget" a
                         * character, but we can set a flag and test for it.
                         * Instead of storing it in a new val3, we just read the
                         * next character into val2.
                         */
                        content += '}';
                        val2 = in.read();
                        if(val2 == '$') {
                            val = val2; //pass read-ahead char into val
                            skipRead = true; // skip next read
                            break; //exit inner while loop
                        }
                    } else {
                        content += (char) val2;
                    }
                }
            } else if (inword && val == '$') {
                inword = false;
                return input_buffer.add(new TransferToken(content,
                        TransferToken.TransferTokenType.tt_word));
            } else if (val == '^') {
                inword = true;
                return input_buffer.add(new TransferToken(content,
                        TransferToken.TransferTokenType.tt_blank));
            } else {
                content += (char) val;
            }
        }
    }

    /**
     * Copied from
     * {@link org.apertium.transfer.Transfer#transfer_wrapper_null_flush(Reader, Writer)}, 
     * then modified slightly, in-line with the changes between transfer.cc
     * and interchunk.cc
     * 
     * @param input
     * @param output
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws Exception
     */
    private void interchunk_wrapper_null_flush(Reader input, Writer output)
            throws IOException, IllegalArgumentException, 
            IllegalAccessException, InvocationTargetException {
        null_flush = false;
        internal_null_flush = true;
        while (input.ready()) {
            interchunk(input, output);
            output.write('\0');
            try {
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not flush output");
            }
        }
        internal_null_flush = false;
        null_flush = true;
    }
    
    public boolean getNullFlush() {
        return null_flush;
    }

    public void setNullFlush(boolean b) {
        null_flush = b;
    }

    /**
     * Much of this code originally copied from {@link org.apertium.transfer.Transfer#transfer(Reader, Writer)}.
     * Modified to be in-line with the differences between transfer.cc and interchunk.cc
     * @param in
     * @param output
     * @throws IOException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws Exception
     */
    public void interchunk(Reader in, Writer output) throws IOException, 
            IllegalArgumentException, IllegalAccessException, 
            InvocationTargetException {
        if (getNullFlush()) {
            interchunk_wrapper_null_flush(in, output);
        }

        Method lastMatchedRule = null; // xmlNode *lastrule;
        ArrayList<String> tmpword = new ArrayList<String>(); // vector<wstring *> tmpword;
        ArrayList<String> tmpblank = new ArrayList<String>(); // vector<wstring *> tmpblank;
        ArrayList<String> matchedWords = new ArrayList<String>();
        ArrayList<String> matchedBlanks = new ArrayList<String>();
        int lastPos = 0;
        ms.init(me.getInitial());
        if (DO_TIMING)
            timing = new Timing("Interchunk");
        while (true) {
            if (ms.size() == 0) {
                if (lastMatchedRule != null) {
                    // there was a rule match
                    applyRule(output, lastMatchedRule, matchedWords, matchedBlanks);
                    lastMatchedRule = null;
                    tmpword.clear();
                    tmpblank.clear();
                    ms.init(me.getInitial());
                    input_buffer.setPos(lastPos);
                } else {
                    // no rule match. then default is to just output the stuff
                    if (tmpword.size() != 0) {
                        switch(icMode) {
                            case POSTCHUNK:
                                unchunk(tmpword.get(0), output);
                                break;
                            //If it's not postchunk, it's interchunk.
                            case INTERCHUNK:
                            default:
                                output.write('^');
                                output.write(tmpword.get(0));
                                output.write('$');
                                break;
                        }
                        tmpword.clear();
                        input_buffer.setPos(lastPos);
                        input_buffer.next();
                        lastPos = input_buffer.getPos();
                        ms.init(me.getInitial());
                    } else if (tmpblank.size() != 0) {
                        output.write(tmpblank.get(0));
                        tmpblank.clear();
                        lastPos = input_buffer.getPos();
                        ms.init(me.getInitial());
                    }
                }
            }
            if (DO_TIMING)
                timing.log("interchunk");
            int val = ms.classifyFinals();
            if (DO_TIMING)
                timing.log("interchunk/ms.classifyFinals");
            if (val != -1) {
                // a rule match was found. This might not be the longest match, though.
                // so, we store the stuff to invoke applyRule() later

                lastMatchedRule = rule_map[(val - 1)];
                lastPos = input_buffer.getPos();

                if (DEBUG)
                    System.err.println("lastrule = " + (val - 1) + " "
                            + lastMatchedRule.getName());
                if (DEBUG)
                    System.err.println("tmpword = " + tmpword.size()
                            + "  tmpblank = " + tmpblank.size());
                if (DEBUG)
                    System.err.println("tmpword = " + tmpword + "  tmpblank = "
                            + tmpblank);
                matchedWords.clear();
                matchedBlanks.clear();
                matchedWords.addAll(tmpword);
                matchedBlanks.addAll(tmpblank);
            }

            if (DO_TIMING)
                timing.log("interchunk");
            TransferToken current = readToken(in);
            if (DO_TIMING)
                timing.log("readToken");

            switch (current.type) {
                case tt_word:
                    applyWord(current.content);
                    tmpword.add(current.content);
                    break;

                case tt_blank:
                    ms.step(' ');
                    tmpblank.add(current.content);
                    break;

                case tt_eof:
                    if (tmpword.size() != 0) {
                        tmpblank.add(current.content);
                        ms.clear();
                    } else {
                        output.write(current.content);
                        //This line only exists in Intechunk, not postchunk
                        if(icMode == InterchunkMode.INTERCHUNK) { tmpblank.clear(); }
                        if (DO_TIMING) {
                            timing.log("interchunk");
                            timing.report();
                        }
                        return;
                    }
                    break;
    
                default:

                    System.err.println("Error: Unknown input token.");
                    return;
            }
        }
    }

    /**
     * Much of this code originally copied from {@link org.apertium.transfer.Transfer#applyRule(Writer)}.
     * Modified to be in-line with the differences between transfer.cc and interchunk.cc
     * @param output
     * @throws IOException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
  protected void applyRule(Writer output, Method rule,
    ArrayList<String> words, ArrayList<String> blanks)
    throws IOException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (DEBUG)
            System.err.println("tmpword = " + words + "  tmpblank = "
                    + blanks);
        if (DO_TIMING)
            timing.log("other1");

        int limit = words.size(); // number of words

        Object[] args = new Object[1 + limit + limit - 1]; // number of arguments out:1, words:limit, blanks:limit-1
        int argn = 0;
        args[argn++] = output;

        for (int i = 0; i != limit; i++) {
          if (i>0) args[argn++] = blanks.get(i-1);
          args[argn++] = new InterchunkWord(words.get(i));
        }

        if (DEBUG)

        if (DEBUG)
            System.err.println("#args = " + args.length);
//        if (DEBUG)
//            System.err.println("processRule:" + lastrule.getName() + "("
//                    + Arrays.toString(args));
        try {
            if (DO_TIMING)
                timing.log("applyRule 1");
            rule.invoke(transferObject, args);
            if (DO_TIMING)
                timing.log("rule invoke");
        } catch (IllegalAccessException e) {
            _outputInvokeErrorMsg(rule, null, args.length,
                    rule.getName() + "(" + Arrays.toString(args) + ")");
            throw e;
        } catch (IllegalArgumentException e) {
            _outputInvokeErrorMsg(rule, null, args.length,
                    rule.getName() + "(" + Arrays.toString(args) + ")");
            throw e;
        } catch (InvocationTargetException e) {
            _outputInvokeErrorMsg(rule, null, args.length,
                    rule.getName() + "(" + Arrays.toString(args) + ")");
            throw e;
        }
        if (DEBUG)
            output.flush();

    }
    
    private void _outputInvokeErrorMsg(Method rule, String word, 
            int numArgs, String processRule) {
        System.err.println("Error during invokation of " + rule);
        System.err.println("word = " + word);
        System.err.println("#args = " + numArgs);
        System.err.println("processRule:" + processRule);
    }

    /**
     * Much of this code originally copied from {@link org.apertium.transfer.Transfer#applyWord(string)}.
     * Modified to be in-line with the differences between transfer.cc and interchunk.cc 
     * @param word_str
     */
    private void applyWord(String word_str) {
        if (DO_TIMING)
            timing.log("other");
        ms.step('^');
        for (int i = 0, limit = word_str.length(); i < limit; i++) {
            switch (word_str.charAt(i)) {
                case '\\':
                    i++;
                    ms.step(Character.toLowerCase(word_str.charAt(i)), any_char);
                    break;
    
                case '<':
                    //This chunk of code is commented out in postchunk.cc
                    if(icMode == InterchunkMode.INTERCHUNK) {
                        for (int j = i + 1; j != limit; j++) {
                            if (word_str.charAt(j) == '>') {
                                int symbol = alphabet.cast(word_str.substring(i, j + 1));
                                if (symbol != 0) {
                                    ms.step(symbol, any_tag);
                                } else {
                                    ms.step(any_tag);
                                }
                                i = j;
                                break;
                            }
                        }
                        break;
                    }
    
                case '{': //ignore the unmodifiable part of the chunk
                    ms.step('$');
                    return;
    
                default:
                    ms.step(Character.toLowerCase(word_str.charAt(i)), any_char);
                    break;
            }
        }
        ms.step('$');
        if (DO_TIMING)
            timing.log("applyWord");
    }

    /**
     * This function only exists to allow for the code in interchunk() to compile. This code helps eliminate
     * duplicating almost all of the code in interchunk() in Postchunk.
     * @param chunk
     * @param output
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    protected void unchunk(final String chunk, Writer output) throws IOException, UnsupportedOperationException {
        String message = "Interchunk.unchunk should never be called. Instead this should only be called from " +
            "a Postchunk object, and instead should run Postchunk.unchunk.";
        throw new UnsupportedOperationException(message);
    }
}
