package com.http.connect;


/*
 *
 */
public class HttpResponseHeader {
	private String Date;// ������ǰ����
	private String Upgrade;// ָ������ʹ�õ�ͨ��Э��
	private String Server;// �����������������Ϣ
	private String Set_Cookie;// ����������ͻ�����cookie
	private String Content_Encoding;// ָ�����뷽��
	private String Content_Language;// ָ������
	private String Content_Length;// �����ĵ��ĳ���
	private String Content_Type;// ָ��ý������
	private String Location;// ����ͻ��������͵���һվ��
	private String Accept_Ranges;// ������������������ֽڷ�Χ
	private String Last_modefied;// �����ϴθı�����ں�ʱ��

	public HttpResponseHeader() {
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

	public String getServer() {
		return Server;
	}

	public void setServer(String server) {
		Server = server;
	}

	public String getSet_Cookie() {
		return Set_Cookie;
	}

	public void setSet_Cookie(String set_Cookie) {
		Set_Cookie = set_Cookie;
	}

	public String getContent_Encoding() {
		return Content_Encoding;
	}

	public void setContent_Encoding(String content_Encoding) {
		Content_Encoding = content_Encoding;
	}

	public String getContent_Language() {
		return Content_Language;
	}

	public void setContent_Language(String content_Language) {
		Content_Language = content_Language;
	}

	public String getContent_Length() {
		return Content_Length;
	}

	public void setContent_Length(String content_Length) {
		Content_Length = content_Length;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getContent_Type() {
		return Content_Type;
	}

	public void setContent_Type(String content_Type) {
		Content_Type = content_Type;
	}

	public String getAccept_Ranges() {
		return Accept_Ranges;
	}

	public void setAccept_Ranges(String accept_Ranges) {
		Accept_Ranges = accept_Ranges;
	}

	public String getLast_modefied() {
		return Last_modefied;
	}

	public void setLast_modefied(String last_modefied) {
		Last_modefied = last_modefied;
	}

}
