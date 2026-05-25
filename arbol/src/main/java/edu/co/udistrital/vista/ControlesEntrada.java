package edu.co.udistrital.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Barra de herramientas superior (Toolbar).
 * Diseño horizontal comprimido para maximizar el espacio del lienzo del árbol.
 */
public class ControlesEntrada extends JPanel {
    private JTextField txtIdValidar, txtIdInsertar;
    private JButton btnValidar, btnInsertar, btnReiniciar;

    private final Color FONDO_OSCURO = new Color(18, 18, 22);
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    public ControlesEntrada() {
        setBackground(FONDO_OSCURO);
        
        // LA MAGIA DE LA COMPRESIÓN: Usamos FlowLayout horizontal
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); 
        
        // Restringimos la altura máxima para que no empuje al árbol hacia abajo
        setMaximumSize(new Dimension(2000, 60)); 
        setBorder(new EmptyBorder(5, 10, 15, 10)); 
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- BLOQUE 1: VALIDAR ---
        JPanel panelValidar = crearBloqueCompacto();
        panelValidar.add(crearEtiqueta("ID Validar:"));
        txtIdValidar = crearCampoTexto();
        btnValidar = crearBoton("VALIDAR", NEON_FUCSIA);
        btnValidar.setActionCommand("Validar");
        panelValidar.add(txtIdValidar);
        panelValidar.add(btnValidar);

        // --- BLOQUE 2: INSERTAR ---
        JPanel panelInsertar = crearBloqueCompacto();
        panelInsertar.add(crearEtiqueta("ID Insertar:"));
        txtIdInsertar = crearCampoTexto();
        btnInsertar = crearBoton("INSERTAR", NEON_AZUL);
        btnInsertar.setActionCommand("Insertar");
        panelInsertar.add(txtIdInsertar);
        panelInsertar.add(btnInsertar);

        // --- BLOQUE 3: REINICIAR ---
        btnReiniciar = crearBoton("REINICIAR", new Color(100, 100, 110));
        btnReiniciar.setActionCommand("Reiniciar");

        // Ensamblar en una sola línea horizontal
        add(panelValidar);
        
        // Separador vertical sutil
        JLabel separador = new JLabel(" | ");
        separador.setForeground(Color.GRAY);
        add(separador);
        
        add(panelInsertar);
        add(separador);
        add(btnReiniciar);
    }

    private JPanel crearBloqueCompacto() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(TEXTO_CLARO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(8); // Más corto (8 caracteres)
        txt.setBackground(new Color(28, 28, 34));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(NEON_AZUL);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setBorder(BorderFactory.createLineBorder(NEON_AZUL, 1, true));
        return txt;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Letra un poco más pequeña
        btn.setFocusPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12)); // Padding reducido
        return btn;
    }      
    
    public void agregarListenerBotones(ActionListener al) {
        btnInsertar.addActionListener(al); 
        btnReiniciar.addActionListener(al); 
        btnValidar.addActionListener(al); 
    }
    
    public void limpiarCajaInsertar() {
        txtIdInsertar.setText("");
    }
    /**
     * Metodo para enfocar un boton
     * 
     * @param btn nombre del boton a enfocar
     */
    public void focusBtn(String btn) {
        JButton b = null;
        switch (btn) {
            case "Insertar" -> b = btnInsertar;
            case "Validar" -> b = btnValidar;
            case "Reiniciar" -> b = btnReiniciar;
            default -> {
            }
        }      
        if(b != null) {
            b.requestFocus();
        }
    }

    // Getters
    public String getIdValidar() { return txtIdValidar.getText(); }
    public String getIdInsertar() { return txtIdInsertar.getText(); }
}