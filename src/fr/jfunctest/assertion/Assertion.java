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
package fr.jfunctest.assertion;

public class Assertion {

	private String message = "";
	private boolean ok;
	private String failureDesc = ""; 

	public Assertion() {}

	public void fails(String desc){
		this.ok = false;
		this.failureDesc = "";
	}
	
	public void fails(Throwable e){
		this.ok = false;
		this.failureDesc = e.getClass().getCanonicalName() + " says: " + e.getMessage();
	}
	
	public Assertion implyingThat(String message){
		this.message = message;
		return this;
	}

	public void isMetWithTrue(boolean value){
		this.ok = value;
	}
	
	public void isMetWithFalse(boolean value){
		this.ok = !value;
	}
	
	public void isMetWithNull(Object o){
		this.ok = (o == null);
	}
	
	public void isMetWithNotNull(Object o){
		this.ok = (o != null);
	}
	
	public void isMetWithEquality(Object a, Object b){
		this.ok = (a.equals(b));
	}
	
	public void isMetWithDifference(Object a, Object b){
		this.ok = (!a.equals(b));
	}
	
	public void isMetWithEquality(boolean a, boolean b){
		this.ok = (a == b);
	}
	
	public void isMetWithDifference(boolean a, boolean b){
		this.ok = (a != b);
	}

	public void isMetWithEquality(long a, long b){
		this.ok = (a == b);
	}
	
	public void isMetWithDifference(long a, long b){
		this.ok = (a != b);
	}

	public void isMetWithEquality(short a, short b){
		this.ok = (a == b);
	}
	
	public void isMetWithDifference(short a, short b){
		this.ok = (a != b);
	}

	public void isMetWithEquality(double a, double b){
		this.ok = (a == b);
	}
	
	public void isMetWithDifference(double a, double b){
		this.ok = (a != b);
	}

	public void isMetWithEquality(float a, float b){
		this.ok = (a == b);
	}
	
	public void isMetWithDifference(float a, float b){
		this.ok = (a != b);
	}

	public void isMetWithEquality(char a, char b){
		this.ok = (a == b);
	}
	
	public void isMetWithDifference(char a, char b){
		this.ok = (a != b);
	}

	public void isMetWithCheck(Checker checker){
		try{
			this.ok = checker.check();
		}
		catch(Exception e){
			this.ok = false;
		}
	}

	public boolean isOk() {
		return this.ok;
	}

	public boolean hasMessage(){
		return this.message != null && this.message.length() > 0;
	}
	
	public String getMessage() {
		return message;
	}

	public Assertion setMessage(String desc) {
		this.message = desc;
		return this;
	}

	public boolean hasFailureDescription() {
		return this.failureDesc != null && this.failureDesc.length() > 0;
	}

	public String getFailureDescription() {
		return this.failureDesc;
	}
	
}
