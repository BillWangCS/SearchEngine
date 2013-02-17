package com.http.connect;


import java.util.HashMap;

public class HttpStatusCodeHandlerImpl {
	private HashMap<Integer, String> status;
	private int code;

	public HttpStatusCodeHandlerImpl(int code) {
		this.initStatus();
	}

	public void discard(HttpConnect httpconnect) {
	}

	public void success(HttpConnect htttpconnect) {

	}

	public void block(HttpConnect httpconnect) {
	}

	public void redirect(HttpConnect httpconnect) {
		String redirecturl = httpconnect.getHttpresponseheader().getLocation()
				.get(0);
		httpconnect.setUrl(redirecturl);
		try {
			httpconnect.Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pp(HttpConnect httpconnect) {
		/*
		 * ����ֵ ��0��ʾ������1��ʾ�ɹ���2��ʾ�����ȴ�,3�ض���
		 */
		switch (code) {
		// �ṩ��Ϣ
		case 100:
			System.out.println("��������");
			this.discard(httpconnect);
			break;
		case 101:
			System.out.println("������ͬ��Э��");
			this.discard(httpconnect);
			break;
		// �ɹ�
		case 200:
			System.out.println("����ɹ�");
			this.success(httpconnect);
			break;
		case 201:
			System.out.println("�µ�URL������");
			break;
		case 202:
			System.out.println("���󱻽���");
			this.block(httpconnect);
			break;
		case 204:
			System.out.println("������û������");
			this.discard(httpconnect);
			break;
		// �ض���
		case 300:
			System.out.println();
			this.discard(httpconnect);
		case 301:
			System.out.println("�������Ѳ���ʹ���������URL");
			this.redirect(httpconnect);
			break;
		case 302:
			System.out.println("�������URL�ѱ���ʱɾ��");
			this.redirect(httpconnect);
			break;
		case 304:
			System.out.println("�ĵ���û�б��޸�");
			this.discard(httpconnect);
			break;
		// �ͻ����
		case 400:
			System.out.println("���������﷨����");
			this.discard(httpconnect);
			break;
		case 401:
			System.out.println("����ȱ���ʵ���Ȩ��");
			this.discard(httpconnect);
			break;
		case 403:
			System.out.println("���񱻾ܾ�");
			this.discard(httpconnect);
			break;
		case 404:
			System.out.println("�ĵ�δ�ҵ�");
			this.discard(httpconnect);
			break;
		case 405:
			System.out.println("��������������URL֧��");
			this.discard(httpconnect);
			break;
		case 406:
			System.out.println("������ĸ�ʽ����֧��");
			this.discard(httpconnect);
			break;
		// ���������
		case 500:
			System.out.println("���������в�������");
			this.discard(httpconnect);
			break;
		case 501:
			System.out.println("������Ķ����������");
			this.discard(httpconnect);
			break;
		case 503:
			System.out.println("������ͣ����");
			this.discard(httpconnect);
			break;
		default:
			System.out.println("error");
			this.discard(httpconnect);
			break;
		}
	}

	private void initStatus() {
		// �ṩ��Ϣ��״̬��
		status.put(101, "Continue");
		status.put(102, "Switching");
		// �ɹ�״̬��
		status.put(200, "OK");
		status.put(201, "Created");
		status.put(202, "Accepted");
		status.put(204, "No content");
		// �ض���״̬��
		status.put(301, "Moved permanently");
		status.put(302, "Moved temporarily");
		status.put(304, "Not modified");
		// �ͻ����״̬��
		status.put(400, "Bad request");
		status.put(401, "Unauthorized");
		status.put(403, "Forbidden");
		status.put(404, "Not found");
		status.put(405, "Method not allowed");
		status.put(406, "Not acceptable");
		// ���������״̬��
		status.put(500, "Internal server error");
		status.put(501, "Not implemented");
		status.put(503, "Service unavailabel");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void HttpCodeHandler(HttpConnect httpconnect) {
		// TODO Auto-generated method stub
		/*
		 * ����ֵ ��0��ʾ������1��ʾ�ɹ���2��ʾ�����ȴ�,3�ض���
		 */
		switch (code) {
		// �ṩ��Ϣ
		case 100:
			System.out.println("��������");
			this.discard(httpconnect);
			break;
		case 101:
			System.out.println("������ͬ��Э��");
			this.discard(httpconnect);
			break;
		// �ɹ�
		case 200:
			System.out.println("����ɹ�");
			this.success(httpconnect);
			break;
		case 201:
			System.out.println("�µ�URL������");
			break;
		case 202:
			System.out.println("���󱻽���");
			this.block(httpconnect);
			break;
		case 204:
			System.out.println("������û������");
			this.discard(httpconnect);
			break;
		// �ض���
		case 300:
			System.out.println();
			this.discard(httpconnect);
		case 301:
			System.out.println("�������Ѳ���ʹ���������URL");
			this.redirect(httpconnect);
			break;
		case 302:
			System.out.println("�������URL�ѱ���ʱɾ��");
			this.redirect(httpconnect);
			break;
		case 304:
			System.out.println("");
			this.discard(httpconnect);
			break;
		// �ͻ����
		case 400:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 401:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 403:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 404:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 405:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 406:
			System.out.println("");
			this.discard(httpconnect);
			break;
		// ���������
		case 500:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 501:
			System.out.println("");
			this.discard(httpconnect);
			break;
		case 503:
			System.out.println("");
			this.discard(httpconnect);
			break;
		default:
			System.out.println("error");
			this.discard(httpconnect);
			break;
		}
	}
}
