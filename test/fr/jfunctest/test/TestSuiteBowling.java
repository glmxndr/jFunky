package fr.jfunctest.test;

import fr.jfunctest.BaseTestSuite;

public class TestSuiteBowling extends BaseTestSuite {

	@Override
	public void beforeSuite() {
		super.beforeSuite();
		System.out.println("Before Suite !");
	}
	
	@Override
	public void afterSuite() {
		super.afterSuite();
		System.out.println("After Suite !");
	}
	
	public static void main(String[] args) {
		
		TestSuiteBowling suite = new TestSuiteBowling();
		suite.addTest(TestBowling.class);
		suite.runTests();
	}
	
}
