package edu.co.udistrital.vista;

import edu.co.udistrital.modelo.BoletaDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel especializado en presentar una boleta ya convertida a DTO.
 * Mantiene la visualización del dato de negocio fuera del controlador y evita
 * que la interfaz dependa del formato textual de {@code toString()}.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class PanelBoletaDetalle extends JPanel {

    /** Color de fondo oscuro para el panel. */
    private final Color FONDO_OSCURO = new Color(25, 25, 30);
    
    /** Color neón azul para títulos y acentos. */
    private final Color NEON_AZUL = new Color(0, 229, 255);
    
    /** Color para texto claro principal. */
    private final Color TEXTO_CLARO = new Color(230, 230, 230);
    
    /** Color para texto secundario o datos ausentes. */
    private final Color TEXTO_SECUNDARIO = new Color(175, 175, 185);

    /**
     * Construye el panel de detalle con los datos de una boleta.
     * 
     * @param boleta DTO con la información de la boleta a mostrar
     */
    public PanelBoletaDetalle(BoletaDTO boleta) {
        setLayout(new GridBagLayout());
        setBackground(FONDO_OSCURO);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        agregarContenido(boleta);
    }

    /**
     * Agrega el contenido visual del panel: título y líneas de información.
     * 
     * @param boleta DTO con los datos a mostrar (ID, evento, zona)
     */
    private void agregarContenido(BoletaDTO boleta) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel titulo = new JLabel("BOLETA VÁLIDA");
        titulo.setForeground(NEON_AZUL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titulo, gbc);

        gbc.gridy++;
        add(crearLinea("ID", String.valueOf(boleta.getIdBoleta())), gbc);

        gbc.gridy++;
        add(crearLinea("Evento", boleta.getEvento()), gbc);

        gbc.gridy++;
        add(crearLinea("Zona", boleta.getZona()), gbc);
    }

    /**
     * Crea una línea de texto con formato etiqueta: valor.
     * Si el valor está vacío o es null, muestra "Sin dato" y usa color secundario.
     * 
     * @param etiqueta Descripción del campo (ej. "ID", "Evento", "Zona")
     * @param valor Valor a mostrar junto a la etiqueta
     * @return JLabel configurada con el texto y estilos correspondientes
     */
    private JLabel crearLinea(String etiqueta, String valor) {
        JLabel linea = new JLabel(etiqueta + ": " + valor);
        linea.setForeground(TEXTO_CLARO);
        linea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        linea.setToolTipText(valor);

        if (valor == null || valor.isBlank()) {
            linea.setText(etiqueta + ": Sin dato");
            linea.setForeground(TEXTO_SECUNDARIO);
        }

        return linea;
    }
}