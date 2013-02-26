package com.gustavoalberola.robot.resourcedownloader.download;

import java.net.URI;

import com.gustavoalberola.robot.resourcedownloader.transport.resource.ResourceTypes;

public class Resource {

	private URI sourceUrl;
	private String destination;
	private int reintentsOnFail;
	private ResourceTypes resourceType;
			
	public Resource(URI sourceUrl, String destination, int reintentsOnFail, ResourceTypes resourceType) {
		super();
		this.sourceUrl = sourceUrl;
		this.destination = destination;
		this.reintentsOnFail = reintentsOnFail;
		this.resourceType = resourceType;
	}

	public URI getSourceUrl() {
		return sourceUrl;
	}

	public String getDestination() {
		return destination;
	}

	public int getReintentsOnFail() {
		return reintentsOnFail;
	}

	public ResourceTypes getAcceptTypes() {
		return resourceType;
	}

}
