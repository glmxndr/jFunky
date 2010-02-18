//    JFuncTest - Functional testig in Java
//    Copyright (C) 2010 G. Andrieu (subtenante gmail com)
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
package fr.jfunctest;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import fr.jfunctest.annotations.After;
import fr.jfunctest.annotations.Before;
import fr.jfunctest.annotations.Ignore;
import fr.jfunctest.annotations.Test;
import fr.jfunctest.render.Renderer;

public class BaseTestSuite {

	private Renderer renderer;
	private final List<BaseTestCase> tests = new ArrayList<BaseTestCase>();
	
	private File folderOut = new File(""); 
	
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
					if ((m.getModifiers() & Modifier.STATIC) > 0) continue;
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
				
				
				// Render
				File outFile = new File(this.folderOut,"TEST-"+clazz.getCanonicalName()+".xml");
				PrintWriter writer = new PrintWriter(outFile);
				synchronized (this.renderer) {
					this.renderer.setWriter(writer);
					this.renderer.render(testcase);
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void afterSuite(){}

	
	//+++++ ACCESSORS
	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
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

	public void setFolderOut(File folderOut) {
		this.folderOut = folderOut;
	}
	//----- ACCESSORS
	
}
