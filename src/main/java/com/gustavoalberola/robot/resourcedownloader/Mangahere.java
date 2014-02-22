package com.gustavoalberola.robot.resourcedownloader;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.model.FindMode;
import com.gustavoalberola.robot.resourcedownloader.model.Task;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.gustavoalberola.robot.resourcedownloader.transport.process.BetweenRangeFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.GeneralContextVarProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpGetterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.ImageProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.StringMatcherProcess;

public class Mangahere {
	
	static final private Log logger = LogFactory.getLog(Mangahere.class);
	
	static public void main (String[] args) {
		
		String chapterFrom 	= "1";
		String chapterTo 	= "999";
		
		if (args.length > 0) {
			logger.debug("From chapter: " + args[0]);
			chapterFrom = args[0];
		}
		if (args.length > 1) {
			logger.debug("To chapter: " + args[1]);
			chapterTo = args[1];
		}		
		
		GeneralContextVarProcess p0 = new GeneralContextVarProcess();
		p0.setName("domain");
		p0.setValue("http://es.mangahere.com");

		GeneralContextVarProcess p00 = new GeneralContextVarProcess();
		p00.setName("input-url");
		p00.setValue("http://es.mangahere.com/manga/shingeki_no_kyojin/c1/");

		GeneralContextVarProcess p000 = new GeneralContextVarProcess();
		p000.setName("manga");
		p000.setValue("Attack on Titan");
		
		// Get the main page of chapter 1 page 1
		HttpGetterProcess p1 = new HttpGetterProcess();
		p1.setUrl("{input-url}");
		
		// Find all the chapters available ===============================================================================================================
		HttpFilterProcess p2 = new HttpFilterProcess();
		p2.setTag("select");
		p2.setId("top_chapter_list");
		p2.setFindMode(FindMode.FIRST);
		
		// Filter each option per Chapter
		HttpFilterProcess p3 = new HttpFilterProcess();
		p3.setTag("Option");
		p3.setFindMode(FindMode.ALL);
		
		// Extract the url for the chapter
		StringMatcherProcess p4 = new StringMatcherProcess();
		p4.setReplacePayload(false); // Don't replace the payload with the result, we need to run the filter again with this result
		p4.setSearchGroup(1);
		p4.setContextName("chapter-url");
		p4.setSearch("value=\"([^\"]+)\"");
		
		// And the number of chapter
		StringMatcherProcess p4b = new StringMatcherProcess();
		p4b.setReplacePayload(false); // Don't replace the payload with the result, we need to run the filter again with this result
		p4b.setSearchGroup(1);
		p4b.setContextName("chapter");
		p4b.setSearch("/c([0-9\\.]+)/");
		
		BetweenRangeFilterProcess p4c = new BetweenRangeFilterProcess();
		p4c.setFrom(chapterFrom);
		p4c.setTo(chapterTo);
		p4c.setValue("{chapter}");
		
		// Get each chapter ==============================================================================
		HttpGetterProcess p5 = new HttpGetterProcess();
		p5.setUrl("{domain}{chapter-url}");
		
		HttpFilterProcess p6 = new HttpFilterProcess();
		p6.setTag("select");
		p6.setClazz("wid60");
		p6.setFindMode(FindMode.FIRST);
		
		// Filter each option per Chapter
		HttpFilterProcess p7 = new HttpFilterProcess();
		p7.setTag("Option");
		p7.setFindMode(FindMode.ALL);
		
		StringMatcherProcess p8 = new StringMatcherProcess();
		p8.setReplacePayload(false); // Don't replace the payload with the result, we need to run the filter again with this result
		p8.setSearchGroup(1);
		p8.setContextName("page-url");
		p8.setSearch("value=\"([^\"]+)\"");
		
		StringMatcherProcess p8b = new StringMatcherProcess();
		p8b.setReplacePayload(false); // Don't replace the payload with the result, we need to run the filter again with this result
		p8b.setSearchGroup(1);
		p8b.setContextName("page");
		p8b.setSearch(">([0-9]+)<");
		
		// Get the page ===================================================================================
		HttpGetterProcess p9 = new HttpGetterProcess();
		p9.setUrl("{domain}{page-url}");
		
		HttpFilterProcess p10 = new HttpFilterProcess();
		p10.setTag("img");
		p10.setId("image");
		p10.setFindMode(FindMode.FIRST);
		
		StringMatcherProcess p12 = new StringMatcherProcess();
		p12.setSearch("src=\"([^\"]+)");
		p12.setSearchGroup(1);
		p12.setContextName("image-url");
		p12.setReplacePayload(true);

		StringMatcherProcess p13 = new StringMatcherProcess();
		p13.setSearch("\\.([\\w]+)$");
		p13.setSearchGroup(1);
		p13.setContextName("img-extension");
		
		ImageProcess p14 = new ImageProcess();
		p14.setLocation("{image-url}");
		p14.setSaveTo("target/{manga}/{4#chapter}/{manga}-{4#chapter}-{2#page}.{img-extension}");
		
		
		List<Process> plist = new LinkedList<Process>();
		plist.add(p0);
		plist.add(p00);
		plist.add(p000);
		plist.add(p1);
		plist.add(p2);
		plist.add(p3);
		plist.add(p4);
		plist.add(p4b);
		plist.add(p4c);
		plist.add(p5);
		plist.add(p6);
		plist.add(p7);
		plist.add(p8);
		plist.add(p8b);
		plist.add(p9);
		plist.add(p10);
		plist.add(p12);
		plist.add(p13);
		plist.add(p14);

		Task t = new Task();
		t.setProcess(plist);
		t.setNumberOfThreads(5);

		t.execute();
	}
}
