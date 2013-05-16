package com.http.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.html.parser.DocumentParser;
import com.html.parser.HtmlLinkFilterChain;
import com.html.parser.HtmlParser;
import com.html.parser.SuseURLFilter;
import com.html.parser.URLFilterChain;
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
	private CrawlUrl crawlurl;
	private Logger logger = LogManager.getLogger("CrawlThread");

	public CrawlThread(CrawlUrl crawurl) {
		super();
		this.crawlurl=crawurl;
	}
	public CrawlUrl getCrawlurl() {
		return crawlurl;
	}

	public void setCrawlurl(CrawlUrl crawlurl) {
		this.crawlurl = crawlurl;
	}

	public void run() {
		
		HttpConnect httpconnect = null;
		while (crawlurl!= null) {
			try {
				logger.info("Task url:" + crawlurl.getOriUrl());
				logger.info("����:"+crawlurl.getLayer());
				logger.entry("httpconnect");
				// ��������
				httpconnect = HttpConnectPool.getHttpConnect();
				httpconnect.setCrawlUrl(crawlurl);
				// ��������
				httpconnect.Connect();
				httpconnect.printFields();
				//
				logger.exit("httpconnect");
				// �����ļ�
				
				FileDownload htmldownload=null;
				if(httpconnect.isDownload()){
					logger.entry("HtmlDownLoad");
					htmldownload= new FileDownload(
						httpconnect.getInputStream());
					htmldownload.setCrawlUrl(httpconnect.getCrawlurl());
					htmldownload.download();
					System.out.println("---------------------------");
					htmldownload.printFile();
					logger.exit("HtmlDownLoad");
				}
				
				//�������֮���ͷ�����
				httpconnect.releaseConnect();
				HttpConnectPool.releaseHttpConnect(httpconnect);
				logger.debug("Release Connect");
				// htmldownload.printFile();
				
				
				// �����ʹ��Ľڵ���ӵ�visited�б�
				
				
				// ����html�ļ�
				if(htmldownload!=null){
					logger.debug("parser:"+htmldownload.isParser());
					//�ж��ǲ��ǿ��Խ�����
					if (htmldownload.isParser()) {
						
						logger.entry("HtmlParser");
						DocumentParser htmlparser = new HtmlParser(htmldownload.getFile(),htmldownload.getCrawlUrl());
										
						// ������������URL��ӽ�todo�б�
						HtmlParser parser=(HtmlParser)htmlparser;
						parser.registerLinkFilterChain(new HtmlLinkFilterChain());
						
						//ע��url������
						URLFilterChain chain=new URLFilterChain();
						chain.addFilter(new SuseURLFilter());
						parser.registerURLFilter(chain);
						parser.parser();
						logger.exit("HtmlParser");
						
						// �����������
					}
				}
								
				// ȡ����һ�����ȼ���ߵ�url����ӵ�visitedurl��
				CrawlUrl new_crawlurl= BreadthFirstTraversal
						.getUNVisitedURL();
								
				if (new_crawlurl!=null) {
					crawlurl=new_crawlurl;
				} else {
					crawlurl = null;
				}
				logger.info("�µ�CrawlUrl:"+crawlurl.getOriUrl());
				logger.info("----------------------------------------------");

			} catch (Exception e) {
				//���û��http������˯�ߵȴ�100ms
				if (e instanceof NOHttpConnectException) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						logger.fatal("fatal error! Thread Sleep default!");
						logger.info("------------------------------------------");
					}
					continue;
				}
				//�ͷ����ӣ�ȡ���µ�url
				HttpConnectPool.releaseHttpConnect(httpconnect);
				CrawlUrl new_crawlurl = BreadthFirstTraversal
						.getUNVisitedURL();
				if (new_crawlurl!= null) {
					crawlurl=new_crawlurl;
				} else {
					crawlurl = null;
				}
				logger.info("�µ�CrawlUrl:"+crawlurl.getOriUrl());
				logger.fatal("Connect Default! or Download default!");
				logger.info("-----------------------------------------------");
			}
		}
	}

	
}
