package com.http.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.http.Search.CrawlUrl;
import com.search.DAO.Connect;
/*
 * һ���÷���FileDownloadTest
 */
public class FileDownload {
	private String url;
	private File file;
	private String filename;
	private CrawlUrl crawlurl;
	private Logger logger = LogManager.getLogger("HtmlDownload");
	private String host;// ���� ��:www.baidu.com/index ��hostΪwww.baidu.com
	private String dir;// Ŀ¼ ·�� �磺www.baidu.com/index/index.html ��default_filenameΪindex/index.html
	private String rootdir="d:\\spider";// ��Ŀ¼
	private String encoding="utf-8";// ����
	private InputStream in;
	private HttpResponseHeader httpresponseheader;// ������Ӧͷ��Ϣ

	// ���췽��
	public FileDownload( InputStream in) {
		this.in = in;
	}
	
	public FileDownload(InputStream in,Connect connect){
		this.in=in;
	}
	
	public File getFile(){
		return file;
	}
	
	public void setUrl(String url){
		this.url=url;
	}
	public void setCrawlUrl(CrawlUrl crawlurl){
		this.crawlurl=crawlurl;
		this.setUrl(crawlurl.getOriUrl());
	}
	
	public CrawlUrl getCrawlUrl(){
		return crawlurl;
	}
	public void setHttpresponseHeader(HttpResponseHeader httpresponseheader){
		this.httpresponseheader=httpresponseheader;
		this.setEncoding();
	}
	
	public FileDownload(Connect connect){
		//undo
	}

	public String getFileName() {
		return filename;
	}
	
	public String getAbsolutePath(){
		return rootdir+"/"+host+"/"+dir;
	}

	public String getEncoding() {
		return encoding;
	}

	/*
	 * �õ��ĵ��ı���
	 */
	protected void setEncoding() {
		
		if(httpresponseheader==null){
			encoding="utf-8";
			return;
		}
		if (httpresponseheader.getContent_Encoding()==null) {
			if(httpresponseheader.getContent_Type()!=null){
				String str=httpresponseheader.getContent_Type();
				String[] s=str.split(";");
				for(String ss:s){
					if(ss.contains("charset=")){
						encoding=ss.substring(8);
						break;
					}
				}
				encoding="utf-8";
			}
		} else {
			encoding = httpresponseheader.getContent_Encoding();
		}
	}

	// ����URL
	public String[] parseURL() {
		SimpleHttpURLParser urlparser = new SimpleHttpURLParser();
		String result[] = urlparser.parserURL(url);
		this.host = result[2];
		if (result[3]==null&&result[1].equals(result[2])) {
			dir= "index.html";
		}
		else if(result[4]==null){
			dir="index.html";
		}else {
			this.dir = result[4];
		}
		String[] dirs = urlparser.parserPath(dir);
		return dirs;
	}
	//�ж������ҳ�ǲ�����Ҫ����
	public boolean isParser() {

		//ͨ���жϷ��ص��ĵ����������ж�
		if (httpresponseheader == null||httpresponseheader.getContent_Type().isEmpty()) {
			return true;
		}
		else {		
			boolean result = Pattern.matches(".*text/html.*",
				httpresponseheader.getContent_Type());
			if (result) {
				return true;
			} else {
				return false;
			}
		}
	}

	/*
	 * �ļ����ص���Ҫ����
	 */
	public void download() throws Exception {
		String[] dir = this.parseURL();// ����url
		SimpleHttpURLParser dirparser = new SimpleHttpURLParser();
		// ����ǲ��Ǻ��зǷ��ַ�
		for (int i = 0; i < dir.length; i++) {
			dir[i] = dirparser.deleteIllegalChar(dir[i]);
			//���ȳ���256��ض�
			if (dir[i].length() > 256) {
				dir[i] = dir[i].substring(0, 256);
			}
		}
		// ������Ӧ��Ŀ¼
		File hostdir = new File(rootdir + "/" + host);
		boolean result = hostdir.mkdirs();
		String currentdir = rootdir + "/" + host;
		if (!result) {
			logger.info("The Dir Exist");
		}
		for (int i = 0; i < dir.length - 1; i++) {
			File tempdir = new File(currentdir, dir[i]);
			tempdir.mkdir();
			currentdir = currentdir + "/" + dir[i];
		}
		// д����Ӧ���ļ���
		filename=dir[dir.length-1];
		file = new File(currentdir+"/"+filename);
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
		File file=new File(this.getAbsolutePath());
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
