Apertium on your mobile
=========
Port Apertium to Android and possibly iPhone. Apertium has a Java port which doesn't currently work on mobile telephones. Lots of people have mobile telephones, and some of them would like to have a translator there.


Features [version 2.3]
=========
<b>Desktop Widget</b>
![Alt widget](http://1.bp.blogspot.com/-4fbKyCbz0j8/T9PI4Dzta4I/AAAAAAAAAtc/RPZEOZYm_LA/s1600/device-2012-06-10-031043.png "Desktop Widget")
<ul>
<li>User can place at maximum of five translation mode shortcut for quick translation.</li>
<li>Easy to config widget from setting panel of application</li>
<li>At present only support single instance.</li>
<li><b>Future work</b>, to create multiple instance of widget</li>
</ul>


<b>Caching support<b>
![Alt cache](http://3.bp.blogspot.com/-TQSJ5wlu5Xg/T9POYjybQEI/AAAAAAAAAtw/dDTTj9jlMtY/s1600/device-2012-06-10-030505.png "Cache in setting")
	<ul>
<li>The cache feature developed by <a href="http://wiki.apertium.org/wiki/User:Mikel/GSoC_2012_Application">Mikel</a> on lttoolbox, has been added to application</li>
<li>This make subseticent translation instaneously fast</li>
<li>Can easliy be enable/disable from setting panel</li>
</ul>

New setting panel<br />
In earlier version there was absence of standard setting panel

Technical approach
========

For basic feature

<ol>
<li>Language pair
<ul>
<li>For language pair is I have used a zip-compress file which include Install.json in root. Install.xml contains information about installation and language pairs.
<br/>
Structure of <b>Install.json</b>
<pre>
{"pair": {
	"id":"eo-en",
	"version" : "2.0.13",
	"modes": {
		"modeitem" : [
			{"id" : "eo-en", "title" : "Esperanto-English"},
			{"id" : "en-eo", "title" : "English-Esperanto"}
		]
	}			
}}
</pre>
<a href="https://github.com/arinkverma/Apertiurm-Androind-app-devlopment/blob/2.2.2/LanguagePairs/android-eo-en.zip?raw=true" >Download dummy Language pair package (en-eo)</a> <br/>
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
<li>http://www.arinkverma.in</li>
</ol>
