package com.http.Search;

import java.util.HashMap;

/* 1.HashMap �м�ֵӳ��ı�ʾΪ�����key�����֣�vaule����ַ��
 * 2.�ڽӱ�key��"adj1"��"adj2"�ȱ�ʾ��vaule�Ǳߵļ�ֵ��
 * 3.�ؼ���key��"keyword1"��"keyword2"�ȱ�ʾ��vaule�ؼ������ַ�����ʽ��ֵ��
 * 4.��������
 */
public class WebNode {
	public HashMap<String, String> Node;

	public WebNode(HashMap<String, String> node) {
		this.Node = node;
	}

}
