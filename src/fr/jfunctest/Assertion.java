package fr.jfunctest;

public class Assertion {

	private String message = "";
	private boolean ok;
	private String reason;

	public Assertion() {}

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
	
}
