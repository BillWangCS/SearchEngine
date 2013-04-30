package com.search.data;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Field {
	private long ID = 1;
	private String text;
	private int priority = 0;
	private final int Max_Row_Count = 1 << 20;// �ĵ������һ������
	//���ԣ�������չ��Ҳ����˵�����Զ����������
	private HashMap<String,String> attributes=new HashMap<String,String>();
	// idΪDocument��id�ţ�offset�ǵ�ǰ�ĵ��е�ƫ����
	public Field(String text, long id, long offset) throws FieldIDOverException {
		if (offset < Max_Row_Count) {
			this.text = text;
			this.ID = id+ (offset << 20);
		} else {
			throw new FieldIDOverException();
		}
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public String getText() {
		return text;
	}

	public long getID() {
		return this.ID;
	}
	
	public HashMap<String,String> getAllAttributes(){
		return attributes;
	}
	public String getAttriubte(String key){
		return attributes.get(key);
	}
	public void addAttribute(String key,String value){
		attributes.put(key, value);
	}
	
}
