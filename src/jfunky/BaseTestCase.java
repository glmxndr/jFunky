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
package jfunky;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jfunky.annotations.After;
import jfunky.annotations.Before;
import jfunky.annotations.Test;
import jfunky.assertion.Assertion;


/**
 * 
 * Extend this class to write a test.
 * 
 * You must use the {@link Test} annotations to set public methods as testable.
 * 
 * Methods with {@link Before} annotation will be run before the Tests,
 * although the order in which they are run is not guaranteed.
 * 
 * Methods with {@link After} annotation will be run before the Tests,
 * although the order in which they are run is not guaranteed.
 * 
 * @author G.Andrieu
 */
public class BaseTestCase {

	//+++++ STATIC ATTRIBUTES
	/** Requirement name for the assertions created through function() */
	private final static String FUNCTION_RQ = "FUNCTION";
	/** Requirement name for the assertions created through internal() */
	private final static String INTERNAL_RQ = "INTERNAL";
	//----- STATIC ATTRIBUTES
	
	//+++++ ATTRIBUTES
	/**
	 * Mapping the requirements to the assertions made on them.
	 */
	private final Map<String,ArrayList<Assertion>> requirements = new TreeMap<String, ArrayList<Assertion>>();
	//----- ATTRIBUTES
	
	//+++++ TO BE OVERRIDEN 
	/**
	 * Override this method to put the test set up.
	 */
	public void beforeAll(){}
	
	/**
	 * Override this method to put the test tear down.
	 */
	public void afterAll(){}
	//----- TO BE OVERRIDEN
	
	//+++++ PROTECTED
	/**
	 * This instanciates an assetion for the given requirement
	 * @param name the requirement name
	 * @return a new Assertion instance, recorded in the Map of 
	 * requirements of this test. Once instanciated, this assertion may
	 * be rendered, so any assertion instanciated through this method
	 * should be checked, or else it will be flagged as a failure by
	 * default.
	 */
	protected final Assertion requirement(String name){
		if (!this.requirements.containsKey(name)){
			this.requirements.put(name, new ArrayList<Assertion>());
		}
		Assertion result = new Assertion();
		this.requirements.get(name).add(result);
		return result;
	}
	
	/**
	 * Used by {@link BaseTestSuite} when a method invocation throws 
	 * an exception.
	 * @param name the function name
	 * @return a new assertion
	 */
	final Assertion function(String name){
		return requirement(FUNCTION_RQ).setMessage(name);
	}
	
	/**
	 * Use this to instanciate an assertion on the test internal state.
	 * For example, when you want to make sure that such File you are reading
	 * exists, because the file is mandatory to the test execution,
	 * although the existence of the file is not specified by any requirement
	 * (for you are in a testing environment).
	 * @param desc the description of the test
	 * @return an new assertion
	 */
	protected final Assertion internalTest(String desc){
		return requirement(INTERNAL_RQ).setMessage(desc);
	}
	//----- PROTECTED

	//+++++ PUBLIC
	/**
	 * Counts ALL the tests done within this test at the time of invocation
	 * @return the number of assertions recorded 
	 */
	public int getTestTotalCount() {
		int cpt = 0;
		for (String req : this.requirements.keySet()){
			cpt += this.requirements.get(req).size();
		}
		return cpt;
	}

	/**
	 * Counts the number of failed tests at the time of invocation
	 * @return the total number of failed assertions
	 */
	public int getTestFailuresCount() {
		int cpt = 0;
		for (String req : this.requirements.keySet()){
			for (Assertion a : this.requirements.get(req)){
				if (!a.isOk()){
					cpt++;
				}
			}
		}
		return cpt;
	}


	//----- PUBLIC
	
	//+++++ ACCESSORS
	/**
	 * Returns all the requirements used in this test.
	 * @return a Set of Strings
	 */
	public Set<String> getRequirements() {
		return this.requirements.keySet();
	}

	/**
	 * Find assertions by requirement
	 * @param requirement the name of the requirement
	 * @return all the assertions made on the given test, empty list
	 * if the requirement is not found
	 */
	public List<Assertion> getAssertions(String requirement) {
		try{
			return this.requirements.get(requirement);
		}
		catch(Exception e){
			return new ArrayList<Assertion>();
		}
	}
	//----- ACCESSORS
}
