/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visione_foto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
       
        JSP_scrollpane = new JScrollPane();
        JSP_scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSP_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        /*
        ImageIcon i1 = new ImageIcon("Applets/build/classes/visione_foto/308.jpg");
        ImageIcon i2 = new ImageIcon("/classes/visione_foto/309.jpg");
        ImageIcon i3 = new ImageIcon("310.jpg");
       
        JLabel jl1 = new JLabel(i1);
        JLabel jl2 = new JLabel(i2);
        JLabel jl3 = new JLabel(i3);
        
        JSP_scrollpane.add(jl1);
        JSP_scrollpane.add(jl2);
        JSP_scrollpane.add(jl3);
        */
        
        final String  parameter = getParameter("parameter");
        
        String[] input;
        input = parameter.split("#");
        
        JLabel[] photo = new JLabel[3];
        
        for(int i=0; i<input.length; i++){
            
            URL photo_url = null;
            if (input[i] != null){
                try {
                    photo_url = new URL(getCodeBase().getProtocol(), getCodeBase().getHost(),getCodeBase().getPort(),"/public_webapp/multimedia/photos/"+input[i]);
                    JOptionPane.showMessageDialog(null, "url="+photo_url.toString());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(viewApplet.class.getName()).log(Level.SEVERE, null, ex);
                }
                ImageIcon II_photo = new ImageIcon(photo_url);
                photo[i] = new JLabel(II_photo);
                JSP_scrollpane.add(photo[i]);
                
            }
        }
        
        getContentPane().add(JSP_scrollpane);
        
        
    }
    
}
