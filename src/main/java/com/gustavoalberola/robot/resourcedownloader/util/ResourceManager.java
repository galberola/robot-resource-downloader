package com.gustavoalberola.robot.resourcedownloader.util;

import com.gustavoalberola.robot.resourcedownloader.download.Resource;

public interface ResourceManager {
	public void addResourceToQueue(Resource resource);
	public void startQueue();
	public void endQueue();
	public boolean isQueueEnded();
	public Resource getNextResourceInQueue();
	public void setNumberOfThreads(int numberOfThreads);
}
