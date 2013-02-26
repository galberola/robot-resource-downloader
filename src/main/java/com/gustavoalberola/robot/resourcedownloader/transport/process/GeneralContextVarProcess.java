package com.gustavoalberola.robot.resourcedownloader.transport.process;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;

/**
 * Add a new variable to the General Context to be used by the process
 * <p>
 * The Payload do not suffer any alteration and it is passed to the next process in the chain 
 */
@XmlRootElement(name = "general-context-var")
public class GeneralContextVarProcess extends Process {
	
	/**
	 * The name to use in the generalContext
	 */
	private String name;
	
	/**
	 * The value to store
	 */
	private String value;
	
	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	@XmlAttribute
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		getGeneralContext().setValue(name, value);
		
		if (isNextProcessToCall()) getNextProcessInChain().execute(payload);
	}
}
