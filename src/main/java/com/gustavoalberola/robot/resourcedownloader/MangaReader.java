package com.gustavoalberola.robot.resourcedownloader;

import java.util.LinkedList;
import java.util.List;

import com.gustavoalberola.robot.resourcedownloader.model.FindMode;
import com.gustavoalberola.robot.resourcedownloader.model.Task;
import com.gustavoalberola.robot.resourcedownloader.transport.Process;
import com.gustavoalberola.robot.resourcedownloader.transport.process.BetweenRangeFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.GeneralContextVarProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpFilterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.HttpGetterProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.ImageProcess;
import com.gustavoalberola.robot.resourcedownloader.transport.process.StringMatcherProcess;

public class MangaReader {

	static final private int NUMBER_OF_THREADS = 10;

	static public void main(String[] args) {
		GeneralContextVarProcess p0 = new GeneralContextVarProcess();
		p0.setName("domain");
		p0.setValue("http://www.mangareader.net");

		GeneralContextVarProcess p00 = new GeneralContextVarProcess();
		p00.setName("input-url");
		p00.setValue("http://www.mangareader.net/670-33181-52/kuroko-no-basket/chapter-1.html"); // Kuroko no Basket

		GeneralContextVarProcess p000 = new GeneralContextVarProcess();
		p000.setName("manga");
		p000.setValue("Kuroko No Basket");
		
		// Get the first URL
//		HttpGetterProcess p1 = new HttpGetterProcess();
//		p1.setUrl("{input-url}");
		
		// Locate the Select with all the chapters
		HttpFilterProcess p2 = new HttpFilterProcess();
		p2.setInput("<select id=\"chapterMenu\" name=\"chapterMenu\"><option value=\"/670-33181-1/kuroko-no-basket/chapter-1.html\">Chapter 1: I am Kuroko</option><option value=\"/670-33182-1/kuroko-no-basket/chapter-2.html\">Chapter 2: Monday at 840 on the Rooftop</option><option value=\"/670-33183-1/kuroko-no-basket/chapter-3.html\">Chapter 3: I am Serious</option><option value=\"/670-33184-1/kuroko-no-basket/chapter-4.html\">Chapter 4: He Might Not be a Decent One</option><option value=\"/670-33185-1/kuroko-no-basket/chapter-5.html\">Chapter 5: It's Not Just for Show</option><option value=\"/670-33186-1/kuroko-no-basket/chapter-6.html\">Chapter 6: It's Pefect If I Can't Win</option><option value=\"/670-33187-1/kuroko-no-basket/chapter-7.html\">Chapter 7: I'm Counting on You for the Counterattack</option><option value=\"/670-33188-1/kuroko-no-basket/chapter-8.html\">Chapter 8: I am Going</option><option value=\"/670-33189-1/kuroko-no-basket/chapter-9.html\">Chapter 9: I Made a Promise</option><option value=\"/670-33190-1/kuroko-no-basket/chapter-10.html\">Chapter 10: Man Proposes, God Disposes</option><option value=\"/670-33191-1/kuroko-no-basket/chapter-11.html\">Chapter 11: Your Basketball</option><option value=\"/670-33192-1/kuroko-no-basket/chapter-12.html\">Chapter 12: Bring It Back</option><option value=\"/670-33193-1/kuroko-no-basket/chapter-13.html\">Chapter 13: Let's Go</option><option value=\"/670-33194-1/kuroko-no-basket/chapter-14.html\">Chapter 14: I'll Tell You Two Things</option><option value=\"/670-33195-1/kuroko-no-basket/chapter-15.html\">Chapter 15: Definitely Strong</option><option value=\"/670-33196-1/kuroko-no-basket/chapter-16.html\">Chapter 16: We'll See Something Incredible</option><option value=\"/670-33197-1/kuroko-no-basket/chapter-17.html\">Chapter 17: Doesn't It Fire You Up</option><option value=\"/670-33198-1/kuroko-no-basket/chapter-18.html\">Chapter 18: Let's Give It a Look</option><option value=\"/670-33199-1/kuroko-no-basket/chapter-19.html\">Chapter 19: It Made Me Realize Again</option><option value=\"/670-33200-1/kuroko-no-basket/chapter-20.html\">Chapter 20: It Will Be All Right</option><option value=\"/670-33201-1/kuroko-no-basket/chapter-21.html\">Chapter 21: We Overcame It</option><option value=\"/670-33202-1/kuroko-no-basket/chapter-22.html\">Chapter 22: Don't Worry</option><option value=\"/670-33203-1/kuroko-no-basket/chapter-23.html\">Chapter 23: In Order to Win</option><option value=\"/670-33204-1/kuroko-no-basket/chapter-24.html\">Chapter 24: It Even Died for that Sake</option><option value=\"/670-33205-1/kuroko-no-basket/chapter-25.html\">Chapter 25: Well Then</option><option value=\"/670-33206-1/kuroko-no-basket/chapter-26.html\">Chapter 26: It Would be a Problem</option><option value=\"/670-33207-1/kuroko-no-basket/chapter-27.html\">Chapter 27: Quarter He's A Veteran</option><option value=\"/670-33208-1/kuroko-no-basket/chapter-28.html\">Chapter 28: </option><option value=\"/670-33209-1/kuroko-no-basket/chapter-29.html\">Chapter 29: </option><option value=\"/670-44025-1/kuroko-no-basket/chapter-30.html\">Chapter 30: I Am Leo</option><option value=\"/670-44026-1/kuroko-no-basket/chapter-31.html\">Chapter 31: I'll Win!</option><option value=\"/670-44094-1/kuroko-no-basket/chapter-32.html\">Chapter 32: </option><option value=\"/670-44163-1/kuroko-no-basket/chapter-33.html\">Chapter 33: Charge!</option><option value=\"/670-44184-1/kuroko-no-basket/chapter-34.html\">Chapter 34: That Is What I Mean By Man Proposes</option><option value=\"/670-44218-1/kuroko-no-basket/chapter-35.html\">Chapter 35: I Believed in You!</option><option value=\"/670-44271-1/kuroko-no-basket/chapter-36.html\">Chapter 36: Lets...Compete Again</option><option value=\"/670-44522-1/kuroko-no-basket/chapter-37.html\">Chapter 37: Idiots Dont Win!</option><option value=\"/670-44775-1/kuroko-no-basket/chapter-38.html\">Chapter 38: I Have Come!</option><option value=\"/670-47621-1/kuroko-no-basket/chapter-39.html\">Chapter 39: They're Just Alike</option><option value=\"/670-47953-1/kuroko-no-basket/chapter-40.html\">Chapter 40: Don't Make Me Laugh</option><option value=\"/670-48453-1/kuroko-no-basket/chapter-41.html\">Chapter 41: </option><option value=\"/670-48682-1/kuroko-no-basket/chapter-42.html\">Chapter 42: </option><option value=\"/670-48683-1/kuroko-no-basket/chapter-43.html\">Chapter 43: I've Got It</option><option value=\"/670-48684-1/kuroko-no-basket/chapter-44.html\">Chapter 44: This Won't Work!!</option><option value=\"/670-48874-1/kuroko-no-basket/chapter-45.html\">Chapter 45: Well, Let's Start</option><option value=\"/670-49217-1/kuroko-no-basket/chapter-46.html\">Chapter 46: Not Bad!!</option><option value=\"/670-49218-1/kuroko-no-basket/chapter-47.html\">Chapter 47: We're Counting on You</option><option value=\"/670-49997-1/kuroko-no-basket/chapter-48.html\">Chapter 48: Bunch of Losers</option><option value=\"/670-50003-1/kuroko-no-basket/chapter-49.html\">Chapter 49: Let's Finish This</option><option value=\"/670-50004-1/kuroko-no-basket/chapter-50.html\">Chapter 50: Your Basketball</option><option value=\"/670-50432-1/kuroko-no-basket/chapter-51.html\">Chapter 51: No Way!!</option><option value=\"/670-50433-1/kuroko-no-basket/chapter-52.html\">Chapter 52: A new Challenge</option><option value=\"/kuroko-no-basket/53\">Chapter 53: </option><option value=\"/kuroko-no-basket/54\">Chapter 54: That's Why I Don't Like Him</option><option value=\"/kuroko-no-basket/55\">Chapter 55: That's Talent</option><option value=\"/kuroko-no-basket/56\">Chapter 56: Abandoning</option><option value=\"/kuroko-no-basket/57\">Chapter 57: It's Not Want</option><option value=\"/kuroko-no-basket/58\">Chapter 58: Leave It To Me</option><option value=\"/kuroko-no-basket/59\">Chapter 59: Let's Begin</option><option value=\"/kuroko-no-basket/60\">Chapter 60: Dont MAke Me Laugh</option><option value=\"/kuroko-no-basket/61\">Chapter 61: Try Jumping</option><option value=\"/kuroko-no-basket/62\">Chapter 62: I Know What I Have To Do!!!</option><option value=\"/kuroko-no-basket/63\">Chapter 63: I'll Win ,Even If It Kills Me</option><option value=\"/kuroko-no-basket/64\">Chapter 64: Still Immature</option><option value=\"/kuroko-no-basket/65\">Chapter 65: Who Do You Think?</option><option value=\"/kuroko-no-basket/66\">Chapter 66: Here's Small Warning</option><option value=\"/kuroko-no-basket/67\">Chapter 67: I Quit</option><option value=\"/kuroko-no-basket/68\">Chapter 68: I Don't Think It's A Strange</option><option value=\"/kuroko-no-basket/69\">Chapter 69: What's Gonna Happen</option><option value=\"/kuroko-no-basket/70\">Chapter 70: Don't Misjudge Me!</option><option value=\"/kuroko-no-basket/71\">Chapter 71: They're Monsters</option><option value=\"/kuroko-no-basket/72\">Chapter 72: Don't Talk Like You Can Win!</option><option value=\"/kuroko-no-basket/73\">Chapter 73: Make Him Pay</option><option value=\"/kuroko-no-basket/74\">Chapter 74: I've Got This Here</option><option value=\"/kuroko-no-basket/75\">Chapter 75: Never Expected To See You Here</option><option value=\"/kuroko-no-basket/76\">Chapter 76: I'll Say I'm His Big Brother</option><option value=\"/kuroko-no-basket/77\">Chapter 77: I've Made Up My Mind</option><option value=\"/kuroko-no-basket/78\">Chapter 78: Be My Opponent!</option><option value=\"/kuroko-no-basket/79\">Chapter 79: At Winter Cup!</option><option value=\"/kuroko-no-basket/80\">Chapter 80: Please Take A Look</option><option value=\"/kuroko-no-basket/81\">Chapter 81: Engine On!!</option><option value=\"/kuroko-no-basket/82\">Chapter 82: Let's Go Have Fun</option><option value=\"/kuroko-no-basket/83\">Chapter 83: Declaration Of War, Please</option><option value=\"/kuroko-no-basket/84\">Chapter 84: We'Re Finally Here</option><option value=\"/kuroko-no-basket/85\">Chapter 85: Once is Enough</option><option value=\"/kuroko-no-basket/86\">Chapter 86: There's Only One Answer</option><option value=\"/kuroko-no-basket/87\">Chapter 87: This Is Much Worse Than Just Tough</option><option value=\"/kuroko-no-basket/88\">Chapter 88: Run!!</option><option value=\"/kuroko-no-basket/89\">Chapter 89: I've Been Waiting</option><option value=\"/kuroko-no-basket/90\">Chapter 90: It's Seirin Highschool Basketball Team!!</option><option value=\"/kuroko-no-basket/91\">Chapter 91: I Surpassed It Ages Ago</option><option value=\"/kuroko-no-basket/92\">Chapter 92: Time's Up</option><option value=\"/kuroko-no-basket/93\">Chapter 93: That's Exactly What I Want</option><option value=\"/kuroko-no-basket/94\">Chapter 94: It's Time For A Cleanup!</option><option value=\"/kuroko-no-basket/95\">Chapter 95: Then Let's Make One</option><option value=\"/kuroko-no-basket/96\">Chapter 96: Give Up</option><option value=\"/kuroko-no-basket/97\">Chapter 97: It's Seirin High School's Bastekball Team!!</option><option value=\"/kuroko-no-basket/98\">Chapter 98: Thank God  I Met You Guys</option><option value=\"/kuroko-no-basket/99\">Chapter 99: I'll Back Soon</option><option value=\"/kuroko-no-basket/100\">Chapter 100: There's No Way We Wouldn't Be Fired Up</option><option value=\"/kuroko-no-basket/101\">Chapter 101: I'll Definitely Beat You!!</option><option value=\"/kuroko-no-basket/102\">Chapter 102: That's Why I Decided</option><option value=\"/kuroko-no-basket/103\">Chapter 103: You'll Lose</option><option value=\"/kuroko-no-basket/104\">Chapter 104: That's The Trap</option><option value=\"/kuroko-no-basket/105\">Chapter 105: It's Trust</option><option value=\"/kuroko-no-basket/106\">Chapter 106: I Just Get That Feeling</option><option value=\"/kuroko-no-basket/107\">Chapter 107: Stop Messing Around</option><option value=\"/kuroko-no-basket/108\">Chapter 108: I'm Tired Of Waiting</option><option value=\"/kuroko-no-basket/109\">Chapter 109: It's Been A While</option><option value=\"/kuroko-no-basket/110\">Chapter 110: Please Take Good Care Of Us!!</option><option value=\"/kuroko-no-basket/111\">Chapter 111: We'll Dive Right In</option><option value=\"/kuroko-no-basket/112\">Chapter 112: We'll Commence The Winter Cup!</option><option value=\"/kuroko-no-basket/113\">Chapter 113: I Kept You Waiting</option><option value=\"/kuroko-no-basket/114\">Chapter 114: This Time, We Definitely....</option><option value=\"/kuroko-no-basket/115\">Chapter 115: Take The Lead!!</option><option value=\"/kuroko-no-basket/116\">Chapter 116: I'll Be Disappointed!</option><option value=\"/kuroko-no-basket/117\">Chapter 117: Only When He's at His Very Best!!</option><option value=\"/kuroko-no-basket/118\">Chapter 118: He Really Hates To Lose</option><option value=\"/kuroko-no-basket/119\">Chapter 119: Just What I Was Thinking</option><option value=\"/kuroko-no-basket/120\">Chapter 120: Useless Effort</option><option value=\"/kuroko-no-basket/121\">Chapter 121: This Time, I'll</option><option value=\"/kuroko-no-basket/122\">Chapter 122: Can You Leave It To Me?</option><option value=\"/kuroko-no-basket/123\">Chapter 123: He's Beside Himself With Joy</option><option value=\"/kuroko-no-basket/124\">Chapter 124: You'll Get Cold</option><option value=\"/kuroko-no-basket/125\">Chapter 125: It's An Upset!!</option><option value=\"/kuroko-no-basket/126\">Chapter 126: Let's Be Friendly</option><option value=\"/kuroko-no-basket/127\">Chapter 127: They're Out Of Options</option><option value=\"/kuroko-no-basket/128\">Chapter 128: We Win Now</option><option value=\"/kuroko-no-basket/129\">Chapter 129: It's Better Than Losing Here</option><option value=\"/kuroko-no-basket/130\">Chapter 130: Point Difference!</option><option value=\"/kuroko-no-basket/131\">Chapter 131: I Am Confident</option><option value=\"/kuroko-no-basket/132\">Chapter 132: Stopping Aomine is Impossible</option><option value=\"/kuroko-no-basket/133\">Chapter 133: Thank You</option><option value=\"/kuroko-no-basket/134\">Chapter 134: Still, I'm The Strongest</option><option value=\"/kuroko-no-basket/135\">Chapter 135: Because I Believe In You</option><option value=\"/kuroko-no-basket/136\">Chapter 136: We'll Absolutely Win!!</option><option value=\"/kuroko-no-basket/137\">Chapter 137: We Won't Loose..!!</option><option value=\"/kuroko-no-basket/138\">Chapter 138: I Believed</option><option value=\"/kuroko-no-basket/139\">Chapter 139: Time's up!!</option><option value=\"/kuroko-no-basket/140\">Chapter 140: We Really Did It</option><option value=\"/kuroko-no-basket/141\">Chapter 141: Nice to meet you (Mucho Gusto)</option><option value=\"/kuroko-no-basket/142\">Chapter 142: Please Teach Me</option><option value=\"/kuroko-no-basket/143\">Chapter 143: It Is Obviously Not A Light</option><option value=\"/kuroko-no-basket/144\">Chapter 144: It's Fun, Isn't It</option><option value=\"/kuroko-no-basket/145\">Chapter 145: Begin The Match</option><option value=\"/kuroko-no-basket/147\">Chapter 147: I Will Defend</option><option value=\"/kuroko-no-basket/148\">Chapter 148: First Basket</option><option value=\"/kuroko-no-basket/149\">Chapter 149: You Can't Cover That Area Not Even With A Toothpick</option><option value=\"/kuroko-no-basket/150\">Chapter 150: Leave It To Me!!</option><option value=\"/kuroko-no-basket/151\">Chapter 151: I Want To Crush Them Back!</option><option value=\"/kuroko-no-basket/152\">Chapter 152: </option><option value=\"/kuroko-no-basket/153\">Chapter 153: It's Obvious</option><option value=\"/kuroko-no-basket/154\">Chapter 154: I Get It!</option><option value=\"/kuroko-no-basket/155\">Chapter 155: I'll Move Them Back!!</option><option value=\"/kuroko-no-basket/156\">Chapter 156: Common Trash</option><option value=\"/kuroko-no-basket/157\">Chapter 157: Please Win</option><option value=\"/kuroko-no-basket/158\">Chapter 158: I Dont Want To Lose</option><option value=\"/kuroko-no-basket/159\">Chapter 159: Where Did He Go!</option><option value=\"/kuroko-no-basket/160\">Chapter 160: I'm Glad To Be Doing It</option><option value=\"/kuroko-no-basket/161\">Chapter 161: You Are Underestimating Me!</option><option value=\"/kuroko-no-basket/162\">Chapter 162: Who Are You?!</option><option value=\"/kuroko-no-basket/163\">Chapter 163: The Light of Seirin</option><option value=\"/kuroko-no-basket/164\">Chapter 164: I Want to See</option><option value=\"/kuroko-no-basket/165\">Chapter 165: I Don't Care Anymore</option><option value=\"/kuroko-no-basket/166\">Chapter 166: To Win!</option><option value=\"/kuroko-no-basket/167\">Chapter 167: it's a do or die!!</option><option value=\"/kuroko-no-basket/168\">Chapter 168: It's Over!!</option><option value=\"/kuroko-no-basket/169\">Chapter 169: So, That's Just How it is</option><option value=\"/kuroko-no-basket/170\">Chapter 170: I'm Just Killing Time</option><option value=\"/kuroko-no-basket/171\">Chapter 171: It's Mine Now</option><option value=\"/kuroko-no-basket/172\">Chapter 172: Do You Get In My Way!</option><option value=\"/kuroko-no-basket/173\">Chapter 173: Give Up</option><option value=\"/kuroko-no-basket/174\">Chapter 174: Fine, IiLL Take It</option><option value=\"/kuroko-no-basket/175\">Chapter 175: I'll Teach You</option><option value=\"/kuroko-no-basket/176\">Chapter 176: That's All It Is</option><option value=\"/kuroko-no-basket/177\">Chapter 177: I Don't Know</option><option value=\"/kuroko-no-basket/178\">Chapter 178: You'll Find Out Soon Enough</option><option value=\"/kuroko-no-basket/179\">Chapter 179: He Has Not Given Up</option><option value=\"/kuroko-no-basket/180\">Chapter 180: They’re Similar To You Guys</option><option value=\"/kuroko-no-basket/181\">Chapter 181: I'll Give Them To You</option><option value=\"/kuroko-no-basket/182\">Chapter 182: You Won't Reach</option><option value=\"/kuroko-no-basket/183\">Chapter 183: Now Let's Go</option><option value=\"/kuroko-no-basket/184\">Chapter 184: Strike First For Certain Victory</option><option value=\"/kuroko-no-basket/185\">Chapter 185: I Can't Help But Laugh</option><option value=\"/kuroko-no-basket/186\">Chapter 186: It's Your Time To Shine</option><option value=\"/kuroko-no-basket/187\">Chapter 187: You Have Teammates, You know</option><option value=\"/kuroko-no-basket/188\">Chapter 188: It's Pointless To Say Anything</option><option value=\"/kuroko-no-basket/189\">Chapter 189: The True Light</option><option value=\"/kuroko-no-basket/190\">Chapter 190: That's Why I'll Go All Out</option><option value=\"/kuroko-no-basket/191\">Chapter 191: You're Completely Visible</option><option value=\"/kuroko-no-basket/192\">Chapter 192: Got Any Complaints?</option><option value=\"/kuroko-no-basket/193\">Chapter 193: Don't Underestimate Us!!</option><option value=\"/kuroko-no-basket/194\">Chapter 194: In Order To Win</option><option value=\"/kuroko-no-basket/195\">Chapter 195: It's The Climax</option><option value=\"/kuroko-no-basket/196\">Chapter 196: We Can't Let Our Guard Down For A Second</option><option value=\"/kuroko-no-basket/197\">Chapter 197: Swallow It</option><option value=\"/kuroko-no-basket/198\">Chapter 198: I'll Make it For Sure</option><option value=\"/kuroko-no-basket/199\">Chapter 199: The Most Difficult</option><option value=\"/kuroko-no-basket/200\">Chapter 200: The Solution</option><option value=\"/kuroko-no-basket/201\">Chapter 201: It is Intended</option><option value=\"/kuroko-no-basket/202\">Chapter 202: It was never thought</option><option value=\"/kuroko-no-basket/203\">Chapter 203: Most Valuable Player</option><option value=\"/kuroko-no-basket/204\">Chapter 204: A Day Of Clear Blue Skies</option><option value=\"/kuroko-no-basket/205\">Chapter 205: </option><option value=\"/kuroko-no-basket/206\">Chapter 206: ASAP</option><option value=\"/kuroko-no-basket/207\">Chapter 207: Welcome</option><option value=\"/kuroko-no-basket/208\">Chapter 208: I'm Alright</option><option value=\"/kuroko-no-basket/209\">Chapter 209: You Can Do it</option><option value=\"/kuroko-no-basket/210\">Chapter 210: I Know</option><option value=\"/kuroko-no-basket/211\">Chapter 211: Goodbye</option><option value=\"/kuroko-no-basket/212\">Chapter 212: Like Hell I'll Lose</option><option value=\"/kuroko-no-basket/213\">Chapter 213: Shishi Hakuto</option><option value=\"/kuroko-no-basket/214\">Chapter 214: I'm Looking Forward to It!</option><option value=\"/kuroko-no-basket/215\">Chapter 215: Let's Do Our Best</option><option value=\"/kuroko-no-basket/216\">Chapter 216: ..Sorry</option><option value=\"/kuroko-no-basket/217\">Chapter 217: Come</option><option value=\"/kuroko-no-basket/218\">Chapter 218: Even If It's Just For Now</option><option value=\"/kuroko-no-basket/219\">Chapter 219: Thank You Very Much</option><option value=\"/kuroko-no-basket/220\">Chapter 220: I've Forgotten</option><option value=\"/kuroko-no-basket/221\" selected=\"selected\">Chapter 221: ...Tetsuya</option><option value=\"/kuroko-no-basket/222\">Chapter 222: We Should No Longer</option><option value=\"/kuroko-no-basket/223\">Chapter 223: I'm Really Terrible</option><option value=\"/kuroko-no-basket/224\">Chapter 224: </option><option value=\"/kuroko-no-basket/225\">Chapter 225: What Business Do You Have?</option></select>");
		p2.setTag("select");
		p2.setId("chapterMenu");
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
		p4b.setSearch("Chapter ([0-9]+)");
		
		BetweenRangeFilterProcess p4c = new BetweenRangeFilterProcess();
		p4c.setFrom("221");
		p4c.setTo("226");
		p4c.setValue("{chapter}");
		
		// Load the page with the chapter
		HttpGetterProcess p5 = new HttpGetterProcess();
		p5.setUrl("{domain}{chapter-url}");
		
		HttpFilterProcess p6 = new HttpFilterProcess();
		p6.setTag("select");
		p6.setId("pageMenu");
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
		
		HttpGetterProcess p9 = new HttpGetterProcess();
		p9.setUrl("{domain}{page-url}");
		
		HttpFilterProcess p10 = new HttpFilterProcess();
		p10.setTag("img");
		p10.setId("img");
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
		p14.setLocation("{payload}");
		p14.setSaveTo("target/{manga}/{4#chapter}/{manga}-{4#chapter}-{2#page}.{img-extension}");
		
//		GeneralContextVarProcess pa = new GeneralContextVarProcess();
//		pa.setName("chapter-from");
//		pa.setValue("571");
//
//		GeneralContextVarProcess pb = new GeneralContextVarProcess();
//		pb.setName("chapter-to");
//		pb.setValue("634");

//		StringMatcherProcess p0000 = new StringMatcherProcess();
//		p0000.setInput("{input-url}");
//		p0000.setSearch("^([.]+)/\\d+");
//		p0000.setSearchGroup(1);
//		p0000.setContextName("base-url");
//		p0000.setReplaceContextVarsInput(true);
//
//		// Get the input url and extract the base path for images
//		HttpGetterProcess p1 = new HttpGetterProcess();
//		p1.setUrl("{input-url}");
//
//		// STAGE 1 - Iterate through all the desired chapters or all
//		HttpFilterProcess p2 = new HttpFilterProcess();
//		p2.setTag("select");
//		p2.setName("chapterMenu");
//		// FIXME: Change it to all
//		p2.setFindMode(FindMode.FIRST);
//
//		// Search the base URL to request for a new chapter
////		StringMatcherProcess p2b = new StringMatcherProcess();
////		p2b.setSearch("change_chapter\\('([^']+)'");
////		p2b.setSearchGroup(1);
////		p2b.setContextName("base-url");
//
//		// Iterate from chapter A to B (A and B inclusive)
//		IteratorProcess p3 = new IteratorProcess();
//		p3.setValueFrom("{chapter-from}");
//		p3.setValueTo("{chapter-to}");
//		p3.setContextName("chapter");
//
//		// Look for the <option> containing the chapter
//		HttpFilterProcess p4 = new HttpFilterProcess();
//		p4.setTag("option");
//		p4.setValue("{chapter}");
//		p4.setFindMode(FindMode.FIRST);
//
//		StringMatcherProcess p5 = new StringMatcherProcess();
//		p5.setSearch("value=\"([\\d]+)");
//		p5.setSearchGroup(1);
//		p5.setContextName("chapter-id");
//
//		// STAGE 2 - Get all the pages that the chapter has
//		HttpGetterProcess p6 = new HttpGetterProcess();
//		p6.setUrl("{domain}{base-url}{chapter-id}/1");
//
//		HttpFilterProcess p7 = new HttpFilterProcess();
//		p7.setTag("Select");
//		p7.setName("page");
//		p7.setFindMode(FindMode.ALL);
//
//		StringMatcherProcess p8 = new StringMatcherProcess();
//		p8.setSearch("value=\"([\\d]+)\"");
//		p8.setSearchGroup(1);
//		p8.setContextName("page");
//		p8.setFindMode(FindMode.ALL);
//
//		// STAGE 3 - For each chapter locate the URL base for the images (all the URLs are the same
//		// only that the page number changes)
//		HttpGetterProcess p9 = new HttpGetterProcess();
//		p9.setUrl("{domain}{base-url}{chapter-id}/{page}");
//
//		HttpFilterProcess p10 = new HttpFilterProcess();
//		p10.setTag("div");
//		p10.setClazz("current_page");
//
//		HttpFilterProcess p11 = new HttpFilterProcess();
//		p11.setTag("img");
//
//		StringMatcherProcess p12 = new StringMatcherProcess();
//		p12.setSearch("src=\"([^\"]+)");
//		p12.setSearchGroup(1);
//		p12.setReplacePayload(true);
//
//		StringMatcherProcess p13 = new StringMatcherProcess();
//		p13.setSearch("\\.([\\w]+)$");
//		p13.setSearchGroup(1);
//		p13.setContextName("img-extension");
//
//		// STAGE 4 - Add download
//		ImageProcess p14 = new ImageProcess();
//		p14.setLocation("{payload}");
//		p14.setSaveTo("target/{manga}/{4#chapter}/{manga}-{4#chapter}-{2#page}.{img-extension}");

		// STAGE 5 - Add the process to the Task (this is maded automatically when the marshall
		// process is runned)
		List<Process> plist = new LinkedList<Process>();
		plist.add(p0);
		plist.add(p00);
		plist.add(p000);
//		plist.add(p1);
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
		t.setNumberOfThreads(NUMBER_OF_THREADS);

		t.execute();
	}
}
