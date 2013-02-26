package com.gustavoalberola.robot.resourcedownloader.util;

import java.io.File;

import com.gustavoalberola.robot.resourcedownloader.exception.ProcessException;

public class FileUtils {
	
	static synchronized public void checkIfParentFolderExistsAndCreate(File copyTo) throws ProcessException {
		if (!copyTo.getParentFile().exists() && !copyTo.getParentFile().mkdirs()) {
			throw new ProcessException("Cannot create the directory to store the folder in the location " + copyTo.getParentFile().toString());
		}
	}
}
