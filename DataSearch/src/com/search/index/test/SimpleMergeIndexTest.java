/**
 * 
 */
package com.search.index.test;


import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.search.data.Document;
import com.search.data.Field;
import com.search.index.SimpleMergeIndex;
import com.search.index.Token_Structure;

/**
 * @author niubaisui
 *
 */
public class SimpleMergeIndexTest {

	/**
	 * @throws java.lang.Exception
	 */
	private LinkedList<Document> documents;
	private SimpleMergeIndex simplemergeindex;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		LinkedList<Document> documents=new LinkedList<Document>();
		Document document1=new Document(1l);
		document1.addIndex_attribute("keyword", "�˽�");
		Document document2=new Document(2l);
		document2.addIndex_attribute("keyword", "dongfangbubei");
		Document document3=new Document(3l);
		document3.addIndex_attribute("keyword", "lejie");
		Document document4=new Document(4l);
		document4.addIndex_attribute("keyword", "aiwo");
		Document document5=new Document(5l);
		document5.addIndex_attribute("keyword", "dongfangbubei guliang dongfangbubei");
		Document document6=new Document(6l);
		document6.addIndex_attribute("keyword", "dongfangbubei ����");
		
		documents=new LinkedList<Document>();
		documents.add(document1);
		documents.add(document2);
		documents.add(document3);
		documents.add(document4);
		documents.add(document5);
		documents.add(document6);
		
		simplemergeindex=new SimpleMergeIndex(documents);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		documents=null;
		simplemergeindex=null;
		
	}

	/**
	 * Test method for {@link com.search.index.SimpleMergeIndex#getToken_Structure()}.
	 */
	@Test
	public void testGetToken_Structure() {
		
	}

	/**
	 * Test method for {@link com.search.index.SimpleMergeIndex#getDocuments()}.
	 */
	@Test
	public void testGetDocuments() {
		
	}

	/**
	 * Test method for {@link com.search.index.SimpleMergeIndex#getField()}.
	 * @throws Exception 
	 */
	@Ignore
	public void testGetField() throws Exception {
		simplemergeindex.mergeIndex();
		LinkedList<Field> fields=simplemergeindex.getField();
		for(Field f:fields){
			System.out.println(f.getID());
			System.out.println(f.getText());
			System.out.println("---------------------");
		}
		
	}

	/**
	 * Test method for {@link com.search.index.SimpleMergeIndex#mergeIndex()}.
	 * @throws Exception 
	 */
	@Test
	public void testMergeIndex() throws Exception {
		simplemergeindex.mergeIndex();
		LinkedList<Token_Structure> tokens_structure=simplemergeindex.getToken_Structure();
		for(Token_Structure s:tokens_structure){
			System.out.println(s.getTerm());
			System.out.println(s.getFrequency());
			for(long n:s.getTokens_id()){
				System.out.println(n);
			}
			System.out.println("-----------------------");
		}
	}

	/**
	 * Test method for {@link com.search.index.SimpleMergeIndex#Sort()}.
	 */
	@Test
	public void testSort() {
		
	}

}
