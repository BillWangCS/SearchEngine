/**
 * 
 */
package com.search.Search.test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.search.Search.QueryResultThread;
import com.search.data.Field;

/**
 * @author niubaisui
 *
 */
public class QueryResultThreadTest {

	/**
	 * @throws java.lang.Exception
	 */
	
	private static QueryResultThread thread;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		thread=new QueryResultThread();
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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.search.Search.QueryResultThread#getSearchResult()}.
	 */
	@Test
	public void testGetSearchResult() {
		
	}

	/**
	 * Test method for {@link com.search.Search.QueryResultThread#run()}.
	 */
	@Test
	public void testRun() {
		LinkedList<Long> fields=new LinkedList<Long>();
		fields.add(2199024304129l);
		thread.setFields(fields);
		thread.run();
		LinkedList<Field> f=thread.getSearchResult();
		for(Field field:f){
			System.out.println(field.getText());
			System.out.println(field.getAttriubte("keyword"));
		}
		
	}

}