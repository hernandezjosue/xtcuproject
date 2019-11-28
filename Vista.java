import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Vista extends JFrame implements ActionListener{
    private JButton bPCSettings, bTerminal,bBuscar, bEnviar;
    private JLabel espacio;
    private JPanel pSuperior, pBase, pConfig, pTerm, pIzq, pDer,pDatos, pEnviar;
    private JTextArea emisor, receptor;
    private JList puertos;
    private JComboBox baudios, paridad, bits, parada, control; 
    private Controlador miControlador;
    private Modelo miModelo;

    public void setMiControlador(Controlador miControlador) {
        this.miControlador = miControlador;
    }

    public void setMiModelo(Modelo miModelo) {
        this.miModelo = miModelo;
    }

    public Vista(){
        super(":D");
        setLocation(150, 100);
        initComponentes();
        setSize(500, 400);
        setVisible(true);
    }
    
    public void initComponentes(){
        bPCSettings = new JButton("PC Settings");
        bTerminal = new JButton("Terminal");
        bPCSettings.setBorder(new Boton(10));
        bTerminal.setBorder(new Boton(10));
        bPCSettings.setBackground(Color.GRAY);
        bTerminal.setBackground(Color.LIGHT_GRAY);
        espacio = new JLabel(" ");
        pSuperior = new JPanel(new GridLayout(1,2));
        pSuperior.add(bPCSettings);
        pSuperior.add(bTerminal);
        pSuperior.add(espacio);
       
        puertos = new JList();
        pIzq = new JPanel(new BorderLayout());
        bBuscar = new JButton("Buscar Puertos");
        pIzq.add(puertos, BorderLayout.CENTER);
        pIzq.add(bBuscar, BorderLayout.SOUTH);
        
        baudios = new JComboBox();
        baudios.addItem("2400");
        baudios.addItem("4800");
        baudios.addItem("9600");
        baudios.addItem("19200");
        baudios.addItem("38400");
        baudios.setSelectedItem("9600");
        paridad = new JComboBox();
        paridad.addItem("5");
        paridad.addItem("6");
        paridad.addItem("7");
        paridad.addItem("8");
        paridad.setSelectedItem("8");
        parada = new JComboBox();
        parada.addItem("1");
        parada.addItem("1.5");
        parada.addItem("2");
        parada.setSelectedItem("1");
        bits = new JComboBox();
        bits.addItem("NONE");
        bits.addItem("ODD");
        bits.addItem("EVEN");
        bits.addItem("MARK");
        bits.addItem("SPACE");
        bits.setSelectedItem("NONE");
        control = new JComboBox();
        control.addItem("NONE");
        control.addItem("HARDWARE");
        control.addItem("Xon/Xoff");
        control.setSelectedItem("NONE");
        
        pDer = new JPanel();
        pDer.setLayout(new BoxLayout(pDer, BoxLayout.Y_AXIS));
        pDer.add(baudios);
        pDer.add(paridad);
        pDer.add(parada);
        pDer.add(bits);
        pDer.add(control);

        pConfig = new JPanel(new BorderLayout());
        pConfig.add(pIzq, BorderLayout.CENTER);
        pConfig.add(pDer, BorderLayout.EAST);
        
        emisor = new JTextArea();
        emisor.setBackground(Color.WHITE);
        emisor.setForeground(Color.BLUE);
        emisor.setLineWrap(true);
        receptor = new JTextArea();
        receptor.setBackground(Color.LIGHT_GRAY);
        receptor.setForeground(Color.RED);
        receptor.setLineWrap(true);
        receptor.setEditable(false);
        pEnviar = new JPanel();
        bEnviar = new JButton("ENVIAR MENSAJE");
        pEnviar.add(bEnviar);
        pTerm = new JPanel(new GridLayout(1,2));
        pTerm.add(emisor);
        pTerm.add(receptor);
        
        pBase = new JPanel(new BorderLayout());
        pBase.add(pSuperior, BorderLayout.NORTH);
        add(pBase,BorderLayout.NORTH);
        add(pTerm, BorderLayout.CENTER);
        add(pConfig, BorderLayout.CENTER);
        add(pEnviar, BorderLayout.SOUTH);
        pEnviar.setVisible(false);
        
        bPCSettings.addActionListener((ActionEvent e) -> {
            bPCSettings.setBackground(Color.GRAY);
            bTerminal.setBackground(Color.LIGHT_GRAY);
            add(pConfig, BorderLayout.CENTER);
            pTerm.setVisible(false);
            pConfig.setVisible(true);
            pEnviar.setVisible(false);
        });
        bTerminal.addActionListener((ActionEvent e) -> {

            bTerminal.setBackground(Color.GRAY);
            bPCSettings.setBackground(Color.LIGHT_GRAY);
            add(pTerm, BorderLayout.CENTER);
            pConfig.setVisible(false);
            pTerm.setVisible(true);
            pEnviar.setVisible(true);
            bEnviar.setEnabled(true);
            if(puertos.isSelectedIndex(0)){
                emisor.setText("");
                configurarPuerto();
                System.out.println();
            }else{
                emisor.setText("Seleccione algún puerto");
                emisor.setEditable(false);
                bEnviar.setEnabled(false);
            }
        });
        bBuscar.addActionListener((ActionEvent e) -> {
            miControlador.buscarPuertos();
            emisor.setEditable(true);
        });
        bEnviar.addActionListener((ActionEvent e) -> {
            miControlador.enviarDatos(emisor.getText());
        });
    }
    public void mostrarPuertos(DefaultListModel puerto){
       int i = 0;
       puertos.setModel(puerto);
       puertos.setSelectedIndex(0);
    }
    public void configurarPuerto(){
       int p1 = Integer.parseInt((String) baudios.getSelectedItem());
       int p2 = Integer.parseInt((String) paridad.getSelectedItem());
       float p3 = Integer.parseInt((String) parada.getSelectedItem());
       String p4 = (String) bits.getSelectedItem();
       String p5 = (String) control.getSelectedItem();
        miControlador.confiurarPuerto((String) puertos.getSelectedValue(), p1, p2, p3, p4, p5);
       
    }
    public void recibirDatos(String cad){
        System.out.println("cad:" + cad);
        receptor.setText(cad);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
