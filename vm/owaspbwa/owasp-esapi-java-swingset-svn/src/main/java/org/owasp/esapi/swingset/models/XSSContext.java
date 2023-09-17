package org.owasp.esapi.swingset.models;

import java.util.ArrayList;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.swingset.models.XSSAttack;

/**
 * @author Craig Younkins
 *
 */
public class XSSContext {
	private String contextCode = "";
	private String comment = "";
	private String vulnerableForm = "";
	private String input = "";
	private ArrayList<XSSAttack> attacks = new ArrayList<XSSAttack>();
	
	public XSSContext(String comment) {
		this.comment = comment;
	}
	
	public void addAttack(XSSAttack attack) {
		attacks.add(attack);
	}
	
	public ArrayList<XSSAttack> getAttacks() {
		return (ArrayList<XSSAttack>)attacks.clone();
	}
	
	public String getFormPart(int part) {
		String[] parts = vulnerableForm.split("\\?\\?");
		return parts[part];
	}
	
/*	public String getFormPartHTMLEncoded(int partnum) {
		String part = getFormPart(partnum);
		return ESAPI.encoder().encodeForHTML(part);
	}
	
	public String getInputHTMLEncoded() {
		return ESAPI.encoder().encodeForHTML(input);
	}
*/
	
	public String getInputEncodedForHTMLAttribute() {
		return ESAPI.encoder().encodeForHTMLAttribute(input);
	}
	
	// GETTERS AND SETTERS
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getInput() {
		return input;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setContextCode(String versionCode) {
		this.contextCode = versionCode;
	}

	public String getContextCode() {
		return contextCode;
	}
	
	public void setVulnerableForm(String vulnerableForm) {
		this.vulnerableForm = vulnerableForm;
	}
	
	public String getVulnerableForm() {
		return vulnerableForm;
	}
	
}
