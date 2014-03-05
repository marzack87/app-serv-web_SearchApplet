/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gestione_annuncio;

import asw1016.HTTPClient;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

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
    
    private ArrayList<Map> result;
    private Box B_vertical;
    
    @Override
    public void init() {
        
        //inizializzo arraylist risultati
        result = new ArrayList<Map>();
        
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
        JCB_tipomenu.setSize(100, 35);
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
        JCB_postiliberimenu.setSize(100, 35);
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
        B_tipo.add(Box.createRigidArea(new Dimension(10,0)));
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
        
        /*
        JP_generalpanel.add(B_address);
        JP_generalpanel.add(B_costo);
        JP_generalpanel.add(B_tipo);
        //JP_generalpanel.add(B_posti);
        */
        
        B_vertical = new Box(BoxLayout.Y_AXIS);
        B_vertical.setSize(600, 350);
        B_vertical.add(B_address);
        B_vertical.add(Box.createVerticalStrut(20));
        B_vertical.add(B_tipo);
        B_vertical.add(Box.createVerticalStrut(20));
        B_vertical.add(B_costo);
        B_vertical.add(Box.createVerticalStrut(20));
        
        
        
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
        //JP_generalpanel.add(B_button);
        B_vertical.add(B_button);
        //JP_generalpanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        B_vertical.add(new JSeparator(SwingConstants.HORIZONTAL));
        //JP_generalpanel.setLayout(new GridLayout(0,1));
        JP_generalpanel.setSize(600, 400);
        
       
        
        //TABLE VIEW
            
        Object[][] values = new Object[][]{{ "" ,""}};
        String[] colnames = {"Immagine", "Descrizione"};
            
        model = new ImageTableModel(values,colnames);
        
        //JPanel JP_tableview = new JPanel();
        JT_table = new JTable();
        JT_table.setModel(model);
        JT_table.setRowHeight(100);
        JT_table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() >= 1){
                    JT_table = (JTable) e.getSource();
                    int row = JT_table.getSelectedRow();
                    if (row >= 0){
                        try {
                            String id_target = (String) result.get(row).get("id_apartment");
                            String dest = "/public_webapp/AnnuncioServlet?id_apartment="+id_target;
                            //JOptionPane.showMessageDialog(null, "url= "+new URL(getCodeBase().getProtocol(), getCodeBase().getHost(),
                            //                            getCodeBase().getPort(), dest));
                            getAppletContext().showDocument(new URL(getCodeBase().getProtocol(), getCodeBase().getHost(),
                                                        getCodeBase().getPort(), dest ), "_top");
                        }
                        catch (MalformedURLException ex) {
                            Logger.getLogger(SearchApplet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        Dimension d = new Dimension(600, 400);
        JT_table.setPreferredScrollableViewportSize(d);
        JS_scrollPane = new JScrollPane(JT_table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //JS_scrollPane.add(JT_table);
        JS_scrollPane.setSize(600, 400);
        Box B_table = new Box(BoxLayout.PAGE_AXIS);
        B_table.setSize(600, 400);
        B_table.add(JS_scrollPane);
        
        
        //JP_generalpanel.add(B_table);
        B_vertical.add(B_table);
        
        JP_generalpanel.add(B_vertical);
        
        getContentPane().add(Box.createHorizontalGlue());
        getContentPane().add(JP_generalpanel,BorderLayout.LINE_START);

    }
    
    
    @Override
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
                                parameter[3] = "0";
                                break;
                            case 2: 
                                parameter[3] = "1";
                                break;
                            case 3: 
                                parameter[3] = "2";
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
        
        private Document createXMLRequest(String [] parameters) throws ParserConfigurationException
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();
            // create the root element node
            Element search = doc.createElement("search");
            doc.appendChild(search);
            
            Element indirizzo_el = doc.createElement("indirizzo");
            search.appendChild(indirizzo_el);

            Text text = doc.createTextNode(parameters[0]);
            indirizzo_el.appendChild(text);
            
            Element prezzo_el = doc.createElement("prezzo");
            search.appendChild(prezzo_el);

            Text text2 = doc.createTextNode(parameters[1]);
            prezzo_el.appendChild(text2);
            
            Element posti_liberi_el = doc.createElement("posti_liberi");
            search.appendChild(posti_liberi_el);

            Text text3 = doc.createTextNode(parameters[2]);
            posti_liberi_el.appendChild(text3);
            
            Element tipologia_el = doc.createElement("tipologia");
            search.appendChild(tipologia_el);

            Text text4 = doc.createTextNode(parameters[3]);
            tipologia_el.appendChild(text4);
            
            JOptionPane.showMessageDialog(null, "XML creato");
            
            return doc;
        }
        
        private ArrayList<Map> handleResponse (Document doc)
        {
            NodeList apartments = doc.getElementsByTagName("search_result");
            
            ArrayList<Map> list = new ArrayList<Map>();
            
            for (int i = 0; i < apartments.getLength(); i++)
            {
                Node node = apartments.item(i);
                NodeList results = node.getChildNodes();
                
                Map <String, String> map = new HashMap<String,String>();
                
                for (int k = 0; k < results.getLength(); k++)
                {
                    if ("img".equals(results.item(k).getNodeName()))
                    {
                        map.put("img", results.item(k).getTextContent());
                        
                    } else if ("description".equals(results.item(k).getNodeName()))
                    {
                        map.put("description", results.item(k).getTextContent());
                    } else if ("id_apartment".equals(results.item(k).getNodeName()))
                    {
                        map.put("id_apartment", results.item(k).getTextContent());
                    }
                }
                
                list.add(map);

            }
            
            return list;
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
                            HTTPClient client_request = new HTTPClient();
                            String servlet_adress = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + ":" + getCodeBase().getPort() + "/public_webapp/SearchServlet";
                            System.out.println(servlet_adress);
                            Document doc = client_request.execute(servlet_adress, createXMLRequest(this.parameters));
                            
                            // receive result from servlet
                            if (running != false)
                            {
                                result = handleResponse(doc);
                                
                                // show result
                                if (running != false)
                                {
                                    //JTF_address.setText(result);
                                    //JOptionPane.showMessageDialog(null, "Appartamenti trovati:"+result.size());
                                    
                                     //cancella righe model.removeRow()
                                    int count = model.getRowCount();
                                    for(int y = 0; y<count; y++){
                                        model.removeRow(y);
                                    }
                                    
                                    for(int i = 0; i<result.size(); i++){
                                        URL img_photo;
                                       
                                        if (!result.get(i).get("img").equals(""))
                                        {
                                            img_photo = new URL(getCodeBase().getProtocol(), getCodeBase().getHost(),getCodeBase().getPort(),"/public_webapp/multimedia/photos/"+result.get(i).get("img"));
                                        } else {
                                            img_photo = new URL(getCodeBase().getProtocol(), getCodeBase().getHost(),getCodeBase().getPort(), "/public_webapp/multimedia/photos/no_foto.png");
                                        }
                                        
                                        
                                        
                                        ImageIcon photo = new ImageIcon(img_photo);

                                        int width = photo.getIconWidth();
                                        int height = photo.getIconHeight();

                                        double ratio = (double) width / (double) height;

                                        int final_height = 100;
                                        int final_width = (int) (ratio * final_height);
                                        
                                        Image im = photo.getImage();
                                        im = im.getScaledInstance(final_width, final_height, Image.SCALE_SMOOTH);
                                        photo.setImage(im);

                                        Object[] newRow = {photo,result.get(i).get("description")};
                                        model.insertRow(0, newRow);
                       
                                    }
                                    
                                    //notifico il possibile aggiornamento della tabella
                                    model.fireTableDataChanged();
                                    
                                }
                            }
                        }
                        
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.toString());
                        //JOptionPane.showMessageDialog(null, "Appartamenti trovati:"+ex.toString());
		}
                
            }
        }
        
        public class ImageTableModel extends DefaultTableModel{
            
            public ImageTableModel(Object[][] data, Object[] columnNames){
                super(data,columnNames);
            }
            
            public boolean isCellEditable(int row, int col){
                return false;
            }
            
            public Class getColumnClass(int column){

                if (column == 0){
                    return Icon.class;
                }
                return Object.class;
            }
        }

}
