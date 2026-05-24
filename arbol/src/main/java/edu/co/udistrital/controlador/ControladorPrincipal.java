package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.vista.VentanaPrincipal;

public class ControladorPrincipal {
    
    private VentanaPrincipal vistaPrincipal;
    private ArbolBPlus<Integer, Boleta> baseDatosBoletas;

    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vistaPrincipal = vista;
        this.baseDatosBoletas = new ArbolBPlus<>(4); 
        
        configurarEventos();
    }

    private void configurarEventos() {
        // Le inyectamos los nuevos paneles al manejador
        ManejadorEventos manejador = new ManejadorEventos(
            baseDatosBoletas, 
            vistaPrincipal.getControlesEntrada(), 
            vistaPrincipal.getVisualizadorArbol()
        );
        
        // ¡Ojo! Ya no necesitamos el addActionListener aquí porque 
        // el ManejadorEventos ya se encarga de cablear los 3 botones por dentro.
    }

    public void arrancar() {
        vistaPrincipal.setVisible(true);
    }
}