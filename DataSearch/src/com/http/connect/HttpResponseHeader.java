package com.http.connect;

import java.util.List;

public class HttpResponseHeader {
	private List<String> Date;// ������ǰ����
	private List<String> Upgrade;// ָ������ʹ�õ�ͨ��Э��
	private List<String> Server;// �����������������Ϣ
	private List<String> Set_Cookie;// ����������ͻ�����cookie
	private List<String> Content_Encoding;// ָ�����뷽��
	private List<String> Content_Language;// ָ������
	private List<String> Content_Length;// �����ĵ��ĳ���
	private List<String> Content_Type;// ָ��ý������
	private List<String> Location;// ����ͻ��������͵���һվ��
	private List<String> Accept_Ranges;// ������������������ֽڷ�Χ
	private List<String> Last_modefied;// �����ϴθı�����ں�ʱ��

	public HttpResponseHeader() {
	}

	public List<String> getDate() {
		return Date;
	}

	public void setDate(List<String> date) {
		Date = date;
	}

	public List<String> getUpgrade() {
		return Upgrade;
	}

	public void setUpgrade(List<String> upgrade) {
		Upgrade = upgrade;
	}

	public List<String> getServer() {
		return Server;
	}

	public void setServer(List<String> server) {
		Server = server;
	}

	public List<String> getSet_Cookie() {
		return Set_Cookie;
	}

	public void setSet_Cookie(List<String> set_Cookie) {
		Set_Cookie = set_Cookie;
	}

	public List<String> getContent_Encoding() {
		return Content_Encoding;
	}

	public void setContent_Encoding(List<String> content_Encoding) {
		Content_Encoding = content_Encoding;
	}

	public List<String> getContent_Language() {
		return Content_Language;
	}

	public void setContent_Language(List<String> content_Language) {
		Content_Language = content_Language;
	}

	public List<String> getContent_Length() {
		return Content_Length;
	}

	public void setContent_Length(List<String> content_Length) {
		Content_Length = content_Length;
	}

	public List<String> getLocation() {
		return Location;
	}

	public void setLocation(List<String> location) {
		Location = location;
	}

	public List<String> getContent_Type() {
		return Content_Type;
	}

	public void setContent_Type(List<String> content_Type) {
		Content_Type = content_Type;
	}

	public List<String> getAccept_Ranges() {
		return Accept_Ranges;
	}

	public void setAccept_Ranges(List<String> accept_Ranges) {
		Accept_Ranges = accept_Ranges;
	}

	public List<String> getLast_modefied() {
		return Last_modefied;
	}

	public void setLast_modefied(List<String> last_modefied) {
		Last_modefied = last_modefied;
	}

}
