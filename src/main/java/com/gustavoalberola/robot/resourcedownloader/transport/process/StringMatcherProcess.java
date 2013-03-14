package com.gustavoalberola.robot.resourcedownloader.transport.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.FindMode;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;

@XmlRootElement(name = "string-matcher")
public class StringMatcherProcess extends Process {

	static final private Log logger = LogFactory.getLog(StringMatcherProcess.class);
	
	private String input;
	
	private String search;
	
	private int searchGroup = 0;
	
	private String contextName;
	
	private FindMode findMode = FindMode.FIRST;
	
	private boolean replacePayload = false;
	
	private boolean replaceContextVarsInput = false;
	
	@XmlAttribute(required = false)
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	@XmlAttribute
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@XmlAttribute(required = false)
	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	@XmlAttribute(name = "find-name", required = false)
	public FindMode getFindMode() {
		return findMode;
	}

	public void setFindMode(FindMode findMode) {
		this.findMode = findMode;
	}

	@XmlAttribute(name = "replace-payload", required = false)
	public boolean isReplacePayload() {
		return replacePayload;
	}

	public void setReplacePayload(boolean replacePayload) {
		this.replacePayload = replacePayload;
	}
	
	@XmlAttribute(name = "search-group", required = false)
	public int getSearchGroup() {
		return searchGroup;
	}

	public void setSearchGroup(int searchGroup) {
		this.searchGroup = searchGroup;
	}

	@XmlAttribute(name = "replace-context-vars-input", required = false)
	public boolean isReplaceContextVarsInput() {
		return replaceContextVarsInput;
	}

	public void setReplaceContextVarsInput(boolean replaceContextVarsInput) {
		this.replaceContextVarsInput = replaceContextVarsInput;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		Pattern pattern = null;
		
		try {
			pattern = Pattern.compile(search);
		} catch (PatternSyntaxException e) {
			throw new ProcessException("The search expression provided it is not valid", e);			 
		}
		
		String input = (this.input == null) ? payload.getValue() : this.input;
		
		if (replaceContextVarsInput)
			input = getGeneralContext().replaceExpressionsInString(input, payload);
		
		Matcher matcher = null;
		
		while ((matcher = pattern.matcher(input)).find()) {
			String result = null;
			if (searchGroup > 0 && matcher.groupCount() <= searchGroup) {
				result = matcher.group(searchGroup);
			} else {
				result = matcher.group();
			}
			
			logger.info(String.format("Match for the pattern %s in group %d. The result is: %s", search, searchGroup, result));
			
			// If exists a contextName the result must be stored in the General Context
			if (contextName != null) {
				getGeneralContext().setValue(contextName, result);
			}
			
			if (isNextProcessToCall()) {
				// If the payload must be replaced, send the result as the new payload
				if (replacePayload) {
					getNextProcessInChain().execute(new Payload(result));
				} else { // Else use the payload of the input as the output
					getNextProcessInChain().execute(payload);
				}
			}
			
			if (FindMode.FIRST.equals(findMode)) {
				break;
			} else {
				input = matcher.replaceFirst(""); // Eliminate the previous match so it does not get stuck in an endless loop
			}
		}
	}

}
