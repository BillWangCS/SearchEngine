package com.http.Search;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.http.control.SpiderThread;

public class BreadthFirstTraversal {
	// private String rootdir;// ������������ļ��ĸ�Ŀ¼
	private static VisitedURL visitedUrl;
	private static UnvisitedURL unvisitedUrl;
	private Logger logger = LogManager.getLogger("BreadthFirstTraversal");

	// ��ʼ��

	public static BreadthFirstTraversal getBreadthFirstTraversal(
			String[] initurl) throws SQLException, Exception {
		return new BreadthFirstTraversal(initurl);
	}

	private BreadthFirstTraversal(String[] initurl) throws SQLException,
			Exception {
		if (initurl == null) {
			logger.fatal("No InitSeedUrl procedure exit...");
			System.exit(0);
		} else {
			visitedUrl = new VisitedURL();
			unvisitedUrl = new UnvisitedURL();
			for (String url : initurl) {
				logger.info("seed: " + url);
				unvisitedUrl.addURL(url);
			}
		}
	}

	// ���url��visitedurl,������ڷ���false,�����ڷ���true,������null
	public static synchronized boolean addURLVisited(String url) {
		try {
			boolean result=visitedUrl.addURL(url);
			return result;
		} catch (Exception e) {
			return (Boolean) null;
		}
	}

	// ��ӵ�unvisitedurl,��Ҫ���֪��������ʱ����
	public static synchronized void addURLVisited(String url, int priority) {
		try {
			visitedUrl.addURL(url, priority);
		} catch (Exception e) {
			
		}
	}

	// ��ӵ�unvisitedurl,���ڷ���false,���򷵻�true
	public static synchronized boolean addUNURLVisited(String url) {
		try {
			boolean result=unvisitedUrl.addURL(url);
			return result;
		} catch (Exception e) {
			return (Boolean) null;
		}
	}
	//û��û�б����ʵ�url���Ƿ���null
	public static synchronized HashMap<String, Integer> getUNVisitedURL() {
		HashMap<String, Integer> url = null;
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
		HashMap<String, Integer> map = BreadthFirstTraversal.getUNVisitedURL();
		String url = (String) map.keySet().toArray()[0];
		ThreadPoolExecutor spiderpool = pool;
		if (url != null) {
			spiderpool.execute(new SpiderThread(url, map.get(url)));
		}
	}
}
