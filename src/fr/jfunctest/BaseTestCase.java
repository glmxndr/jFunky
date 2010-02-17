package fr.jfunctest;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class BaseTestCase {

	private final Map<String,ArrayList<Assertion>> requirements = new TreeMap<String, ArrayList<Assertion>>();
	

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
	
	
	
}
