package com.gustavoalberola.robot.resourcedownloader.exception;

public class EmptyGeneralContextVariableNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EmptyGeneralContextVariableNameException() {
		super("Cannot Set/Get a variable from the General Context if the name is null or empty");
	}
}
