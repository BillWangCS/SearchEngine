package com.search.index;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import com.search.DAO.CURD;
import com.search.DAO.Connect;

public class IndexThread extends Thread {
	private LinkedList<Token_Structure> indexs;

	public IndexThread(LinkedList<Token_Structure> indexs) {
		this.indexs = indexs;
	}

	private String SaveToken_sql(int size) throws Exception, SQLException {
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
		
	private  String Save_update(int size) {
		String sql;
		switch (size) {
		case 1:
			sql = "update token_1 set frequency=?,tokens_id=? where term=?";
			break;
		case 2:
			sql = "update token_2 set frequency=?,tokens_id=? where term=?";
			break;
		case 3:
			sql = "update token_3 set frequency=?,tokens_id=? where term=?";
			break;
		case 4:
			sql = "update token_4 set frequency=?,tokens_id=? where term=?";
			break;
		case 5:
			sql = "update token_5 set frequency=?,tokens_id=? where term=?";
			break;
		case 6:
			sql = "update token_6 set frequency=?,tokens_id=? where term=?";
			break;
		case 7:
			sql = "update token_7 set frequency=?,tokens_id=? where term=?";
			break;
		case 8:
			sql = "update token_8 set frequency=? ,tokens_id=? where term=?";
			break;
		default:
			sql = "update token set frequency=? ,tokens_id=? where term=?";
		}
		return sql;
	}
	
	public void run() {
		Connection con=null;
		try {
			con = Connect.getConnection();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Iterator<Token_Structure> iterator=indexs.iterator();
		while (iterator.hasNext()) {
			try {
				
				// ���ô�����unicode����
				Token_Structure index=iterator.next();
				byte[] unicode = index.getTerm().getBytes();
				String sql = this.SaveToken_sql(index.getSize());
				CURD curd = new CURD();
				Token_Structure updated_index = curd.selectIndex(index
						.getTerm());
				// ���������ִ�и���
				if (updated_index != null) {
					LinkedList<Long> updated_list = updated_index
							.getTokens_id();
					Iterator<Long> iterator_list = index.Iterator();
					int frequency = updated_index.getFrequency();
					while (iterator_list.hasNext()) {
						Long id = iterator_list.next();
						updated_list.addLast(id);
						frequency++;
					}
					// ִ������
					updated_list = BuildIndexDataBase.Sort(updated_list);
					// �������л�
					updated_index.setFrequency(frequency);
					ByteArrayOutputStream selected_bout = new ByteArrayOutputStream();
					ObjectOutputStream selected_out = new ObjectOutputStream(
							selected_bout);
					selected_out.writeObject(updated_list);

					PreparedStatement stmt = con.prepareStatement(this.Save_update(unicode.length));
					// дռλ���ı���
					String term = null;
					// ���ȳ������ֵ,�ض�
					if (index.getTerm().length() > 250) {
						term = updated_index.getTerm().substring(0, 254);
					} else {
						term = updated_index.getTerm();
					}
					ByteArrayInputStream updated_bin = new ByteArrayInputStream(
							selected_bout.toByteArray());
					
					stmt.setInt(1, updated_index.getFrequency());
					stmt.setAsciiStream(2, updated_bin);
					stmt.setString(3, term);
					
					stmt.execute();
				}
				// ִ�в������
				else {
					// ��������tokens_id
					LinkedList<Long> tokens_id = index.getTokens_id();
					tokens_id = BuildIndexDataBase.Sort(tokens_id);
					// ���л�tokes_id
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(bout);
					out.writeObject(tokens_id);
					PreparedStatement stmt = con.prepareStatement(sql);
					// дռλ���ı���
					String term = null;
					// ���ȳ������ֵ,�ض�
					if (index.getTerm().length() > 250) {
						term = index.getTerm().substring(0, 254);
					} else {
						term = index.getTerm();
					}
					ByteArrayInputStream bin = new ByteArrayInputStream(
							bout.toByteArray());
										
					stmt.setInt(1, index.getFrequency());
					stmt.setString(2, term);				
					stmt.setAsciiStream(3, bin);
					stmt.execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}