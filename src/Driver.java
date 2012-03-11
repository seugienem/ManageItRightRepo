import java.awt.Dimension;
import javax.swing.JFrame;

class Driver {
	public static void main(String[] args) {
	        DataManager dm = new DataManager();
	        Event event = new Event();
	        Logic lg = new Logic(event, dm);
	        GUI tp = new GUI(lg);
	        
	        tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        tp.setSize(800, 600);
	        tp.setMinimumSize(new Dimension(800, 600));
	        tp.setMaximumSize(new Dimension(800, 600));
	        tp.setVisible(true);
	        
	    }
}