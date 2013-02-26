package com.gustavoalberola.robot.resourcedownloader.exception;

public class NoSuchGeneralContextVariableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSuchGeneralContextVariableException(String varName) {
		super("Trying to get the variable " + varName + " but it does not exists in the General Context");
	}
}
