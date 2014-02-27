/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visione_foto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import static javax.swing.SwingConstants.HORIZONTAL;
import javax.swing.border.LineBorder;

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
        
        JSB_scrollbarH = new JScrollBar(HORIZONTAL);
        
        JSP_scrollpane = new JScrollPane(JSB_scrollbarH);
        JSP_scrollpane.setBackground(Color.decode("#F5F5F5"));
        JSP_scrollpane.setVisible(true);
        JSP_scrollpane.setSize(new Dimension(600, 600));
        JSP_scrollpane.setViewportBorder(new LineBorder(Color.decode("#F5F5F5")));

        JSP_scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSP_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      
        final String  parameter = getParameter("parameter");
        
        String[] input;
        input = parameter.split("#");
        
        JLabel[] photo = new JLabel[3];
        
        Box B_photo = new Box(BoxLayout.X_AXIS);
        
        for(int i=0; i<input.length; i++){
            
            URL photo_url = null;
            if (input[i] != null){
                try {
                    photo_url = new URL(getCodeBase().getProtocol(), getCodeBase().getHost(),getCodeBase().getPort(),"/public_webapp/multimedia/photos/"+input[i]);
                    //JOptionPane.showMessageDialog(null, "url="+photo_url.toString());
                    
                } catch (MalformedURLException ex) {
                    Logger.getLogger(viewApplet.class.getName()).log(Level.SEVERE, null, ex);
                }
                ImageIcon II_photo = new ImageIcon(photo_url);

                int width = II_photo.getIconWidth();
                int height = II_photo.getIconHeight();

                double ratio = (double) width / (double) height;

                int final_height = 300;
                int final_width = (int) (ratio * final_height);

                Image img = II_photo.getImage();
                img = img.getScaledInstance(final_width, final_height, Image.SCALE_SMOOTH);
                II_photo.setImage(img);
                
               
                photo[i] = new JLabel(II_photo);
                
                B_photo.add(photo[i]);
                B_photo.add(Box.createHorizontalStrut(25));
            }
        }
        JSP_scrollpane.setViewportView(B_photo);
        JSP_scrollpane.getViewport().setBackground(Color.decode("#F5F5F5"));
        JSP_scrollpane.setViewportBorder(new LineBorder(Color.decode("#F5F5F5")));
        getContentPane().add(JSP_scrollpane);
        
        
    }
    
}
