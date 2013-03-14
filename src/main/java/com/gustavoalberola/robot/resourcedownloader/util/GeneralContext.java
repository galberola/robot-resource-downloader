package com.gustavoalberola.robot.resourcedownloader.util;

import com.gustavoalberola.robot.resourcedownloader.exception.EmptyGeneralContextVariableNameException;
import com.gustavoalberola.robot.resourcedownloader.exception.NoSuchGeneralContextVariableException;
import com.gustavoalberola.robot.resourcedownloader.exception.TryingToUseReservedNameInGeneralContextException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;

/**
 * The context stores all the variables that the flows generates across it's execution
 * 
 * @author gustavoalberola
 */
public interface GeneralContext {
	
	/**
	 * Add a value to the context
	 * 
	 * @param key The Key for the context
	 * @param value The value to be stored and referenced by the key
	 * @throws EmptyGeneralContextVariableNameException If you not specify a name attribute
	 * @throws TryingToUseReservedNameInGeneralContextException If the name is a reserved word
	 */
	public void setValue(String key, Object value) throws EmptyGeneralContextVariableNameException, TryingToUseReservedNameInGeneralContextException;
	
	/**
	 * Returns the value contained in the context for certain key
	 * 
	 * @param key The key for the context
	 * @return The value stored in the context for the key
	 * @throws EmptyGeneralContextVariableNameException If you not specify a name attribute
	 * @throws TryingToUseReservedNameInGeneralContextException If the name is a reserved word
	 */
	public Object getValue(String key) throws EmptyGeneralContextVariableNameException, TryingToUseReservedNameInGeneralContextException;
	
	/**
	 * This functions find and replace all the expressions in the string. 
	 * <p>
	 * The expressions are the ones enclosed in \{\}. Inside the expression there will be a reference to a key in the context like \{mykey\}.
	 * 
	 * @param inputString The string to analyze
	 * @param payload The payload from the last message
	 * @return The modified String
	 * @throws NoSuchGeneralContextVariableException If the string contains an expression that point to a key non existent in the context
	 */
	public String replaceExpressionsInString(String inputString, Payload payload) throws NoSuchGeneralContextVariableException;
}
