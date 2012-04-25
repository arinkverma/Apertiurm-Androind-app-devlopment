/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium;

import org.apertium.pipeline.ApertiumMain;
import java.io.File;
import org.apertium.lttoolbox.*;
import java.util.Arrays;
import org.apertium.formatter.TextFormatter;
import org.apertium.interchunk.ApertiumInterchunk;
import org.apertium.postchunk.ApertiumPostchunk;
import org.apertium.pretransfer.PreTransfer;
import org.apertium.tagger.Tagger;
import org.apertium.transfer.ApertiumTransfer;
import org.apertium.transfer.compile.ApertiumTransferCompile;

/**
 *
 * @author Jacob Nordfalk
 */
public class CommandLineInterface {

    public static final String PACKAGE_VERSION = "3.2j";

    static void showHelp(String invocationCommand) {
      String bareCommand = "";
      if (invocationCommand == null) {
        String jar = System.getProperty("java.class.path");
        if (jar.contains(":") || !jar.endsWith("lttoolbox.jar")) jar = "lttoolbox.jar";
        bareCommand = "java -jar " +jar;
        invocationCommand = bareCommand +" [task]";
      }
      /*
      System.out.println(System.getProperty("java.class.path"));
      System.out.println(System.getProperties());
      System.out.println(System.getenv());
      System.out.println(CommandLineInterface.class.getResource("/x"));
      System.out.println(CommandLineInterface.class.getResource("."));
       */
        System.err.println("lttoolbox: a toolbox for lexical processing, morphological analysis and generation of words\n" +
            "USAGE: "+invocationCommand+"\n" +
            "Examples:\n" +
            " " +bareCommand+ " lt-expand dictionary.dix     expand a dictionary\n" +
            " " +bareCommand+ " lt-comp lr dic.dix dic.bin   compile a dictionary\n" +
            " " +bareCommand+ " lt-proc dic.bin              morphological analysis/generation\n" +
            " " +bareCommand+ " lt-validate  dic.dix     validate a  dictionary\n" +
//            "For more help, run without a task, like: " +bareCommand+ "\n" +
            "For more help on a task, run it, like: " +bareCommand+ " lt-proc\n" +
            "See also http://wiki.apertium.org/wiki/Lttoolbox-java");
        System.exit(-1);
    }

  public static void main(String[] argv) throws Exception {
    if (argv.length == 0) showHelp(null);
      // strip evt path
      String task = new File(argv[0]).getName().trim();

      String[] restOfArgs = Arrays.copyOfRange(argv, 1 , argv.length);
      if (task.startsWith("lt-proc")) LTProc.main(restOfArgs);
      else if (task.equals("apertium") || task.equals("apertium-j")) ApertiumMain.main(restOfArgs);
      else if (task.startsWith("apertium-transfer")) ApertiumTransfer.main(restOfArgs);
      else if (task.startsWith("apertium-interchunk")) ApertiumInterchunk.main(restOfArgs);
      else if (task.startsWith("apertium-postchunk")) ApertiumPostchunk.main(restOfArgs);
      else if (task.startsWith("apertium-tagger")) Tagger.main(restOfArgs);
      else if (task.startsWith("apertium-pretransfer")) PreTransfer.main(restOfArgs);
      else if (task.startsWith("apertium-destxt")) {
        // Hack: Here should be
        // String[] restOfArgs = Arrays.copyOfRange(argv, 0 , argv.length);
        // restOfArgs[0] = "-d";
        // TextFormatter.main(restOfArgs);
        // Hack: but in this case we can just reuse argv:
        argv[0] = "-d";
        TextFormatter.main(argv);
      }
      else if (task.startsWith("apertium-retxt")) {
        //TextFormatter.main(restOfArgs);
        argv[0] = "-r";
        TextFormatter.main(argv);
      }
      else if (task.startsWith("lt-expand")) LTExpand.main(restOfArgs);
      else if (task.startsWith("lt-comp")) LTComp.main(restOfArgs);
      else if (task.startsWith("lt-validate")) LTValidate.main(restOfArgs);
      else if (task.startsWith("apertium-preprocess-transfer-bytecode")) ApertiumTransferCompile.main(restOfArgs);
      else {
        System.err.println("Command not recognized: "+task); // Arrays.toString(argv).replaceAll(", ", " ")
        showHelp(null);
      }
    }

}
