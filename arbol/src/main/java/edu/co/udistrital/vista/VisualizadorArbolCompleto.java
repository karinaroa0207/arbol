package edu.co.udistrital.vista;

import edu.co.udistrital.modelo.ArbolBPlusDTO;
import edu.co.udistrital.modelo.NodoDTO;
import javax.swing.*;
import java.awt.*;

/**
 * Componente gráfico estilizado encargado de renderizar de forma recursiva
 * cualquier estructura de Árbol B+ a partir de un DTO topológico.
 */
public class VisualizadorArbolCompleto extends JPanel {
    
    // Paleta de colores premium/oscuro para la interfaz gráfica
    private final Color FONDO_TARJETA = new Color(25, 25, 30, 220);
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    private final Color NEON_PURPURA = new Color(177, 0, 255);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    private ArbolBPlusDTO<?, ?> dtoArbol;

    // Dimensiones de diseño para los componentes del árbol
    private final int ANCHO_NODO = 180;
    private final int ALTO_NODO = 70;
    private final int ESPACIO_HORIZONTAL = 40;
    private final int ESPACIO_VERTICAL = 100;

    public VisualizadorArbolCompleto() {
        setOpaque(false); // Permite ver fondos personalizados del contenedor principal
    }

    /**
     * Recibe el snapshot estructurado (DTO) y gatilla el redibujado del panel.
     */
    public void setArbol(ArbolBPlusDTO<?, ?> dto) {
        this.dtoArbol = dto;
        repaint(); // Invoca asíncronamente a paintComponent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dtoArbol == null || dtoArbol.getRaiz() == null) return; 

        Graphics2D g2d = (Graphics2D) g;
        // Activamos Antialiasing para que las líneas y textos no se vean pixelados
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Iniciamos el renderizado recursivo partiendo del nodo raíz en el centro superior
        dibujarNodoRecursivo(g2d, dtoArbol.getRaiz(), getWidth() / 2, 50, 1);
    }

    /**
     * Método recursivo encargado de pintar nodos, calcular posiciones de hijos y trazar enlaces.
     */
    private void dibujarNodoRecursivo(Graphics2D g2d, NodoDTO<?, ?> nodo, int x, int y, int nivel) {
        // 1. Dibujar el contenedor base del nodo (Caja con esquinas redondeadas)
        g2d.setColor(FONDO_TARJETA);
        g2d.fillRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);
        
        // Bordes decorativos sutiles
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);

        // 2. Renderizar el texto de las claves formateadas internamente en el DTO
        g2d.setColor(TEXTO_CLARO);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22));
        String clavesString = nodo.toStringClaves(); 
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(clavesString, x - fm.stringWidth(clavesString) / 2, y + ALTO_NODO / 2 + fm.getAscent() / 2 - 2);

        // 3. Diferenciar visualmente el comportamiento según el tipo de nodo (Hoja o Interno)
        if (!nodo.esHoja()) {
            // Nodos Internos: Color Azul Neón
            g2d.setColor(NEON_AZUL);
            g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);

            // Calcular distribución simétrica en pantalla para los hijos del nodo
            int numHijos = nodo.getHijos().size();
            int anchoTotalHijos = (numHijos * ANCHO_NODO) + ((numHijos - 1) * ESPACIO_HORIZONTAL);
            int xInicialHijos = x - anchoTotalHijos / 2 + ANCHO_NODO / 2;
            int yHijos = y + ESPACIO_VERTICAL;

            for (int i = 0; i < numHijos; i++) {
                NodoDTO<?, ?> hijoDTO = nodo.getHijos().get(i); // Uso directo de la clase separada
                int xHijo = xInicialHijos + i * (ANCHO_NODO + ESPACIO_HORIZONTAL);
                
                // Trazar línea de unión jerárquica hacia el nodo hijo
                g2d.setColor(NEON_AZUL);
                g2d.drawLine(x, y + ALTO_NODO, xHijo, yHijos);
                
                // Llamada recursiva para procesar el subárbol del hijo
                dibujarNodoRecursivo(g2d, hijoDTO, xHijo, yHijos, nivel + 1);
            }
        } else {
            // Nodos Hoja: Color Fucsia Neón
            g2d.setColor(NEON_FUCSIA);
            g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);
            
            // Si tiene un puntero al siguiente hermano hoja (Lista enlazada del Árbol B+), pintamos la flecha
            if (nodo.tieneSiguiente()) {
                g2d.setColor(NEON_FUCSIA);
                int xSiguiente = x + ANCHO_NODO + ESPACIO_HORIZONTAL;
                
                // Línea horizontal conductora
                g2d.drawLine(x + ANCHO_NODO / 2, y + ALTO_NODO / 2, xSiguiente - ANCHO_NODO / 2, y + ALTO_NODO / 2);
                
                // Punta de la flecha (Triángulo)
                g2d.fillPolygon(
                    new int[]{xSiguiente - ANCHO_NODO / 2 - 5, xSiguiente - ANCHO_NODO / 2 - 5, xSiguiente - ANCHO_NODO / 2},
                    new int[]{y + ALTO_NODO / 2 - 5, y + ALTO_NODO / 2 + 5, y + ALTO_NODO / 2}, 3
                );
            }
        }
        
        // Destacar estéticamente la raíz principal con un borde Púrpura Neón adicional
        if (nivel == 1) {
            g2d.setColor(NEON_PURPURA);
            g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);
        }
    }
}