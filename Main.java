import javax.swing.*;

public class Main {
    public static void main(String args[]){
        Vista miVista = new Vista();
        Controlador miControlador = new Controlador();
        Modelo miModelo = new Modelo();
        miVista.setMiControlador(miControlador);
        miVista.setMiModelo(miModelo);
        miVista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        miVista.setVisible(true);
        
        miModelo.setMiControlador(miControlador);
        miModelo.setMiVista(miVista);
        
       miControlador.setMiModelo(miModelo);
       miControlador.setMiVista(miVista);
    }
}