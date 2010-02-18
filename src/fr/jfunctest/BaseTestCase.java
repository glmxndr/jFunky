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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.jfunctest.assertion.Assertion;
import fr.jfunctest.render.Renderer;

public class BaseTestCase {

	private final Map<String,ArrayList<Assertion>> requirements = new TreeMap<String, ArrayList<Assertion>>();
	
	private Renderer renderer;
	
	public void beforeAll(){}
	
	public void afterAll(){}
	
	protected final Assertion requirement(String name){
		if (!this.requirements.containsKey(name)){
			this.requirements.put(name, new ArrayList<Assertion>());
		}
		Assertion result = new Assertion();
		this.requirements.get(name).add(result);
		return result;
	}
	
	protected final Assertion function(String name){
		return requirement(name);
	}
	
	protected final Assertion internalTest(String desc){
		return requirement("INTERNAL TEST").setMessage(desc);
	}

	public Renderer getRenderer() {
		return renderer;
	}


	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public int getTestTotalCount() {
		int cpt = 0;
		for (String req : this.requirements.keySet()){
			cpt += this.requirements.get(req).size();
		}
		return cpt;
	}

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

	public Set<String> getRequirements() {
		return this.requirements.keySet();
	}

	public List<Assertion> getAssertions(String requirement) {
		try{
			return this.requirements.get(requirement);
		}
		catch(Exception e){
			return new ArrayList<Assertion>();
		}
	}
	
}
