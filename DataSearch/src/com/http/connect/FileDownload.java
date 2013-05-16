package com.http.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.http.Search.CrawlUrl;
import com.search.DAO.Connect;
/*
 * һ���÷���FileDownloadTest
 */
public class FileDownload {
	
	//����·����ʾΪrootdir+host+path
	private File file;
	private CrawlUrl crawlurl;
	private Logger logger = LogManager.getLogger("HtmlDownload");
	private String host;// ���� ��:www.baidu.com/index ��hostΪwww.baidu.com
	private String path;// Ŀ¼ ·�� �磺www.baidu.com/index/index.html ��default_filenameΪindex/index.html
	private String rootdir="d:\\spider";// ��Ŀ¼
	private String encoding="utf-8";// ����
	private InputStream in;

	// ���췽��
	public FileDownload( InputStream in) {
		this.in = in;
	}
	
	public FileDownload(InputStream in,CrawlUrl crawlurl){
		this.in=in;
		this.crawlurl=crawlurl;
	}
	
	
	public File getFile(){
		return file;
	}
	
	
	public void setCrawlUrl(CrawlUrl crawlurl){
		this.crawlurl=crawlurl;
	}
	
	public CrawlUrl getCrawlUrl(){
		return crawlurl;
	}
	
	public FileDownload(Connect connect){
		//undo
	}
	
	public String getAbsolutePath(){
		return rootdir+"/"+host+"/"+path;
	}

	public String getEncoding() {
		return encoding;
	}

	/*
	 * �õ��ĵ��ı���
	 */
	
	// ����URL result[2]=host result[4]=path
	public String[] parseURL() {
		SimpleHttpURLParser urlparser = new SimpleHttpURLParser();
		String result[] = urlparser.parserURL(crawlurl.getOriUrl());
		this.host = result[2];
		this.path =result[4];
		String[] dirs=null;
		
		//�������������������null
		if(path==null){
			return dirs;
		}
		if(path.equals("")){
			return dirs;
		}
		else {
			dirs = urlparser.parserPath(path);
		}
		return dirs;
	}
	//�ж������ҳ�ǲ�����Ҫ����
	public boolean isParser() {

		//ͨ���жϷ��ص��ĵ����������ж�
		if(crawlurl.getType()==null){
			return true;
		}
		else if (crawlurl.getType().equals("text/html")) {
			return true;
		}
		else{
			return false;
		}
	}

	/*
	 * �ļ����ص���Ҫ����
	 */
	public void download() throws Exception {
		logger.info("encoding:"+crawlurl.getCharSet());
		logger.info("�ĵ�����:"+crawlurl.getType());
		
		String[] dirs = this.parseURL();// ����url
		SimpleHttpURLParser dirparser = new SimpleHttpURLParser();
		
		
		
		
		File hostdir = new File(rootdir + "/" + host);
		hostdir.mkdirs();
		String currentdir = rootdir + "/" + host;
		String filename=null;
		if(dirs==null){
			filename="index.html";
		}
		else{
			// ����ǲ��Ǻ��зǷ��ַ�
			for (int i = 0; i < dirs.length; i++) {
				dirs[i] = dirparser.deleteIllegalChar(dirs[i]);
				//���ȳ���256��ض�
				if (dirs[i].length() > 256) {
					dirs[i] = dirs[i].substring(0, 256);
				}
			}
			// ������Ӧ��Ŀ¼
			for (int i = 0; i < dirs.length - 1; i++) {
				File tempdir = new File(currentdir, dirs[i]);
				tempdir.mkdir();
				currentdir = currentdir + "/" + dirs[i];
			}
			filename=dirs[dirs.length-1];
		}
			
		// д����Ӧ���ļ���
		file = new File(currentdir+"/"+filename);
		logger.info("�����ļ�����·��:"+file.getAbsolutePath());
		
		
		//д�ļ�
		FileOutputStream fout = new FileOutputStream(file);
		int ch;
		while ((ch = in.read()) != -1) {
			fout.write(ch);
		}
		in.close();
		fout.close();
	}

	
	// ��ӡ�ļ�
	public void printFile() throws Exception {
		FileInputStream in = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(in, encoding);
		int ch;
		while ((ch = reader.read()) != -1) {
			System.out.print((char) ch);
		}
		in.close();
		reader.close();
		
	}

}
