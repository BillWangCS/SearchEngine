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
	public  void writeWebPage() throws IOException {
		String path = "d:\\search";
		File dir=new File(path);
		dir.mkdir();
		File file = new File(path, "documents.datafile");
		file.createNewFile();
		FileOutputStream fileout = new FileOutputStream(file);	
			while (webpages.hasNext()) {
				Document document = webpages.nextDocument();							
				fileout.write("url:".getBytes());
				fileout.write(document.getStore_attriubte("url").getBytes());
				fileout.write("\n".getBytes());
				fileout.write("ID:".getBytes());
				fileout.write(String.valueOf(document.getID()).getBytes());
				fileout.write("\n".getBytes());
				
				//
				fileout.write("title:".getBytes());				
				if(document.getIndex_attribute("title")!=null){
					fileout.write(document.getIndex_attribute("title").getBytes());
				}
				else{
					fileout.write("null".getBytes());
				}
				fileout.write("\n".getBytes());
			//дkeyword
				fileout.write("keywords".getBytes());
				if(document.getIndex_attribute("keywords")!=null){
					fileout.write(document.getIndex_count("keywords"));
				}			
				else{
					fileout.write("null".getBytes());
				}
				fileout.write("\n".getBytes());
				
				//
				fileout.write("description:".getBytes());
				if(document.getIndex_attribute("description")!=null){
					fileout.write(document.getIndex_attribute("description").getBytes());					
				}
				else{
					fileout.write("null".getBytes());
				}
				fileout.write("\n".getBytes());
				fileout.write("----------------------------------------".getBytes());
			}
			fileout.close();
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
		crawlurl.setLayer(0);
		initurl[0]=crawlurl;
		String rootdir = "d:\\spider";
		
		CrawlWebCentralThread.rootdir = rootdir;
		webcontrol.Init(initurl);
		//д�ĵ��߳�
		for(int i=0;i<5;i++){
			SaveDocumentThread saveDocument=new SaveDocumentThread();
			saveDocument.start();
		}
		
		DocumentSyncThread documentSync=new DocumentSyncThread();
		documentSync.start();
		//ͬ���ĵ��߳�
		
		
		/*
		 * �����߳���������
		 */
		while (true) {
			// �����̵߳Ĳ����ǵ�û�з����б��е�url����100*�̳߳����߳��Ǵ����̣߳������߳������ܳ���15��
			logger.info("httpconnectpoolsize:"+webcontrol.httpconnectpool.getPOOLSIZE());
			logger.info("δ���ʵ�url��"+BreadthFirstTraversal.getUNVisitedURL_Size());
			logger.info("����߳���:"+webcontrol.pool.getActiveCount());
//			logger.info("��д��Document�ĸ���Ϊ:"+CrawlWebCentralThread.webpages.getSize());
			if (BreadthFirstTraversal.getUNVisitedURL_Size()> 100 * webcontrol.pool
					.getActiveCount()
					&& webcontrol.pool.getActiveCount() < 10) {

				CrawlUrl new_crawlurl = BreadthFirstTraversal
						.getUNVisitedURL();

				webcontrol.pool.execute(new CrawlThread(new_crawlurl));
				logger.info("Active Thread :"
						+ webcontrol.pool.getActiveCount());
			}
			
			//��󽫵õ�documentsд���ļ���
			
			try {
				logger.info("���߳�˯��1s");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
