package org.owasp.esapi.swingset.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 
 * 
 * @author Christopher Popp
 */
public class JspGenerator {

	public static enum Chapter {
		ENCODING
	}
	
	private static class NaviEntry {
		private String link;
		
		private String name;

		public NaviEntry(String link, String name) {
			this.link = link;
			this.name = name;
		}

		public String getLink() {
			return link;
		}

		public String getName() {
			return name;
		}
	}
	
	private static final Map<Chapter, List<NaviEntry>> naviElements = Collections.unmodifiableMap(
			new HashMap<Chapter, List<NaviEntry>>() {
		private static final long serialVersionUID = 1L;
		{
			put(Chapter.ENCODING, Collections.unmodifiableList(new ArrayList<NaviEntry>(){
				private static final long serialVersionUID = 1L;
				{
					add(new NaviEntry("Encoding", "Tutorial"));
					add(new NaviEntry("Encoding&lab", "Lab : Encoding"));
					add(new NaviEntry("Encoding&solution", "Solution"));
					add(new NaviEntry("XSS&lab", "Lab : XSS"));
					add(new NaviEntry("XSS&solution", "Solution"));
					add(new NaviEntry("Canonicalize&lab", "Lab : Canonicalize"));
					add(new NaviEntry("Canonicalize&solution", "Solution"));
					add(new NaviEntry("CommandInjection", "Tutorial"));
					add(new NaviEntry("CommandInjection&lab", "Lab : Command Injection"));
					add(new NaviEntry("CommandInjection&solution", "Solution"));
					add(new NaviEntry("SqlInjection", "Tutorial"));
					add(new NaviEntry("SqlInjection&lab", "Lab : SQL Injection"));
					add(new NaviEntry("SqlInjection&solution", "Solution"));
				}
			}));
		}
	});
		
	public static String generateNavigation(Chapter chapter, int current) {
		synchronized (naviElements) {
			StringBuffer sb = new StringBuffer("<div id=\"navigation\"><a href=\"main\">Home</a> ");
			List<NaviEntry> naviEntries = naviElements.get(Chapter.ENCODING);
			for (int i=0; i < naviEntries.size(); i++) {
				NaviEntry ne = naviEntries.get(i);
				sb.append("| ");
				if (i == current) {
					sb.append("<b>");
				}
				sb.append("<a href=\"main?function=" + ne.getLink() + "\">" + ne.getName() + "</a>");
				if (i == current) {
					sb.append("</b>");
				}
			}
			sb.append("</div>");
			return sb.toString();
		}
	}
}
