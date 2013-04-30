package com.search.DAO;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import com.search.data.Attribute;
import com.search.data.Document;
import com.search.data.IDhandler;
import com.search.index.Token_Structure;

public class CURD {
	// ���ض��Ĵʽ��йؼ��ֲ�ѯ
	public Token_Structure selectIndex(String term) throws Exception,
			SQLException {
		Connection con = Connect.getConnection();
		String sql;
		Token_Structure index = new Token_Structure(term);
		byte[] b = term.getBytes();
		switch (b.length) {
		case 1:
			sql = "select * from token_1 where term=?";
			break;
		case 2:
			sql = "select * from token_2 where term=?";
			break;
		case 3:
			sql = "select * from token_3 where term=?";
			break;
		case 4:
			sql = "select * from token_4 where term=?";
			break;
		case 5:
			sql = "select * from token_5 where term=?";
			break;
		case 6:
			sql = "select * from token_6 where term=?";
			break;
		case 7:
			sql = "select * from token_7 where term=?";
			break;
		case 8:
			sql = "select * from token_8 where term=?";
			break;
		default:
			sql = "select * from token where term=?";
		}
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, index.getTerm());
		ResultSet resultset = stmt.executeQuery();
		if (!resultset.next()) {
			con.close();
			return null;
		} else {
			// �õ���tokens_id���з����л�
			ByteArrayInputStream bin = new ByteArrayInputStream(
					resultset.getBytes("tokens_id"));
			ObjectInputStream oin = new ObjectInputStream(bin);
			@SuppressWarnings("unchecked")
			LinkedList<Long> list = (LinkedList<Long>) oin.readObject();
			int frequency = resultset.getInt("frequency");
			index.setFrequency(frequency);
			index.setTokens_id(list);
		}
		con.close();
		return index;
	}

	// ��ѯ�õ�һϵ��Field��content
	public LinkedList<String> selectField(LinkedList<Long> id) throws Exception,
			SQLException {
		Connection con = Connect.getConnection();
		Statement stmt = con.createStatement();
		Iterator<Long> iterator = id.iterator();
		LinkedList<String> str=new LinkedList<String>();
		
		//
		while (iterator.hasNext()) {
			IDhandler idhandler = new IDhandler(iterator.next());
			String sql = "select priority,content from Field where id='"
					+ idhandler.getField_id()+ "'";
			ResultSet resultset = stmt.executeQuery(sql);
			while (resultset.next()) {
				str.add(resultset.getString("content"));
			}
		}
		con.close();
		return str;
	}

	// ��ѯ�õ�Document
	public String selectDocument(long id) throws Exception, SQLException {
		Connection con = Connect.getConnection();
		Statement stmt = con.createStatement();
		String sql = "select content from Document where id='" + id + "'";
		ResultSet resultset = stmt.executeQuery(sql);
		String content = resultset.getString("content");
		con.close();
		return content;
	}
	
	//��ѯ�õ�һϵ��Document
	public LinkedList<Document> selectDocuments(LinkedList<Long> ids) throws Exception,SQLException{
		Connection con=Connect.getConnection();
		LinkedList<Document> documents=new LinkedList<Document>();
		Statement stmt=con.createStatement();
		Iterator<Long> iterator=ids.iterator();
		
		while(iterator.hasNext()){
			long id=iterator.next();
			String sql="select * from Document where id'"+id+"'";
			stmt.executeQuery(sql);
			
			ResultSet result=stmt.getResultSet();
			//�����л�
			while(result.next()){
				Document document=new Document(result.getLong("id"));
				//�����л�attributes
				ByteArrayInputStream attributes_in=new ByteArrayInputStream(result.getBytes("store_attributes"));
				ObjectInputStream attributes_object=new ObjectInputStream(attributes_in);
				@SuppressWarnings("unchecked")
				HashMap<String,String> attributes=(HashMap<String,String>)attributes_object.readObject();
				
				//�����л�index_number
				ByteArrayInputStream indexnumber_in=new ByteArrayInputStream(result.getBytes("index_attributes"));
				ObjectInputStream indexnumber_object=new ObjectInputStream(indexnumber_in);
				@SuppressWarnings("unchecked")
				LinkedHashMap<String,Attribute> index_attributes=(LinkedHashMap<String,Attribute>)indexnumber_object.readObject();
				

				document.setRanks(result.getInt("rank"));
				document.setDate(result.getTimestamp("create_date"));
				
				//���store_attribute
				Set<String> keys=attributes.keySet();
				for(String key:keys){
					document.addStore_attribute(key, attributes.get(key));
				}
				
				//���index_attribute
				Set<String>  index_keys=index_attributes.keySet();
				for(String key:index_keys){
					document.addIndex_attribute(key, index_attributes.get(key).getValue());
				}
				
				documents.add(document);
			}
			
			
		}
		return null;
	}

}

