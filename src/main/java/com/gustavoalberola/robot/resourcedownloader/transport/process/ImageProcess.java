package com.gustavoalberola.robot.resourcedownloader.transport.process;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.download.Resource;
import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.gustavoalberola.robot.resourcedownloader.transport.resource.ResourceTypes;

@XmlRootElement(name = "image")
public class ImageProcess extends Process {

	static final private Log logger = LogFactory.getLog(ImageProcess.class);
	
	private String location;
	private String saveTo;
	private int retriesOnFail = 3;
	
	@XmlAttribute
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@XmlAttribute(name = "save-to")
	public String getSaveTo() {
		return saveTo;
	}

	public void setSaveTo(String saveTo) {
		this.saveTo = saveTo;
	}

	@XmlAttribute(name = "retries-on-fail")
	public int getRetriesOnFail() {
		return retriesOnFail;
	}

	public void setRetriesOnFail(int retriesOnFail) {
		this.retriesOnFail = retriesOnFail;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		String location = (this.location == null) ? payload.getValue() : this.location;
		
		location = getGeneralContext().replaceExpressionsInString(location, payload);
		String saveTo = getGeneralContext().replaceExpressionsInString(this.saveTo, payload);
		
		logger.info(String.format("Identified image %-30s from %s", saveTo, location));
		
		URI from = UriBuilder.fromPath(location).build();
		
		Resource resource = new Resource(from, saveTo, retriesOnFail, ResourceTypes.IMAGE);
		addResourceToQueue(resource);
	}
}
