package com.http.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.html.parser.DocumentParser;
import com.html.parser.HtmlLinkFilterChain;
import com.html.parser.HtmlParser;
import com.http.Search.BreadthFirstTraversal;
import com.http.connect.FileDownload;
import com.http.connect.HttpConnect;
import com.http.connect.HttpConnectPool;
import com.http.connect.NOHttpConnectException;

public class SpiderThread extends Thread {
	private String url;
	private int priority;
	private String rootdir;
	private Logger logger = LogManager.getLogger("SpiderThread");
	private LinkedList<String> todoUrl;// ��Ҫ���ʵ�url�б�

	public String getRootdir() {
		return rootdir;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SpiderThread(String url, int priority) {
		super();
		this.url = url;
		this.rootdir = SpiderWebCentral.rootdir;
		this.priority = priority;
	}

	public LinkedList<String> getURL() {
		return this.todoUrl;
	}

	public void run() {
		logger.info("Task url:" + url);
		HttpConnect httpconnect = null;
		while (url != null) {
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
				BreadthFirstTraversal.addURLVisited(url);
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

					BreadthFirstTraversal.addURLVisited(url, priority);
					logger.exit("HtmlParser");
					// �����������
					Iterator<String> iterator = todoUrl.iterator();
					while (iterator.hasNext()) {
						String url = iterator.next();
						boolean isExist=BreadthFirstTraversal.addURLVisited(url);
						//�������visited�У������ӵ�unvisitedurl
						if(isExist){
							BreadthFirstTraversal.addUNURLVisited(url);
						}
						
					}
				}
				// ȡ����һ�����ȼ���ߵ�url����ӵ�visitedurl��
				HashMap<String, Integer> map = BreadthFirstTraversal
						.getUNVisitedURL();
				if (map != null) {
					this.url = (String) map.keySet().toArray()[0];
					this.priority = map.get(url);
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
				HashMap<String, Integer> map = BreadthFirstTraversal
						.getUNVisitedURL();
				if (map != null) {
					url = (String) map.keySet().toArray()[0];
					this.priority = map.get(url);
				} else {
					url = null;
				}
				logger.fatal("Connect Default! or Download default!");
			}
		}
	}

}
