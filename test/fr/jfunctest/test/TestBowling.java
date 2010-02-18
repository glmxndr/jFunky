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
