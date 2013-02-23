package com.http.Search;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.http.control.CrawlThread;

public class BreadthFirstTraversal {
	// private String rootdir;// ������������ļ��ĸ�Ŀ¼
	private static VisitedURL visitedUrl;
	private static UnvisitedURL unvisitedUrl;
	private Logger logger = LogManager.getLogger("BreadthFirstTraversal");

	// ��ʼ��

	public static BreadthFirstTraversal getBreadthFirstTraversal(
			CrawlUrl[] initurl) throws SQLException, Exception {
		return new BreadthFirstTraversal(initurl);
	}

	private BreadthFirstTraversal(CrawlUrl[] initurl) throws SQLException,
			Exception {
		if (initurl == null) {
			logger.fatal("No InitSeedUrl procedure exit...");
			System.exit(0);
		} else {
			visitedUrl = new VisitedURL();
			unvisitedUrl = new UnvisitedURL();
			for (CrawlUrl url : initurl) {
				logger.info("seed: " + url);
				//
				unvisitedUrl.addURL(url);
			}
		}
	}

	// ���url��visitedurl,������ڷ���false,�����ڷ���true,������null
	public static synchronized boolean addURLVisited(CrawlUrl url) {
		try {
			boolean result=visitedUrl.addURL(url);
			return result;
		} catch (Exception e) {
			return (Boolean) null;
		}
	}

	// ��ӵ�unvisitedurl,��Ҫ���֪��������ʱ����
	public static synchronized void add_known_URLVisited(CrawlUrl url) {
		try {
			visitedUrl.addURL(url);
		} catch (Exception e) {
			
		}
	}

	// ��ӵ�unvisitedurl,���ڷ���false,���򷵻�true
	public static synchronized boolean addUNURLVisited(CrawlUrl url) {
		try {
			boolean result=unvisitedUrl.addURL(url);
			return result;
		} catch (Exception e) {
			return (Boolean) null;
		}
	}
	//û��û�б����ʵ�url���Ƿ���null
	public static synchronized CrawlUrl getUNVisitedURL() {
		CrawlUrl url = null;
		try {
			url = unvisitedUrl.getURL();
		} catch (Exception e) {
			
		}
		return url;
	}

	public static synchronized int getSizeVisited() {
		int size = -1;
		try {
			size = visitedUrl.getSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static synchronized int getSizeUNVisited() {
		int size = -1;
		try {
			size = unvisitedUrl.getSize();
		} catch (Exception e) {
			
		}
		return size;
	}

	// ��Ҫ�ķ������������������ִ�����棬��������߳�ͬʱץȡ��ҳ��
	public void Traversal(ThreadPoolExecutor pool) throws Exception {
		CrawlUrl crawlurl = BreadthFirstTraversal.getUNVisitedURL();
		ThreadPoolExecutor spiderpool = pool;
		if (crawlurl != null) {
			spiderpool.execute(new CrawlThread(crawlurl));
		}
	}
}
