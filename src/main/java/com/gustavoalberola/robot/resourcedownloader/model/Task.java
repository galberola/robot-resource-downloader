package com.gustavoalberola.robot.resourcedownloader.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.gustavoalberola.robot.resourcedownloader.transport.process.GeneralContextVarProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpGetterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.ImageProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.IteratorProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.LoggerProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.StringMatcherProcess;
import com.gustavoalberola.robot.resourcedownloader.util.GeneralContext;
import com.gustavoalberola.robot.resourcedownloader.util.ResourceManager;
import com.gustavoalberola.robot.resourcedownloader.util.impl.GeneralContextImpl;
import com.gustavoalberola.robot.resourcedownloader.util.impl.ResourceManagerImpl;

@XmlRootElement(name = "task")
public class Task {

	private List<Process> process;
	private int numberOfThreads = 1;
	
	@XmlAttribute(name = "numberofthreads", required = false)
	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public List<Process> getProcess() {
		return process;
	}

	@XmlElementWrapper(name = "process-list")
	@XmlElementRefs({ 
			@XmlElementRef(type = GeneralContextVarProcess.class),
			@XmlElementRef(type = IteratorProcess.class),
			@XmlElementRef(type = HttpFilterProcess.class),
			@XmlElementRef(type = HttpGetterProcess.class),
			@XmlElementRef(type = LoggerProcess.class),
			@XmlElementRef(type = StringMatcherProcess.class),
			@XmlElementRef(type = ImageProcess.class)})
	public void setProcess(List<Process> process) {
		this.process = process;
	}

	public void execute() {		
		if (process != null && !process.isEmpty()) {		
			// Create a GeneralContext so the process can use it (it is shared among all of them)
			GeneralContext generalContext = new GeneralContextImpl();
			
			// Create the ResourcesManager so it can add resources to the Queue
			ResourceManager resourceManager = new ResourceManagerImpl();
			resourceManager.setNumberOfThreads(numberOfThreads);
			
			// Assign the general context to all the process and also associate the chain 1-2, 2-3, ... , n-m
			for (int x = process.size() -1 ; x > 0 ; x-- ){
				process.get(x-1).setNextProcessInChain(process.get(x));
				process.get(x).setGeneralContext(generalContext);
				process.get(x).setResourceManager(resourceManager);
			}
			process.get(0).setGeneralContext(generalContext);
			process.get(0).setResourceManager(resourceManager);
			
			// Start the resource manager (this will lauch the excecution of the download threads)
			resourceManager.startQueue();
			
			// Start the chain of execution (the first payload is null)
			process.get(0).execute(null);
			
			// This will tell the ResourceManager that when it hasnt any more resources in the queue must stop the thread
			resourceManager.endQueue();
		}
	}
}
