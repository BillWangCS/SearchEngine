package com.search.data;

//����ID��
public class IDhandler {
	private long id;

	public IDhandler(long id) {
		this.id = id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getPage_id() {
		long a = 1;
		a = ~((a << 40) - 1);// ���ȵõ����Ƶ����� 1111111111 00000000��ʽ
		a = a & id;// ������õ�pageID
		return a;
	}

	//��������кż�field id
	public long getField_id() {
		long a = ((long) 1 << 40) - 1;// �ȹ������Ƶ����� 000000000 1111111111��ʽ
		a = a ^ (((long) 1 << 20) - 1);// �ٹ������ƵĽṹ 00000000 111111111 00000000��ʽ
		a = a & id;// ������õ�fieldID
		return a;
	}
	//�õ��ĵ���+�к�
	public long Field_id() {
		long a = ((long) 1 << 20) - 1;
		a = ~a;
		a = a & id;
		return a;
	}

	//������е�ƫ����
	public long getToken_id() {
		long a = ((long) 1 << 20) - 1;// �ȹ������Ƶ����� 00000000000 111111111
		a = a & id;// ������õ�TokenID
		return a;
	}
}
