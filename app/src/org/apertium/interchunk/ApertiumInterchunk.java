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

import static org.apertium.utils.IOUtils.openInFileReader;
import static org.apertium.utils.IOUtils.openOutFileWriter;
import static org.apertium.utils.IOUtils.openFile;
import static org.apertium.utils.MiscUtils.getLineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.apertium.lttoolbox.Getopt;
import org.apertium.transfer.TransferClassLoader;
//import org.apertium.transfer.generated.apertium_eo_en_eo_en_antaux_t2x;
//import org.apertium.transfer.generated.apertium_eo_en_eo_en_t2x;
//import org.apertium.transfer.generated.apertium_eo_en_eo_en_t3x;
import org.apertium.utils.StringTable;

import android.util.Log;

/**
 * @author Stephen Tigner
 *
 */
public class ApertiumInterchunk {
    
    public static class CommandLineParams {
        public Reader input = null;
        public Writer output = null;

        public String t2xFile = null;
        public String preprocFile = null; //formerly f1, f2, these are more descriptive names
        public boolean nullFlush = false;
    }

    private static void message(String commandName) {
        PrintStream stderr = System.err; //Allows ouput lines to be shorter.
        stderr.println("USAGE: " + commandName + " [-z] t2x preproc [input [output]]");
        stderr.println("  t2x        t2x rules file");
        stderr.println("  preproc    result of preprocess trules file");
        stderr.println("  input      input file, standard input by default");
        stderr.println("  output     output file, standard output by default");
        stderr.println("OPTIONS");
        stderr.println("  -z         flush buffer on '\0'");
    }
    
    /* We don't use or need the testfile() function that's here in the C++ version.
     * It's just used in main() to check for the existence of the t2x and preproc files.
     */
    
    public static boolean parseCommandLine(String[] args, CommandLineParams par,
            String commandName, boolean pipelineMode) throws FileNotFoundException, 
            UnsupportedEncodingException {
        if (args.length == 0) {
            if(!pipelineMode) { message(commandName); }
            return false;
        }

        Getopt getopt = new Getopt(commandName, args, "zh");

        while (true) {
            int c = getopt.getopt();
            if (c == -1) {
                break;
            }
            switch (c) {
                case 'z':
                    par.nullFlush = true;
                    break;

                case 'h':
                default:
                    if(!pipelineMode) { message(commandName); }
                    return false;
            }
        }

        int optIndex = getopt.getOptind();
        switch(args.length - optIndex ) { //number of non-option args
            /* This avoids code duplication by allowing cases to "fall through."
             * The higher cases just add extra lines to the top of the lower cases,
             * so by allowing the code to fall through to the lower cases (instead of
             * breaking), we don't need to duplicate the same code several times.
             */
            case 4:
                /* The reason why the output and input assignments are skipped if
                 * we are in pipeline mode is because they are ignored in pipeline
                 * mode, as we are using internal string readers and writers.
                 */
                if(!pipelineMode) {
                    par.output = openOutFileWriter(args[optIndex + 3]);
                }
            case 3:
                if(!pipelineMode) {
                    par.input = openInFileReader(args[optIndex + 2]);
                }
            case 2:
                par.preprocFile = args[optIndex + 1];
                par.t2xFile = args[optIndex];
                break;
            default:
                if(!pipelineMode) { message(commandName); }
                return false;
        }
        return true;
    }

    /**
     * Split this off from the main() function to help facilitate inter-jvm
     * launching of different components from a central dispatcher.
     * @param par
     * @throws Exception 
     */
    public static void doMain(CommandLineParams par, Interchunk i,Map<String, Class<?>> rules) 
            throws Exception {

        i.setNullFlush(par.nullFlush);

        File txFile = openFile(par.t2xFile);
        File binFile = openFile(par.preprocFile); 
        Class<?> t2xClass = null;//TransferClassLoader.loadTxClass(txFile, binFile);
        
        if(txFile.getName().contains("antaux_t2x")){
        	t2xClass = rules.get("antaux_t2x");
        }else if(txFile.getName().contains("t2x")){
        	t2xClass = rules.get("t2x");
        }else if(txFile.getName().contains("t3x")){
        	t2xClass = rules.get("t3x");
        }
      /*
        if(txFile.getName().contains("eo-en.antaux_t2x")){
        	t2xClass = apertium_eo_en_eo_en_antaux_t2x.class;
        }else if(txFile.getName().contains("eo-en.t2x")){
        	t2xClass = apertium_eo_en_eo_en_t2x.class;
        }else if(txFile.getName().contains("eo-en.t3x")){
        	t2xClass = apertium_eo_en_eo_en_t3x.class;
        }
      */
   
        
        i.read(t2xClass, par.preprocFile);
        i.interchunk(par.input, par.output);
        par.output.flush(); //Have to flush or there won't be any output.
    }

    /**
     * @param args
     * @throws Exception 
     */
    public static int main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        Interchunk i = new Interchunk();
        
        CommandLineParams par = new CommandLineParams();
        try {
            /* Parse the command line. The passed-in CommandLineParams object
             * will be modified by this method.
             */
            if(!parseCommandLine(args, par, "Interchunk", false)) {
                return 1;
            }
        } catch (FileNotFoundException e) {
            String errorString = "ApertiumInterchunk (I/O files) -- " +
                    StringTable.FILE_NOT_FOUND;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        } catch (UnsupportedEncodingException e) {
            String errorString = "ApertiumInterchunk (I/O files) -- " +
                    StringTable.UNSUPPORTED_ENCODING;
            errorString += getLineSeparator() + e.getLocalizedMessage();
            throw new Exception(errorString, e);
        }
        
        doMain(par, i,null);
        return 0;
    }

}
