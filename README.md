Apertium on your mobile
=========
Port Apertium to Android and possibly iPhone. Apertium has a Java port which doesn't currently work on mobile telephones. Lots of people have mobile telephones, and some of them would like to have a translator there.


Features [version 2.2.1]
=========
Share intent
Installation of language pair from local directory
Language list view


Technical approach
========

For basic feature

<ol>
<li>Language pair
<ul>
<li>For language pair is I have used a zip-compress file which include Install.xml in root. Install.xml contains information about installation and language pairs.
<br/>
Structure of <b>Install.xml</b>
<pre>
&lt;install>
&lt;mode12>a-b&lt;/mode12>
&lt;mode21>b-a&lt;/mode21>
&lt;lang12>LanguageA-LanguageB&lt;/lang12>
&lt;lang21>LanguageB-Languagea&lt;/lang21>
&lt;/install>
</pre>
<a href="https://github.com/downloads/arinkverma/Apertiurm-Androind-app-devlopment/eo-en.zip" >Download dummy Language pair package (en-eo)</a> <br/>
[Note : It is en-eo language translation]

</li>
<li>Jar file can be also used to serve same purpose without extraction
</li><li>SVN to fetch content from remote server</li>
</ul>
</li>
<li>Share intent

Android share intent features inorder to connect with other installed application in device.
</li>
</ol>

Links
=====
<ol>
<li>http://apertium.org</li>
<li>http://wiki.apertium.org/wiki/User:Arinkverma</li>
</ol>
