import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.logging.*;
import javax.comm.*;
import static javax.comm.SerialPort.*;

public class Modelo {
    static Enumeration puertos;
    static CommPortIdentifier idPort;
    static SerialPort pSerial;
    private Vista miVista;
    private Controlador miControlador;
    private int bitDeDatos, bitDeStop, paridad, control;
    static OutputStream salida;
    static InputStream entrada;
    static byte[] datos;
    static evSerie ev = new evSerie();

    public void setMiControlador(Controlador miControlador) {
        this.miControlador = miControlador;
    }

    public void setMiVista(Vista miVista) {
        this.miVista = miVista;
    }
 
    public void buscarPuertos(){
        puertos = CommPortIdentifier.getPortIdentifiers();
        miControlador.mostrarPuertos(puertos);
    }
    
    public void configurarPuerto(String nombre, int p1, int p2, float p3, String p4, String p5){ 
        llenarParametros(p2,p3,p4, p5);
        try{
            idPort = CommPortIdentifier.getPortIdentifier(nombre);
            
        }catch(NoSuchPortException e){
            System.err.println("ERROR al identificar puerto");
        }
        try{
           pSerial = (SerialPort) idPort.open(nombre, 0);
        }catch(PortInUseException e){
            System.err.println("ERROR puerto en uso");
        }
        try {
            pSerial.addEventListener(ev);
            pSerial.notifyOnDataAvailable(true);
            pSerial.notifyOnOutputEmpty(true);
            pSerial.notifyOnCTS(true);
            pSerial.notifyOnDSR(true);
            pSerial.notifyOnRingIndicator(true);
            pSerial.notifyOnCarrierDetect(true);
            pSerial.notifyOnParityError(true);
            pSerial.notifyOnOverrunError(true);
            pSerial.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            pSerial.setSerialPortParams(p1, bitDeDatos, bitDeStop, paridad);
            pSerial.setFlowControlMode(control);
        }catch(UnsupportedCommOperationException e){
            System.err.println("ERROR valores no soportados");
        }
    } 
    
    public void llenarParametros(int p2, float p3, String p4, String p5){
       
        switch(p2){
            case 5:
                this.bitDeDatos = DATABITS_5;
                break;
            case 6:
                this.bitDeDatos = DATABITS_6;
                break;
            case 7:
                this.bitDeDatos = DATABITS_7; 
                break;
            case 8:
                this.bitDeDatos = DATABITS_8;
                break;
        }
        
        if(p3 == 1.0)
            this.bitDeStop = STOPBITS_1;
        if(p3 == 1.5)
            this.bitDeStop = STOPBITS_1_5;
        if(p3 == 2.0)
            this.bitDeStop = STOPBITS_2;
            
        switch(p4){
            case "NONE":
                this.paridad = PARITY_NONE;
                break;
            case "ODD":
                this.paridad = PARITY_ODD;
                break;        
            case "EVEN":
                this.paridad = PARITY_EVEN;
                break;
            case "MARK":
                this.paridad = PARITY_MARK;
                break;
            case "SPACE":
                this.paridad = PARITY_SPACE;
                break;
        }
        
        switch(p4){
            case "NONE":
                this.control = FLOWCONTROL_NONE;
                break;
            case "HARDWARE":
                this.control = FLOWCONTROL_RTSCTS_IN;
                break;        
            case "Xon/Xoff":
                this.control = FLOWCONTROL_RTSCTS_IN;
                break;
        }
    }
    
    public void enviarDatos(String dato){

        try{
            salida = pSerial.getOutputStream();
        }catch(IOException e){
        }
        try{
            salida.write(dato.getBytes());
        }catch(IOException e){
        }   
        
        if(ev.getCadenaDatos() != null)
            recibirDatos();
        
    }
    
    public void recibirDatos(){
        miControlador.recibirDatos(ev.getCadenaDatos());
    }
    
    
    
    public static class evSerie implements SerialPortEventListener{

        private String cadena = null;
        private String datosRecibidos;
        public evSerie() {}
        
        @Override
        public void serialEvent(SerialPortEvent evento) {
            if (evento.getEventType() == SerialPortEvent.DATA_AVAILABLE){
                byte[] readBuffer = new byte[100];
                try{
                    entrada = pSerial.getInputStream();
                }catch(IOException e){
                }
                try {
                    while (entrada.available() > 0) {
                        int numBytes = entrada.read(readBuffer);
                    }
                    cadena = new String(readBuffer);
                    if(datosRecibidos == null)
                        datosRecibidos = cadena;
                    else
                        datosRecibidos += cadena;
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        
        String getCadenaDatos(){
            return datosRecibidos;
        }
        
        void setCadenaDatos(){
            datosRecibidos = null;
        }
    }
           
}


    
