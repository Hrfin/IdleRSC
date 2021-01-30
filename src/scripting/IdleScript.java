package scripting;

import controller.Controller;

/**
 * This is the base class from which all IdleScript scripts are derived. 
 * 
 * Note the functions which are currently implemented below are meant to be overriden for functionality purposes.
 * 
 * @author Dvorak
 *
 */

public abstract class IdleScript {
	Controller controller = null;
	
	public void serverMessageInterrupt(String message) {
		
	}
	
	public void chatMessageInterrupt(String message) { 
		
	}
	
	public void npcMessageInterrupt(String message) {
		
	}
	
	public void tradeMessageInterrupt(String message) {
		
	}
	
	public void setController(Controller _controller) {
		controller = _controller;
	}
	
    public void start(String parameters[])
    {
    	System.out.println("If you see this, your script did not come with the start function.");
    }

}
