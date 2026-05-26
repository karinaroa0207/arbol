package edu.co.udistrital.vista;

import edu.co.udistrital.modelo.BoletaDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel especializado en presentar una boleta ya convertida a DTO.
 * Mantiene la visualización del dato de negocio fuera del controlador y evita
 * que la interfaz dependa del formato textual de {@code toString()}.
 */
public class PanelBoletaDetalle extends JPanel {

    private final Color FONDO_OSCURO = new Color(25, 25, 30);
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);
    private final Color TEXTO_SECUNDARIO = new Color(175, 175, 185);

    public PanelBoletaDetalle(BoletaDTO boleta) {
        setLayout(new GridBagLayout());
        setBackground(FONDO_OSCURO);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        agregarContenido(boleta);
    }

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
