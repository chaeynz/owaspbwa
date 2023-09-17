package org.owasp.esapi.swingset.models;

import java.util.ArrayList;

import org.owasp.esapi.swingset.models.*;

/**
 * @author Craig Younkins
 *
 */
public class XSSData {
	private static ArrayList<XSSRule> allRules = new ArrayList<XSSRule>();
	
	static {
		XSSRule rule;
		XSSContext context;
		XSSAttack attack;
		
			rule = new XSSRule("RULE #0 - Never Insert Untrusted Data Except in Allowed Locations", 
				"http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.230_-_Never_Insert_Untrusted_Data_Except_in_Allowed_Locations");
				
				rule.setShowSecure(false);
				context = new XSSContext("Only put untrusted data in the five approved locations! Not into a script:");
				context.setContextCode("0");
				context.setVulnerableForm("<span>data<script>var i=??;</script></span>");
					attack = new XSSAttack("Use the current context");
					attack.addExample("50;alert('xss0a')");
					context.addAttack(attack);
				rule.addContext(context);
			allRules.add(rule);
			
			rule = new XSSRule("RULE #1 - HTML Escape Before Inserting Untrusted Data into HTML Element Content", 
				"http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.231_-_HTML_Escape_Before_Inserting_Untrusted_Data_into_HTML_Element_Content");
				
				rule.setEncoderText("HTML entity encoding excluding space");
				rule.setEncoder("encodeForHTML");
				context = new XSSContext("Normal Element Content, common attacks are:");
				context.setContextCode("1");
				context.setVulnerableForm("<span>??</span>");
					attack = new XSSAttack("Inject down into script context by introducing a new element");
					attack.addExample("<script>alert('xss1a')</script>");
					attack.addExample("<img src=1 onerror=alert('xss1b') />");
					context.addAttack(attack);
				rule.addContext(context);
			allRules.add(rule);
			
			rule = new XSSRule("RULE #2 - Attribute Escape Before Inserting Untrusted Data into HTML Common Attributes",
				"http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.232_-_Attribute_Escape_Before_Inserting_Untrusted_Data_into_HTML_Common_Attributes");
		
				rule.setEncoderText("HTML entity encoding including space");
				rule.setEncoder("encodeForHTMLAttribute");
				context = new XSSContext("Unquoted Attribute, common attacks are:");
				context.setContextCode("21");
				context.setVulnerableForm("<span name=??>test</span>");
					attack = new XSSAttack("Inject up to another attribute using whitespace characters (ASCII 9, 10, 11, 12, 13, 32)");
					attack.addExample("dummy onmouseover=alert('xss2.1a')");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with >");
					attack.addExample("dummy><script>alert('xss2.1b')</script>");
					context.addAttack(attack);
				rule.addContext(context);
				
				context = new XSSContext("Quoted Attribute, common attacks are:");
				context.setContextCode("22");
				context.setVulnerableForm("<span name=\"??\">test</span>");
					attack = new XSSAttack("Inject up to another attribute with \" or ' depending on what quotes were used");
					attack.addExample("dummy\" onmouseover=\"alert('xss2.2a')");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with \">");
					attack.addExample("\"><script>alert('xss2.2b')</script><span name=\"");
					context.addAttack(attack);
				rule.addContext(context);
			allRules.add(rule);
			
			rule = new XSSRule("RULE #3 - JavaScript Escape Before Inserting Untrusted Data into HTML JavaScript Data Values",
			"http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.233_-_JavaScript_Escape_Before_Inserting_Untrusted_Data_into_HTML_JavaScript_Data_Values");
	
				rule.setEncoderText("Javascript escaping");
				rule.setEncoder("encodeForJavaScript");
				context = new XSSContext("Unquoted value, common attacks are:\nNote that JavaScript escaping can be \\A (ascii) or \\xHH (hex) or \\OOO (octal)");
				context.setContextCode("31");
				context.setVulnerableForm("<img src=\"style/images/owasp-logo_130x55.png\" onload=\"var i=??;\"/>");
					attack = new XSSAttack("Inject up to another attribute with ; | and many others");
					attack.addExample("50; alert('xss3.1a')");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject down with a JavaScript expression");
					attack.addExample("50 + alert('xss3.1b')");
					context.addAttack(attack);
				rule.addContext(context);
				
				context = new XSSContext("Quoted value, common attacks are:\nNote that JavaScript escaping can be \\A (ascii) or \\xHH (hex) or \\OOO (octal)");
				context.setContextCode("32");
				context.setVulnerableForm("<img src=\"style/images/owasp-logo_130x55.png\" onload=\"var i='??'\"/>");
					attack = new XSSAttack("Inject up to the JavaScript context with \" or ' depending on what quotes were used");
					attack.addExample("dummy'; alert('xss3.2a'); var j='");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with \">");
					attack.addExample("dummy'\"><script>alert(\"xss3.2b\")</script>\"");
					context.addAttack(attack);
				rule.addContext(context);
			allRules.add(rule);
			
			rule = new XSSRule("RULE #4 - CSS Escape Before Inserting Untrusted Data into HTML Style Property Values",
			"http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.234_-_CSS_Escape_Before_Inserting_Untrusted_Data_into_HTML_Style_Property_Values");
				
				rule.setEncoderText("CSS escaping");
				rule.setEncoder("encodeForCSS");
				context = new XSSContext("Unquoted Style Attribute, common attacks are:");
				context.setContextCode("41");
				context.setVulnerableForm("<span style=??>test</span>");
					attack = new XSSAttack("Inject up to another attribute using whitespace characters (ASCII 9, 10, 11, 12, 13, 32)");
					attack.addExample("dummy onmouseover=alert('xss4.1a')");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with >");
					attack.addExample("dummy><script>alert('xss4.1b')</script>");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject down using xss:expression");
					//attack.addExample("xss:expression(alert('xss4.1c'))");
					//attack.addExample("xss:url(javascript:alert('xss4.1d'))");
					context.addAttack(attack);
				rule.addContext(context);
				
				context = new XSSContext("Quoted Style Attribute, common attacks are:");
				context.setContextCode("42");
				context.setVulnerableForm("<span style=\"??\">test</span>");
					attack = new XSSAttack("Inject up to another attribute with \" or ' depending on what quotes were used");
					attack.addExample("dummy\" onmouseover=\"alert('xss4.2a')\"");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with \">");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject down using xss:expression");
					//attack.addExample("xss:expression(alert('xss4.2b'))");
					//attack.addExample("xss:url(javascript:alert('xss4.2c'))");
					context.addAttack(attack);
					
				rule.addContext(context);
			allRules.add(rule);
			
			rule = new XSSRule("RULE #5 - URL Escape Before Inserting Untrusted Data into HTML URL Attributes",
			"http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.235_-_URL_Escape_Before_Inserting_Untrusted_Data_into_HTML_URL_Attributes");
	
				rule.setEncoderText("URL escaping");
				rule.setEncoder("encodeForURL");
				context = new XSSContext("Unquoted URL attribute, common attacks are:");
				context.setContextCode("51");
				context.setVulnerableForm("<a href=??>test</a>");
					attack = new XSSAttack("Inject up to another attribute using whitespace characters (ASCII 9, 10, 11, 12, 13, 32)");
					attack.addExample("dummy onmouseover=alert('xss5.1a')");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with >");
					attack.addExample("dummy><script>alert('xss5.1b')</script>");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject down with javascript: type URLs");
					context.addAttack(attack);
				rule.addContext(context);
				
				context = new XSSContext("Quoted URL Attribute, common attacks are:");
				context.setContextCode("52");
				context.setVulnerableForm("<a href=\"??\">test</a>");
					attack = new XSSAttack("Inject up to another attribute with \" or ' depending on what quotes were used");
					attack.addExample("dummy\" onmouseover=\"alert('xss5.2a')\"");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject up to the containing HTML element with \">");
					attack.addExample("dummy\"><script>alert('xss5.2b')</script><a href=\"");
					context.addAttack(attack);
					
					attack = new XSSAttack("Inject down with javascript: type URLs");
					context.addAttack(attack);
					
				rule.addContext(context);
			allRules.add(rule);
	}
	
	public static ArrayList<XSSRule> getRulesForSecure() {
		ArrayList<XSSRule> ret = new ArrayList<XSSRule>();
		
		for (XSSRule rule: allRules) {
			if(rule.isShowSecure()) {
				ret.add(rule);
			}
		}
		return ret;
	}
	
	public static ArrayList<XSSRule> getRulesForInsecure() {
		ArrayList<XSSRule> ret = new ArrayList<XSSRule>();
		
		for (XSSRule rule: allRules) {
			if(rule.isShowInsecure()) {
				ret.add(rule);
			}
		}
		return ret;
	}
}
