package com.gustavoalberola.robot.resourcedownloader;


import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gustavoalberola.robot.resourcedownloader.model.FindMode;
import com.gustavoalberola.robot.resourcedownloader.model.Payload;
import com.gustavoalberola.robot.resourcedownloader.model.Task;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.gustavoalberola.robot.resourcedownloader.transport.process.GeneralContextVarProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpGetterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.ImageProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.IteratorProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.LoggerProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.StringMatcherProcess;
import com.gustavoalberola.robot.resourcedownloader.util.GeneralContext;
import com.gustavoalberola.robot.resourcedownloader.util.impl.GeneralContextImpl;

public class JaxbConceptProof {
	
	static final private GeneralContext generalContext;
	static final private Log logger = LogFactory.getLog(JaxbConceptProof.class);
	
	static {
		generalContext = new GeneralContextImpl();
	}
	
	static public void main(String[] args) throws Exception {
		
		logger.info("Initializing app...");
		
//		specialChars();		
//		marshaler();
//		httpGetterTest();
//		generateXsd();
//		testRegEx();
//		System.out.println(String.format("%0"+3+"d", 3));
		testCaseMcAnimeNaruto();
	}
	
	static private void testRegEx() {
//		String regEx = "^(.+/)"; // Get the url
//		String regEx = "\\.([\\w]+)$"; // Get the extension
		String regEx = "change_chapter\\('([^']+)'";
//		String match = "http://manga.mcanime.net/manga/1598/2411/418378/0.jpg";
		String match = "<select onchange=\"change_chapter('/manga_enlinea/naruto/backbeard/'+this.value+'#ver');\"";
		
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(match);
		
		if (m.find()) {
			System.out.println("Match");
			System.out.println("Groups: " + m.groupCount());
			System.out.println("G[]: " + m.group());
			
			for (int x = 0, xMax = m.groupCount() ; x < xMax ; x++) {
				System.out.println(String.format("G[%d]: %s", x+1, m.group(x+1)));
			}
			
		} else {
			System.out.println("Not match");
		}
	}
	
	static private void testCaseMcAnimeNaruto() throws JAXBException {
		
		// Set the context var basics		
		GeneralContextVarProcess p0 = new GeneralContextVarProcess();
		p0.setName("domain");
		p0.setValue("http://www.mcanime.net");
		
		GeneralContextVarProcess p00 = new GeneralContextVarProcess();
		p00.setName("input-url");
//		p00.setValue("http://www.mcanime.net/manga_enlinea/naruto/backbeard/345559/18"); // Naruto
//		p00.setValue("http://www.mcanime.net/manga_enlinea/claymore/shinshin_fansub/331768/1"); // Claymore
		p00.setValue("http://www.mcanime.net/manga_enlinea/fairy_tail/shin_nakamas_no_fansub/346213/1"); // Fairytail
				
		GeneralContextVarProcess p000 = new GeneralContextVarProcess();
		p000.setName("manga");
		p000.setValue("fairytail");	
		
		GeneralContextVarProcess pa = new GeneralContextVarProcess();
		pa.setName("chapter-from");
		pa.setValue("265");
		
		GeneralContextVarProcess pb = new GeneralContextVarProcess();
		pb.setName("chapter-to");
		pb.setValue("310");
		
		StringMatcherProcess p0000 = new StringMatcherProcess();
		p0000.setInput("{input-url}");
		p0000.setSearch("^([.]+)/\\d+");
		p0000.setSearchGroup(1);
		p0000.setContextName("base-url");
		p0000.setReplaceContextVarsInput(true);
		
		// Get the input url and extract the base path for images
		HttpGetterProcess p1 = new HttpGetterProcess();
		p1.setUrl("{input-url}");
		
		// STAGE 1 - Iterate through all the desired chapters or all
		HttpFilterProcess p2 = new HttpFilterProcess();
		p2.setTag("select");
		p2.setName("chapter");
		p2.setFindMode(FindMode.FIRST);
		
		// Search the base URL to request for a new chapter
		StringMatcherProcess p2b = new StringMatcherProcess();
		p2b.setSearch("change_chapter\\('([^']+)'");
		p2b.setSearchGroup(1);
		p2b.setContextName("base-url");
		
		// Iterate from chapter A to B  (A and B inclusive)
		IteratorProcess p3 = new IteratorProcess();
		p3.setValueFrom("{chapter-from}");
		p3.setValueTo("{chapter-to}");
		p3.setContextName("chapter");
		
		// Look for the <option> containing the chapter
		HttpFilterProcess p4 = new HttpFilterProcess();
		p4.setTag("option");
		p4.setValue("{chapter}");
		p4.setFindMode(FindMode.FIRST);
		
		StringMatcherProcess p5 = new StringMatcherProcess();
		p5.setSearch("value=\"([\\d]+)");
		p5.setSearchGroup(1);
		p5.setContextName("chapter-id");
				
		
		// STAGE 2 - Get all the pages that the chapter has
		HttpGetterProcess p6 = new HttpGetterProcess();
		p6.setUrl("{domain}{base-url}{chapter-id}/1");
		
		HttpFilterProcess p7 = new HttpFilterProcess();
		p7.setTag("Select");
		p7.setName("page");
		p7.setFindMode(FindMode.ALL);		
		
		StringMatcherProcess p8 = new StringMatcherProcess();
		p8.setSearch("value=\"([\\d]+)\"");
		p8.setSearchGroup(1);
		p8.setContextName("page");
		p8.setFindMode(FindMode.ALL);
		
		
		// STAGE 3 - For each chapter locate the URL base for the images (all the URLs are the same only that the page number changes)
		HttpGetterProcess p9 = new HttpGetterProcess();
		p9.setUrl("{domain}{base-url}{chapter-id}/{page}");
		
		HttpFilterProcess p10 = new HttpFilterProcess();
		p10.setTag("div");
		p10.setClazz("current_page");		
				
		HttpFilterProcess p11 = new HttpFilterProcess();
		p11.setTag("img");
		
		StringMatcherProcess p12 = new StringMatcherProcess();
		p12.setSearch("src=\"([^\"]+)");
		p12.setSearchGroup(1);
		p12.setReplacePayload(true);
				
		StringMatcherProcess p13 = new StringMatcherProcess();
		p13.setSearch("\\.([\\w]+)$");
		p13.setSearchGroup(1);
		p13.setContextName("img-extension");
		
		// STAGE 4 - Add download
		ImageProcess p14 = new ImageProcess();
		p14.setLocation("{payload}");
		p14.setSaveTo("target/{manga}/{4#chapter}/{manga}-{4#chapter}-{2#page}.{img-extension}");

		// STAGE 5 - Add the process to the Task (this is maded automatically when the marshall process is runned)		
		List<Process> plist = new LinkedList<Process>();
		plist.add(p0);
		plist.add(p00);
		plist.add(p000);
		plist.add(pa);
		plist.add(pb);
		plist.add(p1);
		plist.add(p2);
		plist.add(p2b);
		plist.add(p3);
		plist.add(p4);
		plist.add(p5);
		plist.add(p6);
		plist.add(p7);
		plist.add(p8);
		plist.add(p9);
		plist.add(p10);
		plist.add(p11);
		plist.add(p12);
		plist.add(p13);
		plist.add(p14);
		
		Task t = new Task();
		t.setProcess(plist);
		t.setNumberOfThreads(15);
		
//		JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
//		Marshaller marshaller = jaxbContext.createMarshaller();
//		
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		
//		File f = new File("mcanime-naruto-charpetnumber.xml");
//		marshaller.marshal(t, f);

		t.execute();
		
	}
	
	static private void httpGetterTest() {
		HttpGetterProcess p1 = new HttpGetterProcess();
		p1.setGeneralContext(generalContext);
		p1.setUrl("http://www.mcanime.net/manga_enlinea/naruto/backbeard/345559/18");
		
		LoggerProcess p2 = new LoggerProcess();
		p2.setGeneralContext(generalContext);
		p2.setMessage("HTML has been obtained");
		
		HttpFilterProcess p3 = new HttpFilterProcess();
		p3.setGeneralContext(generalContext);
		p3.setTag("select");
		p3.setName("chapter");
		
		LoggerProcess p4 = new LoggerProcess();
		p4.setGeneralContext(generalContext);
		
		IteratorProcess p4a = new IteratorProcess();
		p4a.setGeneralContext(generalContext);
		p4a.setContextName("capitulo");
		p4a.setValueFrom("570");
		p4a.setValueTo("575");
						
		HttpFilterProcess p4b = new HttpFilterProcess();
		p4b.setGeneralContext(generalContext);
		p4b.setTag("option");
		p4b.setValue("{capitulo}");
		
		LoggerProcess p4c = new LoggerProcess();
		p4c.setGeneralContext(generalContext);
		
		StringMatcherProcess p5 = new StringMatcherProcess();
		p5.setGeneralContext(generalContext);
		p5.setSearch("value=\"([^\"]+)\"");
		p5.setReplacePayload(true);
		p5.setSearchGroup(1);
		
		LoggerProcess p6 = new LoggerProcess();
		p6.setGeneralContext(generalContext);
		
		p1.setNextProcessInChain(p2);
		p2.setNextProcessInChain(p3);
		p3.setNextProcessInChain(p4);		
		p4.setNextProcessInChain(p4a);
		p4a.setNextProcessInChain(p4b);
		p4b.setNextProcessInChain(p4c);
		p4c.setNextProcessInChain(p5);
		p5.setNextProcessInChain(p6);
		
		p1.execute(null);
	}
	
	static private void specialChars() {
		Payload p = new Payload("imthepayload");
		GeneralContext gc = new GeneralContextImpl();
		gc.setValue("var1" , "imvar1");
		gc.setValue("var2", "imvar2");
		
		String s = "www.mysite.com/{var1}/someother/{var2}/{payload}.png";
		s = gc.replaceExpressionsInString(s, p);
		System.out.println(s);
	}
	
	static private void marshaler() throws JAXBException {
		GeneralContextVarProcess generalContextVar = new GeneralContextVarProcess();
		generalContextVar.setGeneralContext(generalContext);
		generalContextVar.setName("myVar");
		generalContextVar.setValue("myValue");
		
		IteratorProcess iterator = new IteratorProcess();
		iterator.setContextName("capitulo");
		iterator.setValueFrom("1");
		iterator.setValueTo("3");
		
		HttpFilterProcess httpFilterProcess = new HttpFilterProcess();
		httpFilterProcess.setFindMode(FindMode.ALL);
		httpFilterProcess.setId("myId");
		httpFilterProcess.setClazz("myClass");
		httpFilterProcess.setInput("myInput");
		httpFilterProcess.setName("myName");
		httpFilterProcess.setTag("myTag");
		
		LoggerProcess loggerProcess = new LoggerProcess();
		loggerProcess.setMessage("myMessage");
		
		HttpGetterProcess httpGetterProcess = new HttpGetterProcess();
		httpGetterProcess.setUrl("myUrl");
		httpGetterProcess.setThrowException(true);
		
		LoggerProcess loggerProcess2 = new LoggerProcess();
		
		StringMatcherProcess stringMatcherProcess = new StringMatcherProcess();
		stringMatcherProcess.setGeneralContext(generalContext);
		stringMatcherProcess.setInput("myInput");
		stringMatcherProcess.setSearch("mySearch");
		
		ImageProcess imageProcess = new ImageProcess();
		imageProcess.setGeneralContext(generalContext);
		imageProcess.setLocation("www.somesite.com/image.jpg");
		imageProcess.setSaveTo("image.jpg");		
						
		List<Process> listProcess = new LinkedList<Process>();
		listProcess.add(generalContextVar);
		listProcess.add(iterator);
		listProcess.add(httpFilterProcess);
		listProcess.add(loggerProcess);
		listProcess.add(httpGetterProcess);
		listProcess.add(loggerProcess2);
		listProcess.add(stringMatcherProcess);
		listProcess.add(imageProcess);
		
		Task task = new Task();
		task.setProcess(listProcess);
		
		JAXBContext jaxbContext2 = JAXBContext.newInstance(Task.class);
		Marshaller marshaller2 = jaxbContext2.createMarshaller();
		
		marshaller2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File f = new File("output.xml");
		
		marshaller2.marshal(task, System.out);
		marshaller2.marshal(task, f);
		
		Unmarshaller u = jaxbContext2.createUnmarshaller();
		Task t2 = (Task) u.unmarshal(f);
		
		List<Process> p = t2.getProcess();
		for (int x = p.size() -1 ; x > 0 ; x-- ){
			p.get(x-1).setNextProcessInChain(p.get(x));
			p.get(x).setGeneralContext(generalContext);
		}
		p.get(0).setGeneralContext(generalContext);
		
		t2.toString();
	}
	
	static private void generateXsd() throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
		MySchemaOutputResolver sor = new MySchemaOutputResolver();
		jaxbContext.generateSchema(sor);
	}
	
	static private class MySchemaOutputResolver extends SchemaOutputResolver {

		@Override
		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			File file = new File(suggestedFileName);
			StreamResult result = new StreamResult(file);
			result.setSystemId(file.toURI().toURL().toString());
			return result;
		}
		
	}
}
