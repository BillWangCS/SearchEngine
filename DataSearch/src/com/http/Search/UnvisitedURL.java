package com.http.Search;

import java.sql.SQLException;
import com.http.ADO.DataBaseCRUD;

public class UnvisitedURL {
	private UrlBloomFilter bloomfilter;
	private DataBaseCRUD crud = new DataBaseCRUD();

	public UnvisitedURL(){
		bloomfilter=new UrlBloomFilter();
	}
	//�������������ҷ���false�����򷵻�true
	public boolean addURL(CrawlUrl url) throws SQLException, Exception {
		// �����������£��������
		if (bloomfilter.contains(url)) {
			
			CrawlUrl crawlurl=crud.query_unvisitedURL(url);
			int priority=crawlurl.getPriority();
			crawlurl.setPriority(priority+10);
			//ÿ��һ���������ȼ���ʮ
			crud.updatedUNVisitedURL(crawlurl);
			return false;
		} else {
			//��ʼ��ʱ���ȼ�Ϊ0
			bloomfilter.add(url);
			url.setPriority(0);
			crud.insert_unvisitedURL(url);
			return true;
		}
	}

	// �õ����ȼ���ߵ�url֮һ
	public CrawlUrl getURL() throws SQLException, Exception {
		return crud.getURL();
	}

	// �ж��Ƿ����

	public int getSize() throws SQLException, Exception {
		return crud.selectUNVisited_Size();
	}

}
