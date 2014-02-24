/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gestione_annuncio;

import static java.awt.FlowLayout.LEFT;
import java.awt.*;
import static java.awt.BorderLayout.PAGE_END;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyleConstants;

/**
 *
 * @author andreabuscarini
 */
public class SearchApplet extends JApplet implements ActionListener{

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    
    JTextField JTF_address;
    JComboBox JCB_tipomenu;
    JComboBox JCB_postiliberimenu;
    JTextField JTF_costomax;
    
    private Thread thread = null;
    private SearchThread runnable = null;
    
    private ImageTableModel model;
    private JTable JT_table;
    private JScrollPane JS_scrollPane;
    
    private ArrayList<Apartment> result;
    Lock lock;
    
    @Override
    public void init() {
        
        //inizializzo arraylist risultati
        result = new ArrayList<Apartment>();
        
        //intestazione
        getContentPane().setBackground(Color.decode("#F5F5F5"));
        
        //cerca indirizzo
        JLabel JL_address = new JLabel("Via: ");
        JL_address.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JTF_address = new JTextField(20);
        JTF_address.setBackground(Color.WHITE);
        JTF_address.setEditable(true);
        JTF_address.setVisible(true);
        Dimension D_address = new Dimension(450, 35);
        JTF_address.setMaximumSize(D_address);
        
        Box B_address = new Box(BoxLayout.X_AXIS);
        
        B_address.setBackground(Color.decode("#F5F5F5"));
        B_address.add(JL_address);
        B_address.add(JTF_address);
        
        //cerca tipologia edificio
        JLabel JL_tipo = new JLabel("Tipologia: ");
        JL_tipo.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JCB_tipomenu = new JComboBox();
        JCB_tipomenu.addItem("Seleziona");
        JCB_tipomenu.addItem("Appartamento");
        JCB_tipomenu.addItem("Villetta");
        JCB_tipomenu.addItem("Casa Indipendente");
        
        Box B_tipo = new Box(BoxLayout.X_AXIS);
        
        B_tipo.setBackground(Color.decode("#F5F5F5"));
        B_tipo.add(JL_tipo);
        B_tipo.add(JCB_tipomenu);
        //B_tipo.add(Box.createHorizontalStrut(300));
        
        JLabel JL_postilib = new JLabel("Posti Liberi");
        JL_postilib.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JCB_postiliberimenu = new JComboBox();
        JCB_postiliberimenu.addItem("Seleziona");
        JCB_postiliberimenu.addItem("1");
        JCB_postiliberimenu.addItem("2");
        JCB_postiliberimenu.addItem("3");
        JCB_postiliberimenu.addItem("4");
        JCB_postiliberimenu.addItem("5");
        JCB_postiliberimenu.addItem("6");
        JCB_postiliberimenu.addItem("7");
        JCB_postiliberimenu.addItem("8");
        JCB_postiliberimenu.addItem("9");
        JCB_postiliberimenu.addItem("10");
        
        //Box B_posti = new Box(BoxLayout.X_AXIS);
        
        //B_posti.setBackground(Color.decode("#F5F5F5"));
        B_tipo.add(Box.createRigidArea(new Dimension(30,0)));
        B_tipo.add(JL_postilib);
        B_tipo.add(JCB_postiliberimenu);
        
        //cerca per costo
        JLabel JL_costomax = new JLabel("Costo massimo per persona:  ");
        JL_costomax.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JTF_costomax = new JTextField(5);
        JTF_costomax.setEditable(true);
        JTF_costomax.setVisible(true);
        Dimension D_costomax = new Dimension(100, 35);
        JTF_costomax.setMaximumSize(D_costomax);
        
        Box B_costo = new Box(BoxLayout.X_AXIS);
        
        B_costo.setBackground(Color.decode("#F5F5F5"));
        B_costo.add(JL_costomax);
        B_costo.add(JTF_costomax);

        //panel generale
        JPanel JP_generalpanel = new JPanel();
        JP_generalpanel.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JP_generalpanel.setBackground(Color.decode("#F5F5F5"));
        
        JP_generalpanel.add(B_address);
        JP_generalpanel.add(B_costo);
        JP_generalpanel.add(B_tipo);
        //JP_generalpanel.add(B_posti);
        
        
        JButton JB_new = new JButton("Reset");
        JB_new.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JB_new.addActionListener(this);
        JButton JB_search = new JButton("Cerca");
        JB_search.setFont(new Font("OpenSans", Font.PLAIN, 16));
        JB_search.addActionListener(this);
        
        Box B_button = new Box(BoxLayout.X_AXIS);
        B_button.add(Box.createHorizontalGlue());
        B_button.add(JB_new);
        B_button.add(Box.createHorizontalGlue());
        B_button.add(JB_search);
        B_button.add(Box.createHorizontalGlue());
        JP_generalpanel.add(B_button);
        JP_generalpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JP_generalpanel.setLayout(new GridLayout(0,1));
        JP_generalpanel.setSize(700, 300);
        
       
        
        //TABLE VIEW
//        JTextArea JTA_control;
//        JTextArea JTA_photo;
//        JTA_photo = new JTextArea();
//        JTA_photo.setText(r);
//        JTA_photo.setVisible(true);
           
//        ImageIcon photo = new ImageIcon(u);
//        JLabel JL_photo = new JLabel(photo);
            
        Object[][] values = new Object[][]{{},{}};
        String[] colnames = {"Immagine", "Descrizione"};
            
        model = new ImageTableModel(values,colnames);
         
        //JPanel JP_tableview = new JPanel();
        JT_table = new JTable();
        JT_table.setModel(model);
        JT_table.setRowHeight(100);
        JS_scrollPane = new JScrollPane(JT_table);
        Box B_table = new Box(BoxLayout.PAGE_AXIS);
        B_table.setSize(600, 350);
        B_table.add(JS_scrollPane);
        
        
        JP_generalpanel.add(B_table);
        
        getContentPane().add(Box.createHorizontalGlue());
        getContentPane().add(JP_generalpanel,BorderLayout.LINE_START);

    }
    
    public void actionPerformed(ActionEvent e) {
        
            //trova la sorgente dell'evento
            JButton b = (JButton) e.getSource();
            String selected = b.getText();
            if(selected == "Reset"){
                 //azzera i campi e i risultati
                 //eviterei di reloadare l'applet se possibile senza sbroccare
                JTF_address.setText("");
                JCB_tipomenu.setSelectedIndex(-1);
                JCB_postiliberimenu.setSelectedIndex(-1);
                JTF_costomax.setText("");
   
            }
            else if(selected == "Cerca")
            {
                    //prendi i paramentri settati
                    String parameter[] = new String[4];
                    
                    //recupero l'indirizzo
                    if(JTF_address.getText() == null){
                        parameter[0] = "";
                    }else{
                        parameter[0] = JTF_address.getText();
                    }
                    
                    //recupero costo max
                    if(JTF_costomax.getText() == null){
                        parameter[1] = "";
                    }else{
                        parameter[1] = JTF_costomax.getText();
                    }
                    
                    //recupero posti liberi
                    switch (JCB_postiliberimenu.getSelectedIndex()){
                        case (-1):
                            parameter[2] = "";
                            break;
                        case (0):
                            parameter[2] = "";
                            break;
                        case 1:
                            parameter[2] = "1";
                            break;
                        case 2:
                            parameter[2] = "2";
                            break;
                        case 3:
                            parameter[2] = "3";
                            break;
                        case 4:
                            parameter[2] = "4";
                            break;
                        case 5:
                            parameter[2] = "5";
                            break;
                        case 6:
                            parameter[2] = "6";
                            break;
                        case 7:
                            parameter[2] = "7";
                            break;
                        case 8:
                            parameter[2] = "8";
                            break;
                        case 9:
                            parameter[2] = "9";
                            break;
                        case 10:
                            parameter[2] = "10";
                            break;
                    }
                    
                    //recupero tipomenu
                    if(JCB_tipomenu.getSelectedIndex() == -1 || JCB_tipomenu.getSelectedIndex() == 0){
                        parameter[3] = "";
                    }else{
                        System.out.println(JCB_tipomenu.getSelectedIndex());
                        switch (JCB_tipomenu.getSelectedIndex()){
                            case 1: 
                                parameter[3] = "Appartamento";
                                break;
                            case 2: 
                                parameter[3] = "Villetta";
                                break;
                            case 3: 
                                parameter[3] = "Casa Indipendente";
                                break;
                        }
                    }
                    
                    //qui
                    if (thread != null)
                    {
                        runnable.terminate();
                        //Questo non serve, perchè in pratica blocca fino a che thread nn ha finito l'operazione
 //                       try {
 //                           thread.join();
 //                       } catch (InterruptedException ex) {
 //                           Logger.getLogger(SearchApplet.class.getName()).log(Level.SEVERE, null, ex);
 //                       }
                    }
                    
                    runnable = new SearchThread(parameter);
                    thread = new Thread(runnable); 
                    thread.start();
                    
            }
        }
    
	/**
	 * Get a connection to the servlet.
	 */
	private URLConnection getServletConnection()
		throws MalformedURLException, IOException {

		// Connection zum Servlet öffnen
                //URL urlServlet = new URL(getCodeBase()+"SearchServlet");
                URL urlServlet = new URL(getCodeBase().getProtocol(), getCodeBase().getHost(), 
                        getCodeBase().getPort(), "/public_webapp/SearchServlet");
                
		URLConnection con = urlServlet.openConnection();

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestProperty(
			"Content-Type",
			"application/x-java-serialized-object");

		return con;
	}
        
        public class SearchThread implements Runnable {
            private String[] parameters;
            private volatile boolean running = true;
            
            public SearchThread(String [] param) {
                this.parameters = param;
            }

            public void terminate() {
                running = false;
            }
            
            public synchronized void run() {

                try {
			// send data to the servlet
                        if (running != false)
                        {
                            URLConnection con = getServletConnection();
                            OutputStream outstream = con.getOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(outstream);
                            oos.writeObject(this.parameters);
                            oos.flush();
                            oos.close();
                            
                            // receive result from servlet
                            if (running != false)
                            {
                                InputStream instr = con.getInputStream();
                                ObjectInputStream inputFromServlet = new ObjectInputStream(instr);
                                result = (ArrayList<Apartment>) inputFromServlet.readObject();
                                inputFromServlet.close();
                                instr.close();
                            
                                // show result
                                if (running != false)
                                {
                                    //JTF_address.setText(result);
                                    JOptionPane.showMessageDialog(null, "Numero Appartamenti trovati:"+result.size());
                                    
                                    String URL_image;
                                    String descrizione;
                                    for(int i = 0; i<result.size(); i++){
                                        URL_image = result.get(i).img_url;
                                        descrizione = "" + result.get(i).tipologia + " posto in " + result.get(i).address + 
                                                      ", " + result.get(i).civico + " a " + result.get(i).citta + " di propietà di "
                                                      + result.get(i).user_owner + ". /n Posti Liberi: " + result.get(i).posti_liberi 
                                                      + " /n Prezzo per persona: " + result.get(i).prezzo + " €";
                                        ImageIcon photo = new ImageIcon(URL_image);
                                        JLabel JL_photo = new JLabel(photo);
                        
                                        Object[] newRow = {JL_photo,descrizione};
                                        model.addRow(newRow);
                       
                                    }
                                    
                                    //notifico il possibile aggiornamento della tabella
                                    model.fireTableDataChanged();
                                    
                                }
                            }
                        }
                        
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.toString());
		}
                
            }
        }
        
        public class ImageTableModel extends DefaultTableModel{
            
            public ImageTableModel(Object[][] data, Object[] columnNames){
                super(data,columnNames);
            }

            public Class getColumnClass(int column){

                if (column == 0){
                    return Icon.class;
                }
                return Object.class;
            }
        }

}
