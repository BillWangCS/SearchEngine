package com.search.index;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;

import com.search.analyzer.AnalyzerImp;
import com.search.data.Field;
import com.search.data.IDhandler;
import com.search.data.Page;
import com.search.data.PageIDOverException;
import com.search.data.Token;

public class IndexWriter {
	private String str;
	private Page page;
	private Field[] field;
	private ArrayList<LinkedList<Token>> token;
	private String charset = "gb2312";
	private String dirpath;
	private long id;

	public IndexWriter(String str, long id) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				charset));
		this.id = id;
		int ch;
		this.str = "";
		while ((ch = reader.read()) != -1) {
			this.str = this.str + (char) ch;
		}
		reader.close();
		in.close();
	}

	public IndexWriter(File file, long id) throws FileNotFoundException,
			IOException {
		FileInputStream in = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(in, charset);
		this.id = id;
		int ch;
		str = "";
		while ((ch = reader.read()) != -1) {
			str = str + (char) ch;
		}
		reader.close();
		in.close();
	}

	//
	public IndexWriter(Reader reader, long id) throws IOException {
		BufferedReader buffer_reader = new BufferedReader(reader);
		this.id = id;
		int ch;
		this.str = "";
		while ((ch = buffer_reader.read()) != -1) {
			this.str = str + (char) ch;
		}
		buffer_reader.close();
	}

	public void setPath(String dirpath) {
		this.dirpath = dirpath;
	}

	public void writer() throws Exception {
		this.writePage();
		this.writeField();
		this.writeToken();
	}

	public Page getPage() {
		return page;
	}

	public Field[] getField() {
		return field;
	}

	public ArrayList<LinkedList<Token>> getToken() {
		return token;
	}

	private void writePage() throws PageIDOverException {
		page = new Page(id);
	}

	private String[] getSplit() {
		String[] split = str.split("\n");
		return split;
	}

	// ��String������д��Field��
	private void writeField() throws Exception {
		String[] split = this.getSplit();
		int offset = 2;
		field = new Field[split.length / 2];
		int j = -1;
		for (int i = 1; i < split.length; i = i + 2) {
			field[++j] = new Field(split[i].substring(6), page.getID(), offset);// ������д��field���ص�value���ַ���
			offset = offset + 2;
		}
	}

	//
	private void writeToken() throws Exception {
		token =new ArrayList<LinkedList<Token>>(field.length);
		for (int i = 0; i < field.length; i++) {
			AnalyzerImp analyzer = new AnalyzerImp(field[i].getText(), true,
					field[i].getID());
			analyzer.analyzer();
			token.set(i,analyzer.getTokens());
		}
	}

	// ��pageд���ļ���
	public void writePage_to_file() throws IOException {
		File file = new File(dirpath);
		file.mkdirs();
		File page_file = new File(dirpath, String.valueOf(id) + ".page");
		FileOutputStream out = new FileOutputStream(page_file);
		out.write('\n');
		out.write(String.valueOf(page.getID()).getBytes());

	}

	// ��fieldд�뵽�ļ���
	public void writeField_to_file() throws IOException {
		IDhandler idhandler = new IDhandler(page.getID());
		for (int i = 0; i < field.length; i++) {
			idhandler.setID(field[i].getID());
			long field_id = idhandler.getField_id();
			File file = new File(dirpath, String.valueOf(field_id >> 20)
					+ ".field");// ��fieldд��dirpathĿ¼����fieldΪ��׺���ļ���
			FileOutputStream out = new FileOutputStream(file);
			out.write(String.valueOf(field[i].getID()).getBytes());
			out.write('\n');
			out.write(String.valueOf(field[i].getText()).getBytes());
		}
	}

	// ��tokenд�뵽�ļ���
	public void writeToken_to_file() throws IOException {
		IDhandler idhandler = new IDhandler(page.getID());
		for (int i = 0; i < token.size(); i++) {
			IDhandler field_idhandler = new IDhandler(field[i].getID());
			long field_id = field_idhandler.getField_id();
			for (int j = 0; j < token.get(i).size(); j++) {
				idhandler.setID(token.get(i).get(j).getID());
				long token_id = idhandler.getToken_id();
				File file = new File(dirpath, String.valueOf(field_id) + "_"
						+ String.valueOf(token_id) + ".token");// ��tokenд��dirpathĿ¼����tokenΪ��׺���ļ���
				FileOutputStream out = new FileOutputStream(file);
				out.write(String.valueOf(token.get(i).get(j).getID()).getBytes());
				out.write('\n');
				out.write(token.get(i).get(j).getTerm().getBytes());
			}
		}
	}

	public void writerPage_to_database() {

	}

	public void writerField_to_database() {

	}

	public void writerToken_to_database() {

	}
}
