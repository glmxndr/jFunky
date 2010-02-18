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
