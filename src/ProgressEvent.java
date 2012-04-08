/*
 * ProgressEvent.java - A customized event used by TableAssigner.java.
 * Authors: Team MIR
 * 
 * 
 * Description: This inherits the generic event object. It helps
 * TableAssigner to fire whenever there's progress in during the running
 * of the algorithm.
 */

import java.util.EventObject;

public class ProgressEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	private int progressValue;
	
	public ProgressEvent(Object source, int progressValue){
		super(source);
		this.progressValue = progressValue;
	}
	
	public int getProgress(){
		return progressValue;
	}
}