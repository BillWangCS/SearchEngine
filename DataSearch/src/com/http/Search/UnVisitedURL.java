package com.http.Search;

import java.sql.SQLException;
import com.http.ADO.DataBaseCRUD;

/*
 * 
 */
public class UnVisitedURL {
	private UrlBloomFilter bloomfilter;
	private DataBaseCRUD crud = new DataBaseCRUD();

	public UnVisitedURL(){
		bloomfilter=new UrlBloomFilter();
	}
	//如果存在则更新且返回true，否则插入返回false
	public boolean addURL(CrawlUrl url) throws SQLException, Exception {
		// 如果存在则更新，否则插入
		if (bloomfilter.contains(url)) {
			
			CrawlUrl crawlurl=crud.query_unvisitedURL(url);
			int priority=crawlurl.getPriority();
			crawlurl.setPriority(priority+10);
			//每多一个链接优先级加十
			crud.updatedUNVisitedURL(crawlurl);
			return true;
		} else {
			//初始化时优先级为0
			bloomfilter.add(url);
			url.setPriority(0);
			crud.insert_unvisitedURL(url);
			return false;
		}
	}

	// 得到优先级最高的url之一
	public CrawlUrl getURL() throws SQLException, Exception {
		return crud.getURL();
	}

	// 判断是否存在

	public int getSize() throws SQLException, Exception {
		return crud.selectUNVisited_Size();
	}

}
