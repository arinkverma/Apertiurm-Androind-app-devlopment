package org.apertium.transfer.generated;

import java.io.*;
import org.apertium.transfer.*;
public class empty_rule extends GeneratedTransferBase
{
	public boolean isOutputChunked()
	{
		return true;
	}
	ApertiumRE attr_a_nom = new ApertiumRE("<n(?:p><ant><mf|p><cog><mf|><acr><re|p><ant><m|p><ant><f|p><ant|p><top|p><ant|p><cog|p><top|><acr|p><al|p|)>");
	ApertiumRE attr_a_prp = new ApertiumRE("<pr>");
	ApertiumRE attr_a_adv = new ApertiumRE("<(?:pre|)adv>");
	ApertiumRE attr_a_adj = new ApertiumRE("<(?:adj><sint><comp|adj><sint><sup|vblex><ger|adj><comp|adj><sint|vblex><pp|adj><sup|adj)>");
	ApertiumRE attr_a_vrb = new ApertiumRE("<v(?:blex><sep|bhaver|blex|bser|aux)>");
	/**  normala verbo: specifa formo nur por p3/sg/pres  can-verbo: ĉiuj formoj egalaj por ĉiuj personoj  be-verbo: specifaj formoj por p1 kaj p3 / sg / pres kaj past  */
	ApertiumRE attr_a_vrb2 = new ApertiumRE("<(?:vbreg|can|be)>");
	ApertiumRE attr_a_det = new ApertiumRE("<det><(?:def|ind|itg|dem|pos|qnt)>");
	ApertiumRE attr_a_ord = new ApertiumRE("<num><ord>");
	ApertiumRE attr_a_prn = new ApertiumRE("<prn(?:><subj|><itg|><obj|><ref|><tn|)>");
	ApertiumRE attr_a_tns = new ApertiumRE("<(?:past|pres|inf|imp|fti|cni|pp2|pp3|ger|pp)>");
	ApertiumRE attr_a_nepersonaj_tempoj = new ApertiumRE("<(?:past|pres|fti|cni)>");
	ApertiumRE attr_a_gen = new ApertiumRE("<(?:nt|mf|m|f)>");
	ApertiumRE attr_a_prs = new ApertiumRE("<p(?:1|2|3)>");
	ApertiumRE attr_a_nbr = new ApertiumRE("<(?:sg|sp|pl|ND)>");
	ApertiumRE attr_a_cas = new ApertiumRE("<(?:nom|acc)>");
	ApertiumRE attr_lem = new ApertiumRE("(([^<]|\"\\<\")+)");
	ApertiumRE attr_lemq = new ApertiumRE("\\#[- _][^<]+");
	ApertiumRE attr_lemh = new ApertiumRE("(([^<#]|\"\\<\"|\"\\#\")+)");
	ApertiumRE attr_whole = new ApertiumRE("(.+)");
	ApertiumRE attr_tags = new ApertiumRE("((<[^>]+>)+)");
	ApertiumRE attr_chname = new ApertiumRE("(\\{([^/]+)\\/)");
	ApertiumRE attr_chcontent = new ApertiumRE("(\\{.+)");
	ApertiumRE attr_content = new ApertiumRE("(\\{.+)");
	
	String var_number = "";
	String var_genere = "";
	String var_vrb2 = "";
	String var_adv_adjectiu = "";
	String var_superlatiu1 = "";
	String var_it = "";
	String var_caseFirstWord = "";
	String var_EOS = "true";
	WordList list_l_pli_plej = new WordList(new String[] { "pli", "plej", });
	WordList list_prn_kun_persono_kaj_genro = new WordList(new String[] { "prpers", "si", "vi", "mi", "ni", "oni", });
	/**  c="
    Adjektivoj kiuj en la angla estas sintezaj,
    ekz. big, bigger, biggest.
    kaj ne big, more big, most big.
    Tiuj estas markitaj en en.dix per la marko (sint)" */
	WordList list_adj_kun_sint = new WordList(new String[] { "able", "angry", "apt", "avid", "bad", "bare", "barren", "big", "blind", "blond", "blunt", "bold", "brave", "bright", "brittle", "broad", "bulky", "busy", "calm", "cheap", "clean", "clear", "clever", "close", "coarse", "cold", "cool", "costly", "crazy", "crude", "dark", "deaf", "dear", "deep", "dense", "dirty", "dry", "dumb", "early", "earthly", "earthy", "easy", "empty", "fair", "false", "fancy", "far", "fast", "fat", "feeble", "fine", "firm", "fit", "flaky", "flat", "fleshy", "floppy", "fond", "free", "fresh", "friendly", "full", "fun", "funky", "funny", "fuzzy", "gentle", "glassy", "good", "grave", "gray", "greasy", "great", "green", "grey", "grim", "gross", "guilty", "hairy", "handy", "happy", "hard", "harsh", "hasty", "hazy", "healthy", "heavy", "high", "holy", "hot", "humble", "hungry", "ill", "impure", "inert", "just", "keen", "kind", "large", "late", "lazy", "lean", "lengthy", "lewd", "light", "lonely", "long", "loud", "lovely", "low", "lucky", "mean", "merry", "mild", "mock", "muddy", "naïve", "narrow", "nasty", "naughty", "near", "neat", "needy", "net", "new", "nice", "noble", "noisy", "north", "nude", "odd", "old", "peaty", "pink", "plain", "poor", "pretty", "prickly", "prompt", "proud", "pure", "purple", "quick", "quiet", "rare", "raw", "rich", "risky", "rough", "round", "safe", "salty", "sandy", "sane", "scaly", "shady", "shallow", "sharp", "shiny", "shoddy", "short", "showy", "shrewd", "shy", "sick", "silly", "simple", "sincere", "slim", "slow", "small", "smart", "smooth", "sober", "soft", "solid", "sore", "sorry", "sour", "south", "spicy", "spiky", "starry", "steep", "sticky", "still", "stormy", "strange", "stray", "streaky", "strong", "subtle", "sunny", "sure", "sweet", "tall", "tender", "thick", "thin", "tight", "tiny", "tough", "tricky", "true", "ugly", "unhappy", "unkind", "unlucky", "unworthy", "vast", "warm", "wary", "weak", "wealthy", "weird", "white", "wide", "wild", "windy", "wise", "woody", "wry", "yellow", "young", });
	/**   c="
    Lingvoj, por ne aldoni la artikolon antaŭ ili
    en la angla"  */
	WordList list_lingvoj = new WordList(new String[] { "angla", "germana", "dana", "norvega", "sveda", "islanda", "feroa", "nederlanda", "portugala", "hispana", "kataluna", "okcitana", "franca", "arpitana", "itala", "rumana", "rusa", "belorusa", "ukraina", "pola", "ĉeĥa", "slovaka", "slovena", "serbokroata", "serba", "kroata", "bosna", "makedona", "bulgara", "albana", "armena", "greka", "persa", "taĝika", "kurda", "paŝtua", "litova", "latva", "bretona", "kimra", "irlanda", "gaela", "hindia", "bengala", "urdua", "nepala", "eŭska", "finna", "hungara", "estona", "turka", "azerbajĝana", "uzbeka", "kazaĥa", "kirgiza", "ujgura", "araba", "malta", "ĉina", "tajvana", "japana", "korea", "vjetnama", "telugua", "tamila", "indonezia", "malaja", "tagala", "filipina", "svahila", "volofa", "fula", "joruba", "igba", "mandinka", "lingala", "konga", "ksosa", "zulua", });
	/**        <list-item v="devi"/>  */
	WordList list_nepersonaj_verboj = new WordList(new String[] { "pluvi", "neĝi", "ebli", "indi", "endi", });
	
	/**  Por rigardi, ĉu la blanko havas aŭ ne havas formaton.
     Tiu makroo estas necesa en la reguloj, en kiuj malaperas vorto por decidi,
     ĉu la blanko de la vorto devas esti forigita aŭ konservita.
     Se ĝi havas formaton, necesas konservi ĝin; se ne, indas viŝi,
     por ke ne aperu du sinsekvaj blankoj.

     Prenita de "f_bcond" apertium-en-es/apertium-en-es.es-en.t1x  */
	private void macro_sendu_blankon_se_havas_formaton2(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("macro_sendu_blankon_se_havas_formaton2",  word1, blank1,  word2); } 
		if (!blank1.equals(" "))
		{
			out.append(blank1);
		}
	}
	
	/** 
            <chunk name="sendu_blankon_se_havas_formaton">
              <tags><tag><lit-tag v="xx"/></tag></tags>
              <lu>
                <b pos="1"/>
              </lu>
            </chunk>
^dark<adj><sint>$
^dark<adj><sint><comp>$
^dark<adj><sint><sup>$

^malluma<adj><sg><nom>$
^pli<preadv>$ ^malluma<adj><sg><nom>$
^plej<preadv>$ ^malluma<adj><sg><nom>$

 Ĉi tiu makroo akceptas adv kaj adj kiel respektive unuan kaj duan parametrojn kaj analizas, ĉu temas
pri konstruo el la tipo 'pli granda', kiu estas sinteza en la angla  */
	private void macro_adv_adj(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("macro_adv_adj",  word1, blank1,  word2); } 
		/**   c="Ĉu estas pli aŭ plej? Ekz: 'tre bona'"  */
		if (list_l_pli_plej.containsIgnoreCase(word1.sl(attr_lemh)))
		{
			/**   c="Ĉu estas sinteza adjektivo? Ekz pli bona → better(sint)(comp)"  */
			if (list_adj_kun_sint.containsIgnoreCase(word2.tl(attr_lemh)))
			{
				macro_sendu_blankon_se_havas_formaton2(out, word1, blank1, word2);
				/**   c="Ĉu pli aŭ plej? Metu (comp) aŭ (sup) en variablo"  */
				if (word1.sl(attr_lem).equalsIgnoreCase("pli"))
				{
					var_adv_adjectiu = (""+word2.tl(attr_lem)+"<adj><sint><comp>");
				}
				else
				{
					var_adv_adjectiu = (""+word2.tl(attr_lem)+"<adj><sint><sup>");
				}
			}
			else
			{
				var_adv_adjectiu = (""+word1.tl(attr_whole)+"$"+blank1+"^"+word2.tl(attr_lemh)+word2.tl(attr_a_adj)+word2.tl(attr_lemq));
			}
		}
		else
		{
			var_adv_adjectiu = (""+word1.tl(attr_whole)+"$"+blank1+"^"+word2.tl(attr_lemh)+word2.tl(attr_a_adj)+word2.tl(attr_lemq));
		}
	}
	
	/**  rigardas, ĉu temas pri nepersona verbo kaj preparas "it" por aldono  */
	private void macro_nepersona(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("macro_nepersona",  word1); } 
		if ((list_nepersonaj_verboj.containsIgnoreCase(word1.sl(attr_lemh))
    && !word1.sl(attr_a_nepersonaj_tempoj).equals("")))
		{
			/**     <lit v="^pron"/><lit-tag v="SN.p3.nt.sg.nom.nepersona"/><lit v="{^prpers"/><lit-tag v="prn.subj.2.3.4"/><lit v="$}$ "/> */
			var_it = (""+"^pron"+"<SN><p3><nt><sg><nom>"+"{^prpers"+"<prn><subj><2><3><4>"+"$}$ ");
		}
		else
		{
			var_it = "";
		}
	}
	
	/**  konvertas imperativon al infinitivo  */
	private void macro_imp_inf(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("macro_imp_inf",  word1); } 
		if (word1.tl(attr_a_tns).equals("<imp>"))
		{
			word1.tlSet(attr_a_tns, "<inf>");
		}
	}
	
	/**  difinas la tipon vrb2 de la verbo   */
	private void macro_set_vrb2(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("macro_set_vrb2",  word1); } 
		if (word1.tl(attr_lemh).equals("be"))
		{
			var_vrb2 = "<be>";
		}
		else
		if (word1.tl(attr_lemh).equals("can"))
		{
			var_vrb2 = "<can>";
		}
		else
		{
			var_vrb2 = "<vbreg>";
		}
	}
	
	private void macro_firstWord(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("macro_firstWord",  word1); } 
		// WARNING: Attribute a_np_acr is not defined. Valid attributes are: [a_nom, a_prp, a_adv, a_adj, a_vrb, a_vrb2, a_det, a_ord, a_prn, a_tns, a_nepersonaj_tempoj, a_gen, a_prs, a_nbr, a_cas, lem, lemq, lemh, whole, tags, chname, chcontent, content]
// Replacing with error_UNKNOWN_ATTR - for <transfer default="chunk">/<section-def-macros>/<def-macro n="firstWord" npar="1">/<choose>/<when>/<test>/<equal>/<clip part="a_np_acr" pos="1" side="sl">
		if (word1.sl(error_UNKNOWN_ATTR).equals(""))
		{
			if (var_EOS.equals("true"))
			{
				word1.tlSet(attr_lem, TransferWord.copycase("aa", word1.tl(attr_lem)));
				var_caseFirstWord = "Aa";
			}
			else
			{
				var_caseFirstWord = "aa";
			}
		}
		else
		{
			var_caseFirstWord = "aa";
		}
		var_EOS = "false";
	}
	
	/** 
  <rule>
     <pattern>
       <pattern-item n="c_ĉu"/>
     </pattern>
     <action>
       <out>
         <chunk name="ĉu" case="caseFirstWord">
           <tags>
             <tag><lit-tag v="adv"/></tag>
             <tag><lit-tag v="itg"/></tag>
           </tags>
           <lu>
              <b />
           </lu>
         </chunk>
       </out>
     </action>
  </rule>
 DATOJ k.s.  */
	// REGULO: la 2a de julio - July 2nd
	public void rule0__la__num_ord__de__monato(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule0__la__num_ord__de__monato",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "dato"));
		out.append("<SN>");
		out.append(word4.tl(attr_a_nbr));
		out.append(word4.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_nom)
			         +(word4.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(" ");
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_ord)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DE TEMPO (de/ekde januaro -> from January -- ŝanĝo de prepozicio
	public void rule1__de_ekde__tempo(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule1__de_ekde__tempo",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^from<pr>$}$");
		out.append(blank1);
		out.append("^tempo<SN>");
		out.append(word2.tl(attr_a_nbr));
		out.append(word2.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_nom)
			         +(word2.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DE DET TEMPO (de/ekde tiu januaro -> from this January -- ŝanĝo de prepozicio
	public void rule2__de_ekde__c_det__tempo(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule2__de_ekde__c_det__tempo",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^from<pr>$}$");
		out.append(blank1);
		out.append("^tempo<SD>");
		/**  Sintagmo determinita  */
		out.append(word3.tl(attr_a_nbr));
		out.append(word3.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_det)
			         +(word2.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_nom)
			         +(word3.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	/**  ne eblas "antaŭ": estas amaso da "en la aĝo de tri jaroj"; bedaŭrinde, ĉar "de tri jaroj li studas medicinon"  */
	// REGULO: DE ANTAŬ NUM TEMPO (de/ekde antaŭ tri tagoj -> since three days ago - ŝanĝo de prepozicio + ago
	public void rule3__de_ekde__anta___num_sp__tempo2(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule3__de_ekde__anta___num_sp__tempo2",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		/**  ne eblas "antaŭ": estas amaso da "en la aĝo de tri jaroj"; bedaŭrinde, ĉar "de tri jaroj li studas medicinon"  */
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^since<pr>$}$");
		out.append(blank1);
		out.append("^tempo<SD>");
		/**  Sintagmo determinita  */
		out.append(word4.tl(attr_a_nbr));
		out.append(word4.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word3.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_nom)
			         +(word4.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(" ^ago<adv>$}$");
	}
	
	// REGULO: DE ANTAŬ QNT TEMPO (de/ekde antaŭ kelkaj tagoj -> since some days ago - ŝanĝo de prepozicio + ago
	public void rule4__de_ekde__anta___qnt__tempo2(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule4__de_ekde__anta___qnt__tempo2",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^since<pr>$}$");
		out.append(blank1);
		out.append("^tempo<SN>");
		out.append(word4.tl(attr_a_nbr));
		out.append(word4.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_det)
			         +(word3.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_nom)
			         +(word4.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(" ^ago<adv>$}$");
	}
	
	// REGULO: ANTAŬ NUM TEMPO (antaŭ tri tagoj -> three days ago - sen prepozicio + ago
	public void rule5__anta___num_sp__tempo2(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule5__anta___num_sp__tempo2",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		out.append("^tempo<SD>");
		/**  Sintagmo determinita  */
		out.append(word3.tl(attr_a_nbr));
		out.append(word3.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_nom)
			         +(word3.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(" ^ago<adv>$}$");
	}
	
	// REGULO: ANTAŬ QNT TEMPO (antaŭ kelkaj tagoj -> some days ago - sen prepozicio + ago
	public void rule6__anta___qnt__tempo2(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule6__anta___qnt__tempo2",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		out.append("^tempo<SN>");
		out.append(word3.tl(attr_a_nbr));
		out.append(word3.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_det)
			         +(word2.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_nom)
			         +(word3.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(" ^ago<adv>$}$");
	}
	
	/**  SN  */
	// REGULO: DET
	public void rule7__c_det(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule7__c_det",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word1.tl(attr_a_nbr));
		out.append(word1.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	/** 
  <rule comment="REGULO: PRN">
     <pattern>
       <pattern-item n="c_prn"/>
     </pattern>
     <action>
        <call-macro n="firstWord">
          <with-param pos="1"/>
        </call-macro>
       <out>
         <chunk name="prn" case="caseFirstWord">
           <tags>
             <tag><lit-tag v="SN"/></tag>
             <tag><clip pos="1" side="tl" part="a_nbr"/></tag>
             <tag><clip pos="1" side="tl" part="a_cas"/></tag>
           </tags>
           <lu>
             <clip pos="1" side="tl" part="lemh"/>
             <clip pos="1" side="tl" part="a_det"/>
             <clip pos="1" side="sl" part="a_nbr" link-to="2"/>
             <clip pos="1" side="tl" part="lemq"/>
           </lu>
         </chunk>
       </out>
     </action>
  </rule>
 */
	// REGULO: PRN
	public void rule8__c_prn(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule8__c_prn",  word1); } 
		macro_firstWord(out, word1);
		if (list_prn_kun_persono_kaj_genro.containsIgnoreCase(word1.sl(attr_lem)))
		{
			/**  persona pronomo: estas persono kaj genro  */
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "pron"));
			out.append("<SN>");
			out.append(word1.tl(attr_a_prs));
			out.append(word1.tl(attr_a_gen));
			out.append(word1.tl(attr_a_nbr));
			out.append(word1.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word1.tl(attr_lemh)
				         +word1.tl(attr_a_prn)
				         +(word1.tl(attr_a_prs).isEmpty()?"" : "<2>")
				         +(word1.tl(attr_a_gen).isEmpty()?"" : "<3>")
				         +(word1.tl(attr_a_nbr).isEmpty()?"" : "<4>")
				         +word1.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			/**  montra, rilativa k.a. pronomoj: estas nek persono nek genro  */
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "pron"));
			out.append("<SN>");
			out.append(word1.tl(attr_a_nbr));
			out.append(word1.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word1.tl(attr_lemh)
				         +word1.tl(attr_a_prn)
				         +(word1.tl(attr_a_nbr).isEmpty()?"" : "<2>")
				         +word1.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: PREP PRN (por li -> for him)
	public void rule9__pr__c_prn(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule9__pr__c_prn",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		if (list_prn_kun_persono_kaj_genro.containsIgnoreCase(word2.sl(attr_lem)))
		{
			if (word2.tl(attr_a_prn).equalsIgnoreCase("<prn><subj>"))
			{
				word2.tlSet(attr_a_prn, "<prn><obj>");
			}
			/**  persona pronomo: estas persono kaj genro  */
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
			out.append("<PREP>{");
			{
				String myword = 
				         word1.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
			out.append(blank1);
			out.append("^pron<SN>");
			out.append(word2.tl(attr_a_prs));
			out.append(word2.tl(attr_a_gen));
			out.append(word2.tl(attr_a_nbr));
			out.append(word2.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_prn)
				         +(word2.tl(attr_a_prs).isEmpty()?"" : "<2>")
				         +(word2.tl(attr_a_gen).isEmpty()?"" : "<3>")
				         +(word2.tl(attr_a_nbr).isEmpty()?"" : "<4>")
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			/**  montra, rilativa k.a. pronomoj: estas nek persono nek genro  */
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
			out.append("<PREP>{");
			{
				String myword = 
				         word1.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
			out.append(blank1);
			out.append("^pron<SN>");
			out.append(word2.tl(attr_a_nbr));
			out.append(word2.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_prn)
				         +(word2.tl(attr_a_nbr).isEmpty()?"" : "<2>")
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: ADV
	public void rule10__c_adv(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule10__c_adv",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "adv"));
		out.append("<SADV>");
		out.append(word1.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_adv)
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: ADJ
	public void rule11__c_adj(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule11__c_adj",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "adj"));
		out.append("<SA>");
		/**  c="sintagmo adjektiva"  */
		out.append(word1.tl(attr_a_nbr));
		out.append(word1.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_adj)
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: PLI/PLEJ ADJ
	public void rule12__c_pli_plej__c_adj(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule12__c_pli_plej__c_adj",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_adv_adj(out, word1, blank1, word2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "sint?_adj"));
		out.append("<SA>");
		/**   c="sintagmo adjektiva"  */
		out.append(word2.tl(attr_a_nbr));
		out.append('{');
		{
			String myword = 
			         var_adv_adjectiu
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: NOM
	public void rule13__c_nom(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule13__c_nom",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "nom"));
		out.append("<SN>");
		out.append(word1.tl(attr_a_nbr));
		out.append(word1.tl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_nom)
			         +(word1.tl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET NOM
	public void rule14__c_det__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule14__c_det__c_nom",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word2.tl(attr_a_nbr));
		out.append(word2.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_nom)
			         +(word2.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: NUM NOM
	public void rule15__num__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule15__num__c_nom",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word2.tl(attr_a_nbr));
		out.append(word2.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_nom)
			         +(word2.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET NUM NOM
	public void rule16__c_det__num__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule16__c_det__num__c_nom",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word3.tl(attr_a_nbr));
		out.append(word3.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_nom)
			         +(word3.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET NUM ADJ NOM
	public void rule17__c_det__num__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule17__c_det__num__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word4.tl(attr_a_nbr));
		out.append(word4.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_adj)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_nom)
			         +(word4.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET NUM ADJ ADJ NOM
	public void rule18__c_det__num__c_adj_pp__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4, String blank4, TransferWord word5) throws IOException
	{
		if (debug) { logCall("rule18__c_det__num__c_adj_pp__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3, blank3,  word4, blank4,  word5); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word5.tl(attr_a_nbr));
		out.append(word5.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_adj)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_adj)
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank4);
		{
			String myword = 
			         word5.tl(attr_lemh)
			         +word5.tl(attr_a_nom)
			         +(word5.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word5.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET ADV ADJ NOM
	public void rule19__c_det__c_adv__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule19__c_det__c_adv__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_adv_adj(out, word2, blank2, word3);
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word4.tl(attr_a_nbr));
		out.append(word4.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         var_adv_adjectiu
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_nom)
			         +(word4.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET NUM ADV ADJ NOM
	public void rule20__c_det__num__c_adv__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4, String blank4, TransferWord word5) throws IOException
	{
		if (debug) { logCall("rule20__c_det__num__c_adv__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3, blank3,  word4, blank4,  word5); } 
		macro_adv_adj(out, word3, blank3, word4);
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word5.tl(attr_a_nbr));
		out.append(word5.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         var_adv_adjectiu
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank4);
		{
			String myword = 
			         word5.tl(attr_lemh)
			         +word5.tl(attr_a_nom)
			         +(word5.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word5.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET ADJ
	public void rule21__c_det__c_adj_pp(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule21__c_det__c_adj_pp",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		if (list_lingvoj.containsIgnoreCase(word2.sl(attr_lemh)))
		{
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "adj"));
			out.append("<SD>");
			/**  Sintagmo determinita  */
			out.append(word2.tl(attr_a_nbr));
			out.append(word2.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_adj)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "det_adj"));
			out.append("<SD>");
			/**  Sintagmo determinita  */
			out.append(word2.tl(attr_a_nbr));
			out.append(word2.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word1.tl(attr_lemh)
				         +word1.tl(attr_a_det)
				         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
				         +word1.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank1);
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_adj)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: DET ADV ADJ
	public void rule22__c_det__c_adv__c_adj_pp(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule22__c_det__c_adv__c_adj_pp",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_adv_adj(out, word2, blank2, word3);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_adj"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word3.tl(attr_a_nbr));
		out.append(word3.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         var_adv_adjectiu
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: ADJ NOM
	public void rule23__c_adj__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule23__c_adj__c_nom",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "adj_nom"));
		out.append("<SN>");
		out.append(word2.tl(attr_a_nbr));
		out.append(word2.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_adj)
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_nom)
			         +(word2.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET ADJ NOM
	public void rule24__c_det__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule24__c_det__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_adj_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word3.tl(attr_a_nbr));
		out.append(word3.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_adj)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_nom)
			         +(word3.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET ADJ ADJ NOM
	public void rule25__c_det__c_adj_pp__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule25__c_det__c_adj_pp__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_adj_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word4.tl(attr_a_nbr));
		out.append(word4.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_adj)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_adj)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_nom)
			         +(word4.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DET ADJ ADJ ADJ NOM
	public void rule26__c_det__c_adj_pp__c_adj_pp__c_adj_pp__c_nom(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4, String blank4, TransferWord word5) throws IOException
	{
		if (debug) { logCall("rule26__c_det__c_adj_pp__c_adj_pp__c_adj_pp__c_nom",  word1, blank1,  word2, blank2,  word3, blank3,  word4, blank4,  word5); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "det_adj_nom"));
		out.append("<SD>");
		/**  Sintagmo determinita  */
		out.append(word5.tl(attr_a_nbr));
		out.append(word5.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_lemh)
			         +word1.tl(attr_a_det)
			         +(word1.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word1.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_adj)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_adj)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_adj)
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank4);
		{
			String myword = 
			         word5.tl(attr_lemh)
			         +word5.tl(attr_a_nom)
			         +(word5.sl(attr_a_nbr).isEmpty()?"" : "<2>")
			         +word5.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	/**  SV  */
	// REGULO: VERBO-os
	public void rule27__c_vb_fti(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule27__c_vb_fti",  word1); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vb_fti"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append(word1.tl(attr_a_prs));
		out.append(word1.tl(attr_a_nbr));
		out.append("{^will<vaux><inf>$ ^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word1.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: VERBO-us
	public void rule28__c_vb_cni(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule28__c_vb_cni",  word1); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		if (word1.sl(attr_lemh).equals("devi"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vb_cni"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word1.tl(attr_a_tns));
			out.append(word1.tl(attr_a_prs));
			out.append(word1.tl(attr_a_nbr));
			out.append("{^");
			out.append(word1.tl(attr_lemh));
			out.append(word1.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word1.tl(attr_lemq));
			out.append("$}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vb_cni"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word1.tl(attr_a_tns));
			out.append(word1.tl(attr_a_prs));
			out.append(word1.tl(attr_a_nbr));
			out.append("{^would<vaux><inf>$ ^");
			out.append(word1.tl(attr_lemh));
			out.append(word1.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word1.tl(attr_lemq));
			out.append("$}$");
		}
	}
	
	// REGULO: NE VERBO-os
	public void rule29__c_ne__c_vb_fti(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule29__c_ne__c_vb_fti",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vb_fti"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word2.tl(attr_a_tns));
		out.append(word2.tl(attr_a_prs));
		out.append(word2.tl(attr_a_nbr));
		out.append("{^will<vaux><inf>$ ^not<adv>$");
		out.append(blank1);
		out.append('^');
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word2.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: NE VERBO-us
	public void rule30__c_ne__c_vb_cni(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule30__c_ne__c_vb_cni",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		if (word1.sl(attr_lemh).equals("devi"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vb_cni"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append(word2.tl(attr_a_prs));
			out.append(word2.tl(attr_a_nbr));
			out.append("{^");
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word2.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			out.append("^not<adv>$}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vb_cni"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append(word2.tl(attr_a_prs));
			out.append(word2.tl(attr_a_nbr));
			out.append("{^would<vaux><inf>$ ^not<adv>$");
			out.append(blank1);
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word2.tl(attr_lemq));
			out.append("$}$");
		}
	}
	
	// REGULO: VERBO EN ONT-FORMO
	public void rule31__c_vb_pp2(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule31__c_vb_pp2",  word1); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vb_pp2"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append(word1.tl(attr_a_prs));
		out.append(word1.tl(attr_a_nbr));
		out.append("{^would<vaux><inf>$ ^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word1.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: VERBO EN INT-FORMO
	public void rule32__c_vb_pp3(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule32__c_vb_pp3",  word1); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vb_pp3"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append(word1.tl(attr_a_prs));
		out.append(word1.tl(attr_a_nbr));
		out.append("{^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<pp>");
		out.append(word1.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: ESTOS -INTA
	public void rule33__c_vbser_fti__c_vb_pp3(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule33__c_vbser_fti__c_vb_pp3",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbfti_vbpp3"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("{^will<vaux><inf>$ ^have<vbhaver><inf>$ ^");
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<pp>");
		out.append(word2.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: ESTUS -INTA
	public void rule34__c_vbser_cni__c_vb_pp3(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule34__c_vbser_cni__c_vb_pp3",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbcni_vbpp3"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("{^would<vaux><inf>$ ^have<vbhaver><inf>$ ^");
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<pp>");
		out.append(word2.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: ESTIsimpla-INTA
	public void rule35__c_vbser_simpla__c_vb_pp3(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule35__c_vbser_simpla__c_vb_pp3",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbser1"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD><ND>{");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("^have<vbhaver>");
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>$");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append(" ^");
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<pp>");
		out.append(word2.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: ESTIsimpla PP
	public void rule36__c_vbser_simpla__c_vb_pp(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule36__c_vbser_simpla__c_vb_pp",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbser2"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD><ND>{");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_vrb)
			         +word2.tl(attr_a_tns)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: ESTIsimpla PP de (estas dirita de -> is told by)
	public void rule37__c_vbser_simpla__c_vb_pp__de(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule37__c_vbser_simpla__c_vb_pp__de",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbser3"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD><ND>{");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_vrb)
			         +word2.tl(attr_a_tns)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
		out.append(blank2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^by<pr>$}$");
	}
	
	// REGULO: ESTIsimpla PP DE PRN (estas dirita de li -> is told by him)
	public void rule38__c_vbser_simpla__c_vb_pp__de__c_prn(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule38__c_vbser_simpla__c_vb_pp__de__c_prn",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbser4"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD><ND>{");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_vrb)
			         +word2.tl(attr_a_tns)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
		out.append(blank2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^by<pr>$}$");
		out.append(blank3);
		if (list_prn_kun_persono_kaj_genro.containsIgnoreCase(word4.sl(attr_lem)))
		{
			if (word4.tl(attr_a_prn).equalsIgnoreCase("<prn><subj>"))
			{
				word4.tlSet(attr_a_prn, "<prn><obj>");
			}
			/**  persona pronomo: estas persono kaj genro  */
			out.append("^pron<SN>");
			out.append(word4.tl(attr_a_prs));
			out.append(word4.tl(attr_a_gen));
			out.append(word4.tl(attr_a_nbr));
			out.append(word4.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word4.tl(attr_lemh)
				         +word4.tl(attr_a_prn)
				         +(word4.tl(attr_a_prs).isEmpty()?"" : "<2>")
				         +(word4.tl(attr_a_gen).isEmpty()?"" : "<3>")
				         +(word4.tl(attr_a_nbr).isEmpty()?"" : "<4>")
				         +word4.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			/**  montra, rilativa k.a. pronomoj: estas nek persono nek genro  */
			out.append("^pron<SN>");
			out.append(word4.tl(attr_a_nbr));
			out.append(word4.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word4.tl(attr_lemh)
				         +word4.tl(attr_a_prn)
				         +(word4.tl(attr_a_nbr).isEmpty()?"" : "<2>")
				         +word4.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: VBSER
	public void rule39__c_vbser(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule39__c_vbser",  word1); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbser5"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD>");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("<ND>{");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lem));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>$}$");
	}
	
	/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
	// REGULO: NE VBSER
	public void rule40__c_ne__c_vbser(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule40__c_ne__c_vbser",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word2);
		macro_set_vrb2(out, word2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vbser6"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word2.tl(attr_a_tns));
		out.append("<PD>");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("<ND>{");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append('^');
		out.append(word2.tl(attr_lem));
		out.append(word2.tl(attr_a_vrb));
		out.append(word2.tl(attr_a_tns));
		out.append("<4><5>$");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append(blank1);
		out.append("^not<adv>$}$");
	}
	
	// REGULO: VBLEX
	public void rule41__c_vblex(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule41__c_vblex",  word1); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD>");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("<ND>{");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word1.tl(attr_lemq));
		out.append("$}$");
	}
	
	// REGULO: NE VBLEX
	public void rule42__c_ne__c_vblex(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule42__c_ne__c_vblex",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word2);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		if (word2.tl(attr_a_tns).equals("<ger>"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append("^without<pr>$");
			out.append(blank1);
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_vrb)
				         +word2.tl(attr_a_tns)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		/**  tl povas esti "vbser" eĉ se sl povas esti "vblex": ekz. "eblas -> is possible;
ERARO: en tiu kazo "not" devus aperi inter lemh kaj lemq
 */
		else
		if ((word2.tl(attr_a_vrb).equalsIgnoreCase("<vaux>")
    || word2.tl(attr_a_vrb).equalsIgnoreCase("<vbser>")))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append(word2.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word2.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			out.append("^not<adv>$}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("^do");
			out.append(word2.tl(attr_a_vrb));
			out.append(word2.tl(attr_a_tns));
			out.append("<4><5>$");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append(" ^not<adv>$");
			out.append(blank1);
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word2.tl(attr_lemq));
			out.append("$}$");
		}
	}
	
	// REGULO: VBLEXsimpla INF
	public void rule43__c_vblex_simpla__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule43__c_vblex_simpla__c_vb_inf",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		if (word1.tl(attr_a_vrb).equalsIgnoreCase("<vaux>"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word1.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word1.tl(attr_lemh));
			out.append(word1.tl(attr_a_vrb));
			out.append(word1.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word1.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_vrb)
				         +word2.tl(attr_a_tns)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word1.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word1.tl(attr_lemh));
			out.append(word1.tl(attr_a_vrb));
			out.append(word1.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word1.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			out.append("^to<pr>$ ");
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_vrb)
				         +word2.tl(attr_a_tns)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: NE VBLEXsimpla INF
	public void rule44__c_ne__c_vblex_simpla__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule44__c_ne__c_vblex_simpla__c_vb_inf",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word2);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		if (word2.tl(attr_a_tns).equals("<ger>"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append("^without<pr>$");
			out.append(blank1);
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_vrb)
				         +word2.tl(attr_a_tns)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank2);
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_vrb)
				         +word3.tl(attr_a_tns)
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		/**  tl povas esti "vbser" eĉ se sl povas esti "vblex": ekz. "eblas -> is possible;
ERARO: en tiu kazo "not" devus aperi inter lemh kaj lemq
 */
		else
		if ((word2.tl(attr_a_vrb).equalsIgnoreCase("<vaux>")
    || word2.tl(attr_a_vrb).equalsIgnoreCase("<vbser>")))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append(word2.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word2.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			out.append("^not<adv>$");
			out.append(blank2);
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_vrb)
				         +word3.tl(attr_a_tns)
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("^do");
			out.append(word2.tl(attr_a_vrb));
			out.append(word2.tl(attr_a_tns));
			out.append("<4><5>$");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append(" ^not<adv>$");
			out.append(blank1);
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word2.tl(attr_lemq));
			out.append('$');
			out.append(blank2);
			out.append("^to<pr>$ ");
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_vrb)
				         +word3.tl(attr_a_tns)
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: VBLEXsimpla ADV INF
	public void rule45__c_vblex_simpla__c_adv__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule45__c_vblex_simpla__c_adv__c_vb_inf",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		if (word1.tl(attr_a_vrb).equalsIgnoreCase("<vaux>"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word1.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word1.tl(attr_lemh));
			out.append(word1.tl(attr_a_vrb));
			out.append(word1.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word1.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			{
				String myword = 
				         word2.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank2);
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_vrb)
				         +word3.tl(attr_a_tns)
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word1.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word1.tl(attr_lemh));
			out.append(word1.tl(attr_a_vrb));
			out.append(word1.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word1.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			out.append("^to<pr>$ ");
			{
				String myword = 
				         word2.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank2);
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_vrb)
				         +word3.tl(attr_a_tns)
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: NE VBLEXsimpla ADV INF
	public void rule46__c_ne__c_vblex_simpla__c_adv__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule46__c_ne__c_vblex_simpla__c_adv__c_vb_inf",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word2);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		if (word2.tl(attr_a_tns).equals("<ger>"))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append("^without<pr>$");
			out.append(blank1);
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_vrb)
				         +word2.tl(attr_a_tns)
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank2);
			{
				String myword = 
				         word3.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank3);
			{
				String myword = 
				         word4.tl(attr_lemh)
				         +word4.tl(attr_a_vrb)
				         +word4.tl(attr_a_tns)
				         +word4.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		/**  tl povas esti "vbser" eĉ se sl povas esti "vblex": ekz. "eblas -> is possible;
ERARO: en tiu kazo "not" devus aperi inter lemh kaj lemq
 */
		else
		if ((word2.tl(attr_a_vrb).equalsIgnoreCase("<vaux>")
    || word2.tl(attr_a_vrb).equalsIgnoreCase("<vbser>")))
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append(word2.tl(attr_a_tns));
			out.append("<4><5>");
			out.append(word2.tl(attr_lemq));
			out.append('$');
			out.append(blank1);
			out.append("^not<adv>$");
			out.append(blank2);
			{
				String myword = 
				         word3.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank3);
			{
				String myword = 
				         word4.tl(attr_lemh)
				         +word4.tl(attr_a_vrb)
				         +word4.tl(attr_a_tns)
				         +word4.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			out.append(var_it);
			out.append('^');
			out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
			out.append("<SV>");
			out.append(var_vrb2);
			out.append(word2.tl(attr_a_tns));
			out.append("<PD>");
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("<ND>{");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
			out.append("^do");
			out.append(word2.tl(attr_a_vrb));
			out.append(word2.tl(attr_a_tns));
			out.append("<4><5>$");
			/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
			out.append(" ^not<adv>$");
			out.append(blank1);
			out.append('^');
			out.append(word2.tl(attr_lemh));
			out.append(word2.tl(attr_a_vrb));
			out.append("<inf>");
			out.append(word2.tl(attr_lemq));
			out.append('$');
			out.append(blank2);
			out.append("^to<pr>$ ");
			{
				String myword = 
				         word3.tl(attr_whole)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append(blank3);
			{
				String myword = 
				         word4.tl(attr_lemh)
				         +word4.tl(attr_a_vrb)
				         +word4.tl(attr_a_tns)
				         +word4.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: VBLEX-os INF
	public void rule47__c_vb_fti__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule47__c_vb_fti__c_vb_inf",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("{^will<vaux><inf>$ ^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_vrb)
			         +word2.tl(attr_a_tns)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: VBLEX-us INF
	public void rule48__c_vb_cni__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule48__c_vb_cni__c_vb_inf",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("{^would<vaux><inf>$ ^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word2.tl(attr_lemh)
			         +word2.tl(attr_a_vrb)
			         +word2.tl(attr_a_tns)
			         +word2.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: NE VBLEX-os INF
	public void rule49__c_ne__c_vb_fti__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule49__c_ne__c_vb_fti__c_vb_inf",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word2.tl(attr_a_tns));
		out.append("{^will<vaux><inf>$ ^not<adv>$");
		out.append(blank1);
		out.append('^');
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word2.tl(attr_lemq));
		out.append('$');
		out.append(blank2);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_vrb)
			         +word3.tl(attr_a_tns)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: NE VBLEX-us INF
	public void rule50__c_ne__c_vb_cni__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule50__c_ne__c_vb_cni__c_vb_inf",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word2.tl(attr_a_tns));
		out.append("{^would<vaux><inf>$ ^not<adv>$");
		out.append(blank1);
		out.append('^');
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word2.tl(attr_lemq));
		out.append('$');
		out.append(blank2);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_vrb)
			         +word3.tl(attr_a_tns)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: VBLEX-os ADV INF
	public void rule51__c_vb_fti__c_adv__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule51__c_vb_fti__c_adv__c_vb_inf",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("{^will<vaux><inf>$ ^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_vrb)
			         +word3.tl(attr_a_tns)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: VBLEX-us ADV INF
	public void rule52__c_vb_cni__c_adv__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule52__c_vb_cni__c_adv__c_vb_inf",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word1);
		macro_nepersona(out, word1);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("{^would<vaux><inf>$ ^");
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word1.tl(attr_lemq));
		out.append('$');
		out.append(blank1);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word2.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank2);
		{
			String myword = 
			         word3.tl(attr_lemh)
			         +word3.tl(attr_a_vrb)
			         +word3.tl(attr_a_tns)
			         +word3.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: NE VBLEX-os ADV INF
	public void rule53__c_ne__c_vb_fti__c_adv__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule53__c_ne__c_vb_fti__c_adv__c_vb_inf",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word2.tl(attr_a_tns));
		out.append("{^will<vaux><inf>$ ^not<adv>$");
		out.append(blank1);
		out.append('^');
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word2.tl(attr_lemq));
		out.append('$');
		out.append(blank2);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word3.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_vrb)
			         +word4.tl(attr_a_tns)
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: NE VBLEX-us ADV INF
	public void rule54__c_ne__c_vb_cni__c_adv__c_vb_inf(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3, String blank3, TransferWord word4) throws IOException
	{
		if (debug) { logCall("rule54__c_ne__c_vb_cni__c_adv__c_vb_inf",  word1, blank1,  word2, blank2,  word3, blank3,  word4); } 
		macro_firstWord(out, word1);
		macro_set_vrb2(out, word2);
		macro_nepersona(out, word2);
		out.append(var_it);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex_inf"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word2.tl(attr_a_tns));
		out.append("{^would<vaux><inf>$ ^not<adv>$");
		out.append(blank1);
		out.append('^');
		out.append(word2.tl(attr_lemh));
		out.append(word2.tl(attr_a_vrb));
		out.append("<inf>");
		out.append(word2.tl(attr_lemq));
		out.append('$');
		out.append(blank2);
		out.append("^to<pr>$ ");
		{
			String myword = 
			         word3.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append(blank3);
		{
			String myword = 
			         word4.tl(attr_lemh)
			         +word4.tl(attr_a_vrb)
			         +word4.tl(attr_a_tns)
			         +word4.tl(attr_lemq)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	/**  ALIAJ REGULOJ  */
	// REGULO: PP de (dirita de -> told by)
	public void rule55__c_vb_pp__de(Writer out, TransferWord word1, String blank1, TransferWord word2) throws IOException
	{
		if (debug) { logCall("rule55__c_vb_pp__de",  word1, blank1,  word2); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD>");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("<ND>{");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word1.tl(attr_lemq));
		out.append("$}$");
		out.append(blank1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^by<pr>$}$");
	}
	
	// REGULO: PP DE PRN (dirita de li -> told by him)
	public void rule56__c_vb_pp__de__c_prn(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule56__c_vb_pp__de__c_prn",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word1);
		macro_set_vrb2(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word1.tl(attr_a_tns));
		out.append("<PD>");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("<ND>{");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word1.tl(attr_lemh));
		out.append(word1.tl(attr_a_vrb));
		out.append(word1.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word1.tl(attr_lemq));
		out.append("$}$");
		out.append(blank2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^by<pr>$}$");
		out.append(blank1);
		if (list_prn_kun_persono_kaj_genro.containsIgnoreCase(word3.sl(attr_lem)))
		{
			if (word3.tl(attr_a_prn).equalsIgnoreCase("<prn><subj>"))
			{
				word3.tlSet(attr_a_prn, "<prn><obj>");
			}
			/**  persona pronomo: estas persono kaj genro  */
			out.append("^pron<SN>");
			out.append(word3.tl(attr_a_prs));
			out.append(word3.tl(attr_a_gen));
			out.append(word3.tl(attr_a_nbr));
			out.append(word3.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_prn)
				         +(word3.tl(attr_a_prs).isEmpty()?"" : "<2>")
				         +(word3.tl(attr_a_gen).isEmpty()?"" : "<3>")
				         +(word3.tl(attr_a_nbr).isEmpty()?"" : "<4>")
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			/**  montra, rilativa k.a. pronomoj: estas nek persono nek genro  */
			out.append("^pron<SN>");
			out.append(word3.tl(attr_a_nbr));
			out.append(word3.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word3.tl(attr_lemh)
				         +word3.tl(attr_a_prn)
				         +(word3.tl(attr_a_nbr).isEmpty()?"" : "<2>")
				         +word3.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: DE PRN PP (de li dirita -> told by him)
	public void rule57__de__c_prpers__c_vb_pp(Writer out, TransferWord word1, String blank1, TransferWord word2, String blank2, TransferWord word3) throws IOException
	{
		if (debug) { logCall("rule57__de__c_prpers__c_vb_pp",  word1, blank1,  word2, blank2,  word3); } 
		macro_firstWord(out, word1);
		macro_imp_inf(out, word3);
		macro_set_vrb2(out, word3);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "vblex"));
		out.append("<SV>");
		out.append(var_vrb2);
		out.append(word3.tl(attr_a_tns));
		out.append("<PD>");
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		out.append("<ND>{");
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de pers en t2x  */
		/**  ni aldonas la spacon por eventuala aldono de nbr en t2x  */
		out.append('^');
		out.append(word3.tl(attr_lemh));
		out.append(word3.tl(attr_a_vrb));
		out.append(word3.tl(attr_a_tns));
		out.append("<4><5>");
		out.append(word3.tl(attr_lemq));
		out.append("$}$");
		out.append(blank2);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{^by<pr>$}$");
		out.append(blank1);
		if (list_prn_kun_persono_kaj_genro.containsIgnoreCase(word2.sl(attr_lem)))
		{
			if (word2.tl(attr_a_prn).equalsIgnoreCase("<prn><subj>"))
			{
				word2.tlSet(attr_a_prn, "<prn><obj>");
			}
			/**  persona pronomo: estas persono kaj genro  */
			out.append("^pron<SN>");
			out.append(word2.tl(attr_a_prs));
			out.append(word2.tl(attr_a_gen));
			out.append(word2.tl(attr_a_nbr));
			out.append(word2.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_prn)
				         +(word2.tl(attr_a_prs).isEmpty()?"" : "<2>")
				         +(word2.tl(attr_a_gen).isEmpty()?"" : "<3>")
				         +(word2.tl(attr_a_nbr).isEmpty()?"" : "<4>")
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
		else
		{
			/**  montra, rilativa k.a. pronomoj: estas nek persono nek genro  */
			out.append("^pron<SN>");
			out.append(word2.tl(attr_a_nbr));
			out.append(word2.sl(attr_a_cas));
			out.append('{');
			{
				String myword = 
				         word2.tl(attr_lemh)
				         +word2.tl(attr_a_prn)
				         +(word2.tl(attr_a_nbr).isEmpty()?"" : "<2>")
				         +word2.tl(attr_lemq)
				         ;
				if (myword.length()>0)
				{
					out.append('^');
					out.append(myword);
					out.append('$');
				}
			}
			out.append("}$");
		}
	}
	
	// REGULO: NUM
	public void rule58__num(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule58__num",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "num"));
		out.append("<SD><nom>{");
		/**  Sintagmo determinita  */
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: DE
	public void rule59__de(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule59__de",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<DE>{");
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: PREP
	public void rule60__pr(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule60__pr",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "pr"));
		out.append("<PREP>{");
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: REL
	public void rule61__rel(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule61__rel",  word1); } 
		macro_firstWord(out, word1);
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "rel"));
		out.append("<REL>");
		out.append(word1.tl(attr_a_nbr));
		out.append(word1.sl(attr_a_cas));
		out.append('{');
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGULO: KOMO
	public void rule62__komo(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule62__komo",  word1); } 
		out.append('^');
		out.append(TransferWord.copycase(var_caseFirstWord, "komo"));
		out.append("<KOMO>{");
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	
	// REGLA: reset variables
	public void rule63__sent(Writer out, TransferWord word1) throws IOException
	{
		if (debug) { logCall("rule63__sent",  word1); } 
		if (!(word1.sl(attr_lem).equals(";")
    || word1.sl(attr_lem).equals(":")))
		{
			var_EOS = "true";
		}
		out.append("^punt<sent>{");
		{
			String myword = 
			         word1.tl(attr_whole)
			         ;
			if (myword.length()>0)
			{
				out.append('^');
				out.append(myword);
				out.append('$');
			}
		}
		out.append("}$");
	}
	ApertiumRE error_UNKNOWN_ATTR = new ApertiumRE("error_UNKNOWN_ATTR");
}
