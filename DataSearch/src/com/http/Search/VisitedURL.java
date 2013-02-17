package com.http.Search;

import java.sql.SQLException;
import java.util.HashMap;

import com.http.ADO.DataBaseCRUD;

public class VisitedURL {
	private DataBaseCRUD crud = new DataBaseCRUD();
	//�����������·���false,���򷵻�true
	public boolean addURL(String url) throws Exception {
		HashMap<String, Integer> map = isExist(url);
		// �����������£�����ʲô������
		if (map != null) {
			updatedURL(url, map.get(url));
			return false;
		}
		else{
			return true;
		}
	}

	private void updatedURL(String url, int priority) throws SQLException,
			Exception {
		crud.updatedVisitedURL(url, priority);
	}

	//���ڲ����ڵ������ֱ�Ӳ���
	public void addURL(String url, int priority) throws SQLException, Exception {
		crud.insert_visitedURL(url, priority);
	}

	private HashMap<String, Integer> isExist(String url) throws Exception {
		HashMap<String, Integer> map = crud.select_visitedURL(url);
		return map;
	}

	public int getSize() throws Exception {
		return crud.selectVisited_Size();
	}

}
