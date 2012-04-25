/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium.transfer.development;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import org.apertium.transfer.Transfer;
import org.apertium.transfer.compile.ParseTransferFile;

/**
 *
 * @author Jacob Nordfalk
 */
public class CompareAndDebugOneTestFile {

  static String dir = "testdata/transfer/";
  //static String inputFile = dir+"x";
  static String inputFile = dir+"transferinput-en-eo.t1x-malgranda.txt";

  public static void main(String[] args) throws Exception {
      compareAndDebugOneTestFile("apertium-es-ast.es-ast.t1x");
      /*
      compareAndDebugOneTestFile("apertium-eo-en.en-eo.t1x");
"apertium-nn-nb/apertium-nn-nb.nn-nb.t1x",
"apertium-cy-en/apertium-cy-en.en-cy.t1x",
"apertium-cy-en/apertium-cy-en.cy-en.t1x",
"apertium-es-ast/apertium-es-ast.es-ast.t1x",
       */

    }

  private static void compareAndDebugOneTestFile(String t1xFile) throws Exception {

      Transfer t = new Transfer();
      //Class transferClass = apertium_eo_en_eo_en_t1x.class;
      Class transferClass = Class.forName("org.apertium.transfer.generated."+ParseTransferFile.javaIdentifier(t1xFile));
      String binFile = dir+ParseTestTransferFiles.findFileNameOfBinFile(t1xFile);
      t.read(transferClass, binFile, dir+"en-eo.autobil.bin");
      t.transferObject.debug = true;


      //String inputFile = dir+"transferinput-en-eo.t1x.txt";
      FindAndCompareAllReleasedTransferFiles.exec("apertium-transfer", dir+t1xFile,  binFile, dir+"en-eo.autobil.bin",
          inputFile, "./tmp/"+t1xFile+"-expected.txt");

      Reader input = new FileReader(inputFile);
      //StringReader input = new StringReader("^116<num>$^.<sent>$ ^And<cnjcoo>$ ^the<det><def><sp>$ ^dialogue<n><sg>$ ^that<rel><an><mf><sp>$ ^occur<vblex><pres><p3><sg>$ ^during<pr>$ ^that<det><dem><pl>$ ^professional<adj>$ ^development<n><sg>$\n");
      String outFile = "./tmp/"+t1xFile+"-actual.txt";
      Writer output = new FileWriter(outFile);
      //Writer output = new OutputStreamWriter(System.out);
      t.transfer( input, output);
      output.close();

      FindAndCompareAllReleasedTransferFiles.exec("diff","./tmp/"+t1xFile+"-expected.txt", outFile );

      //timing.read(args[0], args[1], args[2]);
      //timing.transfer(new InputStreamReader( System.in ),  new OutputStreamWriter(System.out));
/*
      //Reader input = new FileReader(dir+"transferinput-en-eo.t1x-malgranda.txt");
      StringReader input = new StringReader("^good<adj><sint>$ ^deal<n><sg>$\n");

      // echo "^good<adj><sint>$ ^deal<n><sg>$" | apertium-transfer apertium-eo-en.en-eo.t1x en-eo.t1x.bin en-eo.autobil.bin

      Writer output = new StringWriter(); //new PrintWriter(System.err); //
      //Writer output = new OutputStreamWriter(System.out);
      //Writer output = new FileWriter("./tmp/transferoutput-en-eo.t1x.txt");
      timing.transfer( input, output);
      output.flush();
      System.err.println("transfer output = " + output);


      String outputs = timing.fstp.biltransWithQueue("good<adj><sint>" , false).first;
      System.err.println("output = " + outputs);
*/

  }

}
