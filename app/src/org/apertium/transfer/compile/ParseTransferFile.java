/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium.transfer.compile;

import static org.apertium.utils.IOUtils.openFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apertium.transfer.development.ParseTestTransferFiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import static org.apertium.transfer.compile.DOMTools.*;


/**
 *
 * @author Jacob Nordfalk
 */
public class ParseTransferFile {

  public String className;

  private int mode = 1; // Java by default
  private int JAVA = 1;
  private int JAVASCRIPT = 2;

  private enum ParseMode {
    TRANSFER, INTERCHUNK, POSTCHUNK
  }
  
  /* This is set to null initially to initialize it to something,
   * but something that will raise flags/exceptions if it's not set
   * properly further down the line.
   */
  ParseMode parseMode = null;
  
  /** For checking macro names and numbers of parameters */
  private HashMap<String, Integer> macroList = new HashMap<String, Integer>();

  /** For checking attributes. order is important */
  private LinkedHashSet<String> attrList = new LinkedHashSet<String>();

  /** For checking variables */
  private LinkedHashSet<String> varList = new LinkedHashSet<String>();

  /** For checking lists */
  private LinkedHashSet<String> listList = new LinkedHashSet<String>();

  public void setMode(int m) {
      if (m == JAVASCRIPT)
          this.mode = 2;
  }

  /**
   * The number of parameters in the rule/macro/method currently being defined
   */
  private int currentNumberOfWordInParameterList;

  private Element currentNode;

  private void writeMethodBody_optimized(Element c0) {
    // Investigate need of caching clip expressions
    int codeMark=javaCode.length();
    clipExprReadCache_reset();
    int generateJustBeforeInstrNo=0;
    int instrNo=0;
    for (Element instr : listElements(c0.getChildNodes())) {
      processInstruction(instr);
      if (instr.getTagName().equals("call-macro")) {
        generateJustBeforeInstrNo=instrNo+1;
        clipExprReadCache_reset(); // can't cache as macro could modify
      }
      instrNo++;
    }
    printComments(); // flush
    javaCode.setLength(codeMark);
    //System.err.println(methodName+" clipExprReadCount = " + clipExprReadCount);
    // now do the real code generation
    clipExprReadCache_decide();
    instrNo=0;
    for (Element instr : listElements(c0.getChildNodes())) {
      if (instrNo==generateJustBeforeInstrNo) {
        clipExprReadCache_generate();
      }
      printComments();
      processInstruction(instr);
      instrNo++;
    }
  }

  /** Set to true of cache evalutation of clip expressions in local variables.
   * Note that THIS CAN MAKE SOME CONSTRUCTS FAIL, like:
   * 		String cached_word2_tl_attr_nbr_ = word2.tl(attr_nbr);
   *
   * 				word2.tlSet(attr_whole, var_paraula1);
   *  ... now also attr_nbr is changed, but the cache is not!
   */
  boolean cacheClipExcpression = false;

  private void writeMethodBody(Element c0) {
    if (cacheClipExcpression) {
      writeMethodBody_optimized(c0);
    } else {
      for (Element instr : listElements(c0.getChildNodes())) {
        printComments();
        processInstruction(instr);
      }
    }
  }

  private void processLu(Element e) {
    // the lexical unit should only be outputted if it contains something
    boolean surelyNotEmpty=false;

    ArrayList<String> luelems = new ArrayList<String>();

    for (Element lu : listChildren(e)) {
      String s = evalString(lu);
      luelems.add(s);
      if (s.length()>2 && s.startsWith("\"")) {
        surelyNotEmpty = true;
      }
    }

    if (luelems.size()==0) {
      System.err.println("!!!!!!!!!! XXXXXXXXXXXXX luelems.size() = " + luelems.size());
      return; 
    }

    if (surelyNotEmpty) {
      luelems.add(0,"'^'"); // insert first
      luelems.add("'$'"); // append
      for (String s : luelems) {
        append(s);
      }
    } else {
      // Perhaps empty expression. Do a temp string and evaluate runtime
      println("{");
      if (mode == JAVASCRIPT)
          println("var myword = ");
      else
          println("String myword = ");
      for (int i=0; i<luelems.size(); i++) {
        String s = luelems.get(i);
        println((i==0?"         ":"         +")+s);
      }
      println(luelems.size()==0?"         \"\";":"         ;");
      println("if (myword.length()>0)");
      println("{");
      append("'^'");
      append("myword");
      append("'$'");
      println("}");
      println("}");
    }

  }

    public static void main(String[] args) throws Exception {
      ParseTestTransferFiles.main(args);
      String res = optimizeCode(
			"    out.append('{');\n"+
			"   out.append('^');\n"
          );

      System.err.println("res = " + res);
    }

  private static String optimizeCode(String code) {
    // Replace 2 following appends as one, if they are both strings/chars
    String newCode = code.replaceAll(
        "out\\.append\\(['\"]([^'\"]+)['\"]\\);\\s*"+
        "out\\.append\\(['\"]([^'\"]+)['\"]\\);"
        , 
        "out.append(\"$1$2\");");

    // Repeat until stable
    if (newCode.equals(code)) return newCode;
    return optimizeCode(newCode);
  }



  //public static final int OutputType_LU = 0;
  //public static final int OutputType_CHUNK = 1;
  enum OutputType {
    lu, chunk
  };
  OutputType defaultAttrs;
  

  public String getJavaCode() {
    return javaCode.toString();
  }

  public String getOptimizedJavaCode() {
    //return optimizeCode(javaCode.toString());
    return optimizeCode(javaCode.toString());
  }


  private void append(String str) {
    println("out.append("+str+");");
  }



  private String getPathAsString(Node n) {
    if (n==null) return "";
    String path = "";
    do  {
      String attrss = "";
      NamedNodeMap attrs = n.getAttributes();
      //for (int i=0; i<attrs.getLength(); i++) attrss += " "+attrs.item(i).getNodeName()+"="+attrs.item(i).getNodeValue();
      if (attrs!=null) {
        for (int i=0; i<attrs.getLength(); i++) attrss += " "+attrs.item(i);
      }

      if (path.length()>0) path = "/"+path;
      path = "<"+n.getNodeName() + attrss + ">" + path;
      n = n.getParentNode();

    } while (!(n instanceof Document));
    return " - for "+path;
  }

  private void parseError(String string) {
    string = string + getPathAsString(currentNode);
    println(string);
    System.err.println( string);
  }

  private String escapeStr(String unescaped) {
    return unescaped.replace("\\", "\\\\").replace("\"", "\\\"");
  }



  private String attrItemRegexpSimple(ArrayList<String> items) {
    StringBuilder re = null;
    for (String item : items) {
      if (re==null) re = new StringBuilder(items.size()*20);
      else re.append( ">|<");
      re.append(escapeStr(item.replace(".", "><")));
    }
    return "<"+ re.toString() + ">";
  }

  // Optimization doesent seem to do much difference...
  private String attrItemRegexp(ArrayList<String> items) {

    String item0 = items.get(0);
    int startSame = 0;
    stop:
    while (startSame<item0.length()) {
      char ch = item0.charAt(startSame);
      for (String item : items)
        if (startSame==item.length() || item.charAt(startSame) != ch) break stop;
      startSame++;
    }

    int stopSame = 0;
    stop:
    while (stopSame<item0.length()-startSame) {
      char ch = item0.charAt(item0.length()- stopSame-1);
      for (String item : items)
        if (stopSame==item.length() || item.charAt(item.length()- stopSame-1) != ch) break stop;
      stopSame++;
    }

    StringBuilder re = null;
    for (String item : items) {
      if (re==null) re = new StringBuilder(items.size()*20);
      else re.append( '|');
      re.append(escapeStr(item.substring(startSame,item.length()- stopSame)));
    }
    String res = "<"+item0.substring(0,startSame)+(re.length()==0? "": "(?:"+ re.toString() + ")")+item0.substring(item0.length()- stopSame)+">";
    res = res.replace(".", "><");

    //System.err.println("items = " + items);
    //System.err.println("res = " + res);
    return res;
  }

  LinkedHashMap<String, Integer> clipExprReadCount = new LinkedHashMap<String,Integer>();
  LinkedHashMap<String, String> clipExprCacheVars = new LinkedHashMap<String,String>();
  LinkedHashMap<String, String> clipExprCacheVars_decided = new LinkedHashMap<String,String>();
  
  private void clipExprReadCache_reset() {
    clipExprReadCount.clear();
    clipExprCacheVars.clear();
    clipExprCacheVars_decided.clear();
  }

  private void clipExprReadCache_decide() {
    for (String clipReadExpr : clipExprReadCount.keySet()) {
      if (clipExprReadCount.get(clipReadExpr)<= 4) continue; // If only used a few times, don''t cache
      String var = "cached_"+ javaIdentifier(clipReadExpr);
      clipExprCacheVars_decided.put(clipReadExpr, var);
    }
  }

  private void clipExprReadCache_generate() {
    // iterate alphabetivally (thru TreeSet)
    for (String clipReadExpr : new TreeSet<String>(clipExprCacheVars_decided.keySet())) {
      String var = clipExprCacheVars_decided.get(clipReadExpr);
      if (mode == JAVASCRIPT)
          println("var "+var+" = "+clipReadExpr+";");
      else
          println("String "+var+" = "+clipReadExpr+";");
      clipExprCacheVars.put(clipReadExpr, var);
    }
    
  }
  /**
   * Generates Java code for reading the value of a clip
   * @param e the clip tag
   * @return java code
   */
  private String getReadClipExpr(Element e) {
    currentNode = e;
    String side=e.getAttribute("side");
    // the 'side' attribute only really makes sense when a bilingual dictionary is used to translate the words (i.e. english 'dog<n><sg>' is translated to for example esperanto 'hundo<n><pl>').
    // If no bilingual dictionary is used (i.e. for apertium-transfer -n, for apertium-interchunk and for apertium-postchunk), then the sl and tl values will be the same
    if (parseMode != ParseMode.TRANSFER) side = "tl";

    String part=e.getAttribute("part");
    String pos=e.getAttribute("pos");
    String queue = (e.getAttribute("queue").equals("no")?"NoQueue":"");

    String clipReadExpr = word(pos)+"."+side+queue+"("+attr(part)+")";

    // fix for apertium/interchunk.cc evalString(): if(ti.getContent() == "content") then we need to strip the brackets
    if ("content".equals(part)) {
      clipReadExpr = "TransferWord.stripBrackets("+clipReadExpr+")";
    }

    // Update/check cache
    Integer count = clipExprReadCount.get(clipReadExpr);
    count = count==null? 1 : count+1;
    //System.err.println("getReadClipExpr clipReadExpr = " + clipReadExpr+ " count="+count);
    clipExprReadCount.put(clipReadExpr, count);
    String cacheVar = clipExprCacheVars.get(clipReadExpr);
    if (cacheVar!=null) return cacheVar;

    return clipReadExpr;
  }

  /**
   * Generates Java code for writing to a clip
   * @param e the clip tag
   * @param value new value the clip will get assigned
   * @return java code
   */
  private String getWriteClipExpr(Element e, String value) {
    currentNode = e;
    String side=e.getAttribute("side");
    // the 'side' attribute only really makes sense when a bilingual dictionary is used to translate the words (i.e. english 'dog<n><sg>' is translated to for example esperanto 'hundo<n><pl>').
    // If no bilingual dictionary is used (i.e. for apertium-transfer -n, for apertium-interchunk and for apertium-postchunk), then the sl and tl values will be the same
    if (parseMode != ParseMode.TRANSFER) side = "tl";
    String part=e.getAttribute("part");
    String pos=e.getAttribute("pos");
    String queue = (e.getAttribute("queue").equals("no")?"NoQueue":"");

    String clipReadExpr = word(pos)+"."+side+queue+"("+attr(part)+")";

    // check if cached variable needs to be updated
    String cacheVar = clipExprCacheVars.get(clipReadExpr);
    if (cacheVar==null) {
      return word(pos)+"."+side+"Set"+queue+"("+attr(part)+", "+value+");";    
    } else {
      String writeClipWithUpdateOfCachedVar = word(pos)+"."+side+"Set"+queue+"("+attr(part)+", ("+cacheVar+"="+value+"));";
      //System.err.println("writeClipWithUpdateOfCachedVar = " + writeClipWithUpdateOfCachedVar);
      return writeClipWithUpdateOfCachedVar;
    }

  }

  public static String javaIdentifier(String str) {
    return str.replaceAll("\\W", "_");
  }

  private String javaStringArray(ArrayList<String> items) {
    String s = "new String[] { ";
    for (String i : items) s += "\""+i+"\", ";
    s += "}";
    return s;
  }

  //
  //  Code from transfer.c
  //

  private String evalString(Element e) {
    printComments();
    currentNode = e;
    String n = e.getTagName();
    if (n.equals("clip")) {
      String as = e.getAttribute("link-to");
      String expr=getReadClipExpr(e);
      if (as.isEmpty()) return expr;
      else return "("+expr+".isEmpty()?\"\" : \"<"+as+">\")";
    } else if (n.equals("lit-tag")) {
      return str("<"+e.getAttribute("v").replaceAll("\\.", "><")+">");
    } else if (n.equals("lit")) {
      return str(e.getAttribute("v"));
    } else if (n.equals("b")) {
      String pos = e.getAttribute("pos");
      if (pos.isEmpty()) return(str(" "));
      else return blank(pos);
    } else if (n.equals("get-case-from")) {
      String pos = e.getAttribute("pos");
      String eval = evalString(getFirstChildElement(e));
      String queue = (e.getAttribute("queue").equals("no")?"NoQueue":"");
      // TODO cache
      return "TransferWord.copycase("+word(pos)+".sl"+queue+"(attr_lem), "+ eval + ")";
    } else if (n.equals("var")) {
      return var(e.getAttribute("n"));
    } else if (n.equals("case-of")) {
      return "TransferWord.caseOf("+getReadClipExpr(e)+")";
    } else if (n.equals("concat")) {
      String res = "("+str("");
      for (Element c : listElements(e.getChildNodes())) {
        printComments();
        res += "+"+ evalString(c);
      }
      res += ")";
      return res;
    } else if (n.equals("lu-count") && parseMode==ParseMode.POSTCHUNK) {
      // the number of lexical units inside the chunk is the length of the words array, but as we might be in a
      // macro, where we dont have access to the array, we use a global variable
      return "lu_count";
    }
    throwParseError("// ERROR: unexpected rvalue expression "+e);
    return str("");// +"/* not supported yet: "+e + "*/";
  }

  private void processOut(Element instr) {
    printComments();
    currentNode = instr;
    if (defaultAttrs == OutputType.lu) {
    } else { // defaultAttrs == Transfer.OutputType.chunk
    }
   
    for (Element e : listChildren(instr)) {
      printComments();
      String n = e.getTagName();
      if (n.equals("lu")) {
        processLu(e);
      } else if (n.equals("mlu")) {
        append("'^'");
        for (java.util.Iterator<Element> it = listChildren(e).iterator(); it.hasNext();) {
            Element mlu = it.next();
            for (Element lu : listChildren(mlu)) {
              append(evalString(lu));
            }
            if (it.hasNext())
              append("'+'");
        }
        append("'$'");
      } else if (n.equals("chunk")) processChunk(e);
      else append( evalString(e) ); // takes care of 'b'
    }
  }


  private void processChunk(Element e) {
    /* If we are processing an interchunk file, chunk tags should be treated
     * like lu tags?
     */
    if(parseMode == ParseMode.INTERCHUNK) {
      /* Try just calling processLu() for now, if that doesn't work, will have
       * to try something else. ;)
       * Not sure if this is the right way to go, may be causing the issues with
       * missing the ^ and $ on chunks in Interchunk.
       */
      processLu(e);
      return;
    }
    
    printComments();
    currentNode = e;
    String name = e.getAttribute("name");
    String namefromvar = e.getAttribute("namefrom");
    String caseofchunkvar = e.getAttribute("case");
//    if (caseofchunkvar.isEmpty()) caseofchunkvar = "aa";
//    else println("// not supported yet: case");

    append("'^'");
    if (caseofchunkvar.isEmpty()) {
      if (!name.isEmpty()) append(str(name));
      else if (!namefromvar.isEmpty()) {
        append(var(namefromvar));
      }
      else parseError("// ERROR: you must specify either 'name' or 'namefrom' for the 'chunk' element");
    } else {
      if (!name.isEmpty()) {
        append("TransferWord.copycase("+var(caseofchunkvar)+", "+str(name)+")");
      }
      else if (!namefromvar.isEmpty()) {
        append("TransferWord.copycase("+var(caseofchunkvar)+", "+var(namefromvar)+")");
      }
      else parseError("// ERROR: you must specify either 'name' or 'namefrom' for the 'chunk' element");
    }


    for (Element c0 : listChildren(e)) {
      printComments();
      String n = c0.getTagName();
      if (n.equals("tags")) {
        for (Element tag : listChildren(c0))
          append(evalString(findElementSibling(tag.getFirstChild())));
        append("'{'");
      } else if (n.equals("lu")) {
        processLu(c0);
        //append("'^'");
        //for (Element lu : listChildren(c0))
        //  append(evalString(lu));
        //append("'$'");
      } else if (n.equals("mlu")) {
        append("'^'");
        for (java.util.Iterator<Element> it = listChildren(c0).iterator(); it.hasNext();) {
            Element mlu = it.next();
            for (Element lu : listChildren(mlu)) {
              append(evalString(lu));
            }
            if (it.hasNext())
              append("'+'");
        }
        append("'$'");
      } else {
        append(evalString(c0));
      }
    }
    append("\"}$\"");
  }


  private void processInstruction(Element instr) {
    printComments();
    currentNode = instr;
    String n = instr.getTagName();
    if(n.equals("choose")) {
      processChoose(instr);
    }
    else if(n.equals("let"))
    {
      processLet(instr);
    }
    else if(n.equals("append"))
    {
      processAppend(instr);
    }
    else if(n.equals("out"))
    {
      processOut(instr);
    }
    else if(n.equals("call-macro"))
    {
      processCallMacro(instr);
    }
    else if(n.equals("modify-case"))
    {
      processModifyCase(instr);
    }
    else
      throwParseError("processInstruction(n = " + n);
  }

  private void processLet(Element instr) {
    printComments();
    currentNode = instr;
    Element leftSide = findElementSibling(instr.getFirstChild());
    Element rightSide = findElementSibling(leftSide.getNextSibling());

    String n = leftSide.getTagName();
    if (n.equals("var")) {
      String name = leftSide.getAttribute("n");
      println(var(name)+" = " + evalString(rightSide)+ ";");
    } else if (n.equals("clip")) {
      println(getWriteClipExpr(leftSide, evalString(rightSide)));
    } else throwParseError(n);
  }


  private void processAppend(Element instr) {
    printComments();
    currentNode = instr;
    String var = var(instr.getAttribute("n"));

    println(var +" = " + var);
    Element appendElement = findElementSibling(instr.getFirstChild());
    while (appendElement!=null) {
      println("    +" + evalString(appendElement));
      appendElement = findElementSibling(appendElement.getNextSibling());
    }
    println("    ;");
  }



  private void processModifyCase(Element instr) {
    printComments();
    currentNode = instr;
    Element leftSide = findElementSibling(instr.getFirstChild());
    Element rightSide = findElementSibling(leftSide.getNextSibling());

    String n = leftSide.getTagName();
    if (n.equals("var")) {
      String name = leftSide.getAttribute("n");
      if (varList.contains(name)) {
        String var = "var_"+name;
        println(var+" = TransferWord.copycase(" + evalString(rightSide)+ ", "+var+");");
      } else {
        parseError("// WARNING variable "+name+" doesent exist. Ignoring modify-case");
      }
    } else if (n.equals("clip")) {
      println(getWriteClipExpr(leftSide, "TransferWord.copycase("+evalString(rightSide)+", "+getReadClipExpr(leftSide)+")"));
    } else throwParseError(n);
  }


  private void processCallMacro(Element instr) {
    currentNode = instr;
    String n = instr.getAttribute("n");
    if (!macroList.containsKey(n)) {
      // this macro doesent exists!
      parseError("// WARNING: Macro "+n+" is not defined. Ignoring call. Defined macros are: "+macroList.keySet());
      return;
    }
    int macronpar = macroList.get(n);
    String par = "";
    int npar = 0;
    for (Element c : listChildren(instr)) {
      if (npar>=macronpar) {
        parseError("// WARNING: Macro "+n+" is invoked with too many parameters. Ignoring: "+c);
        break;
      }
      int pos = Integer.parseInt(c.getAttribute("pos"));
      if (!par.isEmpty())  {
        par += (pos>1?", "+blank(pos-1): ", "+str(" "));
      }
      par += ", "+word(pos);
      npar++;
    }

    while (npar<macronpar) {
      parseError("// WARNING: Macro "+n+" is invoked with too few parameters. Adding blank parameters ");
      if (!par.isEmpty())  {
        par += ", "+str("");
      }
      if (this.parseMode == ParseMode.TRANSFER) {
        par += ", "+"new TransferWord(\"\", \"\", 0)";
      } else {
        par += ", "+"new InterchunkWord(\"\")";
      }
      npar++;
    }

    printComments();
    println("macro_"+javaIdentifier(n)+"(out"+par+");");
  }

  private void processChoose(Element e) {
    currentNode = e;
    boolean first = true;
    for (Element whenC : listChildren(e))
    {
      printComments();
      String n = whenC.getTagName();
      Element c0 = getFirstChildElement(whenC);
      if (!first) println("else");
      first = false;

      if (n.equals("when")) {
        String evalLogic = processLogical( getFirstChildElement(c0));
        c0 = findElementSibling(c0.getNextSibling());
        println("if ("+evalLogic+")");
      } else {
        assert(n.equals("otherwise"));
      }

      println("{");
      while (c0 !=null)
      {
        processInstruction(c0);
        c0 = findElementSibling(c0.getNextSibling());
      }
      println("}");
    }
  }





  String processLogical(Element e) {
    printComments();
    currentNode = e;
    String n=e.getTagName();

    if (n.equals("equal")) {
      return processEqual(e);
    } else if (n.equals("begins-with")) {
      return processBeginsWith(e);
    } else if (n.equals("begins-with-listElements")) {
      return processBeginsWithList(e);
    } else if (n.equals("ends-with")) {
      return processEndsWith(e);
    } else if (n.equals("ends-with-listElements")) {
      return processEndsWithList(e);
    } else if (n.equals("contains-substring")) {
      return processContainsSubstring(e);
    } else if (n.equals("in")) {
      return processIn(e);
    } else if (n.equals("or")) {
      Element first = getFirstChildElement(e);
      String str = "("+processLogical(first);
      Element next = first;
      while ( (next = findElementSibling(next.getNextSibling())) != null) {
        str += "\n    || " + processLogical(next);
      }
      return  str+")";
    } else if (n.equals("and")) {
      Element first = getFirstChildElement(e);
      String str = "("+processLogical(first);
      Element next = first;
      while ( (next = findElementSibling(next.getNextSibling())) != null) {
        str += "\n    && " + processLogical(next);
      }
      return  str+")";
    } else if (n.equals("not")) {
      return "!" + processLogical(getFirstChildElement(e));
    }
    parseError("// SORRY: not supported yet: processLogical(c0 = "+e);
    return "false /*not supported: processLogical("+e+" */";
  }

  private String processEqual(Element e) {
    printComments();
    currentNode = e;
    Element first = findElementSibling(e.getFirstChild());
    Element second = findElementSibling(first.getNextSibling());
    boolean caseless = "yes".equals(e.getAttribute("caseless"));
    if (caseless) {
      return evalString(first)+".equalsIgnoreCase("+evalString(second)+")";
    } else {
      return evalString(first)+".equals("+evalString(second)+")";
    }
  }


  private String processBeginsWith(Element e) {
    printComments();
    currentNode = e;
    Element first = findElementSibling(e.getFirstChild());
    Element second = findElementSibling(first.getNextSibling());
    boolean caseless = "yes".equals(e.getAttribute("caseless"));
    if (caseless) {
      return evalString(first)+".startsWith("+evalString(second)+")";
    } else {
      return evalString(first)+".toLowerCase().startsWith("+evalString(second)+".toLowerCase())";
    }
  }


  private String processEndsWith(Element e) {
    printComments();
    currentNode = e;
    Element first = findElementSibling(e.getFirstChild());
    Element second = findElementSibling(first.getNextSibling());
    boolean caseless = "yes".equals(e.getAttribute("caseless"));
    if (caseless) {
      return evalString(first)+".endsWith("+evalString(second)+")";
    } else {
      return evalString(first)+".toLowerCase().endsWith("+evalString(second)+".toLowerCase())";
    }
  }

  private String processBeginsWithList(Element e) {
    printComments();
    currentNode = e;
    Element first = getFirstChildElement(e);
    Element second = findElementSibling(first.getNextSibling());
    String listName = list(second.getAttribute("n"));

    if (e.getAttribute("caseless").equals("yes")) {
      return listName +".containsIgnoreCaseBeginningWith("+evalString(first)+")";
    }
    return listName +".containsBeginningWith("+evalString(first)+")";
  }


  private String processEndsWithList(Element e) {
    printComments();
    currentNode = e;
    Element first = getFirstChildElement(e);
    Element second = findElementSibling(first.getNextSibling());
    String listName = list(second.getAttribute("n"));

    if (e.getAttribute("caseless").equals("yes")) {
      return listName +".containsIgnoreCaseEndingWith("+evalString(first)+")";
    }
    return listName +".containsEndingWith("+evalString(first)+")";
  }

  private String processContainsSubstring(Element e) {
    printComments();
    currentNode = e;
    Element first = findElementSibling(e.getFirstChild());
    Element second = findElementSibling(first.getNextSibling());
    boolean caseless = "yes".equals(e.getAttribute("caseless"));
    if (caseless) {
      return evalString(first)+".contains("+evalString(second)+")";
    } else {
      return evalString(first)+".toLowerCase().contains("+evalString(second)+".toLowerCase())";
    }
  }



  private String processIn(Element e) {
    printComments();
    currentNode = e;
    Element first = getFirstChildElement(e);
    Element second = findElementSibling(first.getNextSibling());
    String listName = list(second.getAttribute("n"));

    if (e.getAttribute("caseless").equals("yes")) {
      return listName +".containsIgnoreCase("+evalString(first)+")";
    }
    return listName +".contains("+evalString(first)+")";
  }


  private String str(String n) {
    if (n==null) return "null";
    return "\""+escapeStr(n)+"\"";
  }


//  Appendable javaCode = System.out; // new StringBuilder(1000);
  private StringBuilder javaCode = new StringBuilder(1000);

  int indent = 0;

  private void println(String string) {
    if (indent>0 && string.equals("}")) indent--;
    print("\t\t\t\t\t\t\t\t\t\t".substring(0,indent));
    print(string+"\n");
    if (string.equals("{") && indent < 10) indent++;
  }

  private void print(String string) {
     javaCode.append(string);
     //System.err.print(string);
  }

  private void printComments() {
    String c = commentHandler.toString();
    if (c.length()!=0) {
      println("/** "+c+" */");
    }
    commentHandler.getBuffer().setLength(0);
  }

  private void throwParseError(String n) {
    throw new UnsupportedOperationException("Not yet implemented:"+n+getPathAsString(currentNode));
  }

  private boolean error_UNKNOWN_VAR = false;
  private String var(String name) {
    if (varList.contains(name)) {
      return "var_"+javaIdentifier(name);
    }
    parseError("// WARNING variable "+name+" doesent exist. Valid variables are: "+varList
       + "\n// Replacing with error_UNKNOWN_VAR");
    error_UNKNOWN_VAR = true;
    return "error_UNKNOWN_VAR";
  }


  private boolean error_UNKNOWN_ATTR = false;
  private String attr(String name) {
    if (attrList.contains(name))
      return "attr_"+javaIdentifier(name);

    parseError("// WARNING: Attribute "+name+" is not defined. Valid attributes are: "+attrList
        + "\n// Replacing with error_UNKNOWN_ATTR");
    error_UNKNOWN_ATTR = true;
    return "error_UNKNOWN_ATTR";
  }

  private boolean error_UNKNOWN_LIST = false;
  private String list(String name) {
    if (listList.contains(name))
      return "list_"+javaIdentifier(name);

    parseError("// WARNING: List "+name+" is not defined. Valid lists are: "+listList
        + "\n// Replacing with error_UNKNOWN_LIST");
    error_UNKNOWN_LIST = true;
    return "error_UNKNOWN_LIST";
  }

  /**
    // in postchunk there is no certain fixed number of words when a rule is invoked
    // therefore word and blank parameters are implemented as an array
    // however, macros are the same, so we have to know if we are in a macro or not
   */
  private boolean inMacro = false;

  private String word(int pos) {
    if (parseMode == ParseMode.POSTCHUNK && !inMacro) {
      // in postchunk there is no certain fixed number of words in the rules.
      // therefore its implemented as an array
      // word[0] refers to the chunk lemma and tags
      return "words["+pos+"]";
    }

    if (pos <= currentNumberOfWordInParameterList) {
      return "word"+pos;
    }

    parseError("// WARNING clip pos="+pos+" is out of range. Replacing with an empty placeholder.");
      if (this.parseMode == ParseMode.TRANSFER) {
        return "new TransferWord(\"\", \"\", 0)";
      } else {
        return "new InterchunkWord(\"\")";
      }

  }

  private String word(String pos) {
    return word(Integer.parseInt(pos.trim()));
  }

  private String blank(int pos) {
    if (parseMode == ParseMode.POSTCHUNK && !inMacro) {
      // in postchunk there is no certain fixed number of words in the rules.
      // therefore its implemented as an array
      // TODO: check if index should be shifted one time  (word[0] refers to the chunk lemma and tags)
      return "blanks["+pos+"]";
    }

    if (pos < currentNumberOfWordInParameterList) {
      return "blank"+pos;
    }
    parseError("// WARNING blank pos="+pos+" is out of range. Replacing with a zero-space blank.");
    return str("");
  }

  private String blank(String pos) {
    return blank(Integer.parseInt(pos));
  }

 
   /**
     * @param file the address of the XML file to be read
     */
    public void parse(String file) throws IOException, ParserConfigurationException, SAXException {
      /* Don't need to switch this new File() call with an IOUtils version, because this
       * isn't actually opening a file. The purpose of this new File() is to easily split
       * the filename name off from the rest of the path.
       */
      className = javaIdentifier(new File(file).getName());
      commentHandler = new StringWriter();

      try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(openFile(file));
        Element root = doc.getDocumentElement();
        String rootTagName = root.getTagName();
        if(rootTagName.equals("transfer")) {
          parseMode = ParseMode.TRANSFER;
        } else if (rootTagName.equals("interchunk")) {
          parseMode = ParseMode.INTERCHUNK;
        } else if (rootTagName.equals("postchunk")) {
          parseMode = ParseMode.POSTCHUNK;
        } else throw new IllegalArgumentException("illegal rootTagName: "+rootTagName);

        println("package org.apertium.transfer.generated;");
        println("import java.io.*;");
        println("import org.apertium.transfer.*;");
        if (this.parseMode != ParseMode.TRANSFER) {
          println("import org.apertium.interchunk.InterchunkWord;");
        }
        println("public class "+className+" extends GeneratedTransferBase");
        println("{");
        println("public boolean isOutputChunked()");
        println("{");
        if (root.getAttribute("default").equals("chunk")) {
          defaultAttrs = OutputType.chunk;
          println("return true;");
        } else {
          defaultAttrs = OutputType.lu;
          println("return false;");
        }
        println("}");

        for (Element c0 : getChildsChildrenElements(root, "section-def-attrs")) {
          String n = c0.getAttribute("n");
          ArrayList<String> items = new ArrayList<String>();
          for (Element c1 : listChildren(c0))
            items.add(c1.getAttribute("tags"));

          /* FIX:
java match of (<prn>|<prn><ref>|<prn><itg>|<prn><tn>)  on ^what<prn><itg><sp>  is '<prn>'
pcre match of (<prn>|<prn><ref>|<prn><itg>|<prn><tn>)  on ^what<prn><itg><sp>  is '<prn><itg>'
           therefore I reorder so the longest are first.
           */
          Collections.sort(items, new Comparator<String>() {
            public int compare(String o1, String o2) {
              return o2.length() - o1.length();
            }
          });

          printComments();
          println("ApertiumRE attr_"+javaIdentifier(n)+" = new ApertiumRE(\""+attrItemRegexp(items)+"\");");
          attrList.add(n);
        }

        /*
        from transfer_data.cc:
        // adding fixed attr_items
        attr_items[L"lem"]          = L"(([^<]|\"\\<\")+)";
        attr_items[L"lemq"]        = L"\\#[- _][^<]+";
        attr_items[L"lemh"]        = L"(([^<#]|\"\\<\"|\"\\#\")+)";
        attr_items[L"whole"]       = L"(.+)";
        attr_items[L"tags"]         = L"((<[^>]+>)+)";
        attr_items[L"chname"]    = L"({([^/]+)\\/)"; // includes delimiters { and / !!!
        attr_items[L"chcontent"] = L"(\\{.+)";
        attr_items[L"content"]    = L"(\\{.+)";
        */

        String[][] fixed_attributes = {
          { "lem" , "(([^<]|\"\\<\")+)" },
          { "lemq" , "\\#[- _][^<]+" },
          { "lemh" , "(([^<#]|\"\\<\"|\"\\#\")+)" },
          { "whole" , "(.+)" },
          { "tags" , "((<[^>]+>)+)" },
          { "chname" , "(\\{([^/]+)\\/)" }, // includes delimiters { and / !!!
          { "chcontent" , "(\\{.+)" },
          { "content" ,  "(\\{.+)" }, // "\\{(.+)\\}" } would be correct, but wont work as InterchunkWord.chunkPart()
          // requires the match to have the same length as the matched string
        };

        for (String[] nameval : fixed_attributes) {
          if (attrList.add(nameval[0])) {
            println("ApertiumRE attr_"+nameval[0]+" = new ApertiumRE(\""+escapeStr(nameval[1])+"\");");
          } else {
            parseError("// WARNING: Don't define attribute "+nameval[0]+", it should keep its predefined value: "+nameval[1]);
          }
        }


        for (Element c0 : getChildsChildrenElements(root, "section-def-vars")) {
          String n = c0.getAttribute("n");
          varList.add(n);
          // fix e.g. <def-var n="nombre" v="&amp;lt;sg&amp;gt;"/> that gives
          // String var_nombre = "&lt;sg&gt;";
          String v = c0.getAttribute("v").replace("&lt;", "<").replace("&gt;", ">");
          printComments();
          if (mode == JAVASCRIPT)
              println("var var_"+javaIdentifier(n)+" = \""+v+"\";");
          else
              println("String var_"+javaIdentifier(n)+" = \""+v+"\";");
        }

        if (parseMode == ParseMode.POSTCHUNK) {
          println("String lu_count;");
        }

        for (Element c0 : getChildsChildrenElements(root, "section-def-lists")) {
          String n = c0.getAttribute("n");
          ArrayList<String> items = new ArrayList<String>();
          for (Element c1 : listChildren(c0)) {
            items.add(c1.getAttribute("v"));
          }
          listList.add(n);
          printComments();
          println("WordList list_"+javaIdentifier(n)+" = new WordList("+javaStringArray(items)+");");
        }


        inMacro = true;
        for (Element c0 : getChildsChildrenElements(root, "section-def-macros")) {
          currentNode = c0;
          String name = c0.getAttribute("n");
          String npars = c0.getAttribute("npar");
          int npar = npars.length()>0 ?  Integer.parseInt(npars) : 0;
          currentNumberOfWordInParameterList = npar;
          macroList.put(name, npar);
          String methodArguments = "";
          if (this.parseMode == ParseMode.TRANSFER) {
              for (int i=1; i<=npar; i++) methodArguments += (i==1?", ":", String "+blank(i-1)+", ")+"TransferWord "+word(i);
          } else {
              for (int i=1; i<=npar; i++) methodArguments += (i==1?", ":", String "+blank(i-1)+", ")+"InterchunkWord "+word(i);
          }
          String logCallParameters = "";
          for (int i=1; i<=npar; i++) logCallParameters += (i==1?", ":", "+blank(i-1)+", ")+" "+word(i);
          println("");
          printComments();
          String methodName = "macro_"+javaIdentifier(name);
          println("private void "+methodName+"(Writer out"+methodArguments+") throws IOException");
          println("{");
          println("if (debug) { logCall(\""+methodName+"\""+logCallParameters+"); } "); // TODO Check performance impact

          writeMethodBody(c0);

          println("}");
        }


        inMacro = false;
        int ruleNo = 0;
        printComments();
        for (Element c0 : getChildsChildrenElements(root, "section-rules")) {
          currentNode = c0;
          ArrayList<String> patternItems = new ArrayList<String>();

          String methodName = "rule"+(ruleNo++);
          for (Element c1 : getChildsChildrenElements(c0, "pattern")) {
            String n = c1.getAttribute("n");
            methodName += "__"+javaIdentifier(n);
            patternItems.add(n);
          }
          currentNumberOfWordInParameterList = patternItems.size();
          String methodArguments = "";
          if (this.parseMode == ParseMode.TRANSFER) {
              for (int i=1; i<=currentNumberOfWordInParameterList; i++) methodArguments += (i==1?", ":", String "+blank(i-1)+", ")+"TransferWord "+ word(i);
          } else if (this.parseMode == ParseMode.INTERCHUNK) {
              for (int i=1; i<=currentNumberOfWordInParameterList; i++) methodArguments += (i==1?", ":", String "+blank(i-1)+", ")+"InterchunkWord "+ word(i);
          } else { 
            assert(parseMode == ParseMode.POSTCHUNK);
            // in postchunk there is no certain fixed number of words when a rule is invoked
            // therefore its implemented as an array
            // words[0] refers to the chunk lemma (and tags)
            methodArguments += ", InterchunkWord[] words, String[] blanks";
          }

          String logCallParameters = "";
          for (int i=1; i<=currentNumberOfWordInParameterList; i++) logCallParameters += (i==1?", ":", "+blank(i-1)+", ")+" "+ word(i);
          println("");
          printComments();
          String comment = c0.getAttribute("comment");
          if (!comment.isEmpty()) println("// "+comment);
          if (mode == JAVASCRIPT)
              println("function "+methodName+"(Writer out"+methodArguments+") ");
          else
              println("public void "+methodName+"(Writer out"+methodArguments+") throws IOException");
          println("{");
          println("if (debug) { logCall(\""+methodName+"\""+logCallParameters+"); } "); // TODO Check performance impact

          if (parseMode == ParseMode.POSTCHUNK) {
            println("lu_count = Integer.toString(words.length-1);");
          }
          //System.err.println("methodName = " + methodName);

          writeMethodBody((Element) getElement(c0, "action"));

          println("}");
        }

        // Error handling
        if (error_UNKNOWN_ATTR) {
          println("ApertiumRE error_UNKNOWN_ATTR = new ApertiumRE(\"error_UNKNOWN_ATTR\");");
        }

        if (error_UNKNOWN_VAR) {
            if (mode == JAVASCRIPT)
                println("var error_UNKNOWN_VAR = \"\";");
            else
                println("String error_UNKNOWN_VAR = \"\";");
        }

        if (error_UNKNOWN_LIST) {
          println("WordList error_UNKNOWN_LIST = new WordList(new String[0]);");
        }

        println("}");

      } catch (FileNotFoundException e) {
          throw new RuntimeException("Error: Cannot open '" + file + "'.");
      }
  }

}
