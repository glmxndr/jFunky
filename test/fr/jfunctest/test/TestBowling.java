package fr.jfunctest.test;

import fr.jfunctest.BaseTestCase;
import fr.jfunctest.annotations.After;
import fr.jfunctest.annotations.Before;
import fr.jfunctest.annotations.Ignore;
import fr.jfunctest.annotations.Test;
import fr.jfunctest.assertion.Checker;
import fr.jfunctest.test.model.Bowling;

public class TestBowling extends BaseTestCase {

	@Override
	public void beforeAll() {
		super.beforeAll();
		System.out.println("TestBowling - Before All !");
	}
	
	@Before public void before1(){
		System.out.println("TestBowling - Before 1 !");
	}
	
	@Before public void before2(){
		System.out.println("TestBowling - Before 2 !");
	}
	
	
	@Test @Ignore
	public void testIgnored(){
		System.out.println("TestBowling - Ignored test : Should not see this !");
	}
	
	@Test
	public void testGutter(){
		
		requirement("BR0001")
			.implyingThat("8 gutter means no point")
			.isMetWithCheck(new Checker(){
				public boolean check() {
					Bowling b = new Bowling();
					for (int i=0;i<8;i++){
						b.hit(0);
					}
					return b.result() == 0;
				}
			});
		
	}

	@After public void after1(){
		System.out.println("TestBowling - After 1 !");
	}
	
	@After public void after2(){
		System.out.println("TestBowling - After 2 !");
	}
	
	
	
	@Override
	public void afterAll() {
		super.beforeAll();
		System.out.println("TestBowling - After All !");
	}
	
	
}
