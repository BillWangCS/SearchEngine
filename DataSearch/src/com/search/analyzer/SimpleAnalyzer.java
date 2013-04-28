package com.search.analyzer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.search.data.Token;

public class SimpleAnalyzer implements Analyzer {
	private String content;
	private boolean useSmart;
	private String charset = "gbk";
	private long id;// field��ID��
	private LinkedList<Token> tokens=new LinkedList<Token>();

	public SimpleAnalyzer(String content,boolean useSmart){
		this.content=content;
		this.useSmart=useSmart;
	}
	//���췽������
	public SimpleAnalyzer(String content, boolean useSmart, long id)
			throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
		InputStreamReader in_reader = new InputStreamReader(in, charset);
		int ch;
		this.content = "";
		while ((ch = in_reader.read()) != -1) {
			this.content = this.content + (char) ch;
		}
		this.useSmart = useSmart;
		this.id = id;
	}

	//�������ط���
	public SimpleAnalyzer(File file, boolean useSmart, long id) throws IOException {
		this.useSmart = useSmart;
		this.id = id;
		FileInputStream in = new FileInputStream(file);
		InputStreamReader in_reader = new InputStreamReader(in, charset);
		int ch;
		content = "";
		while ((ch = in_reader.read()) != -1) {
			content = content + (char) ch;
		}
		in_reader.close();
	}

	public void setContent(String content){
		this.content=content;
	}
	
	public void setID(long id){
		this.id=id;
	}
	//����linkedlist<string>��ʽ
	public LinkedList<String> _analyzer() throws IOException{
		LinkedList<String> list = new LinkedList<String>();
		StringReader reader = new StringReader(content);
		IKSegmenter analyzer = new IKSegmenter(reader, useSmart);
		Lexeme lex = analyzer.next();
		while (lex != null) {
			list.add(lex.getLexemeText());
			lex = analyzer.next();
		}
		
		return list;
	}
	// �ִ���Ҫ�ĺ��������ִʵĽ����Token���ݵ���ʽ����
	@Override
	public LinkedList<Token> analyzer() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		StringReader reader = new StringReader(content);
		IKSegmenter analyzer = new IKSegmenter(reader, useSmart);
		Lexeme lex = analyzer.next();
		while (lex != null) {
			list.add(lex.getLexemeText());
			lex = analyzer.next();
		}
		Iterator<String> iterator = list.iterator();
		int length = 1;
		while (iterator.hasNext()) {
			Token token = new Token(iterator.next(), id, length);
			length = length + this.getTokenSize(token);
			tokens.add(token);
		}
		return tokens;
	}

	private int getTokenSize(Token token) {
		return token.getTerm().length();
	}
}
