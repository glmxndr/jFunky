package fr.jfunctest.assertion;

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
