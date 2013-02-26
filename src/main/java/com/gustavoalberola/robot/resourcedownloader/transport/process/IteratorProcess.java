package com.gustavoalberola.robot.resourcedownloader.transport.process;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;

@XmlRootElement(name = "iterator")
public class IteratorProcess extends Process {

	private String contextName;
	private String valueFrom;
	private String valueTo;
		
	public String getContextName() {
		return contextName;
	}
	
	@XmlAttribute(name = "context-name")
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getValueFrom() {
		return valueFrom;
	}

	@XmlAttribute(name = "value-from")
	public void setValueFrom(String valueFrom) {
		this.valueFrom = valueFrom;
	}

	public String getValueTo() {
		return valueTo;
	}
	
	@XmlAttribute(name = "value-to")
	public void setValueTo(String valueTo) {
		this.valueTo = valueTo;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		
		if (isNextProcessToCall()) {
			int valueFrom = 0;
			int valueTo = 0;
			
			try {
				valueFrom = Integer.valueOf(getGeneralContext().replaceSpecialCharactersInString(this.valueFrom, payload));
				valueTo = Integer.valueOf(getGeneralContext().replaceSpecialCharactersInString(this.valueTo, payload));
			} catch (NumberFormatException e) {
				throw new ProcessException("Iterator cannot take valueFrom or valueTo as Integer", e);
			}
			
			if (valueFrom > valueTo) {
				int tmp = valueTo;
				valueTo = valueFrom;
				valueFrom = tmp;
			}
			
			for(int x = valueFrom ; x <= valueTo ; x++) {				
				getGeneralContext().setValue(contextName, x);
							
				getNextProcessInChain().execute(payload);
			}
		}
	}

}
