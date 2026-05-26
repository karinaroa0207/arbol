package edu.co.udistrital.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel superior de la interfaz.
 * Contiene el input y el botón de acción con un diseño limpio y espaciado correcto.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class PanelBusqueda extends JPanel {
    
    /** Campo de texto para ingresar el ID de la boleta a buscar. */
    private JTextField txtIdBoleta;
    
    /** Botón que ejecuta la acción de validación/búsqueda. */
    private JButton btnBuscar;

    /** Color de fondo oscuro para el panel. */
    private final Color FONDO_OSCURO = new Color(18, 18, 22);
    
    /** Color neón azul para bordes y acentos. */
    private final Color NEON_AZUL = new Color(0, 229, 255);
    
    /** Color neón fucsia para el botón de acción. */
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    
    /** Color para texto claro sobre fondo oscuro. */
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    /**
     * Construye el panel de búsqueda.
     * Configura el fondo, layout e inicializa los componentes visuales.
     */
    public PanelBusqueda() {
        setBackground(FONDO_OSCURO);
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        inicializarComponentes();
    }

    /** Inicializa y organiza todos los componentes visuales del panel. */
    private void inicializarComponentes() {
        JLabel lbl = new JLabel("ID Boleta:");
        lbl.setForeground(TEXTO_CLARO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        txtIdBoleta = new JTextField(12);
        txtIdBoleta.setBackground(new Color(28, 28, 34));
        txtIdBoleta.setForeground(Color.WHITE);
        txtIdBoleta.setCaretColor(NEON_AZUL);
        txtIdBoleta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        txtIdBoleta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEON_AZUL, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));

        btnBuscar = new JButton("VALIDAR");
        btnBuscar.setBackground(NEON_FUCSIA);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setBorder(new EmptyBorder(10, 20, 10, 20));

        add(lbl);
        add(txtIdBoleta);
        add(btnBuscar);
    }

    /**
     * Obtiene el ID ingresado por el usuario en el campo de texto.
     * @return ID de la boleta como String
     */
    public String getIdIngresado() { return txtIdBoleta.getText(); }
    
    /**
     * Obtiene el botón de búsqueda/validación.
     * @return Botón Validar
     */
    public JButton getBtnBuscar() { return btnBuscar; }
}