package com.gustavoalberola.robot.resourcedownloader.transport.process;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.FindMode;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;

@XmlRootElement(name = "http-filter")
public class HttpFilterProcess extends Process {
	
	static final private Log logger = LogFactory.getLog(HttpFilterProcess.class);
	
	private String input;
	private String tag;
	private String clazz;
	private String name;
	private String id;
	private String value;
	private FindMode findMode;
	
	@XmlTransient
	private Payload payload;
	
	public HttpFilterProcess() {}
	
	@XmlAttribute(required = false)
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	@XmlAttribute(name = "node-value", required = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute(required = true)
	public String getTag() {
		return tag;
	}
		
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@XmlAttribute(name = "class", required = false)
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@XmlAttribute(required = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(required = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "find-mode", required = false)
	public FindMode getFindMode() {
		return findMode;
	}

	public void setFindMode(FindMode findMode) {
		this.findMode = findMode;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		this.payload = payload;
		
		// Assing default values
		if (findMode == null)
			findMode = FindMode.FIRST;

		String input = (this.input == null || this.input.isEmpty()) ? payload.getValue() : this.input; 
		
		InputSource inputSource = new InputSource(new StringReader(input));
		
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
		} catch (Exception e) {
			throw new ProcessException("Cannot parse the HTML document", e);
		}
		
		Document doc = parser.getDocument();
		// Filter the nodes by tag name
		NodeList l1 = doc.getElementsByTagName(tag);
		
		// Iterate through the Elements that match the tag
		for (int a = 0, aMax = l1.getLength() ; a < aMax ; a++) {
			Node n1 = l1.item(a);
			if (n1 instanceof Element) {
				Element e1 = (Element) n1;
				// Check if the node match the requirements specified by the class
				if (matchRequirements(e1)) {
					
					logger.info(String.format("Finded match for filter Tag:%s - name:%s - class:%s - id:%s - value:%s", tag, name, clazz, id, value));
					if (isNextProcessToCall()) {						
						Payload newPayload = new Payload(nodeToString(n1));
						getNextProcessInChain().execute(newPayload);
					}
					
					if (FindMode.FIRST.equals(findMode))
						break; // Exit the for, only the first match is processed
				}
			}
		}
		
	}
	
	private static String nodeToString(Node node) throws ProcessException {
		
		StringWriter sw = new StringWriter();
		
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException e) {
			throw new ProcessException("Cannot parse the Node into HTML again...", e);		  
		}
		
		return sw.toString();
	  }
	
	private boolean matchRequirements(Element e) {
		
		if (!isMatchingAttribute(e, "name", name)) return false;
		if (!isMatchingAttribute(e, "class", clazz)) return false;
		if (!isMatchingAttribute(e, "id", id)) return false;
		if (!isMatchingNodeValue(e, value)) return false;
		
		return true;
	}
	
	private boolean isMatchingNodeValue(Element e, String match) {
		// The match is the attribute loaded in the class. NULL means skip this check
		if (match == null)
			return true;
		
		// If it has a child, that means that has a value
		if (!e.hasChildNodes())
			return false;
		
		Node child = e.getFirstChild();
		String tmp = child.getNodeValue();
		
		// And finally check if the match is inside the attribute
		if (tmp.indexOf(getGeneralContext().replaceExpressionsInString(match, payload)) < 0)
			return false;
		
		return true;
	}
	
	private boolean isMatchingAttribute(Element e, String attributeName, String match) {
		// The match is the attribute loaded in the class. NULL means skip this check
		if (match == null)
			return true;
		
		// If it has a match, it needs to have the attribute or else it wont calify
		if (!e.hasAttribute(attributeName))
			return false;
		
		String tmp = e.getAttribute(attributeName);
		
		// And finally check if the match is inside the attribute
		if (tmp.indexOf(getGeneralContext().replaceExpressionsInString(match, payload)) < 0)
			return false;
		
		return true;
	}
}
