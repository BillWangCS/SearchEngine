package com.http.connect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpURLParserImpl implements HttpURLParser {
	private String url;
	private static String regex = "\\Ahttps?://(([\\w+\\.]+)(:\\d{1,5})?/{0,1}(.*))\\Z";
	private static String regexpath = "/";
	private static String illegalcharacter = "[\\<>:%*|&?]";
	private Pattern pattern;

	public HttpURLParserImpl() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRegex() {
		return regex;
	}

	public String[] parserURL(String url) throws IllegalStateException {
		pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		matcher.find();
		String[] result = new String[matcher.groupCount() + 1];
		for (int i = 0; i <= matcher.groupCount(); i++) {
			result[i] = matcher.group(i);// ��0��ΪURL������һ��Ϊȫ·�����ڶ���Ϊ������������Ϊ�˿ڣ����ĸ�Ϊ�ļ�Ŀ¼·��
		}
		return result;
	}

	public boolean vertifyURL(String url) throws IllegalStateException {
		pattern = Pattern.compile(regex);
		if (url != null) {
			boolean result = pattern.matcher(url).matches();
			return result;
		} else
			return false;
	}

	public String[] parserPath(String path) {
		pattern = Pattern.compile(regexpath);
		String[] result = pattern.split(path);
		return result;
	}

	public String deleteIllegalChar(String str) {
		pattern = Pattern.compile(illegalcharacter);
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll("_").trim();
		return str;
	}
}
