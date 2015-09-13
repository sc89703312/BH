package edu.nju.controller.msgqueue.operation;

import java.io.Serializable;

public abstract class MineOperation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isClient = false;
	
	public void setClient(){
		isClient = true;
	}
	
	public boolean getIsClient(){
		return isClient;
	}

	public abstract void execute();
}
