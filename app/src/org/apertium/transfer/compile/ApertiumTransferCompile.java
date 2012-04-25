package org.apertium.transfer.compile;

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

import org.apertium.CommandLineInterface;

import static org.apertium.utils.IOUtils.addTrailingSlash;
import static org.apertium.utils.IOUtils.openFile;

import org.apertium.lttoolbox.*;
import java.io.*;
import org.apertium.transfer.compile.ParseTransferFile;
import org.apertium.utils.StringTable;

/**
 * 
 * @author Jacob Nordfalk
 */
public class ApertiumTransferCompile {


    static void endProgram(String name) {
        System.out.print(name + CommandLineInterface.PACKAGE_VERSION +": \n" +
"USAGE: "+name+" trules  trules-class\n" +
"  trules     transfer rule (.t1x) source file\n" +
"  trules-class  Java bytecode compiled transfer rules (.class) output file\n" +
"");
        System.exit(-1);

    }

  public static void main(String[] argv) throws Exception {

      if (argv.length != 2) {
          endProgram("apertium-preprocess-transfer-bytecode-j");
      }

      ParseTransferFile p = new ParseTransferFile();
      System.err.println("Parsing " + argv[0]);
      p.parse(argv[0]);

      File dest = openFile(argv[1]);
      File dir = dest.getParentFile();
      if (dir!=null) {
        dir.mkdirs();
      } else {
        dir = new File(".");
      }
      File javaDest = new File(dir, p.className+".java");
      File classDest = new File(dir, p.className+".class");

      FileWriter fw = new FileWriter(javaDest);
      fw.append(p.getOptimizedJavaCode());
      fw.close();

      doMain(openFile(argv[0]), dest, javaDest, classDest, true);
  }

  /**
   * 
   * @param txFile -- The .t*x file to generate java code from.
   * @param targetClass -- The final name desired for the .class file
   * @param javaSource -- Java source file name
   * @param outputClass -- The name of the expected class output by the
   * Java compiler. Should be the same as javaSource but with a .class
   * instead of a .java extension.
   * @param displayStatusMessages -- Controls whether or not status messages
   * normally printed out to stderr while running standalone are sent.
   * @return A File object representing the filename of the resultant
   * class after compilation. Should be targetClass unless that the compiled
   * file cannot be renamed to targetClass. If the rename/move fails, then
   * it will be attempted to the same filename as targetClass, but in the system
   * temp directory. If that fails as well, null is returned.
   * @throws IOException
   * 
   */
  public static File doMain(File txFile, File targetClass, File javaSource,
          File outputClass, boolean displayStatusMessages) throws IOException {

      /* 
       * If you're calling ApertiumCompile.doMain(), you should really want to 
       * compile the file. If you don't want to compile it if it already exists, 
       * check if it exists *before* you call ApertiumCompile.doMain()
       */
      //if (!outputClass.exists()) //Left here to provide context for the above comment
      
      // Try invoking javac
      String cps =  System.getProperty("lttoolbox.jar");
      File cp = new File(cps!=null? cps : "lttoolbox.jar");
      if (!cp.exists()) cp = new File("dist/lttoolbox.jar");
      if (!cp.exists()) cp = new File("/usr/local/share/apertium/lttoolbox.jar");
      if (!cp.exists()) cp = new File("/usr/share/apertium/lttoolbox.jar");
      if (!cp.exists()) cp = new File("dist/lttoolbox.jar"); // fall back to this, to give the best suggestion below
      if (cps==null && displayStatusMessages) {
          System.err.println("Please specify location of lttoolbox.jar, for example writing java -Dlttoolbox.jar="+cp.getPath());
      }

      String exec = "javac -cp "+cp.getPath()+" "+javaSource;
      System.err.println("Compiling: "+exec);
      if (!cp.exists()) {
        if(displayStatusMessages) {
          System.err.println("Error: "+cp.getPath()+" is missing.");
          System.err.println("Please rebuild lttoolbox-java to make it appear.");
        }
        throw new FileNotFoundException(cp.getPath()+" is needed to be able to compile transfer files.");
      }

//      BaseTransferCompile tc = new InternalTransferCompile();
//      int result = tc.compile(cp, javaSource);
//      if(result != 0) { //compilation failed with internal, try external instead
//          tc = new ExternalTransferCompile();
//          try {
//              result = tc.compile(cp, javaSource);
//          } catch (FileNotFoundException e) {
//              throw new 
//                  InternalError(StringTable.COMPILATION_FAILURE);
//          }
//          if(result != 0) {
//              throw new 
//                  InternalError(StringTable.COMPILATION_FAILURE);
//          }
//      }

      if (!outputClass.exists()) {
        throw new InternalError("Compilation error - compiled " + javaSource + 
                " but " + outputClass + " didnt appear.");
      }
      
      if (!outputClass.equals(targetClass)) {
          return _renameClass(outputClass, targetClass);
      } else {
          return outputClass;
      }
  }
  
  private static File _renameClass(File outputClass, File targetClass) {
      //System.err.println("Renaming " + classDest+" to "+dest);
      boolean renSucc = outputClass.renameTo(targetClass);
      if(!renSucc) { //rename was not successful
          String tempDir = System.getProperty("java.io.tmpdir");
          tempDir = addTrailingSlash(tempDir);
          //Try to put the renamed output file in the system temp directory
          File newTarget = openFile(tempDir + targetClass.getName());
          renSucc = outputClass.renameTo(newTarget);
          if(!renSucc) {
              return null; 
              //Returning null means rename failed for temp directory too
          } else {
              return newTarget;
          }
      } else { //Added this else clause to make the code more readable.
          /* calling File.renameTo() doesn't change the filename for the outputClass
           * File object, so if we are successful, we need to return the targetClass
           * instead.
           */
          return targetClass;
      }
  }

}
