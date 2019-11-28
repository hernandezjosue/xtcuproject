import java.util.*;
import javax.comm.*;
import javax.swing.DefaultListModel;
public class Controlador {
    private Vista miVista;
    private Modelo miModelo;

    public void setMiVista(Vista miVista) {
        this.miVista = miVista;
    }
    public void setMiModelo(Modelo miModelo) {
        this.miModelo = miModelo;
    }  
    
    public void mostrarPuertos(Enumeration puerto){
        int i = 0;
        CommPortIdentifier idPort;
        DefaultListModel lista = new DefaultListModel<>();
        while(puerto.hasMoreElements()){
            idPort = (CommPortIdentifier) puerto.nextElement(); 
            if(idPort.getPortType() == CommPortIdentifier.PORT_SERIAL )
                lista.addElement(idPort.getName());
            i++;
        }
        miVista.mostrarPuertos(lista);
    }
    public void buscarPuertos(){
        miModelo.buscarPuertos();
    }
    
    public void confiurarPuerto(String nombre, int p1, int p2, float p3, String p4, String p5){
        miModelo.configurarPuerto(nombre,p1,p2,p3,p4,p5);
    }
    
    public void enviarDatos(String dato){
        miModelo.enviarDatos(dato);
    }
    
    public void recibirDatos(String cad){
        System.out.println(cad);
        miVista.recibirDatos(cad);
    }
}
