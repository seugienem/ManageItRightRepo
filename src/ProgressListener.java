/*
 * ProgressListener.java - A customized listener for progress events.
 * Authors: Team MIR
 * 
 * 
 * Description: This inherits the generic event listener and is customized
 * to generate Progress Events. (See ProgressEvent.java)
 */

import java.util.EventListener;

public interface ProgressListener extends EventListener{
	public void progressEventOccured(ProgressEvent evt);
}
