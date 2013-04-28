package com.http.connect;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * 
 */
public class HttpConnectPool {
	private static int POOLSIZE;
	//���õ�httpconnecct��
	private static LinkedList<HttpConnect> AvailibleConnectList;
	//��ļ������õ�httpconnect��
	private static LinkedList<HttpConnect> ActiveConnectList;
	
	private static HttpConnectPool pool=null;
	private static Logger logger = LogManager.getLogger("HttpConnectPool");

	public synchronized  int getPOOLSIZE() {
		return POOLSIZE;
	}

	private synchronized static void setPOOLSIZE(boolean flag) {
		if (flag) {
			POOLSIZE = POOLSIZE + 1;
		} else {
			POOLSIZE = POOLSIZE - 1;
		}
	}

	private HttpConnectPool(int poolsize) {
		POOLSIZE=poolsize;
		AvailibleConnectList = new LinkedList<HttpConnect>();
		ActiveConnectList = new LinkedList<HttpConnect>();
		for (int i = 0; i < poolsize; i++) {
			AvailibleConnectList.add(new HttpConnect());// ����һ����url��httpconnect
		}
		
	}

	public synchronized  static HttpConnect getHttpConnect()
			throws NOHttpConnectException {

		if (AvailibleConnectList == null || AvailibleConnectList.size() == 0) {
			logger.info("pool no httpconnect��");
			throw new NOHttpConnectException();
		} else {
			HttpConnect httpconnect = AvailibleConnectList.pollFirst();
			ActiveConnectList.add(httpconnect);
			setPOOLSIZE(false);
			logger.info("new conncet:");
			return httpconnect;
		}
	}

	/*
	 * ��û���õ����ӷŻ����ӳ�
	 */
	public synchronized static void releaseHttpConnect(HttpConnect connect) {
		if (!ActiveConnectList.contains(connect)) {
			logger.error("release default! httpconnect had dead or isn't pool");
			return;
		}
		ActiveConnectList.remove(connect);
		AvailibleConnectList.add(connect);
		setPOOLSIZE(true);
	}

	/*
	 * ʵ�ֵ���ģʽ
	 */
	public synchronized static HttpConnectPool GetHttpConnectPool(int poolsize) {
		if (poolsize < 0) {
			logger.fatal("poolsize should be greater than 0!");
			System.exit(0);
		}
		if(pool==null){
			return pool=new HttpConnectPool(poolsize);
		}
		else{
			return pool;
		}
	}

}
