<%@page import="org.owasp.esapi.swingset.utility.JspGenerator.Chapter"%>
<%@page import="org.owasp.esapi.swingset.utility.JspGenerator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<%=JspGenerator.generateNavigation(Chapter.ENCODING, 7)%>
<div id="header"></div>
<p>
<hr>

<h2 align="center">Tutorial</h2>

<p>Command injection vulnerabilities allow an attacker to inject arbitrary system commands 
into an application. The commands execute at the same privilege level as the Java application 
and provides an attacker with functionality similar to a system shell. In Java, Runtime.exec or 
the ProcessBuilder are often used to invoke a new process, but they do not invoke a new command 
shell, which means that chaining or piping multiple commands together does not usually work. 
Command injection is still possible if the process spawned with Runtime.exec/ProcessBuilder 
is a command shell like command.com, cmd.exe, or /bin/sh. </p>

<p>The code below invokes the system shell in order to execute a non-executable command using 
user input as parameters. Non-executable Window's commands such as dir and copy are part of the 
command interpreter and therefore cannot be directly invoked by Runtime.exec/ProcessBuilder. 
The same applies to script files like batches and shell scripts. In those cases, command injections 
are possible and an attacker could chain multiple commands together. For example, inputting ". &amp; 
rmdir /s /q C:\SomeDir" will cause the dir command to list the contents of the current directory and 
than quietly removes C:\SomeDir and all of its subdirectories and files.</p>

<p class="newsItem">
<code>
ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "dir", args[0]);<br />
Process process = pb.start();
</code>
</p>

<p>To close this last gap ESAPI provides the Executor class which encodes all arguments before 
passing them to the command shell. Alternatively you could use the encodeForOS() method from the encoder 
to encode the arguments on your own and than pass them to Runtime.exec/ProcessBuilder. If you are 
looking for the output of a command shell you should use the Executor because it also handles the 
concurrent output and error stream and provides them as String results.</p>

<p class="newsItem">
<code>
List&lt;String&gt; args = new ArrayList&lt;String&gt;();<br />
args.add("/C");<br />
args.add("dir");<br />
args.add(arg[0]);<br />
<br />
ExecuteResult er = ESAPI.executor().executeSystemCommand(<br />
new File("C:\\Windows\\System32\\cmd.exe"), args);<br />
<br />
String output = er.getOutput();<br />
String errors = er.getErrors();
</code>
</p>

<p>You should avoid invoking the shell using Runtime.exec/ProcessBuilder in order to call operating system 
specific commands and should use Java APIs instead. For example, instead of calling ls or dir from 
the shell use the Java File class and the list function.</p>

<ul>
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/Executor.html#executeSystemCommand(java.io.File, java.util.List)">ExecuteResult executeSystemCommand(java.io.File executable, java.util.List params)</a></b> Invokes the specified executable with default workdir and codec and not logging parameters.</li>
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/Executor.html#executeSystemCommand(java.io.File, java.util.List, java.io.File, org.owasp.esapi.codecs.Codec, boolean, boolean)">ExecuteResult executeSystemCommand(java.io.File executable, java.util.List params, java.io.File workdir, Codec codec, boolean logParams, boolean redirectErrorStream) </a></b> Executes a system command after checking that the executable exists and escaping all the parameters to ensure that injection is impossible.</li>
	<li><b><a href="http://owasp-esapi-java.googlecode.com/svn/trunk_doc/latest/org/owasp/esapi/Encoder.html#encodeForOS(org.owasp.esapi.codecs.Codec, java.lang.String)">java.lang.String 	encodeForOS(Codec codec, java.lang.String input)  </a></b> Encode for an operating system command shell according to the selected codec (appropriate codecs include the WindowsCodec and UnixCodec).</li>
</ul>

<%@include file="footer.jsp" %>
