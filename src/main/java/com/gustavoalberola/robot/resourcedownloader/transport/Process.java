package com.gustavoalberola.robot.resourcedownloader.transport;

import javax.xml.bind.annotation.XmlTransient;

import com.gustavoalberola.robot.resourcedownloader.download.Resource;
import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.util.GeneralContext;
import com.gustavoalberola.robot.resourcedownloader.util.ResourceManager;

public abstract class Process {
	
	private Process nextProcessInChain;
		
	private GeneralContext generalContext;
	
	private ResourceManager resourceManager;
	
	public Process getNextProcessInChain() {
		return nextProcessInChain;
	}

	@XmlTransient
	public void setNextProcessInChain(Process nextProcessInChain) {
		this.nextProcessInChain = nextProcessInChain;
	}
	
	public GeneralContext getGeneralContext() {
		return generalContext;
	}

	@XmlTransient
	public void setGeneralContext(GeneralContext generalContext) {
		this.generalContext = generalContext;
	}
	
	@XmlTransient	
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	abstract public void execute(Payload payload) throws ProcessException;
	
	final protected boolean isNextProcessToCall() {
		return nextProcessInChain != null;
	}
	
	final protected void callNextProcessInChain(Payload payload) {
		if (isNextProcessToCall()) {
			nextProcessInChain.execute(payload);
		}
	}
	
	final protected void addResourceToQueue(Resource resource) {
		resourceManager.addResourceToQueue(resource);
	}
}
