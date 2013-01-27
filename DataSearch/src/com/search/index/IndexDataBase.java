package com.search.index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.search.data.Field;
import com.search.data.IDhandler;
import com.search.data.Token;

public class IndexDataBase {
	private LinkedList<Field> field=new LinkedList<Field>();
	private int file_num;
	private int count = 10;
	private int Max_Merge_File_Count;
	private int filecount = 1;
	private String from_dirpath;
	private LinkedList<Index_Structure> indexs=new LinkedList<Index_Structure>();

	public IndexDataBase(String from_dirpath) {
		this.from_dirpath = from_dirpath;	
	}

	public LinkedList<Index_Structure> getIndexs(){
		return indexs;
	}
	public int getFile_Number(){
		return file_num;
	}
	public LinkedList<Field> getField(){
		return field;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
	//�����ɵ������tokenд�뵽�ļ���
	public void writeTokens(Token[][] token, String dirpath) throws IOException {
		File dir = new File(dirpath, "tokens");
		dir.mkdir();
		File file = new File(String.valueOf(filecount++) + ".tokens");
		file.createNewFile();
		FileOutputStream in = new FileOutputStream(file);
		for (int i = 0; i < token.length; i++) {
			for (int j = 0; j < token[i].length; j++) {
				in.write(token[i][j].getTerm().getBytes());
				in.write('\n');
			}
		}
	}
	//����ά����ת��Ϊһά����
	private Token[] getToken(Token[][] token) {
		LinkedList<Token> tokens = new LinkedList<Token>();
		for (int i = 0; i < token.length; i++) {
			for (int j = 0; j < token[i].length; j++) {
				tokens.add(token[i][j]);
			}
		}
		Token[] t = new Token[tokens.size()];
		int i = -1;
		Iterator<Token> iterator = tokens.iterator();
		while (iterator.hasNext()) {
			t[++i] = iterator.next();
		}
		return t;
	}

	//������ת��������
	private LinkedList<Token> getList(Token[] token) {
		LinkedList<Token> t = new LinkedList<Token>();
		for (int i = 0; i < token.length; i++) {
			t.addLast(token[i]);
		}
		return t;
	}

	// ���ļ��еõ�tokensȻ��ϲ�����
	public void Sort(long id)throws Exception {
		LinkedList<LinkedList<Token>> t = new LinkedList<LinkedList<Token>>();
		// ���ļ��еõ�tokens
		Max_Merge_File_Count=(int)Math.pow(2, count);
		this.file_num=Max_Merge_File_Count;
		for (int i = 1; i <= Max_Merge_File_Count; i++) {
			File file = new File(from_dirpath, "datafile" + id);
			IndexWriter indexwriter = new IndexWriter(file,id);	
			indexwriter.writer();
			//����field
			for(Field f:indexwriter.getField()){
				field.add(f);
			}
			Token[][] token = indexwriter.getToken();
			Token[] tk = getToken(token);
			TokenSort.Sort(tk);
			t.addLast(getList(tk));
			id++;
		}
		// ���кϲ�����
		for (int i = 0; i < count; i++) {
			LinkedList<LinkedList<Token>> list = new LinkedList<LinkedList<Token>>();
			list = this.Merge(t);
			t.clear();
			while (!list.isEmpty()) {
				t.addLast(list.pollFirst());
			}
		}
		//ת��Ϊһ���ļ�����������
		LinkedList<Token> index_list=new LinkedList<Token>();
		Iterator<LinkedList<Token>> iterator_list=t.iterator();
		while(iterator_list.hasNext()){
			Iterator<Token> iterator=iterator_list.next().iterator();
			while(iterator.hasNext()){
				index_list.add(iterator.next());
			}
		}
		LinkedList<Index_Structure> indexs=getIndexs(index_list);
		this.indexs=indexs;
	}
	// �ϲ������ļ������ļ���һ�������ļ�
	private LinkedList<LinkedList<Token>> Merge(LinkedList<LinkedList<Token>> token) {
		LinkedList<LinkedList<Token>> t = new LinkedList<LinkedList<Token>>();
		while (!token.isEmpty()) {
			t.addLast(TokenSort.MergeSort(token.pollFirst(), token.pollLast()));
		}
		Iterator<LinkedList<Token>> t_iterator = t.iterator();
		while (t_iterator.hasNext()) {
			Iterator<Token> iterator = t_iterator.next().iterator();
			System.out.println("*********************************************");
			while (iterator.hasNext()) {
				System.out.println(iterator.next().getTerm());
			}
			System.out
					.println("**********************************************");
		}
		return t;
	}
	// �õ������ṹ
	private LinkedList<Index_Structure> getIndexs(LinkedList<Token> linkedList) {
		LinkedList<Index_Structure> index_list = new LinkedList<Index_Structure>();
		while (!linkedList.isEmpty()) {
			Token token = linkedList.pollFirst();
			Index_Structure index = new Index_Structure(token.getTerm());
			index.add(token.getID());
			index_list.addLast(index);
			int frequency = 1;// ���ô�Ƶ
			while (!linkedList.isEmpty()
					&& linkedList.peekFirst().getTerm().equals(token.getTerm())) {
				index.add(linkedList.pollFirst().getID());
				frequency++;
			}
			index.setFrequency(frequency);
		}
		return index_list;
	}
	// ������д���ض����ļ���
	public void write_index_to_file(String dirpath)
			throws IOException {
		File file = new File(dirpath, filecount + ".index");
		FileOutputStream out = new FileOutputStream(file);
		Iterator<Index_Structure> iterator=this.indexs.iterator();
		while (iterator.hasNext()) {
			Index_Structure index = iterator.next();
			out.write("Term:".getBytes());
			out.write(index.getTerm().getBytes());
			out.write("\n".getBytes());
			while (!index.isEmpty()) {
				out.write(String.valueOf(index.pollFirst()).getBytes());
				out.write("\n".getBytes());
			}
		}
	}
	// ��ӡ����
	public void printIndex() throws Exception {
		Iterator<Index_Structure> iterator = indexs.iterator();
		while (iterator.hasNext()) {
			Index_Structure index = iterator.next();
			System.out.print(index.getTerm() + ":");
			Iterator<Long> index_iterator = index.Iterator();
			while(index_iterator.hasNext()) {
				System.out.print(index_iterator.next() + "   ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		IndexDataBase database=new IndexDataBase("e:\\datafile");
		database.setCount(0);
		database.Sort(1);
		LinkedList<Field> field=database.field;
		Iterator<Field> iterator=field.iterator();
		while(iterator.hasNext()){
			Field f=iterator.next();
			System.out.println(f.getText());
			IDhandler idhandler=new IDhandler(f.getID());
			System.out.println(idhandler.getField_id()>>20);
		}
	}
}
