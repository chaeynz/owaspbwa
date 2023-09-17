package org.owasp.esapi.swingset.models;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.swingset.models.XSSContext;

/**
 * @author Craig Younkins
 *
 */
public class XSSRule {
	private String ruleTitle = "";
	private String ruleURL = "";
	private ArrayList<XSSContext> contexts = new ArrayList<XSSContext>();
	private boolean showInsecure = true;
	private boolean showSecure = true;
	private String encoder = "";
	private String encoderText = "";
	
	public XSSRule(String title, String url) {
		this.setRuleTitle(title);
		this.setRuleURL(url);
	}
	
	public ArrayList<XSSContext> getContexts() {
		return (ArrayList<XSSContext>)contexts.clone();
	}
	
	public void addContext(XSSContext newContext) {
		contexts.add(newContext);
	}

	public void setRuleTitle(String ruleTitle) {
		this.ruleTitle = ruleTitle;
	}

	public String getRuleTitle() {
		return ruleTitle;
	}

	public void setRuleURL(String ruleURL) {
		this.ruleURL = ruleURL;
	}

	public String getRuleURL() {
		return ruleURL;
	}
	
	public void setShowSecure(boolean show) {
		showSecure = show;
	}
	
	public void setShowInsecure(boolean show) {
		showInsecure = show;
	}
	
	public boolean isShowSecure() {
		return showSecure;
	}
	
	public boolean isShowInsecure() {
		return showInsecure;
	}
	
	public void setEncoder(String newEncoder) {
		encoder = newEncoder;
	}
	
	public String encodeForRule(String input) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Encoder instance = ESAPI.encoder();
		Class[] signature = new Class[1];
		signature[0] = String.class;
		Method meth = instance.getClass().getMethod(encoder, signature);
		return (String)meth.invoke(instance, input);
	}

	public void setEncoderText(String encoderText) {
		this.encoderText = encoderText;
	}

	public String getEncoderText() {
		return encoderText;
	}
}
