package com.gustavoalberola.robot.resourcedownloader.exception;

public class TryingToUseReservedNameInGeneralContextException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TryingToUseReservedNameInGeneralContextException(String varName) {
		super("Trying to Get/Set a variable with a reserved name. The  " + varName + " is a reserved word in the General Context and cannot be used");
	}
}
