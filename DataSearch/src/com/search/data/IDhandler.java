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

	//������ڵ�ǰ���ĵ���
	public long getCurrent_Document_id(){
		long a=id;
		a=a>>40;
		return a;
	}
	//�õ�documentid��40λ�������
	public long getDocumnent_id() {
		long a = 1;
		a = ~((a << 40) - 1);// ���ȵõ����Ƶ����� 1111111111 00000000��ʽ
		a = a & id;// ������õ�pageID
		return a;
	}

	//��������кż�field id
	public long getField_id() {
		long a=id;
		a=a>>20;
		a=a<<20;
		return a;
	}
	//�õ�Field�ڵ�ǰ�ĵ��еĺ�
	public long getCurrent_Field_id() {
		long a=id;
		a=a<<24;
		a=a>>44;
		return a;
	}

	//������ڵ�ǰ�ĵ��е�ƫ����
	public long getCurrent_Token_id() {
		long a=id;
		a=a<<44;
		a=a>>44;
		return a;
	}
}
