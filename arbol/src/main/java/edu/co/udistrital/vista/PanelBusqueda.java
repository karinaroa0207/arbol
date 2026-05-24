package edu.co.udistrital.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel superior de la interfaz. 
 * Contiene el input y el botón de acción con un diseño limpio y espaciado correcto.
 */
public class PanelBusqueda extends JPanel {
    private JTextField txtIdBoleta;
    private JButton btnBuscar;

    // Paleta Dark Premium
    private final Color FONDO_OSCURO = new Color(18, 18, 22);
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    public PanelBusqueda() {
        setBackground(FONDO_OSCURO);
        // Layout fluido centrado sin elementos complejos a los lados
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10)); 
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // 1. Etiqueta limpia
        JLabel lbl = new JLabel("ID Boleta:");
        lbl.setForeground(TEXTO_CLARO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // 2. Input de texto con Padding interno (para que el texto respire)
        txtIdBoleta = new JTextField(12);
        txtIdBoleta.setBackground(new Color(28, 28, 34));
        txtIdBoleta.setForeground(Color.WHITE);
        txtIdBoleta.setCaretColor(NEON_AZUL);
        txtIdBoleta.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        // Borde compuesto: Borde redondeado por fuera + Margen invisible por dentro
        txtIdBoleta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEON_AZUL, 1, true),
            new EmptyBorder(8, 12, 8, 12) 
        ));

        // 3. Botón estilizado
        btnBuscar = new JButton("VALIDAR");
        btnBuscar.setBackground(NEON_FUCSIA);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false); // Quita el recuadro punteado feo de Java
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de manito
        btnBuscar.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding del botón

        // Ensamblar
        add(lbl);
        add(txtIdBoleta);
        add(btnBuscar);
    }

    public String getIdIngresado() { return txtIdBoleta.getText(); }
    public JButton getBtnBuscar() { return btnBuscar; }
}