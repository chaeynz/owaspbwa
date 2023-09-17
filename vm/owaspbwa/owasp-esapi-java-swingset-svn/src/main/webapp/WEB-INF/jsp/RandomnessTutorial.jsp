<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2>Tutorial</h2>
<p>Insecure randomness errors occur when a function that can produce predictable values is used as a source of randomness in security-sensitive context.</p>
<p>Computers are deterministic machines, and as such are unable to produce true randomness. Pseudo-Random Number Generators (PRNGs) approximate randomness algorithmically, starting with a seed from which subsequent values are calculated.<br /></p>
<p>There are two types of PRNGs: statistical and cryptographic. Statistical PRNGs provide useful statistical properties, but their output is highly predictable and forms an easy to reproduce numeric stream that is unsuitable for use in cases where security depends on generated values being unpredictable. Cryptographic PRNGs address this problem by generating output that is more difficult to predict. For a value to be cryptographically secure, it must be impossible or highly improbable for an attacker to distinguish between it and a truly random value. In general, if a PRNG algorithm is not advertised as being cryptographically secure, then it is probably a statistical PRNG and should not be used in security-sensitive contexts.</p>
<p>Examples:<br />
The following code uses a statistical PRNG to create generate a pseudo-random number.<br />
<p class="newsItem">
	<code>
	int GenerateRandomNumber() { <br />
		Random ranGen = new Random(); <br />
		ranGen.setSeed((new Date()).getTime()); <br />
		return (ranGen.nextInt(400000000)); <br />
		} <br />
	</code>
</p>
<p>This code uses the Random.nextInt() function to generate "unique" identifiers for the receipt pages it generates. Because Random.nextInt() is a statistical PRNG, it is easy for an attacker to guess the strings it generates. Although the underlying design of the receipt system is also faulty, it would be more secure if it used a random number generator that did not produce predictable receipt identifiers, such as a cryptographic PRNG.</p>
<br />
<br />
<%@include file="footer.jsp" %>