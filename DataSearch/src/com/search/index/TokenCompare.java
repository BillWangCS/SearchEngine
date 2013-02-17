package com.search.index;

import java.util.Comparator;

import com.search.data.Token;
/*
 * token�Ƚ��࣬�����Ƚ�����token���ֵ���
 * ���ص�ֵΪ1ʱ��t1����t2��
 * ����0ʱ��t1����t2
 * ����-1ʱ��t1С��t2
 */
public class TokenCompare implements Comparator<Token> {
	public int compare(Token t1, Token t2) {
		//ת��Ϊ�ַ�����
		char[] c1 = ((com.search.data.Token) t1).getTerm().toCharArray();
		char[] c2 = ((com.search.data.Token) t2).getTerm().toCharArray();
		int i, j;
		i = 0;
		j = 0;
		ChineseCompare compare = new ChineseCompare();
		
		//ѭ���Ƚ�ֱ���Ƚϳ����
		while (true) {
			if (compare.compare(c1[i], c2[j]) < 0) {
				return -1;
			}
			if (compare.compare(c1[i], c2[j]) > 0) {
				return 1;
			} else {
				if (i == c1.length - 1 && j == c2.length - 1) {
					return 0;
				}
				if (i == c1.length - 1) {
					return -1;
				}
				if (j == c2.length - 1) {
					return 1;
				}
				i++;
				j++;
			}
		}
	}
}
