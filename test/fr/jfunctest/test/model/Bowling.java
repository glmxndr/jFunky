package fr.jfunctest.test.model;

public class Bowling {

	int down = 0;
	
	public void hit(int n){
		this.down+=n;
	}
	
	public int result(){
		return this.down;
	}
	
	
}
