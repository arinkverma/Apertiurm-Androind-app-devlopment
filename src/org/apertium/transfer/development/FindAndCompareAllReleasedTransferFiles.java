/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium.transfer.development;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import org.apertium.transfer.ApertiumTransfer;
import org.apertium.transfer.compile.ApertiumTransferCompile;

/**
 * comparison of all language pairs of interpreded and compiled version of transfer.
 * @author Jacob Nordfalk
 */
public class FindAndCompareAllReleasedTransferFiles {

  static String datadir = "/home/j/esperanto/apertium-svn/apertium/trunk";

static String[] transferFileList = {
  /*
"apertium-nn-nb/apertium-nn-nb.nn-nb.t1x",
   */
"apertium-cy-en/apertium-cy-en.en-cy.t1x",
"apertium-cy-en/apertium-cy-en.cy-en.t1x",
//"apertium-es-ast/apertium-es-ast.es-ast.t1x",
"apertium-en-ca/apertium-en-ca.en-ca.t1x",

//apertium-en-gl/apertium-en-gl.gl-en.t1x, apertium-en-ca/apertium-en-ca.en-ca.t1x
};


  public static String[] findAlllTransferFilesOnDisk() throws IOException {
    ArrayList<String> transerFiles = new ArrayList<String>();
    Process pf=Runtime.getRuntime().exec(new String[] {"find", ".", "-name", "*.t1x"}, null, new File(datadir));
    BufferedReader br=new BufferedReader(new InputStreamReader(pf.getInputStream()));
    String t1xFile;
    while ((t1xFile=br.readLine())!=null) {
      if (t1xFile.startsWith("./")) t1xFile = t1xFile.substring(2); // strip ./
      transerFiles.add(t1xFile);
    }
    br.close();
    return transerFiles.toArray(new String[transerFiles.size()]);
  }




    public static void main(String[] args) throws Exception {
      String testdir = "testdata/transfer/";
      //String rootDirRoot = "./tmp/transfertest/";
      String rootDirRoot = "transfertest/";
      boolean redoInterpretedTransfer = true;
      boolean redoBytecodeCompilation = false;

      System.err.println("rootDirRoot = " + new File(rootDirRoot).getAbsolutePath());

      // Average speedup: 4.189285714285715 - individual: [3.96, 4.01, 4.43, 4.46, 4.27, 3.97, 4.4, 4.36, 4.44, 4.56, 4.34, 4.03, 4.21, 3.94, 4.75, 4.48, 3.83, 4.2, 4.41, 4.1, 4.8, 4.3, 4.09, 4.85, 4.22, 4.59, 3.68, 3.93, 4.78, 3.42, 4.54, 4.86, 4.56, 3.2, 3.97, 3.8, 4.86, 3.33, 4.09, 2.97, 4.54, 4.52, 4.64, 4.91, 3.27, 4.11, 4.3, 4.1, 4.34, 3.67, 3.68, 4.13, 4.35, 3.27, 4.46, 4.32]
      // Average speedup: 4.945645161290321 - individual: [4.36, 4.29, 5.04, 4.49, 5.76, 4.29, 5.06, 4.92, 4.05, 5.37, 5.02, 5.63, 6.87, 5.83, 5.78, 4.64, 4.76, 5.45, 4.6, 5.21, 4.06, 5.32, 4.5, 5.76, 4.67, 5.41, 5.68, 6.63, 5.37, 6.13, 4.84, 5.6, 3.82, 4.7, 5.13, 4.92, 6.12, 4.72, 3.95, 5.33, 5.3, 4.62, 4.01, 3.21, 7.8, 3.48, 4.49, 4.97, 4.73, 4.78, 3.81, 4.74, 4.62, 5.9, 4.19, 4.55, 3.2, 4.05, 4.32, 5.64, 5.03, 5.11]
      String inputFile = testdir+"transferinput-en-eo.t1x.txt";

      // Average speedup: 1.168421052631579 - individual: [0.28, 0.63, 0.65, 1.07, 1.13, 1.01, 1.28, 1.24, 1.1, 1.33, 1.36, 1.0, 0.91, 0.94, 1.37, 1.47, 0.54, 1.69, 1.42, 1.37, 1.47, 1.51, 1.61, 1.6, 1.48, 1.35, 0.36, 1.63, 1.57, 1.46, 1.47, 1.22, 1.31, 0.31, 1.45, 1.52, 1.23, 0.34, 1.21, 1.64, 0.28, 1.49, 1.76, 1.4, 1.72, 0.43, 1.5, 0.65, 1.12, 1.27, 0.42, 1.45, 1.4, 1.05, 1.58, 1.1, 0.45]
      // Average speedup: 1.2274137931034483 - individual: [0.21, 0.73, 0.7, 1.76, 1.91, 1.06, 1.18, 0.89, 1.12, 1.25, 1.77, 1.84, 0.79, 1.45, 1.25, 1.5, 1.48, 0.46, 1.75, 1.17, 1.22, 1.3, 1.38, 1.68, 1.85, 1.36, 1.54, 0.29, 1.53, 1.61, 1.26, 1.43, 0.83, 1.5, 0.31, 1.94, 1.28, 1.34, 0.28, 1.28, 1.17, 0.29, 1.67, 1.52, 1.12, 1.5, 0.35, 1.68, 1.23, 1.53, 1.3, 0.42, 1.72, 1.42, 1.62, 0.94, 1.6, 0.63]
      // After 7th of may optimization 
      // Average speedup: 1.7729824561403507 - individual: [0.29, 0.82, 0.52, 0.75, 0.98, 1.18, 1.12, 0.47, 1.59, 1.44, 1.26, 1.37, 2.04, 1.91, 1.71, 1.13, 1.11, 3.28, 1.6, 1.82, 1.09, 1.99, 1.51, 1.53, 1.76, 1.2, 4.48, 1.66, 1.41, 1.77, 1.5, 1.73, 1.36, 3.9, 1.13, 1.78, 1.74, 4.69, 1.93, 1.92, 4.54, 0.77, 1.52, 1.33, 1.74, 5.13, 1.97, 1.7, 1.71, 2.08, 1.61, 1.4, 1.61, 1.54, 1.55, 1.79, 2.6]

      //inputFile = testdir+ "transferinput-en-eo.t1x-malgranda.txt";

      String[] transferFiles = transferFileList;
      //transferFiles = findAlllTransferFilesOnDisk();


      //redoInterpretedTransfer = false;
      redoBytecodeCompilation = false;
      //printFilesAsJavaArray(transferFiles);

      ArrayList<String> doesentValidate = new ArrayList<String>();
      ArrayList<String> interpretedTranferFails = new ArrayList<String>();
      ArrayList<String> compiledTranferFails = new ArrayList<String>();
      ArrayList<String> tranferCompareOK = new ArrayList<String>();
      ArrayList<String> tranferCompareFail = new ArrayList<String>();
      ArrayList<Double> speedUps = new ArrayList<Double>();
      double speedupSum = 0;

      for (String relFile : transferFiles) try {
        System.err.println("\n============");
        String absFile = datadir + File.separator + relFile;
        String relFileWOSufffix = relFile.substring(0,relFile.lastIndexOf('.'));
        String resFileWOSufffix = rootDirRoot+"/res/"+relFileWOSufffix;
        int ret = 0;
        if (redoInterpretedTransfer) {
          new File(rootDirRoot+"/res/"+relFile).getParentFile().mkdirs();
          new File(rootDirRoot+"/actual/"+relFile).getParentFile().mkdirs();
          new File(rootDirRoot+"/expected/"+relFile).getParentFile().mkdirs();
          ret = exec("apertium-validate-transfer "+absFile);
          if (ret!=0) {
            System.err.println("ERROR: "+absFile+"\ndoes not pass apertium-validate-transfer. Skipping...");
            doesentValidate.add(relFile);
            continue;
          }
          exec("apertium-preprocess-transfer "+absFile+" "+resFileWOSufffix+".bin");
          exec("cp "+absFile+" "+new File(rootDirRoot+"/res/"+relFile).getParent());
        } else if (!new File(resFileWOSufffix+".bin").exists()) {
          System.err.println("ERROR: "+absFile+"\ndid not pass apertium-validate-transfer. Skipping...");          
          continue;
        }

        if (redoBytecodeCompilation)
        try {
          ApertiumTransferCompile.main(new String[]{absFile, resFileWOSufffix+".class"});
        } catch (Throwable e) {
          System.err.println("COMPILATION failed:"+e);
          e.printStackTrace();
          compiledTranferFails.add(relFile);
          continue;
        }




        long time = System.currentTimeMillis();
        long interpretedTime = 1000; // 1 sekundo, se ni transsaltas
        if (redoInterpretedTransfer || !new File(rootDirRoot+"/expected/"+relFileWOSufffix+".txt").exists() ) {
          ret = exec("apertium-transfer", absFile, resFileWOSufffix+".bin", testdir+"en-eo.autobil.bin",
            inputFile, rootDirRoot+"/expected/"+relFileWOSufffix+".txt");

          if (ret!=0) interpretedTranferFails.add(relFile);

          interpretedTime = System.currentTimeMillis()-time;
          System.err.println("Interpreted transfer took " + (interpretedTime/10)*0.01+" secs");
          time = System.currentTimeMillis();
        }

        try {
        ApertiumTransfer.main(new String[]{resFileWOSufffix+".class", resFileWOSufffix+".bin", testdir+"en-eo.autobil.bin",
          inputFile, rootDirRoot+"/actual/"+relFileWOSufffix+".txt"});
        } catch (Exception e) {
          e.printStackTrace();
          compiledTranferFails.add(relFile);
          ret = -1;
        }

        long bytecodeCompiledTime = System.currentTimeMillis()-time;
        System.err.println("bytecode compiled transfer took " + (bytecodeCompiledTime/10)*0.01+" secs");

        if (ret!=0) {
          System.err.println("(transfer failed so not comparing)");
        } else {
          double speedup =  (100*interpretedTime/bytecodeCompiledTime)/100.0;
          speedupSum += speedup;
          speedUps.add(speedup);
          System.err.println("Speedup factor: " +speedup);
          String diff = "diff -q "+rootDirRoot+"/expected/"+relFileWOSufffix+".txt "+rootDirRoot+"/actual/"+relFileWOSufffix+".txt";
          ret = exec(diff);
          if (ret==0) {
            System.err.println("OK: Output of interpreted and bytecode compiled transfer is exactly the same");
            tranferCompareOK.add(relFile);

          } else {
            System.err.println(diff);
            System.err.println("FAIL: Output of interpreted and bytecode compiled transfer HAS DIFFERENCES");
            tranferCompareFail.add(relFile);
          }
        }

        /*
        ApertiumTransfer.main(new String[]{resFileWOSufffix+".class", resFileWOSufffix+".bin", testdir+"en-eo.autobil.bin",
          testdir+"transferinput-en-eo.t1x-malgranda.txt"});
//        ApertiumTransfer.main(new String[]{resFileWOSufffix+".class", resFileWOSufffix+".bin", testdir+"en-eo.autobil.bin",
//          testdir+"transferinput-en-eo.t1x-malgranda.txt"});
*/
      } catch (Exception e) {
        e.printStackTrace();
      }

      print("Transfer files doesent validate", doesentValidate);
      print("Interpreted (old) tranfer fails", interpretedTranferFails);
      print("Compiled (new) tranfer fails", compiledTranferFails);
      print("Old & new tranfer give same results", tranferCompareOK);
      print("Old & new tranfer give different results",tranferCompareFail );
      printFilesAsJavaArray(tranferCompareFail.toArray(new String[0]));


      System.err.println("Average speedup: "+speedupSum/speedUps.size()+" - individual: "+speedUps);
    }

  public static void printFilesAsJavaArray(String[] transferFiles) {
    System.out.println("transferFiles = new String[] {");
    for (String relFile : transferFiles) {
      System.out.println("\""+relFile+"\",");
    }
    System.out.println("};");
  }

  public static int exec(String... cmd) throws Exception {
    //System.err.println("exec: " + Arrays.toString(cmd).replaceAll(", ", " "));
    Process p = Runtime.getRuntime().exec(cmd);
    BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
    String s=br.readLine();
    br.close();
    if (s!=null) {
      System.err.println(s);
      p.destroy();
      return -1;
    }
    br=new BufferedReader(new InputStreamReader(p.getErrorStream()));
    s=br.readLine();
    br.close();
    if (s!=null) {
      System.err.println(s);
      p.destroy();
      return -1;
    }
      int ret = p.waitFor();
   // System.err.println("exec finish");
      p.getErrorStream().close();
      p.getInputStream().close();
      p.getOutputStream().close();
      return ret;
  }

  public static int exec(String cmd) throws Exception {
    return exec(cmd.split(" "));
  }

  private static void print(String string, ArrayList<String> doesentValidate) {
    System.err.println(string+" "+doesentValidate.size()+": "+doesentValidate);
  }



}
