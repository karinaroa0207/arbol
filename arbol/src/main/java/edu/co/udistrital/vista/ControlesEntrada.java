package edu.co.udistrital.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Barra de herramientas superior (Toolbar).
 * Centraliza las operaciones básicas del Árbol B+: validación por clave,
 * inserción única, eliminación y búsqueda por rangos.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class ControlesEntrada extends JPanel {
    
    /** Campos de texto para IDs de validación, inserción, eliminación y rango. */
    private JTextField txtIdValidar, txtIdInsertar, txtIdEliminar, txtRangoInicio, txtRangoFin;
    
    /** Spinner para seleccionar el orden (grado) del árbol. */
    private JSpinner spnOrden;
    
    /** Botones de acción: Validar, Insertar, Eliminar, Rango, Orden, Reiniciar. */
    private JButton btnValidar, btnInsertar, btnEliminar, btnRango, btnOrden, btnReiniciar;

    /** Color de fondo oscuro para la interfaz. */
    private final Color FONDO_OSCURO = new Color(18, 18, 22);
    
    /** Color neón azul para botones y acentos. */
    private final Color NEON_AZUL = new Color(0, 229, 255);
    
    /** Color neón fucsia para elementos de advertencia o peligro. */
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    
    /** Color para texto claro sobre fondo oscuro. */
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    /**
     * Construye el panel de controles de entrada.
     * Configura el fondo, layout, tamaño y componentes visuales.
     */
    public ControlesEntrada() {
        setBackground(FONDO_OSCURO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(2000, 145));
        setPreferredSize(new Dimension(1000, 125));
        setBorder(new EmptyBorder(4, 10, 12, 10));

        inicializarComponentes();
    }

    /** Inicializa y organiza todos los componentes visuales del panel. */
    private void inicializarComponentes() {
        JPanel panelValidar = crearBloqueCompacto();
        panelValidar.add(crearEtiqueta("ID Validar:"));
        txtIdValidar = crearCampoTexto();
        btnValidar = crearBoton("VALIDAR", NEON_FUCSIA);
        btnValidar.setActionCommand("Validar");
        panelValidar.add(txtIdValidar);
        panelValidar.add(btnValidar);

        JPanel panelInsertar = crearBloqueCompacto();
        panelInsertar.add(crearEtiqueta("ID Insertar:"));
        txtIdInsertar = crearCampoTexto();
        btnInsertar = crearBoton("INSERTAR", NEON_AZUL);
        btnInsertar.setActionCommand("Insertar");
        panelInsertar.add(txtIdInsertar);
        panelInsertar.add(btnInsertar);

        JPanel panelEliminar = crearBloqueCompacto();
        panelEliminar.add(crearEtiqueta("ID Eliminar:"));
        txtIdEliminar = crearCampoTexto();
        btnEliminar = crearBoton("ELIMINAR", new Color(255, 80, 80));
        btnEliminar.setActionCommand("Eliminar");
        panelEliminar.add(txtIdEliminar);
        panelEliminar.add(btnEliminar);

        JPanel panelRango = crearBloqueCompacto();
        panelRango.add(crearEtiqueta("Rango:"));
        txtRangoInicio = crearCampoTexto();
        txtRangoFin = crearCampoTexto();
        btnRango = crearBoton("BUSCAR RANGO", new Color(177, 0, 255));
        btnRango.setActionCommand("Rango");
        panelRango.add(txtRangoInicio);
        panelRango.add(crearEtiqueta("a"));
        panelRango.add(txtRangoFin);
        panelRango.add(btnRango);

        JPanel panelOrden = crearBloqueCompacto();
        panelOrden.add(crearEtiqueta("Orden:"));
        spnOrden = crearSelectorOrden();
        btnOrden = crearBoton("APLICAR", new Color(0, 180, 150));
        btnOrden.setActionCommand("Orden");
        panelOrden.add(spnOrden);
        panelOrden.add(btnOrden);

        btnReiniciar = crearBoton("REINICIAR", new Color(100, 100, 110));
        btnReiniciar.setActionCommand("Reiniciar");

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
        filaAcciones.add(crearSeparador());
        filaAcciones.add(panelOrden);

        add(filaPrincipal);
        add(filaSecundaria);
        add(filaAcciones);
    }

    /**
     * Crea una fila horizontal para organizar controles.
     * @return Panel con FlowLayout centrado
     */
    private JPanel crearFilaControles() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 3));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        fila.setMaximumSize(new Dimension(2000, 38));
        return fila;
    }

    /**
     * Crea un bloque compacto para agrupar controles relacionados.
     * @return Panel con FlowLayout izquierdo sin márgenes
     */
    private JPanel crearBloqueCompacto() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Crea una etiqueta con estilo visual oscuro.
     * @param texto Texto a mostrar
     * @return JLabel configurada
     */
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(TEXTO_CLARO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    /**
     * Crea un campo de texto con estilo visual neon.
     * @return JTextField configurado
     */
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

    /**
     * Crea un selector (spinner) para elegir el orden del árbol.
     * @return JSpinner configurado con valores de 3 a 10
     */
    private JSpinner crearSelectorOrden() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 3, 10, 1));
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spinner.setPreferredSize(new Dimension(54, 28));
        spinner.setBorder(BorderFactory.createLineBorder(NEON_AZUL, 1, true));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            JTextField txt = defaultEditor.getTextField();
            txt.setHorizontalAlignment(JTextField.CENTER);
            txt.setBackground(new Color(28, 28, 34));
            txt.setForeground(Color.WHITE);
            txt.setCaretColor(NEON_AZUL);
        }
        return spinner;
    }

    /**
     * Crea un botón con estilo personalizado.
     * @param texto Texto del botón
     * @param color Color de fondo
     * @return JButton configurado
     */
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

    /**
     * Crea un separador visual entre bloques de controles.
     * @return JLabel con texto "|"
     */
    private JLabel crearSeparador() {
        JLabel separador = new JLabel(" | ");
        separador.setForeground(Color.GRAY);
        return separador;
    }

    /** @return ID ingresado para validación */
    public String getIdValidar() { return txtIdValidar.getText(); }
    
    /** @return ID ingresado para inserción */
    public String getIdInsertar() { return txtIdInsertar.getText(); }
    
    /** @return ID ingresado para eliminación */
    public String getIdEliminar() { return txtIdEliminar.getText(); }
    
    /** @return Valor inicial del rango de búsqueda */
    public String getRangoInicio() { return txtRangoInicio.getText(); }
    
    /** @return Valor final del rango de búsqueda */
    public String getRangoFin() { return txtRangoFin.getText(); }
    
    /** @return Orden seleccionado en el spinner */
    public int getOrdenSeleccionado() { return (Integer) spnOrden.getValue(); }
    
    /** @return Botón Validar */
    public JButton getBtnValidar() { return btnValidar; }
    
    /** @return Botón Insertar */
    public JButton getBtnInsertar() { return btnInsertar; }
    
    /** @return Botón Eliminar */
    public JButton getBtnEliminar() { return btnEliminar; }
    
    /** @return Botón Búsqueda por Rango */
    public JButton getBtnRango() { return btnRango; }
    
    /** @return Botón Aplicar Orden */
    public JButton getBtnOrden() { return btnOrden; }
    
    /** @return Botón Reiniciar */
    public JButton getBtnReiniciar() { return btnReiniciar; }
    
    /**
     * Agrega un ActionListener a todos los botones del panel.
     * @param al Listener a registrar en cada botón
     */
    public void agregarListenerBotones(ActionListener al) {
        btnInsertar.addActionListener(al); 
        btnReiniciar.addActionListener(al); 
        btnValidar.addActionListener(al); 
        btnEliminar.addActionListener(al);
        btnRango.addActionListener(al); 
        btnOrden.addActionListener(al);
    }
    
    /** Limpia el contenido del campo de texto de inserción. */
    public void limpiarCajaInsertar() {
        txtIdInsertar.setText("");
    }
    
    /**
     * Enfoca (solicita el foco) a un botón específico por su nombre.
     * @param btn Nombre del botón a enfocar ("Insertar", "Validar", "Reiniciar", "Eliminar", "Rango", "Orden")
     */
    public void focusBtn(String btn) {
        JButton b = null;
        switch (btn) {
            case "Insertar" -> b = btnInsertar;
            case "Validar" -> b = btnValidar;
            case "Reiniciar" -> b = btnReiniciar;
            case "Eliminar" -> b = btnEliminar;
            case "Rango" -> b = btnRango;
            case "Orden" -> b = btnOrden;
            default -> {
            }
        }      
        if(b != null) {
            b.requestFocus();
        }
    }
}