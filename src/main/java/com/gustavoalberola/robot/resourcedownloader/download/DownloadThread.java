package com.gustavoalberola.robot.resourcedownloader.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;
import com.gustavoalberola.robot.resourcedownloader.util.FileUtils;
import com.gustavoalberola.robot.resourcedownloader.util.ResourceManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DownloadThread implements Runnable {

	static final private Log logger = LogFactory.getLog(DownloadThread.class);
	
	private ResourceManager rm;
	private long sleepTime;
	private String name;
	
	public DownloadThread(ResourceManager rm, String name) {
		this.rm = rm;
		this.name = name;
		this.sleepTime = 1000;
	}
	
	
	public DownloadThread(ResourceManager rm, String name, long sleepTime) {
		this.rm = rm;
		this.name = name;
		this.sleepTime = sleepTime;
	}
	
	@Override
	public void run() {
		
		logger.info(name + " started");
		
		boolean keepRunning = true;
		Resource r;
		
		while (keepRunning) {
			r = rm.getNextResourceInQueue();
			
			// Process resource
			if (r != null) {
				try {
					downloadResource(r);
				} catch (Throwable e) {
					logger.error("Cannot download the file", e);
				}
			} else {
				// There are no resources left and the queue has ended. Stop the thread
				if (rm.isQueueEnded()) {
					keepRunning = false;
				// The are no resources left but the thread is still alive, make sleep the thread
				} else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {}
				}
			}
			
		}
		
		logger.info(name + " ended");
	}
	
	private void downloadResource(Resource r) {
		
		Client jerseyClient = Client.create();
		WebResource wr = jerseyClient.resource(r.getSourceUrl());
		
		File copyTo = new File(System.getProperty("user.dir"));
		copyTo = new File(copyTo, r.getDestination());
		
		// This method is synchronized to prevent more than one thread from creating the folder at the same time
		FileUtils.checkIfParentFolderExistsAndCreate(copyTo);
	
		logger.info(name + " downloading " + r.getSourceUrl().toString() + " to " + copyTo.toString());
		
		int contentLength = 0;
		ClientResponse cr = null;
		
		try {
			cr = wr.head();
			contentLength = cr.getLength();
		} catch (ClientHandlerException e) {
			throw new ProcessException("Cannot get the header of the file", e);
		}
		
		for (int x = 0, xMax = r.getReintentsOnFail() ; x <= xMax ; x++) {
			try {
				cr = wr.get(ClientResponse.class);
				InputStream i = cr.getEntity(InputStream.class);
				
				writeToFile(i, copyTo, contentLength);
				
				break;
			} catch (Throwable e) {
				if (x == xMax) {
					throw new ProcessException("All the reintents for getting the file have failed", e);
				}
			}
		}
		
		logger.info(name + " downloaded complete " + r.getSourceUrl().toString() + " to " + copyTo.toString());
	}
	
	private void writeToFile(InputStream uploadedInputStream, File uploadedFileLocation, int expectedSize) 
			throws ProcessException {
			
			int totalRead = 0;
		
			try {
				OutputStream out = new FileOutputStream(uploadedFileLocation);
				int read = 0;
				
				byte[] bytes = new byte[1024];

				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
					totalRead += read;
				}
				out.flush();
				out.close();		
				
			} catch (IOException e) {	 
				throw new ProcessException("Failed to save the file to disk", e);
			}
			
			if (totalRead != expectedSize)
				throw new ProcessException(String.format("The size of the downloaded file doesnt match the expected one. Have: %s - Required: %s", totalRead, expectedSize));
			
		}
}
