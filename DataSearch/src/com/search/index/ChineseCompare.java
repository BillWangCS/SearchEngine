package com.search.index;

import java.util.Comparator;
//�����ַ��Ƚ���
public class ChineseCompare implements Comparator<Character>{
	@Override
	public int compare(Character c1, Character c2) {
		// TODO Auto-generated method stub
		char[] ch=new char[2];
		ch[0]=c1;
		ch[1]=c2;
		byte[] b=String.valueOf(ch).getBytes();
		//�������ַ�
		if(b[0]<0&&b[2]<0){
			if(b[0]<b[2]){//��һ�����ֵĵ�һ���ֽ�С�ڵڶ������ֵĵ�һ���ֽ�
				return -1;
			}
			if(b[0]==b[2]){/*��һ�����ֵĵ�һ���ֽڵ��ڵڶ������ֵĵ�һ���ֽڣ�
			                                                ������Ƚϵڶ����ֽ�*/
				if(b[1]<b[3]){
					return -1;
				}
				if(b[1]==b[3]){
					return 0;
				}
				else{
					return 1;
				}
			}
			//��һ�����ֵĵ�һ���ֽڴ��ڵڶ������ֵĵ�һ���ֽ�
			else{
				return 1;
			}
		}
		//���������ַ�
		else{
			//���߶����������ַ�
			if (b[0] >=0 && b[1] >=0) {
				if (b[0] < b[1]) {
					return -1;
				}
				if (b[0] == b[1]) {
					return 0;
				} else {
					return 1;
				}
			}
			//����֮һ�������ַ�
			else{
				if(b[0]>=0){
					return -1;
				}
				else{
					return 1;
				}
			}
		}
	}
}
