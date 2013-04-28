package com.search.data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
/*
 * IDΪ64λ��long������Document�е�24λ������ʾ���������ĵ������������20λ������ʾ�ĵ��е����������
 *20λ��ʾһ���еĴ������������(��Ŀǰ���㹻�ġ�������
 */


public class Document {
	private long ID = 1;// �ĵ���
	private Timestamp date; 
	private int ranks;
	private HashMap<String,String> attributes=new HashMap<String,String>();
	private int indexcount=1;
	private HashMap<String,Integer> index_number=new HashMap<String,Integer>();
	private final long Max_Page_ID = 1 << 24;
	/*
	 * �������Ž�������
	 */
	

	//��ǰ�ĵ���id�ţ�����1,2,3�ȣ�
	public Document(long id) throws PageIDOverException {
		if (id < this.Max_Page_ID) {
			ID = id * ID << 40;
			this.date = new Timestamp(Calendar.getInstance().getTimeInMillis());
		} else {
			throw new PageIDOverException();
		}
	}

	public long getID() {
		return ID;
	}

	public void setRanks(int ranks){
		this.ranks=ranks;
	}
	
	public int getRanks(){
		return ranks;
	}
	
	public void setDate(Timestamp date){
		this.date=date;
	}
	public void setAttributes(HashMap<String,String> attributes){
		this.attributes=attributes;
	}
	
	public void setIndex_number(HashMap<String,Integer> index_number){
		this.index_number=index_number;
	}
	public HashMap<String,String> getAllAttributes(){
		return attributes;
	}
	
	public HashMap<String,Integer> getIndex_number(){
		return index_number;
	}
	public void addAttribute(String k,String v){
		index_number.put(k, indexcount++);
		attributes.put(k,v);
		
	}
	
	public int getIndexcount(String k){
		return index_number.get(k);
	}
	
	public String getAttribute(String key){
		return attributes.get(key);
	}
	
	public Timestamp getDate() {
		return date;
	}
	

}
