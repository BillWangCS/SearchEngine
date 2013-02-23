package com.http.control;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.html.parser.DocumentParser;
import com.html.parser.HtmlLinkFilterChain;
import com.html.parser.HtmlParser;
import com.http.Search.BreadthFirstTraversal;
import com.http.Search.CrawlUrl;
import com.http.connect.FileDownload;
import com.http.connect.HttpConnect;
import com.http.connect.HttpConnectPool;
import com.http.connect.NOHttpConnectException;
/*
 * 
 */
public class CrawlThread extends Thread {
	private String url;
	private int priority;
	private CrawlUrl crawlurl;
	private String rootdir;
	private Logger logger = LogManager.getLogger("SpiderThread");
	private LinkedList<String> todoUrl;// ��Ҫ���ʵ�url�б�

	public String getRootdir() {
		return rootdir;
	}

	
	public CrawlThread(CrawlUrl crawurl) {
		super();
		this.rootdir = CrawlWebCentral.rootdir;
		this.crawlurl=crawurl;
		this.url=crawlurl.getOriUrl();
		this.priority=crawlurl.getPriority();
	}

	public LinkedList<String> getURL() {
		return this.todoUrl;
	}

	public void run() {
		logger.info("Task url:" + crawlurl.getOriUrl());
		HttpConnect httpconnect = null;
		while (crawlurl!= null) {
			try {
				logger.entry("httpconnect");
				// ��������
				httpconnect = HttpConnectPool.getHttpConnect();
				httpconnect.setUrl(url);
				// ��������
				httpconnect.Connect();
				httpconnect.printFields();
				logger.exit("httpconnect");
				// �����ļ�
				logger.entry("HtmlDownLoad");
				FileDownload htmldownload = new FileDownload(
						httpconnect.getHttpresponseheader(),
						httpconnect.getInputStream(), url, rootdir);
				htmldownload.download(url);
				logger.exit("HtmlDownLoad");
				//�������֮���ͷ�����
				httpconnect.releaseConnect();
				HttpConnectPool.releaseHttpConnect(httpconnect);
				logger.debug("Release Connect");
				// htmldownload.printFile();
				
				
				// �����ʹ��Ľڵ��콾��visited�б�
				BreadthFirstTraversal.addURLVisited(crawlurl);
				
				
				// ����html�ļ�
				logger.entry("HtmlParser");
				logger.debug(htmldownload.isParser());
				
				
				//�ж��ǲ��ǿ��Խ�����
				if (htmldownload.isParser()) {
					DocumentParser htmlparser = new HtmlParser();
					htmlparser.setUrl(url);
					
					
					// ������������URL��ӽ�todo�б�
					htmlparser
							.registerLinkFilterChain(new HtmlLinkFilterChain());
					todoUrl = htmlparser.parser(htmldownload.getFile(),
							htmldownload.getEncoding());

					
					logger.exit("HtmlParser");
					
					
					// �����������
					Iterator<String> iterator = todoUrl.iterator();
					while (iterator.hasNext()) {
						String url = iterator.next();
						CrawlUrl temp_crawlurl=new CrawlUrl();
						temp_crawlurl.setOriUrl(url);
						boolean isExist=BreadthFirstTraversal.addURLVisited(temp_crawlurl);
						//�������visited�У������ӵ�unvisitedurl
						if(isExist){
							BreadthFirstTraversal.addUNURLVisited(temp_crawlurl);
						}
						
					}
				}
				// ȡ����һ�����ȼ���ߵ�url����ӵ�visitedurl��
				CrawlUrl new_crawlurl= BreadthFirstTraversal
						.getUNVisitedURL();
				if (new_crawlurl!=null) {
					this.url = new_crawlurl.getOriUrl();
					this.priority = new_crawlurl.getPriority();
				} else {
					this.url = null;
				}

			} catch (Exception e) {
				//���û��http������˯�ߵȴ�100ms
				if (e instanceof NOHttpConnectException) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						logger.fatal("fatal error! Thread Sleep default!");
					}
					continue;
				}
				//�ͷ����ӣ�ȡ���µ�url
				HttpConnectPool.releaseHttpConnect(httpconnect);
				CrawlUrl crawlurl = BreadthFirstTraversal
						.getUNVisitedURL();
				if (crawlurl!= null) {
					url = crawlurl.getOriUrl();
					this.priority = crawlurl.getPriority();
				} else {
					url = null;
				}
				logger.fatal("Connect Default! or Download default!");
			}
		}
	}

	public CrawlUrl getCrawlurl() {
		return crawlurl;
	}

	public void setCrawlurl(CrawlUrl crawlurl) {
		this.crawlurl = crawlurl;
	}

}
