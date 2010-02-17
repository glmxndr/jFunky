package fr.jfunctest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import fr.jfunctest.annotations.After;
import fr.jfunctest.annotations.Before;
import fr.jfunctest.annotations.Ignore;
import fr.jfunctest.annotations.Test;

public abstract class BaseTestSuite {

	private TestCaseRenderer renderer;
	private final List<BaseTestCase> tests = new ArrayList<BaseTestCase>();
	
	public void beforeSuite(){}
	
	public void runTests(){
		for (BaseTestCase testcase: tests){
			try{
				Class<?> clazz = testcase.getClass();
				
				// Before
				testcase.beforeAll();
				for (Method m : clazz.getMethods()){
					m.getDeclaredAnnotations();
					if (m.getAnnotation(Before.class) != null){
						m.invoke(testcase, new Object[]{});	
					}
				}
				
				// Run tests
				for (Method m : clazz.getMethods()){
					if ((m.getModifiers() & Modifier.PUBLIC) == 0) continue;
					if (m.getName().equals("beforeAll")) continue;
					if (m.getAnnotation(Before.class) != null) continue;
					if (m.getAnnotation(After.class) != null) continue;
					if (m.getName().equals("afterAll")) continue;
					if (m.getAnnotation(Ignore.class) != null) continue;
					if (m.getAnnotation(Test.class) != null){
						m.invoke(testcase, new Object[]{});	
					}
				}
				
				// After
				for (Method m : clazz.getMethods()){
					if (m.getAnnotation(After.class) != null){
						m.invoke(testcase, new Object[]{});	
					}
				}
				testcase.afterAll();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void afterSuite(){}

	
	//+++++ ACCESSORS
	public TestCaseRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(TestCaseRenderer renderer) {
		this.renderer = renderer;
	}
	
	public void addTest(Class<? extends BaseTestCase> testClass) {
		try{
			this.tests.add(testClass.newInstance());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//----- ACCESSORS
	
}
