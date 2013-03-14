package com.gustavoalberola.robot.resourcedownloader.transport.process;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;

@XmlRootElement(name = "logger")
public class LoggerProcess extends Process {

	private String message;
	
	public LoggerProcess() {}
	
	public LoggerProcess(String message) {
		this.message = message;
	}
	
	@XmlAttribute(required = false)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		if (message == null) {
			System.out.println("Logger payload:" + payload.getValue());
		} else {
			String m = getGeneralContext().replaceExpressionsInString(message, payload);
			System.out.println("Logger:" + m);
		}
		
		callNextProcessInChain(payload);
	}
}
