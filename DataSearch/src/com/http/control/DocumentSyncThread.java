/**
 * 
 */
package com.http.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.search.DAO.Connect;

/**
 * @author niubaisui
 *
 */
public class DocumentSyncThread implements Runnable{

	private Logger logger=LogManager.getLogger("DocumentSyncThread");
	private String sql="update document set rank=? where id=?";
	private String url_sql="select url from document where id=?";
	private String priority_sql="select priority from visitedurl where oriUrl=?";
	private String min_document="select min(id) from document";
	private String max_document="select max(id) from document";
	private long min_id=0;
	private long max_id=0;
	private Connection con=null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			con=Connect.getConnection();
		}catch(Exception e){
			logger.fatal("���ݿ�����ʧ��");
			e.printStackTrace();
		}
		
		try {
			logger.info("ͬ���߳���˯��50s");
			Thread.sleep(50000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			
			
			try{
				//�õ���С��id
				Statement min_stmt=con.createStatement();
				ResultSet min_resultset=min_stmt.executeQuery(min_document);
				while(min_resultset.next()){
					min_id=min_resultset.getLong("min(id)");
					min_id=min_id>>40;
				}
				
				//�õ�����id
				Statement max_stmt=con.createStatement();
				ResultSet max_resultset=max_stmt.executeQuery(max_document);
				while(max_resultset.next()){
					max_id=max_resultset.getLong("max(id)");
					max_id=max_id>>40;
				}
				
				//����rank
				PreparedStatement url_stmt=con.prepareStatement(url_sql);
				PreparedStatement priority_stmt=con.prepareStatement(priority_sql);
				PreparedStatement stmt=con.prepareStatement(sql);
				
				
				for(long i=min_id;i<=max_id;i++){
					
					//��document�в�ѯ��id��Ӧ��url
					
					long id=i<<40;				
					url_stmt.setLong(1, id);
					ResultSet url_resultset=url_stmt.executeQuery();
					String url=null;
					while(url_resultset.next()){
						url=url_resultset.getString("url");
					}					
					//��visitdurl�в�ѯrank				
					priority_stmt.setString(1, url);
					ResultSet priority_resultset=priority_stmt.executeQuery();
					int rank=-1;
					while(priority_resultset.next()){
						rank=priority_resultset.getInt("priority");
					}
					
					if(rank==-1){
						logger.info("����Ҫͬ��");
						break;
					}
					
					//����documnet rank					
					stmt.setInt(1, rank);
					stmt.setLong(2, id);
					stmt.executeUpdate();				
				}
				
				//˯��500s
				logger.info("�߳̽�˯��500s");
				Thread.sleep(500000);
				
			}catch(Exception e){
				logger.fatal("�߳�˯���쳣");
				e.printStackTrace();
			}
		}
	}

}
