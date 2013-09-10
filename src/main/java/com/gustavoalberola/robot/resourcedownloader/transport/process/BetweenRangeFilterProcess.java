package com.gustavoalberola.robot.resourcedownloader.transport.process;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;

@XmlRootElement(name = "between")
public class BetweenRangeFilterProcess extends Process {

	private String value;
	private String from;
	private String to;

	public String getValue() {
		return value;
	}
	
	@XmlAttribute(name = "value")
	public void setValue(String value) {
		this.value = value;
	}

	public String getFrom() {
		return from;
	}
	
	@XmlAttribute(name = "from")
	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	@XmlAttribute(name = "to")
	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {

		if (isNextProcessToCall()) {
			int from = 0;
			int to = 0;
			int value = 0;

			try {
				from = Integer.valueOf(getGeneralContext().replaceExpressionsInString(this.from, payload));
				to = Integer.valueOf(getGeneralContext().replaceExpressionsInString(this.to, payload));
				value = Integer.valueOf(getGeneralContext().replaceExpressionsInString(this.value, payload));
			} catch (NumberFormatException e) {
				throw new ProcessException("Iterator cannot take from, to or value as Integer", e);
			}

			if (value >= from && value <= to) {
				getNextProcessInChain().execute(payload);
			}
		}
	}
}
