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
package jfunky.assertion;

public abstract class Checker {
	
	public abstract boolean check();
	
    public void assertTrue(boolean test) {
        if (!test){
        	throw new CheckException();
        }
    }
    
    public void assertFalse(boolean test) {
        if (test){
        	throw new CheckException();
        }
    }
    
    public void assertNotNull(Object o) {
        if (o == null){
        	throw new CheckException();
        }
    }
    
    public void assertNull(Object o) {
        if (o != null){
        	throw new CheckException();
        }
    }
    
    public void fail(){
    	throw new CheckException();
    }
}
