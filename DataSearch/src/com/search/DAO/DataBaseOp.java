package com.search.DAO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.search.data.Field;
import com.search.data.IDhandler;
import com.search.data.Page;
import com.search.data.Token;
import com.search.index.IndexDataBase;
import com.search.index.IndexWriter;
import com.search.index.Index_Structure;

/*
 * 
 */
public class DataBaseOp {
	private static int id = 1;
	public String SavePage_sql() throws Exception, SQLException {
		String sql = "insert into document(ID,CREATE_DATE,CONTENT)values (?,?,?)";
		return sql;
	}

	public void write() throws Exception {

	}

	public String SaveField_sql() throws Exception, SQLException {
		String sql = "insert into field(ID,PRIORITY,CONTENT)values" + "(?,?,?)";
		return sql;
	}

	public String SaveToken_sql(int size) throws Exception, SQLException {
		String sql;
		switch (size) {
		case 1:	
			sql = "insert into token_1(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 2:
			sql = "insert into token_2(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 3:
			sql = "insert into token_3(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 4:
			sql = "insert into token_4(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 5:
			sql = "insert into token_5(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 6:
			sql = "insert into token_6(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 7:
			sql = "insert into token_7(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		case 8:
			sql = "insert into token_8(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
			break;
		default:
			sql = "insert into token(FREQUENCY,TERM,TOKENS_ID)values(?,?,?)";
		}
		return sql;
	}

	private String Save_update(int size){
		String sql;
		switch(size){
		case 1:sql="update token_1 set frequency=?,tokens_id=? where term=?";
		break;
		case 2:sql="update token_2 set frequency=?,tokens_id=? where term=?";
		break;
		case 3:sql="update token_3 set frequency=?,tokens_id=? where term=?";
		break;
		case 4:sql="update token_4 set frequency=?,tokens_id=? where term=?";
		break;
		case 5:sql="update token_5 set frequency=?,tokens_id=? where term=?";
		break;
		case 6:sql="update token_6 set frequency=?,tokens_id=? where term=?";
		break;
		case 7:sql="update token_7 set frequency=?,tokens_id=? where term=?";
		break;
		case 8:sql="update token_8 set frequency=? ,tokens_id=? where term=?";
		break;
		default:
			sql="update token set frequency=? ,tokens_id=? where term=?";
		}
		return sql;
	}
	private String PrepareProcessor(String str) {
		String s = str.replaceAll("'", "##");
		return s;
	}
	//�ϲ��ļ��Ĳ���,ÿ1024���ļ����кϲ�����һ��,ʣ�µķֶ����������
	private LinkedList<Integer> getPart(int sum) {
		LinkedList<Integer> count = new LinkedList<Integer>();
		int n = sum / 1024;
		count.addLast(n);
		int mod = sum % 1024;
		while (true) {
			if (mod >= 512) {
				count.addLast(512);
				mod = mod - 512;
			}
			if (mod < 512 && mod >= 256) {
				count.addLast(256);
				mod = mod - 256;
			}
			if (mod < 256 && mod >= 128) {
				count.addLast(128);
				mod = mod - 128;
			}
			if (mod < 128 && mod >= 64) {
				count.addLast(64);
				mod = mod - 64;
			}
			if (mod < 64) {
				count.addLast(mod);
				break;
			}
		}
		for (int i : count) {
			System.out.println(i);
		}
		return count;
	}
//�������ݿ����һ���ε�д��
	public void DataBaseSave(String dirpath, boolean isQuery, int count)
			throws Exception, SQLException {
		//�������ݿ�
		Connection con = Connect.getConnection(
				"jdbc:mysql://localhost:3306/niubaisui", "root", "niubaisui",
				Connect.DATABASE_TYPE_MYSQL);
		//����
		IndexDataBase database = new IndexDataBase(dirpath);
		database.setCount(count);
		long sortid=id;
		database.Sort(sortid);
		int filenum = database.getFile_Number();
		System.out.println(filenum);
		// дpage
		for (int i = 1; i <= filenum; i++) {
			File file = new File(dirpath, "datafile" + id);
			System.out.println(file.getName());
			FileReader reader = new FileReader(file);
			PreparedStatement stmt = con.prepareStatement(SavePage_sql());
			DateFormat format = DateFormat.getDateTimeInstance();
			String date = format.format(Calendar.getInstance().getTime());
			Page page = new Page(id);			
			id++;
			System.out.println("����д��" + id + "���ļ�");
			System.out.println(page.getID());
			System.out.println(date);
			stmt.setLong(1, page.getID());
			stmt.setTimestamp(2, new Timestamp(Calendar.getInstance()
					.getTimeInMillis()));
			stmt.setCharacterStream(3, reader);
			stmt.execute();
		}

		// дField
		LinkedList<Field> list = database.getField();
		Iterator<Field> iterator_field = list.iterator();
		while (iterator_field.hasNext()) {
			String sql = SaveField_sql();
			PreparedStatement stmt = con.prepareStatement(sql);
			Field field = iterator_field.next();
			stmt.setLong(1, field.getID());
			stmt.setInt(2, field.getPriority());
			//���л�
			ByteArrayInputStream bin = new ByteArrayInputStream(field.getText()
					.getBytes());
			InputStreamReader reader = new InputStreamReader(bin, "gb2312");
			stmt.setCharacterStream(3, reader);
			System.out.println("����дField");
			System.out.println(sql);
			IDhandler idhandler = new IDhandler(field.getID());
			System.out.println(idhandler.getField_id() >> 20);
			System.out.println(field.getText());
			stmt.execute();
		}
		// д����
		LinkedList<Index_Structure> indexs = database.getIndexs();
		Iterator<Index_Structure> iterator_indexs = indexs.iterator();
		// ��������
		
		while (iterator_indexs.hasNext()) {
			Index_Structure index = iterator_indexs.next();
			// ���л�tokes_id
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(index.getTokens_id());
			// ���ô�����unicode����
			byte[] unicode = index.getTerm().getBytes();
			String sql = this.SaveToken_sql(unicode.length);
			CURD curd = new CURD();
			Index_Structure updated_index = curd.selectIndex(index.getTerm());
			// ִ�и���
			if (isQuery && updated_index != null) {
				LinkedList<Long> updated_list = updated_index.getTokens_id();
				Iterator<Long> iterator_list = index.Iterator();
				int frequency = updated_index.getFrequency();
				while (iterator_list.hasNext()) {
					Long id=iterator_list.next();
					updated_list.addLast(id);
					frequency++;
				}
				//�������л�
				updated_index.setFrequency(frequency);
				ByteArrayOutputStream selected_bout = new ByteArrayOutputStream();
				ObjectOutputStream selected_out = new ObjectOutputStream(
						selected_bout);
				selected_out.writeObject(updated_list);
				
				PreparedStatement stmt = con
						.prepareStatement(Save_update(unicode.length));
				//дռλ���ı���
				String term=null;
				//���ȳ������ֵ,�ض�
				if(index.getTerm().length()>250){
					term=updated_index.getTerm().substring(0,254);
				}
				else{
					term=updated_index.getTerm();
				}
				stmt.setInt(1, updated_index.getFrequency());
				ByteArrayInputStream updated_bin = new ByteArrayInputStream(
						selected_bout.toByteArray());
				
				stmt.setAsciiStream(2, updated_bin);
				stmt.setString(3, term);
				System.out.println("���ڸ���Token");
				System.out.println(Save_update(term.length()));
				stmt.execute();
			}
			//���в������
			else {
				PreparedStatement stmt = con.prepareStatement(sql);
				//дռλ���ı���
				String term=null;
				//���ȳ������ֵ,�ض�
				if(index.getTerm().length()>250){
					term=index.getTerm().substring(0,254);
				}
				else{
					term=index.getTerm();
				}
				stmt.setInt(1, index.getFrequency());
				stmt.setString(2,term);
				ByteArrayInputStream bin = new ByteArrayInputStream(
						bout.toByteArray());
				
				stmt.setAsciiStream(3, bin);
				System.out.println("����дToken");
				System.out.println(sql);
				stmt.execute();
			}
		}
		con.close();
	}
//������д�����ݿ���
	public void Save(String dirpath) throws Exception, SQLException {
		boolean isFirst = true;//count�д�ŵ���д�ı���,���Ǹ�д�Ǹ�,count�еĵ�һ������,��������б��
		boolean isQuery = true;//��һ��дʱ,ֱ��д,�����в�ѯ
		File f = new File(dirpath);
		int filesum = f.list().length;
		LinkedList<Integer> count = getPart(filesum);//�ϲ�����Ĳ���
		while (!count.isEmpty()) {
			int n = count.pollFirst();
			//д��������,д��֮������Ϊfalse
			if (isFirst) {
				for (; n>0; n--) {
					this.DataBaseSave(dirpath, isQuery,10);
					isQuery = true;
				}
				isFirst = false;
			} else {
				if(n==512){
					this.DataBaseSave(dirpath, isQuery,9);
				}
				else if(n==256){
					this.DataBaseSave(dirpath, isQuery,8);
				}
				else if(n==128){
					this.DataBaseSave(dirpath, isQuery,7);
				}
				else if(n==64){
					this.DataBaseSave(dirpath, isQuery,6);
				}
				else{
					for(;n>0;n--){
						this.DataBaseSave(dirpath, isQuery,0);
					}
				}
			}
		}
	}

	// ����index
	public static void main(String[] args) throws SQLException, Exception {
		DataBaseOp database = new DataBaseOp();
		try {
			database.Save("e:\\datafile");
		} catch (MySQLSyntaxErrorException e) {
			System.out.println(e.getErrorCode());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getMessage());
			System.out.println(e.getSQLState());
		}
	}
}
