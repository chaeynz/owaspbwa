package org.owasp.esapi.swingset.models;

import java.util.ArrayList;

import org.owasp.esapi.ESAPI;

/**
 * @author Craig Younkins
 *
 */
public class XSSAttack {
	private String comment;
	private ArrayList<String> examples = new ArrayList<String>();
	
	public XSSAttack(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void addExample(String example) {
		this.examples.add(example);
	}
	
	public ArrayList<String> getExamples() {
		return (ArrayList<String>)examples.clone();
	}
	
//	public String getExampleForJS(int index) {
//		// Replace ' with \' because the target JS context is in single quotes
//		return examples.get(index).replace("'", "\\'");
//	}
	
//	public String getExampleHTMLEncoded(int index) {
//		return ESAPI.encoder().encodeForHTML(examples.get(index));
//	}
	
}
