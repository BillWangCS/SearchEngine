package com.http.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Stack;

public class LRUDns {
	private static HashMap<String, String> DNS;
	private static Stack<String> lru;
	private int size;

	public LRUDns(int size) {
		this.size = size;
		DNS = new HashMap<String, String>(size);
		lru = new Stack<String>();
	}

	public synchronized static LRUDns getLRUDns(int size) {
		return new LRUDns(size);
	}

	public synchronized String getInetAddress(String dns)
			throws UnknownHostException {
		return this.querydnsLRU(dns);
	}

	private String queryDns(String dns) throws UnknownHostException {
		InetAddress inet = InetAddress.getByName(dns);
		return inet.getHostAddress();
	}

	private String querydnsLRU(String dns) throws UnknownHostException {
		if (lru.size() < size) {// ���ջû����
			if (DNS.containsKey(dns)) {
				/*
				 * ���DNS�����д���ָ������ �����ָ�������Ƶ�ջ��
				 */
				int index = lru.indexOf(dns);
				String tempdns = lru.elementAt(index);
				lru.remove(index);
				lru.push(tempdns);
				return DNS.get(dns);
			} else {
				String query_inet = this.queryDns(dns);
				lru.push(dns);
				DNS.put(dns, query_inet);
				return query_inet;
			}
		} else {
			/*
			 * ջ���ˣ���DNS�����д���ָ�������ջ�׵�Ԫ���Ƴ��������ָ������ѹ��ջ���� ��DNS�����е�ָ�����
			 */
			if (DNS.containsKey(dns)) {
				lru.remove(0);
				lru.push(dns);
				return DNS.get(dns);
			} else {
				/*
				 * DNS��û��ָ�������ѯָ��������DNS�������ˣ����Ƴ����û�з��ʵ���,
				 * ����ѯ�õ�������뻺���У�lruջ��ɾ��ջ�׵�Ԫ�أ���ָ����dnsѹ��ջ��
				 */
				String query_inet = queryDns(dns);
				String no_recently_dns = lru.elementAt(0);
				DNS.remove(no_recently_dns);

				DNS.put(dns, query_inet);
				lru.remove(0);
				lru.push(dns);
				return query_inet;
			}
		}
	}
}
