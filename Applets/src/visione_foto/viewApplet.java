/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visione_foto;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author andreabuscarini
 */
public class viewApplet extends JApplet {

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    
    private JScrollPane JSP_scrollpane;
    private JScrollBar JSB_scrollbar;
    
    public void init() {
       
        final String  parameter = getParameter("parameter");
        
        String[] input;
        input = parameter.split("#");
        
        JLabel[] photo = new JLabel[3];
        
        for(int i=0; i<input.length; i++){
            
            //URL photo_url = new URL(input[i]);
            //ImageIcon II_photo = new ImageIcon(photo_url);
            //photo[i] = JLabel(II_photo);
        }
        
        
    }
    
}
