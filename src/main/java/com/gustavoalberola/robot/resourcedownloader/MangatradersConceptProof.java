package com.gustavoalberola.robot.resourcedownloader;

import java.util.LinkedList;
import java.util.List;

import com.gustavoalberola.robot.resourcedownloader.model.FindMode;
import com.gustavoalberola.robot.resourcedownloader.model.Task;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.gustavoalberola.robot.resourcedownloader.transport.process.GeneralContextVarProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpGetterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.ImageProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.LoggerProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.StringMatcherProcess;

public class MangatradersConceptProof {
	
	static private final String BASE_URL			= "http://www.mangatraders.com/view/file/";
	static private final String START_URL			= BASE_URL + "141436";
	static private final String MANGA_FILE_NAME		= "SwordArtOnlineExtras";
	static private final String MANGA_FOLDER_NAME 	= "Sword Art Online Extras"; 
	
	static public void main(String[] args) {
						
		List<Process> plist = new LinkedList<Process>();
		
		GeneralContextVarProcess gcvp 	= null;
		HttpGetterProcess httpgp 		= null;
		HttpFilterProcess httpfp 		= null;
		LoggerProcess lp 				= null;
		StringMatcherProcess smp 		= null;
		ImageProcess ip 				= null;
		
		gcvp = new GeneralContextVarProcess();
		gcvp.setName("base-url");
		gcvp.setValue(BASE_URL);
		plist.add(gcvp);
				
		gcvp = new GeneralContextVarProcess();
		gcvp.setName("start-url");
		gcvp.setValue(START_URL);
		plist.add(gcvp);
						
		gcvp = new GeneralContextVarProcess();
		gcvp.setName("manga-file-name");
		gcvp.setValue(MANGA_FILE_NAME);
		plist.add(gcvp);
		
		gcvp = new GeneralContextVarProcess();
		gcvp.setName("manga-folder-name");
		gcvp.setValue(MANGA_FOLDER_NAME);
		plist.add(gcvp);
		
		httpgp = new HttpGetterProcess();
		httpgp.setUrl("{start-url}");
		plist.add(httpgp);
		
		httpfp = new HttpFilterProcess();
		httpfp.setTag("select");
		httpfp.setName("file_selector_top");
		httpfp.setFindMode(FindMode.FIRST);
		plist.add(httpfp);
		
		lp = new LoggerProcess();
		plist.add(lp);
		
		smp = new StringMatcherProcess();
		smp.setSearch("value=\"([\\d]+)\"");
		smp.setSearchGroup(1);
		smp.setContextName("chapter");
		smp.setFindMode(FindMode.ALL);
		plist.add(smp);
		
		httpgp = new HttpGetterProcess();
		httpgp.setUrl("{base-url}{chapter}");
		plist.add(httpgp);
		
		httpfp = new HttpFilterProcess();
		httpfp.setTag("select");
		httpfp.setName("page_top2");
		httpfp.setFindMode(FindMode.FIRST);
		plist.add(httpfp);
		
		smp = new StringMatcherProcess();
		smp.setSearch("value=\"([\\d]+)\"");
		smp.setSearchGroup(1);
		smp.setContextName("page");
		smp.setFindMode(FindMode.ALL);
		plist.add(smp);
		
		httpgp = new HttpGetterProcess();
		httpgp.setUrl("{base-url}{chapter}/page/{page}");
		plist.add(httpgp);
				
		httpfp = new HttpFilterProcess();
		httpfp.setTag("img");
		httpfp.setId("image");
		httpfp.setFindMode(FindMode.FIRST);
		plist.add(httpfp);
		
		smp = new StringMatcherProcess();
		smp.setSearch("src=\"([^\"]+)");
		smp.setSearchGroup(1);
		smp.setReplacePayload(true);
		plist.add(smp);
				
		smp = new StringMatcherProcess();
		smp.setSearch("\\.([\\w]+)$");
		smp.setSearchGroup(1);
		smp.setContextName("img-extension");
		plist.add(smp);
		
		ip = new ImageProcess();
		ip.setLocation("{payload}");
		ip.setSaveTo("target/{manga-folder-name}/{4#chapter}/{manga-file-name}-{4#chapter}-{2#page}.{img-extension}");
		plist.add(ip);
		
		Task t = new Task();
		t.setProcess(plist);
		t.setNumberOfThreads(4);
		t.execute();
	}
}
