package com.gustavoalberola.robot.resourcedownloader.util.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.exception.EmptyGeneralContextVariableNameException;
import com.gustavoalberola.robot.resourcedownloader.exception.NoSuchGeneralContextVariableException;
import com.gustavoalberola.robot.resourcedownloader.exception.TryingToUseReservedNameInGeneralContextException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.util.GeneralContext;

/**
 * Implementation of the context. Uses a hashMap to store variables, so no more than one value for a Key.
 * <p>
 * <b>Note</b>: payload is a reserved word (it cannot be used as key)
 * 
 * @author gustavoalberola
 */
public class GeneralContextImpl implements GeneralContext {

	static private final Log logger = LogFactory.getLog(GeneralContextImpl.class);
	static private final String RESERVER_WORD_PAYLOAD = "payload";	
	static private final String[] RESERVED_WORDS = new String[]{RESERVER_WORD_PAYLOAD};
	static private final Pattern SPECIAL_CHARACTERS_PATTERN = Pattern.compile("\\{([^\\}]+)\\}");
	static private final Pattern CHECK_NUMBER_MODIFICATOR_PATTERN = Pattern.compile("([\\d]+)#(.+)");
	
	private Map<String, Object> contextVars;
	
	public GeneralContextImpl() {
		contextVars = new HashMap<String, Object>();
	}

	public void setValue(String key, Object value) throws EmptyGeneralContextVariableNameException, TryingToUseReservedNameInGeneralContextException {
		checkIfKeyIsEmpty(key);
		key = key.toLowerCase();
		checkIfKeyIsReservedWord(key);
		
		contextVars.put(key, value);
		logger.info(String.format("Added/Updated Context var \"%s\":\"%s\"", key, value));	
	}

	public Object getValue(String key) throws EmptyGeneralContextVariableNameException, TryingToUseReservedNameInGeneralContextException {
		checkIfKeyIsEmpty(key);
		key = key.toLowerCase();
		checkIfKeyIsReservedWord(key);		
		
		return contextVars.get(key);
	}
	
	private void checkIfKeyIsEmpty(String key) throws EmptyGeneralContextVariableNameException {
		if (key == null || key.isEmpty())
			throw new EmptyGeneralContextVariableNameException();
	}
	
	private void checkIfKeyIsReservedWord(String key) throws TryingToUseReservedNameInGeneralContextException {
		for (String rWord : RESERVED_WORDS) {
			if (rWord.equalsIgnoreCase(key)) 
				throw new TryingToUseReservedNameInGeneralContextException(rWord);
		}		
	}
	
	private boolean isReservedWord(String word) {
		for (String rWord : RESERVED_WORDS) {
			if (rWord.equalsIgnoreCase(word))
				return true;
		}
		return false;
	}

	public String replaceExpressionsInString(String inputString, Payload payload) throws NoSuchGeneralContextVariableException {
				
		Matcher m = null;
		Matcher m2 = null;
		String numberModificator = null;
		Object value = null;
		String varName = null;
		String newInputString = new String(inputString);
		
		while ((m = SPECIAL_CHARACTERS_PATTERN.matcher(newInputString)).find()) {
						
			if (m.groupCount() > 0) {

				varName = m.group(1).toLowerCase();
				
				// We add the special functionality in the form of {4#chapter} this will replace the value with a %04d
				// The result is if that chapter has less numbers than 4 it will add 0s to the left. Ex. chapter 12 value 0012
				if ((m2 = CHECK_NUMBER_MODIFICATOR_PATTERN.matcher(varName)).find()) {
					numberModificator = m2.group(1);
					varName = m2.group(2);
				} else {
					numberModificator = null;
				}
				
				// Check if it is a reserved word and the value must be obtained from somewhere else than contextVars
				if (isReservedWord(varName)) {
					if (RESERVER_WORD_PAYLOAD.equalsIgnoreCase(varName)) {
						newInputString = m.replaceFirst(replaceOutputWithNumberModificator(payload.getValue(), numberModificator));
						continue;
					}
				}
				
				if (!contextVars.containsKey(varName)) {
					throw new NoSuchGeneralContextVariableException(varName);
				}
				
				// If not, it is a regular context var, so we simply use contextVars
				value = contextVars.get(varName);								
				newInputString = m.replaceFirst(replaceOutputWithNumberModificator(value.toString(), numberModificator));
			}
		}
		
		return newInputString;
	}
	
	// If there was a match for a number modificator, apply the String.format()
	private String replaceOutputWithNumberModificator(String output, String numberModificator) {
		if (numberModificator == null)
			return output;
					
		return String.format("%0"+numberModificator+"d", Integer.valueOf(output)); 
	}

}
