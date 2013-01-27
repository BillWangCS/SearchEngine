package com.search.data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
//IDΪ64λ��long������Page�е�24λ������ʾ���������ĵ������������20λ������ʾ�ĵ��е����������
//20λ��ʾһ���еĴ������������
public class Page {
	private long ID=1;//�ĵ���
	private Calendar date;
	private final long Max_Page_ID=1<<24;
	public Page(long id) throws PageIDOverException{
		if(id<this.Max_Page_ID){
			ID=id*ID<<40;
			this.date=Calendar.getInstance();
		}
		else{
			throw new PageIDOverException();
		}
	}
	public Calendar getDate(){
		return date;
	}
	public long getID(){
		return ID;
	}
	public static void main(String[] args) throws Exception {
		for(int i=0;i<256;i++){
			Page page=new Page(i);
			System.out.println(page.getID());
		}
	}
}
