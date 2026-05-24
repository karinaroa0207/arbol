package edu.co.udistrital.controlador;

import edu.co.udistrital.controlador.ControladorPrincipal;
import edu.co.udistrital.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            ControladorPrincipal controlador = new ControladorPrincipal(vista);
            controlador.arrancar();
        });
    }
}
