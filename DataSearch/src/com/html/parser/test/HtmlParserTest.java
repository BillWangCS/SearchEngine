/**
 * 
 */
package com.html.parser.test;


import java.io.File;
import java.util.LinkedList;

import org.junit.Ignore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.html.parser.HtmlLinkFilterChain;
import com.html.parser.HtmlParser;
import com.html.parser.SuseURLFilter;
import com.html.parser.URLFilterChain;
import com.http.Search.CrawlUrl;
import com.http.connect.FileDownload;
import com.http.connect.HttpConnect;
import com.search.data.Document;

/**
 * @author niubaisui
 *
 */
public class HtmlParserTest {

	/**
	 * @throws java.lang.Exception
	 */
	private static HtmlParser parser;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HttpConnect httpconnect=new HttpConnect();
		httpconnect.Connect();
		httpconnect.printFields();
		FileDownload filedownload=new FileDownload(httpconnect.getInputStream());
		filedownload.setCrawlUrl(httpconnect.getCrawlurl());
		filedownload.download();
		System.out.println(filedownload.getAbsolutePath());
		filedownload.printFile();
		
		System.out.println("isparser:"+filedownload.isParser());
		CrawlUrl crawlurl=filedownload.getCrawlUrl();
		crawlurl.setLayer(1);
		parser=new HtmlParser(filedownload.getFile(),crawlurl);
		parser.registerLinkFilterChain(new HtmlLinkFilterChain());
		URLFilterChain chain=new URLFilterChain();
		chain.addFilter(new SuseURLFilter());
		parser.registerURLFilter(chain);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	public void testMatcher_link(){
		String str="sview-7.aspx";
		boolean result=parser.matcher_link(str);
		System.out.println(result);
	}
	/**
	 * Test method for {@link com.html.parser.HtmlParser#parser()}.
	 * @throws Exception 
	 */
	@Test
	public void testParser() throws Exception {
		parser.parser();
		LinkedList<CrawlUrl> links=parser.getLinks();
		Document document=parser.getDocument();
		System.out.println(document.getID());
		System.out.println(document.getDate());
//		System.out.println(document.getAttribute("title"));
		System.out.println("---------------------------");
		for(CrawlUrl link:links){
			System.out.println(link.getOriUrl());
		}
	}

}
