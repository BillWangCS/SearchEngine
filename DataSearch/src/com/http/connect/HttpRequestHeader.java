package com.http.connect;

/*
 * 
 */
public class HttpRequestHeader {
	private String User_agent;// ��־�ͻ�����
	private String Accept;// �����ͻ��ܹ����ܵ�ý���ʽ
	private String Accept_charset;// �����ͻ��ܹ�������ַ���
	private String Accept_encoding;// �����ͻ��ܹ�����ı��뷽��
	private String Accpet_language;// �����ͻ��ܹ����ܵ�����
	private String Authorization;// �����ͻ����к���׼��
	private String Host;// �����ͻ��������Ͷ˿ں�
	private String Date;// ������ǰ����
	private String Upgrade;// ָ������ʹ�õ�ͨ��Э��
	private String Cookie;// ��cookie���͸�������
	private String If_Modified_Since;// ֻ����ָ�������Ժ���µ��ĵ��ŷ���

	public String getUser_agent() {
		return User_agent;
	}

	public void setUser_agent(String user_agent) {
		User_agent = user_agent;
	}

	public String getAccept() {
		return Accept;
	}

	public void setAccept(String accept) {
		Accept = accept;
	}

	public String getAccept_encoding() {
		return Accept_encoding;
	}

	public void setAccept_encoding(String accept_encoding) {
		Accept_encoding = accept_encoding;
	}

	public String getAccept_charset() {
		return Accept_charset;
	}

	public void setAccept_charset(String accept_charset) {
		Accept_charset = accept_charset;
	}

	public String getAccpet_language() {
		return Accpet_language;
	}

	public void setAccpet_language(String accpet_language) {
		Accpet_language = accpet_language;
	}

	public String getAuthorization() {
		return Authorization;
	}

	public void setAuthorization(String authorization) {
		Authorization = authorization;
	}

	public String getHost() {
		return Host;
	}

	public void setHost(String host) {
		Host = host;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getUpgrade() {
		return Upgrade;
	}

	public void setUpgrade(String upgrade) {
		Upgrade = upgrade;
	}

	public String getCookie() {
		return Cookie;
	}

	public void setCookie(String cookie) {
		Cookie = cookie;
	}

	public String getIf_Modified_Since() {
		return If_Modified_Since;
	}

	public void setIf_Modified_Since(String if_Modified_Since) {
		If_Modified_Since = if_Modified_Since;
	}
}
