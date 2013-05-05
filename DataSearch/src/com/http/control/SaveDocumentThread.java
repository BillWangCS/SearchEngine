/**
 * 
 */
package com.http.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.search.DAO.Connect;
import com.search.data.Document;

/**
 * @author niubaisui
 *
 */
public class SaveDocumentThread extends Thread{

	private String sql = "insert into Document(id,rank,url,create_date,store_attributes,index_attributes) values (?,?,?,?,?,?)";
	private Logger logger=LogManager.getLogger("SaveDocumentThread");
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Connection con=null;
		try {
			con = Connect.getConnection();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		while (true){
				if(CrawlWebCentralThread.webpages.hasNext()){
					try {
						//ȡ��һ��Document
						Document document=CrawlWebCentralThread.webpages.nextDocument();
						
						PreparedStatement stmt = con.prepareStatement(sql);
						
						logger.info("writing id="+document.getID()+" document");
						
						//���л�
						ByteArrayOutputStream attributes_out=new ByteArrayOutputStream();
						ObjectOutputStream attributes_object=new ObjectOutputStream(attributes_out);
						attributes_object.writeObject(document.getStore_attributes());
						ByteArrayInputStream attributes_in=new ByteArrayInputStream(attributes_out.toByteArray());
						
						//���л�
						ByteArrayOutputStream indexattributes_out=new ByteArrayOutputStream();
						ObjectOutputStream indexattributes_object=new ObjectOutputStream(indexattributes_out);
						indexattributes_object.writeObject(document.getIndex_attributes());
						ByteArrayInputStream indexattributes_in=new ByteArrayInputStream(indexattributes_out.toByteArray());
						
						//дռλ��
						stmt.setLong(1, document.getID());
						stmt.setInt(2, document.getRanks());
						stmt.setString(3, document.getUrl());
						stmt.setTimestamp(4, new Timestamp(Calendar.getInstance()
								.getTimeInMillis()));
						stmt.setAsciiStream(5,attributes_in);
						stmt.setAsciiStream(6,indexattributes_in);
						stmt.execute();
						logger.info("�����ĵ��ɹ�");
					} catch (Exception e) {
						logger.fatal("дDocumentʧ��");
						e.printStackTrace();
						System.exit(0);
						
					}		
				}
				else{		
					try {
						//˯��10s;
						logger.info("д�ĵ��߳�˯��10s");
						sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}	
	}

}
