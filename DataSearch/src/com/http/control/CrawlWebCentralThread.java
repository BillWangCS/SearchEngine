package com.http.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.http.Search.BreadthFirstTraversal;
import com.http.Search.CrawlUrl;
import com.http.Search.WebPage;
import com.http.connect.HttpConnectPool;
import com.search.data.Document;

/*
 * 
 */
public class CrawlWebCentralThread {
	
	private static Logger logger = LogManager.getLogger(CrawlWebCentralThread.class
			.getName());
	private Marker Web_Central = MarkerManager.getMarker("WEB_CENTRAL");
	public HttpConnectPool httpconnectpool;
	//Document �����
	public static int document_id=1;
	//�����̳߳�
	private CrawlThreadPool pool;
	public static String rootdir;
	//�ļ�����
	public static long filenumber = 0;
	public static WebPage webpages=new WebPage();

	public synchronized static void addWebPage(Document document) {
		CrawlWebCentralThread.webpages.addDocument(document);
	}

	//��Documentд�����ݿ�
	public static void writeWebPage() {
		try {
			String path = "d:\\search";
			while (webpages.hasNext()) {
				Document document = webpages.nextDocument();
				File file = new File(path, "datafile" + document.getID());
				FileOutputStream fileout = new FileOutputStream(file);			
				fileout.write("url:".getBytes());
				fileout.write(String.valueOf(document.getID()).getBytes());
				fileout.write("\n".getBytes());
				fileout.write("keyword".getBytes());
//				fileout.write(document.getDate().getBytes());
//				fileout.write("\n".getBytes());
//				fileout.write("title".getBytes());
//				fileout.write(document.getTitle().getBytes());
//				fileout.write("\n".getBytes());
				fileout.close();
				filenumber++;
			}
		} catch (IOException e) {
			System.out.println("writefile default!");
		}
	}

	protected CrawlWebCentralThread() {
		logger.info(Web_Central, "Spider Start running...");
	}

	//��ʼ������֩��������
	public void Init(CrawlUrl[] initurl) throws SQLException, Exception {
		if (initurl == null || rootdir == null) {
			logger.error("initurl or rootdir null");
			System.exit(0);
		}
		//��ʼ��http���ӳ�
		httpconnectpool = HttpConnectPool.GetHttpConnectPool(20);
		//��ʼ��������
		BreadthFirstTraversal traversal = BreadthFirstTraversal
				.getBreadthFirstTraversal(initurl);
		//��ʼ�������̳߳�
		CrawlThreadPool pool = CrawlThreadPool.getCrawlthreadpool();
		this.pool = pool;
		traversal.setCrawlThreadPool(pool);
		
		try {
			//��ʼ����
			traversal.Traversal();
		} catch (Exception e) {
			logger.fatal(Web_Central, "BreadFirstTraversal occur exception");
			logger.error(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		CrawlWebCentralThread webcontrol = new CrawlWebCentralThread();
		CrawlUrl[] initurl = new CrawlUrl[1];
		String url = "http://www.suse.edu.cn";
		CrawlUrl crawlurl=new CrawlUrl();
		crawlurl.setOriUrl(url);
		crawlurl.setPriority(0);
		initurl[0]=crawlurl;
		String rootdir = "d:\\spider";
		
		CrawlWebCentralThread.rootdir = rootdir;
		webcontrol.Init(initurl);
		File file = new File("d:\\datafile");
		file.mkdir();
		
		/*
		 * �����߳���������
		 */
		while (true) {
			// �����̵߳Ĳ����ǵ�û�з����б��е�url����100*�̳߳����߳��Ǵ����̣߳������߳������ܳ���15��
			logger.debug("httpconnectpoolsize:"+webcontrol.httpconnectpool.getPOOLSIZE());
			System.out.println("δ���ʵģ�"+BreadthFirstTraversal.getUNVisitedURL_Size());
			System.out.println("����߳���:"+webcontrol.pool.getActiveCount());
//			if (BreadthFirstTraversal.getUNVisitedURL_Size()> 10 * webcontrol.pool
//					.getActiveCount()
//					&& webcontrol.pool.getActiveCount() < 25) {
//
//				CrawlUrl new_crawlurl = BreadthFirstTraversal
//						.getUNVisitedURL();
//
//				webcontrol.pool.execute(new CrawlThread(new_crawlurl));
//				logger.info("Active Thread :"
//						+ webcontrol.pool.getActiveCount());
//			}
//			CrawlWebCentralThread.writeWebPage();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
