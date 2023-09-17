<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<h2 align="center">Tutorial</h2>
<p>A Globally Unique Identifier or GUID is a special type of identifier used in software applications in order to provide a reference number which is unique in any context (hence, "Globally"), for example, in defining the internal reference for a type of access point in a software application, or for creating unique keys in a database. While each generated GUID is not guaranteed to be unique, the total number of unique keys (2128 or 3.4×1038) is so large that the probability of the same number being generated twice is very small.</p> 
<h4>Generating random GUID using ESAPI</h4>
<p>Following method in the ESAPI's Randomizer interface generates a random GUID.</p>
<p><b>public String getRandomGUID()</b></p>
<p>This method could use a hash of random Strings, the current time, and any other random data available. The format is a well-defined sequence of 32 hex digits grouped into chunks of 8-4-4-4-12.</p> 

<%@include file="footer.jsp" %>
