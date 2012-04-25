/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium.transfer.development;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apertium.transfer.compile.ParseTransferFile;

/**
 *
 * @author Jacob Nordfalk
 */
public class ParseTestTransferFiles {

  public static String findFileNameOfBinFile(String t1xFile) {
    String x = t1xFile.split("\\.")[1];
    String filetype = t1xFile.split("\\.")[2]; // t1x or t2x
    if (t1xFile.contains("genitive")) {
      x = x+ ".genitive"; // ugly UGLY hack
      filetype = t1xFile.split("\\.")[3]; // t1x or t2x
    }
    String binFile=new File(new File(t1xFile).getParent(), x).getPath()+"."+filetype+".bin";
    
    System.err.println(t1xFile +" ->  binFile = " + binFile);
    return binFile;
  }

  private static String parseAndWriteToSrc(String t1xFile) throws IOException, InterruptedException {
    ParseTransferFile p=new ParseTransferFile();
    try {
      p.parse(t1xFile);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Thread.sleep(100);
    System.out.flush();
    System.err.flush();
    Thread.sleep(100);
  //  System.err.println("p.javaCode = "+p.javaCode);
    Thread.sleep(100);
    System.out.flush();
    System.err.flush();
    Thread.sleep(100);
    //FileWriter fw = new FileWriter("/home/j/esperanto/apertium/apertium-eo-en/"+p.className+"java");
    new File("src/org/apertium/transfer/generated/").mkdirs();
    FileWriter fw=new FileWriter("src/org/apertium/transfer/generated/"+p.className+".java");
    fw.append(p.getOptimizedJavaCode());
    fw.close();
    return p.className;
  }

  private static void generateTestFiles(String t1xFile) throws Exception {
      String className = parseAndWriteToSrc(t1xFile);
      String binFile = findFileNameOfBinFile(t1xFile);
      FindAndCompareAllReleasedTransferFiles.exec("apertium-preprocess-transfer "+t1xFile+" "+binFile);
  }

    public static String[] generatedTestTranfserFiles = {
      "apertium-nn-nb.nn-nb.t1x",
      "apertium-eo-en.en-eo.t1x",
      "apertium-eo-en.en-eo.genitive.t1x",
      "apertium-eo-en.eo-en.t1x",
      "apertium-nn-nb.nb-nn.t1x",
      "apertium-cy-en.en-cy.t1x",
      "apertium-en-ca.en-ca.t1x",
      "apertium-en-es.en-es.genitive.t1x",
      "apertium-en-es.en-es.t1x"
    };

    /**
     * This rebuilds the included transfer files
     */
    public static void main(String[] args) throws Exception {

      System.err.println("System.out = " + System.in);
      //parseAndWriteToSrc("/home/j/esperanto/apertium/apertium-eo-en/apertium-eo-en.en-eo.t1x");

      for (String t1x : generatedTestTranfserFiles) {
        generateTestFiles("testdata/transfer/"+t1x);
      }
      generateTestFiles("testdata/interchunk/apertium-eo-en.en-eo.t2x");
      generateTestFiles("testdata/interchunk/apertium-en-es.en-es.t2x");

      generateTestFiles("testdata/postchunk/apertium-eo-en.en-eo.t3x");
      generateTestFiles("testdata/postchunk/apertium-en-es.en-es.t3x");

/*
      generateTestFiles("testdata/transfer/apertium-eo-en.en-eo.t1x");
      generateTestFiles("testdata/transfer/apertium-eo-en.eo-en.t1x");
      generateTestFiles("testdata/transfer/apertium-nn-nb.nn-nb.t1x");
*/
    //p.parse();
    //p.parse("/home/jim/NetBeansProjects/lttoolbox-java/apertium-en-ca.en-ca.t1x");
    }


}
