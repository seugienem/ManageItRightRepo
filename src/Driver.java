import java.awt.Dimension;
import java.io.IOException;
import javax.swing.WindowConstants;

class Driver {
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	public void run(){
        		DataManager dm = new DataManager();
                Event event = new Event();
                Logic lg = new Logic(event, dm);
                
        		GUI tp;
				try {
					tp = new GUI(lg);
					tp.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		            tp.setSize(800, 600);
	                tp.setMinimumSize(new Dimension(800, 600));	                
	                tp.setVisible(true);
				} catch (IOException e) {			
					e.printStackTrace();
				}
        	}
        });

        
	}
}