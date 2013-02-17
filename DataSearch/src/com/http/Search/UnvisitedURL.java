package com.http.Search;

import java.sql.SQLException;
import java.util.HashMap;

import com.http.ADO.DataBaseCRUD;

public class UnvisitedURL {
	private DataBaseCRUD crud = new DataBaseCRUD();
	//�������������ҷ���false�����򷵻�true
	public boolean addURL(String url) throws SQLException, Exception {
		HashMap<String, Integer> map = isExist(url);
		// �����������£��������
		if (map != null) {
			crud.updatedUNVisitedURL(url, map.get(url));
			return false;
		} else {
			crud.insert_unvisitedURL(url);
			return true;
		}
	}

	// �õ����ȼ���ߵ�url֮һ
	public HashMap<String, Integer> getURL() throws SQLException, Exception {
		return crud.getURL();
	}

	// �ж��Ƿ����
	private HashMap<String, Integer> isExist(String url) throws SQLException,
			Exception {
		return crud.select_unvisitedURL(url);
	}

	public int getSize() throws SQLException, Exception {
		return crud.selectUNVisited_Size();
	}

}
