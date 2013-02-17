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

public class AnalyzerImp implements Analyzer {
	private String content;
	private boolean useSmart;
	private String charset = "gbk";
	private long id;// field��ID��

	public AnalyzerImp(String content,boolean useSmart){
		this.content=content;
		this.useSmart=useSmart;
	}
	public AnalyzerImp(String content, boolean useSmart, long id)
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

	
	public AnalyzerImp(File file, boolean useSmart, long id) throws IOException {
		this.useSmart = useSmart;
		this.id = id;
		FileInputStream in = new FileInputStream(file);
		InputStreamReader in_reader = new InputStreamReader(in, charset);
		int ch;
		content = "";
		while ((ch = in_reader.read()) != -1) {
			content = content + (char) ch;
		}
		System.out.println(content);
	}

	public String[] _analyzer() throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		StringReader reader = new StringReader(content);
		IKSegmenter analyzer = new IKSegmenter(reader, useSmart);
		Lexeme lex = analyzer.next();
		while (lex != null) {
			list.add(lex.getLexemeText());
			lex = analyzer.next();
		}
		String[] str = new String[list.size()];
		Iterator<String> iterator = list.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			str[i] = new String(iterator.next());
			i++;
		}
		return str;
	}
	// �ִ���Ҫ�ĺ��������ִʵĽ����Token���ݵ���ʽ����
	public Token[] analyzer() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		StringReader reader = new StringReader(content);
		IKSegmenter analyzer = new IKSegmenter(reader, useSmart);
		Lexeme lex = analyzer.next();
		while (lex != null) {
			list.add(lex.getLexemeText());
			lex = analyzer.next();
		}
		Token[] token = new Token[list.size()];
		Iterator<String> iterator = list.iterator();
		int i = 0;
		int length = 1;
		while (iterator.hasNext()) {
			token[i] = new Token(iterator.next(), id, length);
			length = length + this.getTokenSize(token[i]);
			i++;
		}
		return token;
	}

	public LinkedList<Token> getTokens() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		StringReader reader = new StringReader(content);
		IKSegmenter analyzer = new IKSegmenter(reader, useSmart);
		Lexeme lex = analyzer.next();
		while (lex != null) {
			list.add(lex.getLexemeText());
			lex = analyzer.next();
		}
		LinkedList<Token> t = new LinkedList<Token>();
		Iterator<String> iterator = list.iterator();
		int length = 1;
		while (iterator.hasNext()) {
			Token token = new Token(iterator.next(), id, length);
			length = length + this.getTokenSize(token);
			t.add(token);
		}
		return t;
	}

	private int getTokenSize(Token token) {
		return token.getTerm().length();
	}
	public static void main(String[] args) throws Exception{
		String str="ѧУ�Ը�����ѧԺ������..[ͼ]";
		AnalyzerImp analyzer=new AnalyzerImp(str,true);
		String[] s=analyzer._analyzer();
		for(String s1:s){
			System.out.println(s1);
		}
	}

}
