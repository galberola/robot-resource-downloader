package com.gustavoalberola.robot.resourcedownloader.util;

import com.gustavoalberola.robot.resourcedownloader.exception.EmptyGeneralContextVariableNameException;
import com.gustavoalberola.robot.resourcedownloader.exception.NoSuchGeneralContextVariableException;
import com.gustavoalberola.robot.resourcedownloader.exception.TryingToUseReservedNameInGeneralContextException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;

public interface GeneralContext {
	
	public void setValue(String name, Object value) throws EmptyGeneralContextVariableNameException, TryingToUseReservedNameInGeneralContextException;
	
	public Object getValue(String name) throws EmptyGeneralContextVariableNameException, TryingToUseReservedNameInGeneralContextException;
	
	public String replaceSpecialCharactersInString(String inputString, Payload payload) throws NoSuchGeneralContextVariableException;
}
