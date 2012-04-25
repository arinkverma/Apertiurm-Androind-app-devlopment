/*
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

package org.apertium.pipeline;

import static org.apertium.utils.MiscUtils.getLineSeparator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.apertium.formatter.TextFormatter;
import org.apertium.interchunk.ApertiumInterchunk;
import org.apertium.interchunk.Interchunk;
import org.apertium.lttoolbox.LTProc;
import org.apertium.postchunk.ApertiumPostchunk;
import org.apertium.postchunk.Postchunk;
import org.apertium.pretransfer.PreTransfer;
import org.apertium.tagger.Tagger;
import org.apertium.transfer.ApertiumTransfer;
import org.apertium.utils.StringTable;

/**
 * @author Stephen Tigner
 *
 */
public class Dispatcher {
    
    private static final String splitPattern = "[ ]+";
    
    private static void doInterchunk(Program prog, Reader input, Writer output,Map<String, Class<?>> rules) 
            throws Exception {
        ApertiumInterchunk.CommandLineParams par = new ApertiumInterchunk.CommandLineParams();
        /* Parse the command line. The passed-in CommandLineParams object
         * will be modified by this method.
         */
        String[] args = prog.getParameters().split(splitPattern);
        try {
            if (!ApertiumInterchunk.
                    parseCommandLine(args, par, "Interchunk", true)) {
                throw new IllegalArgumentException("Failed to parse " +
                    "Interchunk arguments.");
            }
        } catch (FileNotFoundException e) {
            /* This is here because the compiler requires it, but with pipelineMode
             * set to true, it won't ever be thrown.
             * If we get this, something is wrong.
             * Append a message to the existing error message and
             * throw it up.
             */
            String errorString = "Apertium (Dispatch, Interchunk) -- " + 
                StringTable.UNEXPECTED_FILE_NOT_FOUND;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        } catch (UnsupportedEncodingException e) {
            /* This is here because the compiler requires it, but with pipelineMode
             * set to true, it won't ever be thrown.
             * If we get this, something is wrong.
             * Append a message to the existing error message and
             * throw it up.
             */
            String errorString = "Apertium (Dispatch, Interchunk) -- " + 
                StringTable.UNEXPECTED_UNSUPPORTED_ENCODING;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
        /* Assume internal i/o, don't allow for specifying external temp
         * files for i/o.
         */
        par.input = input;
        par.output = output;

        try {
        	
        	
            ApertiumInterchunk.doMain(par, new Interchunk(),rules);
        } catch (Exception e) {
            String errorString = "Interchunk -- " + e +
                StringTable.GENERIC_EXCEPTION;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
    }

    private static void doPostchunk(Program prog, Reader input, Writer output,Map<String, Class<?>> rules) 
            throws Exception {
        /* Yes, there's duplicate code here with the method above, but
         * there's only a few lines of actual code here, and I ran into issues
         * trying to reduce the duplication further than this.
         */
        
        ApertiumPostchunk.CommandLineParams par = 
            new ApertiumPostchunk.CommandLineParams();
        /* Parse the command line. The passed-in CommandLineParams object
         * will be modified by this method.
         */
        String[] args = prog.getParameters().split(splitPattern);
        try {
            if(!ApertiumPostchunk.
                    parseCommandLine(args, par, "Interchunk", true)) {
                throw new IllegalArgumentException("Failed to parse " +
                		"Postchunk arguments.");
            }
        } catch (FileNotFoundException e) {
            /* This is here because the compiler requires it, but with pipelineMode
             * set to true, it won't ever be thrown.
             * If we get this, something is wrong.
             * Append a message to the existing error message and
             * throw it up.
             */
            String errorString = "Apertium (Dispatch, Postchunk) -- " + 
                StringTable.UNEXPECTED_FILE_NOT_FOUND;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        } catch (UnsupportedEncodingException e) {
            /* This is here because the compiler requires it, but with pipelineMode
             * set to true, it won't ever be thrown.
             * If we get this, something is wrong.
             * Append a message to the existing error message and
             * throw it up.
             */
            String errorString = "Apertium (Dispatch, Postchunk) -- " + 
                StringTable.UNEXPECTED_UNSUPPORTED_ENCODING;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
        /* Assume internal I/O, don't allow for specifying external temp
         * files for I/O.
         * External input and output files are used only at the beginning
         * and end of the chain, and are handled by the code that calls the
         * dispatcher.
         */
        par.input = input;
        par.output = output;

        try {
            ApertiumPostchunk.doMain(par, new Postchunk(),rules);
        } catch (Exception e) {
            String errorString = "PostChunk -- " + StringTable.GENERIC_EXCEPTION;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
    }

    private static void doPretransfer(Program prog, Reader input, Writer output) 
            throws IOException {
        PreTransfer.CommandLineParams params = new PreTransfer.CommandLineParams();
        String[] args = prog.getParameters().split(splitPattern);
        PreTransfer.parseArgs(args, params, true);

        try {
            /* Assume internal I/O, don't allow for specifying external temp
             * files for I/O.
             * External input and output files are used only at the beginning
             * and end of the chain, and are handled by the code that calls the
             * dispatcher.
             */
            PreTransfer.processStream(input, output, params.nullFlush);
        } catch (IOException e) {
            String errorString = "Pretransfer -- " + StringTable.IO_EXCEPTION;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new IOException(errorString, e);
        }
    }
    
    private static void doTagger(Program prog, Reader input, Writer output, 
            boolean dispAmb) {
        String paramString = prog.getParameters();
        String replacement = (dispAmb ? "-m" : "");
        paramString = paramString.replaceAll("\\$2", replacement);
        
        String[] args = paramString.split(splitPattern);
        Tagger.taggerDispatch(args, input, output);
    }
    
    private static void doTextFormat(Program prog, Reader input, Writer output, 
            boolean deformatMode) throws Exception {
        String paramString = prog.getParameters();

        if(deformatMode) {
            /* Since the same class is used for deformatting and re-formatting, but the
             * .mode files aren't setup like that, so prepending "-d" to set it to 
             * deformatting mode.
             */
            paramString = "-d " + paramString;
        } else {
            /* If not in deformatting mode, must be in reformatting mode.
             * So prepend with "-r" instead.
             */
            paramString = "-r " + paramString;
        }
        
        TextFormatter formatter = new TextFormatter();

        String[] args = paramString.split(splitPattern);
        try {
            formatter.doMain(args, input, output);
        } catch (UnsupportedEncodingException e) {
            String errorString = "TextFormatter -- " + 
                StringTable.UNSUPPORTED_ENCODING;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        } catch (FileNotFoundException e) {
            String errorString = "TextFormatter -- " + 
                    StringTable.FILE_NOT_FOUND;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
    }
    
    private static void doTransfer(Program prog, Reader input, Writer output,Map<String, Class<?>> rules)
            throws Exception {
        String[] args = prog.getParameters().split("[ ]+");
        try {
            ApertiumTransfer.doMain(args, input, output,rules);
        } catch (UnsupportedEncodingException e) {
            String errorString = "Transfer -- " + 
                StringTable.UNSUPPORTED_ENCODING;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new UnsupportedEncodingException(errorString);
        } catch (FileNotFoundException e) {
            String errorString = "Transfer -- " + 
                    StringTable.FILE_NOT_FOUND;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        } catch (Exception e) {
            String errorString = "Transfer12 -- " + e + " |"+
                    StringTable.GENERIC_EXCEPTION;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
    }

    private static void doLTProc(Program prog, Reader input, Writer output,
            boolean dispMarks) throws IOException {
        String paramString = prog.getParameters();
        String replacement = (dispMarks ? "-g" : "-n");
        paramString = paramString.replaceAll("\\$1", replacement);
        
        String[] args = paramString.split(splitPattern);
        try {
            LTProc.doMain(args, input, output);
        } catch (IOException e) {
            String errorString = "LTProc -- " + StringTable.IO_EXCEPTION;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new IOException(errorString, e);
        }
    }

    private static void doUnknown(Program prog, byte[] input, OutputStream output) 
            throws Exception {
        try {
            Process extProcess = Runtime.getRuntime().exec(prog.getFullPath() + 
                    " " + prog.getParameters());
            extProcess.getOutputStream().write(input);
            while(true) { //Keep waiting until process is finished.
                try {
                    extProcess.waitFor();
                    /* If external process is finished, we'll get to the break
                     * statement below. If we are interrupted, we won't.
                     */
                    break;
                } catch (InterruptedException e) {
                    /* We got interrupted. Run the loop again.
                     */
                }
            }
            if(extProcess.exitValue() != 0) { 
                //Assume process follows convention of 0 == Success
                String errorString = prog.getCommandName() + " (Unknown) -- " +
                        "External program failed, returned non-zero value: " + 
                        extProcess.exitValue();
                throw new Exception(errorString);
            }
            int currByte;
            while((currByte = extProcess.getInputStream().read()) != -1 ) {
                output.write(currByte);
            }
        } catch (IOException e) {
            String errorString = prog.getCommandName() + " (Unknown) -- " +
                    StringTable.IO_EXCEPTION;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new IOException(errorString, e);
        }
    }

    /**
     * This separate dispatch for UNKNOWN programs is because we have to use
     * a byte array and an output stream instead of Reader and Writer, when redirecting
     * input and output to and from the externally existing program.
     * @param prog
     * @param input
     * @param output
     * @throws Exception 
     */
    public static void dispatchUnknown(Program prog, byte[] input, 
            OutputStream output) throws Exception {
        switch(prog.getProgram()) {
            case UNKNOWN:
                doUnknown(prog, input, output);
                break;
            default:
                throw new IllegalArgumentException("dispatchUnknown() should only be " + 
                        "used for UNKNOWN programs. ProgEnum was: " + prog.getProgram());
        }
    }
    

    public static void dispatch(Program prog, Reader input, Writer output, Map<String, Class<?>> rules,
            boolean dispAmb, boolean dispMarks) throws Exception {
        switch(prog.getProgram()) {
            case INTERCHUNK:
                doInterchunk(prog, input, output,rules);
                break;
            case LT_PROC:
                doLTProc(prog, input, output, dispMarks);
                break;
            case POSTCHUNK:
                doPostchunk(prog, input, output,rules);
                break;
            case PRETRANSFER:
                doPretransfer(prog, input, output);
                break;
            case TAGGER:
                doTagger(prog, input, output, dispAmb);
                break;
            case TRANSFER:
                doTransfer(prog, input, output,rules);
                break;
            case TXT_DEFORMAT:
                doTextFormat(prog, input, output, true);
                break;
            case TXT_REFORMAT:
                doTextFormat(prog, input, output, false);
                break;
            case UNKNOWN:
                throw new IllegalArgumentException("dispatch() should not be used for " + 
                        "UNKNOWN programs, use dispatchUnknown() instead.");
            default:
                //We should never get here.
                throw new IllegalArgumentException("Unrecognized ProgEnum: " + 
                        prog.getProgram());
        }
    }
}
