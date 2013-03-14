package com.gustavoalberola.robot.resourcedownloader.transport.process;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

@XmlRootElement(name = "http-getter")
public class HttpGetterProcess extends Process {

	static final private Log logger = LogFactory.getLog(HttpGetterProcess.class);
	
	/**
	 * A regex representation of the url
	 */
	private String url;
	
	/**
	 * Indicates if the http call returns a code diferent than 200 it should throw an exception or not
	 */
	private boolean throwException;
	
	@XmlAttribute
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@XmlAttribute(name = "throw-exception", required = false)
	public boolean isThrowException() {
		return throwException;
	}

	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}

	@Override
	public void execute(Payload payload) throws ProcessException {
		// Use the this.url if it is not null or the payload as the URL 
		// but first replace the special characters that might be in it
		String url = (this.url == null) ? payload.getValue() : this.url;		
		url = getGeneralContext().replaceExpressionsInString(url, payload);
		
		URI uri = UriBuilder.fromPath(url).build();		
		
		// Create the Jersey Client in order to make the request
		Client jerseyClient = new Client();
		WebResource webResource = jerseyClient.resource(uri);
		
		Payload newPayload = null;
		
		try {
			newPayload = new Payload(webResource.get(String.class)); // Load here the content of the page
			logger.info("Getting content from: " + webResource.toString());
		} catch (UniformInterfaceException e) {
			int statusCode = e.getResponse().getStatus();
			throw new ProcessException(String.format("The reponse returned a code: %d when calling %s", statusCode, uri.toString()) , e);
		}
		
		if (isNextProcessToCall()) {
			getNextProcessInChain().execute(newPayload);
		}
	}	
}
