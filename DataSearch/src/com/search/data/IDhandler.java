package com.search.data;
//����ID��
public class IDhandler {
	private long id;
	public IDhandler(long id){
		this.id=id;
	}
	public void setID(long id){
		this.id=id;
	}
	public long getPage_id(){
		long a=1;
		a=~((a<<40)-1);//���ȵõ����Ƶ����� 1111111111 00000000��ʽ
		a=a&id;//������õ�pageID
		return a;
	}
	public long getField_id(){
		long a=((long)1<<40)-1;//�ȹ������Ƶ�����  000000000 1111111111��ʽ
		a=a^(((long)1<<20)-1);//�ٹ������ƵĽṹ   00000000 111111111 00000000��ʽ
		a=a&id;//������õ�fieldID
		return a;
	}
	public long Field_id(){
		long a=((long)1<<20)-1;
		a=~a;
		a=a&id;
		return a;
	}
	public long getToken_id(){
		long a=((long)1<<20)-1;//�ȹ������Ƶ����� 00000000000 111111111
		a=a&id;//������õ�TokenID
		return a;
	}
	public static void main(String[] args) {
		long id=704786960744459L;
		IDhandler handler=new IDhandler(id);
		System.out.println(handler.getPage_id());
		System.out.println(handler.getField_id());
		System.out.println(handler.getToken_id());
		System.out.println(handler.Field_id());
	}
}
