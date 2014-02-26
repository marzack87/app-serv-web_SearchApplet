/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visione_foto;

import java.awt.Color;
import java.awt.Font;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static javax.swing.SwingConstants.HORIZONTAL;
import static javax.swing.SwingConstants.VERTICAL;

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
    private JScrollBar JSB_scrollbarH;
    private JScrollBar JSB_scrollbarV;
    private JPanel JP_photopanel;
    
    public void init() {
       
        JLabel JL_label = new JLabel("Foto");
        JL_label.setBackground(Color.decode("#F5F5F5"));
        JL_label.setFont(new Font("OpenSans", Font.PLAIN, 16));
        
        JSP_scrollpane = new JScrollPane(JL_label,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSP_scrollpane.setBackground(Color.decode("#F5F5F5"));
        JSP_scrollpane.setVisible(true);
        
        JSB_scrollbarH = new JScrollBar(HORIZONTAL);
        JSB_scrollbarV = new JScrollBar(VERTICAL);
        
        JP_photopanel = new JPanel();
        JP_photopanel.setBackground(Color.decode("#F5F5F5"));
        JP_photopanel.setVisible(true);
        
        //JSP_scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //JSP_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
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
                //JLabel p = new JLabel(II_photo);
                JSP_scrollpane.setViewportView(photo[i]);
                
            }
        }
        JP_photopanel.add(JSP_scrollpane);
        getContentPane().add(JP_photopanel);
        
        
    }
    
}
