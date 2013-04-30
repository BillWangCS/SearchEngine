/**
 * 
 */
package com.search.index;

import java.util.LinkedList;

import com.search.data.Document;
import com.search.data.Field;

/**
 * @author niubaisui
 *
 */
public class IndexWriter_to_Database implements IndexWriter{

	private LinkedList<Document> documents=new LinkedList<Document>();
	private LinkedList<Field> fields=new LinkedList<Field>();
	private LinkedList<Token_Structure> tokens=new LinkedList<Token_Structure>();
	

	public IndexWriter_to_Database(LinkedList<Document> documents){
		this.documents=documents;
	}
	//��Ҫ�ķ���
	@Override
	public void indexWriter() throws Exception {
		int size=documents.size();
		int count=size/100;
		//document_thread�߳������Ϊ5
		if(count>5){
			count=5;
		}
		
		//
		if(size%100!=0){
			count++;
		}
		
		LinkedList<Document> document=new LinkedList<Document>();
		int n=0;
		for(int i=0;i<count;i++){
			
			if(i==count-1){
				for(int j=0;j<documents.size()-(count-1)*100;j++){
					document.add(documents.get(n++));
				}
			}
			else{
				for(int j=0;j<100;j++){
					document.add(documents.get(n++));
				}
			}
			SimpleMergeIndex mergeindex=new SimpleMergeIndex(document);
			mergeindex.mergeIndex();
			
			//����һ��document_thread�߳�
			DocumentThread document_thread=new DocumentThread(document);
			document_thread.run();
			
			//����һ��field_thread�߳�
			FieldThread field_thread=new FieldThread(mergeindex.getField());
			field_thread.run();
			
			//����һ��index�߳�
			IndexThread index_thread=new IndexThread(mergeindex.getToken_Structure());
			index_thread.run();
		}
	}
	
	public LinkedList<Document> getDocuments() {
		return documents;
	}
	
	public LinkedList<Field> getFields() {
		return fields;
	}
	
	public LinkedList<Token_Structure> getTokens() {
		return tokens;
	}
	
}
