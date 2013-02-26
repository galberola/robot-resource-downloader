package com.gustavoalberola.robot.resourcedownloader.util.impl;

import java.util.LinkedList;
import java.util.List;

import com.gustavoalberola.robot.resourcedownloader.download.DownloadThread;
import com.gustavoalberola.robot.resourcedownloader.download.Resource;
import com.gustavoalberola.robot.resourcedownloader.util.ResourceManager;

public class ResourceManagerImpl implements ResourceManager {

	private LinkedList<Resource> queue;
	private List<Thread> threads;
	private Object queueLock;
	private boolean queueRunning;
	private int numberOfThreads;
	
	public ResourceManagerImpl() {
		queue = new LinkedList<Resource>();
		queueLock = new Object();
		queueRunning = false;
		threads = new LinkedList<Thread>();
	}
	
	@Override
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}
	
	@Override
	public void addResourceToQueue(Resource resource) {
		synchronized (queueLock) {
			queue.add(resource);
		}
	}

	@Override
	public void startQueue() {
		queueRunning = true;		
		Thread t = null;
		
		int numThreads = (numberOfThreads < 1) ? 1 : numberOfThreads;
		
		for (int x = 0 ; x < numThreads ; x++) {
			t = new Thread(new DownloadThread(this, "DM["+x+"]"));
			threads.add(t);
			t.start();
		}
	}

	@Override
	public void endQueue() {
		queueRunning = false;
	}
	
	@Override
	public boolean isQueueEnded() {
		return !queueRunning;
	}

	@Override
	public Resource getNextResourceInQueue() {
		Resource resInQueue = null;
		synchronized (queueLock) {
			if (!queue.isEmpty()) {
				resInQueue = queue.removeFirst();
			}
		}
		return resInQueue;
	}
}
