package com.search.Search;


import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.search.DAO.CURD;
import com.search.analyzer.SimpleAnalyzer;
import com.search.data.Document;
import com.search.data.Field;
/*
 * 
 * 
 */
public class Search {
	
	private Logger logger=LogManager.getLogger("Search");
		
	public LinkedList<Field> selectFields(LinkedList<Long> id) throws InterruptedException{
		
		LinkedList<Field> fields=new LinkedList<Field>();
		//ͬ��������
		CountDownLatch latch=new CountDownLatch(1);
		QueryFieldThread thread=new QueryFieldThread();
		thread.setFields(id);
		thread.run();
		//
		latch.await();		
		fields=thread.getSearchResult();	
		return fields;
	}
	
	//��ѯdocumentͨ��Field
	public LinkedList<Document>  selectDocument(LinkedList<Field> fields) throws SQLException, Exception{
			
		
		LinkedList<Document> documents=new LinkedList<Document>();
			
		documents=CURD.selectDocuments(fields);
		
		logger.info("�ѵ�document����:"+documents.size());
		
		
		return documents;
		}
	//���ݴ��������õ�LinkedList<Document>
	public LinkedList<Document> search(String term) throws SQLException, Exception  {
		LinkedList<Long> fields_id=new LinkedList<Long>();
		LinkedList<Field> fields=new LinkedList<Field>();
		fields_id = CURD.selectIndex(term).getTokens_id();	
		logger.info("�ѵ�Token������"+fields_id.size());
		
		//ͬ��������
		CountDownLatch latch=new CountDownLatch(1);
		QueryFieldThread thread=new QueryFieldThread(latch);
		thread.setFields(fields_id);
		thread.run();
		
		//
		latch.await();		
		fields=thread.getSearchResult();	
		logger.info("�ѵ�Feild����:"+fields.size());
		
		LinkedList<Document> documents=this.selectDocument(fields);
		
		logger.info("�ѵ�Document����:"+documents.size());
		return documents;
	}
	
	
	public LinkedList<Field> searchField(String term) throws Exception{
		
		LinkedList<Long> fields_id=new LinkedList<Long>();
		LinkedList<Field> fields=new LinkedList<Field>();
		fields_id = CURD.selectIndex(term).getTokens_id();	
		logger.info("�ѵ�Field������"+fields_id.size());
		
			
		//ͬ��������
		CountDownLatch latch=new CountDownLatch(1);
		QueryFieldThread thread=new QueryFieldThread(latch);
		thread.setFields(fields_id);
		thread.run();
		
		//
		latch.await();		
		fields=thread.getSearchResult();
		return fields;
	}
	public LinkedList<Field> Sort_ID(LinkedList<Field> result){
		return result;
	}
	
	//�������ȼ�����
	public LinkedList<Field> Sort_Priority(LinkedList<Field> result){
		
		return result;
	}
		
	//�������
	public LinkedList<Document> mergeSearch(String str) throws SQLException, Exception{
		//�ִ�
		SimpleAnalyzer analyzer=new SimpleAnalyzer(str,true);
		LinkedList<String> terms=analyzer._analyzer();
		for(String s1:terms){
			logger.info(s1);
		}
		
		//�õ�LinkedList<SearchResult>
		ArrayList<LinkedList<Field>> search_result=
				new ArrayList<LinkedList<Field>>(terms.size());

		//��ÿһ��������������
		for(int i=0;i<terms.size();i++){
			search_result.add(i, this.searchField(terms.get(i)));
		}
		
		//�Դ�֮���ƥ��ȣ�����
		logger.info("��ʼƥ��:");
		for(int i=1;i<terms.size();i++){
			matcher(search_result.get(i-1),search_result.get(i),terms.get(i).length());
		}
		LinkedList<Field> fields=new LinkedList<Field>();
		//
		for(int i=0;i<search_result.size();i++){
			
			LinkedList<Field> search_fields=search_result.get(i);			
			//ȫ������
			while(!search_fields.isEmpty()){
				fields.addLast(search_fields.removeFirst());
			}			
			
		}		
		
		//����ͬƥ��ȵ�document����rank����
		logger.info("����Rank��������:");
		LinkedList<Document> documents=new LinkedList<Document>();
		documents=this.selectDocument(fields);
		
		ShellSort.Sort(documents, new DocumentCompare_Rank());
		return documents;
	}
	//���ݴ�֮���ƥ��̶Ƚ����������ȼ�
	public void matcher(LinkedList<Field> result1,LinkedList<Field> result2,int term_size) throws IOException, Exception{
		

		//Ϊ�յĻ�������
		if(result1.isEmpty()||result2.isEmpty()){
			return;
		}
		
		//ת��Ϊ����
		Field[] fields=new Field[result2.size()];
	    for(int i=0;i<result2.size();i++){
	    	fields[i]=result2.get(i);
	    }
	    
	    //ƥ��
		for(int i=0;i<result1.size();i++){
			
			Field f=new Field("",result1.get(i).getID(),term_size);
			int r=Arrays.binarySearch(fields,f,new FieldCompare_ID());
			
			if(r>=0){
				
				//��������Ƴ�result1�е�Field,result2�е�Field priority+10
				if(r<result2.size()){
					result1.remove(i);
					int matcher=Integer.parseInt(result2.get(r).getAttriubte("matcher"))+10;
					logger.info("result2�е�:"+r+"��������ƥ���");
					result2.get(r).alterAttribute("matcher", String.valueOf(matcher));
				}
			}
			
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Search search = new Search();
		LinkedList<Document> result = search.search("dongfangbubai");
		long end = System.currentTimeMillis();
		System.out.println("��ʱΪ��" + (end - start) + "ms  ������:"+result.size()+"�����");
		for(Document document:result){
			System.out.println(document.getIndex_attribute("keyword"));
		}
		System.out.println("-------------------------------------------------------");
		LinkedList<Document> documents=search.mergeSearch("lejie dongfangbubai");
		for(Document document:documents){
			System.out.println("url:"+document.getUrl());
			System.out.println("keyword:"+document.getIndex_attribute("keyword"));
			System.out.println("ranks:"+document.getRanks());
			System.out.println("matcher:"+Integer.parseInt(document.getStore_attriubte("matcher")));
			System.out.println("------------------------------");
		}
	}
}
