package edu.co.udistrital.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de renderizado del nodo B+.
 * Utiliza Java 2D Graphics para crear un efecto Glassmorphism elegante
 * con gradientes oscuros y acentos de color.
 */
public class PanelVisualizador extends JPanel {
    
    // Paleta de colores
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    private final Color TEXTO_GRIS = new Color(170, 170, 180);

    // Estado del panel
    private String idVisual = "";
    private String eventoVisual = "";
    private String zonaVisual = "";
    private boolean huboBusqueda = false;
    private boolean fueEncontrado = false;

    public PanelVisualizador() {
        setOpaque(false); // Transparente para que el Frame dibuje su fondo oscuro
    }

    public void actualizarDatos(String id, String evento, String zona, boolean encontrado) {
        this.idVisual = id;
        this.eventoVisual = evento;
        this.zonaVisual = zona;
        this.fueEncontrado = encontrado;
        this.huboBusqueda = true;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Calidad máxima de renderizado (bordes ultra suaves)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int padding = 50;
        int width = getWidth() - (padding * 2);
        int height = 280;

        // 1. DIBUJAR EFECTO CRISTAL (Glassmorphism con Gradiente)
        // Crea un degradado sutil de gris oscuro a negro translúcido
        GradientPaint gradienteCristal = new GradientPaint(
            padding, 20, new Color(45, 45, 55, 220), 
            padding, 20 + height, new Color(20, 20, 25, 220)
        );
        g2d.setPaint(gradienteCristal);
        g2d.fillRoundRect(padding, 20, width, height, 30, 30);
        
        // Borde brillante sutil en la parte superior del cristal (Reflejo de luz)
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.drawRoundRect(padding, 20, width, height, 30, 30);

        // 2. DIBUJAR CONTENIDO DEPENDIENDO DEL ESTADO
        if (!huboBusqueda) {
            g2d.setColor(TEXTO_GRIS);
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            FontMetrics fm = g2d.getFontMetrics();
            String msj = "Ingresa un ID para recorrer el árbol...";
            g2d.drawString(msj, (getWidth() - fm.stringWidth(msj)) / 2, 160);
            return;
        }

        if (fueEncontrado) {
            // Título de éxito
            g2d.setColor(NEON_AZUL);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.drawString("Nodo Hoja O(log n)", padding + 40, 65);

            // Caja interior que simula el espacio de memoria (El Registro)
            int nodeY = 90;
            g2d.setColor(new Color(15, 15, 18, 200));
            g2d.fillRoundRect(padding + 30, nodeY, width - 60, 140, 20, 20);
            
            // Borde Fucsia de la caja interior
            g2d.setColor(new Color(255, 0, 127, 150));
            g2d.drawRoundRect(padding + 30, nodeY, width - 60, 140, 20, 20);

            // Datos físicos del registro (K, V)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 26));
            g2d.drawString("[ ID: " + idVisual + " ]", padding + 55, nodeY + 45);
            
            // Separador sutil
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.drawLine(padding + 50, nodeY + 65, padding + width - 110, nodeY + 65);

            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString("Evento :  " + eventoVisual, padding + 55, nodeY + 95);
            g2d.drawString("Zona   :  " + zonaVisual, padding + 55, nodeY + 125);

        } else {
            // Diseño de error limpio
            g2d.setColor(new Color(255, 80, 80));
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2d.drawString("ACCESO DENEGADO", padding + 50, 100);
            
            g2d.setColor(TEXTO_GRIS);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2d.drawString("El recorrido alcanzó el nivel de las hojas,", padding + 50, 140);
            g2d.drawString("pero la clave [" + idVisual + "] no existe.", padding + 50, 165);
        }
    }
}