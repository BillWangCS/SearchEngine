package com.http.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileDownload {
	private String url;
	private File file;
	private Logger logger = LogManager.getLogger("HtmlDownload");

	public File getFile() {
		return file;
	}

	private String host;// ���� ��:www.baidu.com/index ��hostΪwww.baidu.com
	private String dirpath;// Ŀ¼ ·�� �磺www.baidu.com/index ��dirpathΪindex
	private String rootdir;// ��Ŀ¼
	private String encoding;// ����
	private InputStream in;
	private HttpResponseHeader httpresponseheader;// ������Ӧͷ��Ϣ

	// ���췽��
	public FileDownload(HttpResponseHeader httpresponseheader, InputStream in,
			String url, String rootdir) {
		this.url = url;
		this.rootdir = rootdir;
		this.in = in;
		this.httpresponseheader = httpresponseheader;
		this.setEncoding();
	}

	public HttpResponseHeader getHttpresponseheader() {
		return httpresponseheader;
	}

	public String getEncoding() {
		return encoding;
	}

	protected void setEncoding() {
		System.out.println(httpresponseheader);
		if (httpresponseheader.getContent_Encoding() == null
				|| httpresponseheader.getContent_Encoding().isEmpty()) {
			encoding = "utf-8";
		} else {
			List<String> list = httpresponseheader.getContent_Encoding();
			encoding = list.get(0);
		}
	}

	// ����URL
	protected String[] parseURL() {
		HttpURLParserImpl urlparser = new HttpURLParserImpl();
		String result[] = urlparser.parserURL(url);
		this.host = result[result.length - 3];
		if (result[result.length - 1].equals("")) {
			dirpath = "index.html";
		} else {
			this.dirpath = result[result.length - 1];
		}
		String[] dir = urlparser.parserPath(dirpath);
		return dir;
	}
	//�ж������ҳ�ǲ���Ҫ����
	public boolean isParser() {
		// System.out.println(httpresponseheader.getContent_Type().get(0));
		if (httpresponseheader == null) {
			return true;
		}
		if (httpresponseheader.getContent_Type().isEmpty()) {
			return true;
		} else {
			if (!httpresponseheader.getContent_Type().isEmpty()) {
				boolean result = Pattern.matches(".*text/html.*",
						httpresponseheader.getContent_Type().get(0));
				if (result) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}

	public void download(String path) throws Exception {
		String[] dir = this.parseURL();// ����url
		HttpURLParserImpl dirparser = new HttpURLParserImpl();
		// ����ǲ��ǷǷ���Ŀ¼
		for (int i = 0; i < dir.length; i++) {
			dir[i] = dirparser.deleteIllegalChar(dir[i]);
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
		logger.info(result);
		for (int i = 0; i < dir.length - 1; i++) {
			File tempdir = new File(currentdir, dir[i]);
			boolean results = tempdir.mkdir();
			currentdir = currentdir + "/" + dir[i];
			logger.info(results);
			logger.info("The Dir Exist");
		}
		// д����Ӧ���ļ���
		file = new File(currentdir, dir[dir.length - 1]);
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
		this.setEncoding();
		FileInputStream fileinput = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(fileinput, encoding);
		int ch;
		while ((ch = reader.read()) != -1) {
			System.out.print((char) ch);
		}
		reader.close();
		fileinput.close();
	}

}
