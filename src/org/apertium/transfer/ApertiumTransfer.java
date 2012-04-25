package org.apertium.transfer;

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

import static org.apertium.utils.IOUtils.getStdinReader;
import static org.apertium.utils.IOUtils.getStdoutWriter;
import static org.apertium.utils.IOUtils.openFile;
import static org.apertium.utils.IOUtils.openInFileReader;
import static org.apertium.utils.IOUtils.openOutFileWriter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Map;

import org.apertium.CommandLineInterface;
import org.apertium.lttoolbox.Getopt;
import org.apertium.lttoolbox.process.FSTProcessor;
import org.apertium.lttoolbox.process.State;


class MyGetOpt extends Getopt {

  public MyGetOpt(String[] argv, String string) {
    super("lttoolbox", argv, string);
  }

  int getNextOption() {
    return getopt();
  }
}

/**
 *
 * @author Raah
 */
public class ApertiumTransfer {

static void endProgram(String name) {
        System.out.print(name + CommandLineInterface.PACKAGE_VERSION +": \n" +
"USAGE: "+name+" trules-class preproc biltrans [input [output]]\n" +
"       "+name+" -b trules preproc [input [output]]\n" +
"       "+name+" -n trules-class preproc [input [output]]\n" +
//"       "+name+" -x extended trules preproc biltrans [input [output]]\n" +
"       "+name+" -c trules-class  preproc biltrans [input [output]]\n" +
"  trules-class  Java bytecode compiled transfer rules (.class file)\n" +
"                or XML transfer rules (.t1x file)\n" +
"  preproc       result of preprocess trules (.bin file)\n" +
"  biltrans      bilingual letter transducer file\n" +
"  input         input file, standard input by default\n" +
"  output        output file, standard output by default\n" +
"  -b            input from lexical transfer (single level transfer only)\n" +
"  -n            don't use bilingual dictionary\n" +
//"  -x bindix  extended mode with user dictionary\n" +
"  -c         case-sensitiveness while accessing bilingual dictionary\n" +
"  -z            null-flushing output on '\n" +
"  -h            shows this message\n" +
"");
        System.exit(-1);

    }

    public static void main(String[] argv) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        doMain(argv, null, null,null);
    }

    public static void doMain(String[] argv, Reader input, Writer output,Map<String, Class<?>> rules)
            throws IOException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {

        boolean useBD = true;

        if (argv.length == 0) {
            endProgram("apertium-transfer-j");
        }

        Transfer t = new Transfer();
        MyGetOpt getopt = new MyGetOpt(argv, "cvbnzhD");

        int optind = -1;
        while (true) {
            try {

                int c = getopt.getNextOption();
                if (c == -1) {
                    break;
                }
                optind ++;
                switch (c) {
                    case 'c':
                        t.setCaseSensitiveMode(true);
                        break;

                    case 'D':
                      FSTProcessor.DEBUG = true;
                      State.DEBUG = true;
                      Transfer.DEBUG = true;
                        break;

                    case 'z':
                        t.setNullFlush(true);
                        break;

                    case 'b':
                        t.setPreBilingual(true);
                        useBD = false;
                        break;

                    case 'n':
                        t.setUseBilingual(false);
                        useBD = false;
                        break;

                    case 'v':
                        System.out.println(CommandLineInterface.PACKAGE_VERSION);
                        return;

                    case 'h':
                    default:
                        endProgram(argv[0]);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                endProgram();
            }
        }

        /* If we're not using the billingual dictionary, we don't need the
         * biltrans argument.
         * This number should be one less than the number of args present, minus
         * command-line switches.
         */
        int minArgs =(useBD ? optind + 3 : optind + 2);

        if (argv.length > minArgs) {
            /* Split out into explicit variables for readability and because
             * tRulesClass originally was going to be tweaked here, but that
             * was split off into a separate method so that it could be used
             * in Interchunk and Postchunk as well.
             */

            /* Now, heres a dilemma: We might have been invoked with an XML file
             * (t1x, t2x, t3x) instead of a bytecode .class file!
             * This is because apertium-j is interpreting the .mode files in a fully
             * ignorant way with no check of argument suffices etc and letting the
             * stages themselves decide what to do. (This is a good thing!)
             * C++ way is: apertium-transfer    apertium-eo-en.eo-en.t1x  eo-en.t1x.bin  eo-en.autobil.bin
             * expected is:  apertium-transfer-j  eo-en.t1x.class                  eo-en.t1x.bin  eo-en.autobil.bin
             * see also http://wiki.apertium.org/wiki/Bytecode_for_transfer
             */
            String tRulesOrClassString = argv[optind + 1];
            String preProc = argv[optind + 2];
            String bilTrans = null;
            if(useBD) { bilTrans = argv[optind + 3]; }

           // File txOrClassFile = openFile(tRulesOrClassString);
           // File binFile = openFile(preProc);
           // Class tRulesClass = TransferClassLoader.loadTxClass(txOrClassFile, binFile);
            Class tRulesClass = null;
            if(tRulesOrClassString.contains("genitive.t1x")){
            	tRulesClass = rules.get("genitive_t1x");            	
            }else
            	tRulesClass = rules.get("t1x"); 

			
           t.read(tRulesClass, preProc, bilTrans);
        //   t.read(rules, preProc, bilTrans);
        //    if (Transfer.DEBUG)
          //      t.transferObject.debug = true;
        } else {
            endProgram();
        }

        if(input != null || output != null) {
            /* If either is supplied, ignore command-line input/output files,
             * as we are in inter-jvm pipeline mode, and if the modes file
             * is supplying input/ouput files, we don't want to use them,
             * as we are keeping everything in memory inside the jvm.
             */
            if(input == null) { input = getStdinReader(); }
            if(output == null) { output = getStdoutWriter(); }
        } else {
            /* If we aren't using the billingual dictionary, then there
             * will be one less argument on the command line than if we are.
             * And input and output files are the last two arguments on the
             * command line
             */
            int inputIndex = (useBD ? optind + 4 : optind + 3);
            if (argv.length > inputIndex) {
                input = openInFileReader(argv[optind + 4]);
            } else {
              input = getStdinReader();
            }
            int outputIndex = (useBD ? optind + 5 : optind + 4);
            if (argv.length > outputIndex) {
                output = openOutFileWriter(argv[optind + 5]);
            } else {
              output = getStdoutWriter();
            }
        }

        try {
            t.transfer(input, output);
            input.close();
            output.close();
        } catch (Exception e) {
            output.flush();
            System.out.flush();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e1) {
                /* Do nothing, we really don't care if we've been
                 * interrupted by anything, as this isn't synchronized
                 * code.
                 */
            }
            e.printStackTrace();
            if (t.getNullFlush()) {
                output.write('\0');
            }
            System.exit(1);
        }

    }

  private static void endProgram() {
    endProgram("apertium-transfer-j");
  }
}
