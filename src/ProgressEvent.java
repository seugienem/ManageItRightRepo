import java.util.EventObject;

public class ProgressEvent extends EventObject{
	private int progressValue;
	
	public ProgressEvent(Object source, int progressValue){
		super(source);
		this.progressValue = progressValue;
	}
	
	public int getProgress(){
		return progressValue;
	}
}