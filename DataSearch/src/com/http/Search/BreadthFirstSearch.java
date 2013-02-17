package com.http.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreadthFirstSearch {
	private int[] pred;
	private int[] dist;
	private int[] color;
	private WebGraph graph;

	public BreadthFirstSearch(WebGraph graph) {
		this.graph = graph;
	}

	/*
	 * �õ��ڵ�ͷ��Arraylist�������е�λ��
	 */
	private int getNumber(WebNode node) {
		int number = 0;
		Iterator<String> iterator = node.Node.keySet().iterator();
		Pattern pattern = Pattern.compile("\\d.*");// ������ʽ�������ڵ��е�λ��
		while (iterator.hasNext()) {
			Matcher matcher = pattern.matcher(iterator.next());
			boolean result = matcher.matches();
			if (result) {
				/*
				 * ������ڵĻ��Խ����������ַ����任��int
				 */
				String key = matcher.group();
				char[] data = new char[key.length()];
				data = key.toCharArray();
				for (int i = data.length; i < 0; i--) {
					number = data[i] * (int) Math.pow(10, (double) i - 1);
				}
				return number;
			} else {
				return -1;
			}
		}
		return -1;

	}

	/*
	 * ��ʼ��������ʼʱ������
	 */
	private void init() {
		int size = graph.Graph.size();
		pred = new int[size];
		dist = new int[size];
		color = new int[size];
		/*
		 * ��ɫ0��ʾ�ǰ�ɫ����û�з��ʹ���; ��ɫ1��ʾ�Ļ�ɫ�������ʹ��˵���û��ȫ������; ��ɫ2��ʾ�Ǻ�ɫ�ģ�����ȫ���ʹ��ġ�
		 */
		for (int i = 0; i < size; i++) {
			pred[i] = -1;
			dist[i] = Integer.MAX_VALUE;
			color[i] = 0;
		}
	}

	/*
	 * ������ȱ���
	 */
	public void Search() {
		this.init();
		ArrayList<HashMap<WebNode, LinkedList<WebNode>>> arraylist;
		HashMap<WebNode, LinkedList<WebNode>> element;
		WebNode node;
		LinkedList<WebNode> nodelist;
		LinkedList<WebNode> queue = new LinkedList<WebNode>();
		arraylist = graph.Graph;
		element = arraylist.get(0);// �����ڽӱ���Ԫ�ص�ͷԪ��
		node = element.keySet().iterator().next();// ����ͼ���е������еĽڵ�
		color[0] = 1;
		dist[0] = 1;
		queue.add(node);
		while (!queue.isEmpty()) {
			node = queue.getFirst();// �õ�������ͷ�ڵ�
			int local = this.getNumber(node);// �õ��ڵ��������е�λ��
			element = arraylist.get(local);// �õ��ڵ�ͷ
			nodelist = element.get(node);// �õ��ڵ���ڽӵ�����

			Iterator<WebNode> list_iterator = nodelist.iterator();// �õ������е�linkedlist������
			while (list_iterator.hasNext()) {
				WebNode childnode = list_iterator.next();
				int num = this.getNumber(childnode);// �õ�node�������е�λ��
				if (color[num] == 0) {
					color[num] = 1;// Ⱦ�ɻ�ɫ
					pred[num] = local;// ������ǰ���ڵ�
					dist[num] = dist[local] + 1;// ��������
					queue.add(childnode);// ����ڵ㵽����
				}
			}
			queue.removeFirst();// �Ӷ������Ƴ�
			color[local] = 2;// �ڵ�Ⱦ�ɺ�ɫ
		}
	}

	public static void main(String[] args) {

	}
}
