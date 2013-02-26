package com.gustavoalberola.robot.resourcedownloader.exception;

public class ProcessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ProcessException(String message) {
		super(message);
	}
	
	public ProcessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ProcessException(Throwable cause) {
		super(cause);
	}

}
