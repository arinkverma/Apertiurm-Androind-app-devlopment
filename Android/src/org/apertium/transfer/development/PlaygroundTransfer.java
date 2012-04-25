/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium.transfer.development;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apertium.transfer.Transfer;

/**
 *
 * @author Jacob Nordfalk
 */
public class PlaygroundTransfer {


    public static void main(String[] args) throws Exception {
      Transfer t = new Transfer();
      String dir = "/home/j/esperanto/apertium/apertium-eo-en/";
      //t.read(dir+"apertium-eo-en.en-eo.t1x", dir+"en-eo.t1x.bin", dir+"en-eo.autobil.bin");
      Class transferClass =
       Class.forName("org.apertium.transfer.generated.apertium_eo_en_en_eo_t1x");

      t.read(transferClass, dir+"en-eo.t1x.bin", dir+"en-eo.autobil.bin");

      String input = "^Prpers<prn><subj><p3><m><sg>$ ^see<vblex><past>$ ^the<det><def><sp>$ ^saw<n><sg>$^'s<gen>$ ^tooth<n><sg>$   ^.<sent>$  \n";
      //String input = " ^tooth<n><sg>$^.<sent>$\n";
      Writer output = new StringWriter(); //new PrintWriter(System.err); //
      t.transfer(new StringReader( input ), output);
      System.err.println("transfer output = " + output);

//   fstp.load(new BufferedInputStream(new FileInputStream("apertium-eo-en.en-eo.bin")));
/*
      FSTProcessor fstp = new FSTProcessor();
      fstp.load(new BufferedInputStream(new FileInputStream(dir+"en-eo.autobil.bin")));
      fstp.initBiltrans();
      String output = fstp.biltrans("house<n><sg>" , false);
      System.err.println("output = " + output);
*/
    }


}
