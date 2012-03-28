import java.util.EventListener;

public interface ProgressListener extends EventListener{

	public void progressEventOccured(ProgressEvent evt);
}
