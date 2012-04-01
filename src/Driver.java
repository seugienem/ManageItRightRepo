import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

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
					tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		            tp.setSize(800, 600);
	                tp.setMinimumSize(new Dimension(800, 600));
	                //tp.setMaximumSize(new Dimension(800, 600));
	                tp.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });

        
	}
}