package edu.co.udistrital.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Barra de herramientas superior (Toolbar).
 * Centraliza las operaciones básicas del Árbol B+: validación por clave,
 * inserción única, eliminación y búsqueda por rangos.
 */
public class ControlesEntrada extends JPanel {
    private JTextField txtIdValidar, txtIdInsertar, txtIdEliminar, txtRangoInicio, txtRangoFin;
    private JButton btnValidar, btnInsertar, btnEliminar, btnRango, btnReiniciar;

    private final Color FONDO_OSCURO = new Color(18, 18, 22);
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    public ControlesEntrada() {
        setBackground(FONDO_OSCURO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(2000, 145));
        setPreferredSize(new Dimension(1000, 125));
        setBorder(new EmptyBorder(4, 10, 12, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelValidar = crearBloqueCompacto();
        panelValidar.add(crearEtiqueta("ID Validar:"));
        txtIdValidar = crearCampoTexto();
        btnValidar = crearBoton("VALIDAR", NEON_FUCSIA);
        panelValidar.add(txtIdValidar);
        panelValidar.add(btnValidar);

        JPanel panelInsertar = crearBloqueCompacto();
        panelInsertar.add(crearEtiqueta("ID Insertar:"));
        txtIdInsertar = crearCampoTexto();
        btnInsertar = crearBoton("INSERTAR", NEON_AZUL);
        panelInsertar.add(txtIdInsertar);
        panelInsertar.add(btnInsertar);

        JPanel panelEliminar = crearBloqueCompacto();
        panelEliminar.add(crearEtiqueta("ID Eliminar:"));
        txtIdEliminar = crearCampoTexto();
        btnEliminar = crearBoton("ELIMINAR", new Color(255, 80, 80));
        panelEliminar.add(txtIdEliminar);
        panelEliminar.add(btnEliminar);

        JPanel panelRango = crearBloqueCompacto();
        panelRango.add(crearEtiqueta("Rango:"));
        txtRangoInicio = crearCampoTexto();
        txtRangoFin = crearCampoTexto();
        btnRango = crearBoton("BUSCAR RANGO", new Color(177, 0, 255));
        panelRango.add(txtRangoInicio);
        panelRango.add(crearEtiqueta("a"));
        panelRango.add(txtRangoFin);
        panelRango.add(btnRango);

        btnReiniciar = crearBoton("REINICIAR", new Color(100, 100, 110));

        JPanel filaPrincipal = crearFilaControles();
        filaPrincipal.add(panelValidar);
        filaPrincipal.add(crearSeparador());
        filaPrincipal.add(panelInsertar);

        JPanel filaSecundaria = crearFilaControles();
        filaSecundaria.add(panelEliminar);
        filaSecundaria.add(crearSeparador());
        filaSecundaria.add(panelRango);

        JPanel filaAcciones = crearFilaControles();
        filaAcciones.add(btnReiniciar);

        add(filaPrincipal);
        add(filaSecundaria);
        add(filaAcciones);
    }

    private JPanel crearFilaControles() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 3));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        fila.setMaximumSize(new Dimension(2000, 38));
        return fila;
    }

    private JPanel crearBloqueCompacto() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
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
        JTextField txt = new JTextField(6);
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private JLabel crearSeparador() {
        JLabel separador = new JLabel(" | ");
        separador.setForeground(Color.GRAY);
        return separador;
    }

    public String getIdValidar() { return txtIdValidar.getText(); }
    public String getIdInsertar() { return txtIdInsertar.getText(); }
    public String getIdEliminar() { return txtIdEliminar.getText(); }
    public String getRangoInicio() { return txtRangoInicio.getText(); }
    public String getRangoFin() { return txtRangoFin.getText(); }
    public JButton getBtnValidar() { return btnValidar; }
    public JButton getBtnInsertar() { return btnInsertar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnRango() { return btnRango; }
    public JButton getBtnReiniciar() { return btnReiniciar; }
}
