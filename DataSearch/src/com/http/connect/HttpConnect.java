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

import com.http.Search.CrawlUrl;
import com.http.constant.HttpResponseHeaderConstant;

/*
 * HTTP������Ҫ����
 */
public class HttpConnect {
	private String url;// ������Դ��URL
	private CrawlUrl crawlurl;//ץȡ����URL 
	private InputStream in;//�����������
	private OutputStream out;//���������
	private Map<String, List<String>> fields;
	private HttpRequestHeader  httprequestheader;//http����ͷ
	private HttpResponseHeader httpresponseheader;//http��ͷ
	private HttpURLConnection httpconnect;
	private Logger logger = LogManager.getLogger("HttpConnect");
	private Map<String,String> header=new HashMap<String,String>();
	public CrawlUrl getCrawlurl() {
		return crawlurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	private void setHttpResponseHeader(Map<String,String> header){
		Set<String> keyset=header.keySet();
		for(String key:keyset){
			this.setEntry(key, header.get(key));
		}
	}
	public HttpConnect() {
		
	}
	/*
	 * ���ӳɹ�ʱ��������
	 */
	public void success() throws Exception {
		httpconnect.setConnectTimeout(1000);
		if (httpconnect.getResponseCode() == 301
				|| httpconnect.getResponseCode() == 302) {
			this.redirect();
		}
		else{
			in = httpconnect.getInputStream();
			fields = httpconnect.getHeaderFields();
			//����ͷ
			int i=0;
			while(true){
				String key=httpconnect.getHeaderFieldKey(i);
				String value=httpconnect.getHeaderField(i);
				if(i==0){
					header.put("statuscode", value);
					i++;
					continue;
				}
				if(key==null){
					break;
				}
				header.put(key, value);
				i++;
			}
			httpresponseheader = new HttpResponseHeader();
			this.setHttpResponseHeader(header);
			this.setCrawlUrl();			
		}
		
	}

	/*
	 * ��������
	 */
	public void discard() throws Exception {
		throw new Exception();
	}

	/*
	 * �ض�������
	 */
	public void redirect() throws Exception {
		String redirecturl = this.getHttpresponseheader().getLocation();
		this.setUrl(redirecturl);
		Connect();
	}
	/*
	 * ���ӵ���Ҫ����
	 */
	public void Connect() throws Exception {
		SimpleHttpURLParser httpparser = new SimpleHttpURLParser();
		boolean result = httpparser.vertifyURL(url);
		logger.info(url);
		if (!result) {
			throw new IOException();
		}
		
		URL urlconnect = new URL(url);
		httpconnect = (HttpURLConnection) urlconnect.openConnection();
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
		crawlurl=new CrawlUrl();
		crawlurl.setPriority(0);
		crawlurl.setOriUrl(url);
		crawlurl.setLastUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		crawlurl.setCharSet("utf-8");
	}
	private void setEntry(String key,String value) {
		if (key == null) {
			return;
		}
		switch (key) {
		case HttpResponseHeaderConstant.Date:
			httpresponseheader.setDate(value);
			break;
		case HttpResponseHeaderConstant.Upgrade:
			httpresponseheader.setUpgrade(value);
			break;
		case HttpResponseHeaderConstant.Set_Cookie:
			httpresponseheader.setSet_Cookie(value);
		case HttpResponseHeaderConstant.Server:
			httpresponseheader.setServer(value);
			break;
		case HttpResponseHeaderConstant.Location:
			httpresponseheader.setLocation(value);
			break;
		case HttpResponseHeaderConstant.Last_modefied:
			httpresponseheader.setLast_modefied(value);
			break;
		case HttpResponseHeaderConstant.Content_Type:
			httpresponseheader.setContent_Type(value);
			break;
		case HttpResponseHeaderConstant.Content_Length:
			httpresponseheader.setContent_Length(value);
			break;
		case HttpResponseHeaderConstant.Content_Language:
			httpresponseheader.setContent_Language(value);
			break;
		case HttpResponseHeaderConstant.Content_Encoding:
			httpresponseheader.setContent_Encoding(value);
			break;
		case HttpResponseHeaderConstant.Accept_Ranges:
			httpresponseheader.setAccept_Ranges(value);
			break;
		default:
		}
	}

	public void printFields() throws IOException {
		// ����http��ͷ����
		Set<Map.Entry<String, List<String>>> entryset = fields.entrySet();
		Iterator<Map.Entry<String, List<String>>> iterator = entryset
				.iterator();
		System.out.println("http header fields:");
		System.out.println();
		while (iterator.hasNext()) {
			Map.Entry<String, List<String>> map = iterator.next();
			System.out.print(map.getKey() + ":");
			List<String> value = map.getValue();
			Iterator<String> valueiterator = value.iterator();
			while (valueiterator.hasNext()) {
				System.out.print(valueiterator.next() + "  ");
			}
			System.out.println();
		}
	}
}
