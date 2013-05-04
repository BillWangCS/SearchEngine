package com.http.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.http.Search.BreadthFirstTraversal;
import com.http.Search.CrawlUrl;
import com.http.constant.HttpResponseHeaderConstant;

/*
 * HTTP������Ҫ����
 */
public class HttpConnect {
	private CrawlUrl crawlurl;//ץȡ����URL 
	private InputStream in;//�����������
	private OutputStream out;//���������
	private Map<String, List<String>> fields;
	private HttpRequestHeader  httprequestheader;//http����ͷ
	private HttpResponseHeader httpresponseheader;//http��ͷ
	private HttpURLConnection httpconnect;
	private Logger logger = LogManager.getLogger("HttpConnect");
	public CrawlUrl getCrawlurl() {
		return crawlurl;
	}

	public void setCrawlUrl(CrawlUrl crawlurl){
		this.crawlurl=crawlurl;
	}
	public InputStream getInputStream() {
		return this.in;
	}

	public OutputStream getOutputStream(){
		return out;
	}
	public Map<String, List<String>> getFields() {
		return this.fields;
	}

	public HttpRequestHeader getHttprequestheader() {
		return httprequestheader;
	}	

	public HttpResponseHeader getHttpresponseheader() {
		return httpresponseheader;
	}
	//
	private void setHttpResponseHeader(Map<String,List<String>> header){
		Set<String> keyset=header.keySet();
		for(String key:keyset){
			
			
			if(key==null){
				continue;
			}
			
			//����content-Type
			if(key.equals("Content-Type")){
				List<String> content_type=header.get(key);
				String document_type=content_type.get(0);
				if(document_type!=null){
					String[] str=document_type.split(";");
					if(str.length>1){
						httpresponseheader.setContent_Type(str[0]);
						httpresponseheader.setContent_Encoding(str[1].substring(9, str[1].length()));
					}
					else{
						if(str[0].contains("charset=")){
							httpresponseheader.setContent_Encoding(str[0].substring(9, str[0].length()));
						}
						else{
							httpresponseheader.setContent_Type(str[0]);
						}
					}
				}
			}
			//
			else if(key.equals("Last-Modified")){
				httpresponseheader.setLast_modefied(header.get(key).get(0));
			}
			//
			else if(key.equals("Content-Location")){
				httpresponseheader.setLocation(header.get(key).get(0));
			}
			//
			else if(key.equals("Date")){
				
			}
		}
	}
	
	public HttpConnect(){
		
	}
	public HttpConnect(CrawlUrl crawlurl) {
		this.crawlurl=crawlurl;
	}
	/*
	 * ���ӳɹ�ʱ��������
	 */
	public void success() throws Exception {
		if (httpconnect.getResponseCode() == 301
				|| httpconnect.getResponseCode() == 302) {
//			this.redirect();
			logger.info("����ʧ��URL:"+crawlurl.getOriUrl());
		}
		else{
			in = httpconnect.getInputStream();
			fields = httpconnect.getHeaderFields();
			
			
			//����httpresponseheader��crawlurl
			httpresponseheader = new HttpResponseHeader();
			this.setHttpResponseHeader(fields);
			this.setCrawlUrl();	
			logger.info("�ɹ�������URL��"+crawlurl.getOriUrl());
		}
		
	}

	/*
	 * ��������
	 */
	public void discard() throws Exception {
		logger.info("����ʧ��URL:"+crawlurl.getOriUrl());
		throw new Exception();
	}

	/*
	 * �ض�������
	 */
	public void redirect() throws Exception {
		logger.info("�ض���URL:"+crawlurl.getOriUrl());
		String redirecturl = this.getHttpresponseheader().getLocation();
		crawlurl.setOriUrl(redirecturl);
		Connect();
	}
	/*
	 * ���ӵ���Ҫ����
	 */
	public void Connect() throws Exception {
		
		//���ʹ��ģ�����visitiedurl����
		logger.info("����VistiedUrl����URL:"+crawlurl.getOriUrl());
		BreadthFirstTraversal.add_known_URLVisited(crawlurl);
		
		SimpleHttpURLParser httpparser = new SimpleHttpURLParser();
		boolean result = httpparser.vertifyURL(crawlurl.getOriUrl());
		logger.info("���ӵ�url:"+crawlurl.getOriUrl());
		if (!result) {
			throw new IOException();
		}
		
		URL urlconnect = new URL(crawlurl.getOriUrl());
		httpconnect = (HttpURLConnection) urlconnect.openConnection();
//		httpconnect.setConnectTimeout(100);
		if(httpconnect!=null){
			success();
		}
		else{
			discard();
		}
		
	}
	// ������Ӧͷ����
	public void releaseConnect() {
		httpconnect.disconnect();
	}

	private void setCrawlUrl(){
		crawlurl.setLastUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		this.setEncoding();
		this.setContentType();
		logger.info("encoding:"+crawlurl.getCharSet());
	}
	
	//�����ĵ�����
	private void setContentType(){
		crawlurl.setType(httpresponseheader.getContent_Type());
	}
	
	//�����ĵ���������
	private void setEncoding() {
		crawlurl.setCharSet(httpresponseheader.getContent_Encoding());
	}
	public void printFields() throws IOException {
		// ����http��ͷ����
		System.out.println("Http Header:");
		Set<String> keys=fields.keySet();
		for(String key:keys){
			List<String> list=fields.get(key);
			System.out.println(key+":");
			for(String s:list){
				System.out.println("   "+s);
			}
		}
	}
}
