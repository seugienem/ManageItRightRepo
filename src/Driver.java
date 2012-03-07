import java.awt.Dimension;


import javax.swing.JFrame;

import java.util.*;

class Driver {



public static void main(String[] args) {
		Event eventOnStartUp = new Event();	//must init all variables
		Logic lg = new Logic(eventOnStartUp);
        GUI tp = new GUI(lg);
        tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tp.setSize(800, 600);
        tp.setMinimumSize(new Dimension(800, 600));
        tp.setMaximumSize(new Dimension(800, 600));
        tp.setVisible(true);
        
    }
}