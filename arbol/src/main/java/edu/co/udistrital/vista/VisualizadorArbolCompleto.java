package edu.co.udistrital.vista;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.modelo.NodoBPlus;
import edu.co.udistrital.modelo.NodoHoja;
import edu.co.udistrital.modelo.NodoInterno;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de renderizado del Árbol B+ completo.
 * Dibuja la estructura jerárquica recursivamente usando Java 2D.
 * Se encarga de la traducción de objetos matemáticos genéricos a gráficos visuales.
 */
public class VisualizadorArbolCompleto extends JPanel {
    
    // Paleta Dark Premium
    private final Color FONDO_TARJETA = new Color(25, 25, 30, 220);
    private final Color NEON_AZUL = new Color(0, 229, 255);
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    private final Color NEON_PURPURA = new Color(177, 0, 255);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    // Referencia al modelo matemático
    private ArbolBPlus<Integer, Boleta> motorArbol;

    // Parámetros de dibujo para mantener proporciones correctas
    private final int ANCHO_NODO = 180;
    private final int ALTO_NODO = 70;
    private final int ESPACIO_HORIZONTAL = 40;
    private final int ESPACIO_VERTICAL = 100;

    public VisualizadorArbolCompleto() {
        setOpaque(false); // Transparente para que el Frame dibuje su fondo oscuro
    }

    /**
     * Recibe la referencia al árbol actual y ordena redibujar el lienzo.
     * @param arbol La estructura matemática con los datos actualizados.
     */
    public void setArbol(ArbolBPlus<Integer, Boleta> arbol) {
        this.motorArbol = arbol;
        repaint(); // Ordena a Java redibujar este panel inmediatamente
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (motorArbol == null) return; // Evitar null pointer al inicio

        Graphics2D g2d = (Graphics2D) g;
        // Calidad máxima de renderizado para bordes suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Arrancar el dibujo recursivo desde la raíz en la posición central superior
        if (motorArbol.getRaiz() != null) {
            // SOLUCIÓN: Casteo explícito a Integer para evitar el error de genéricos en NetBeans
            NodoBPlus<Integer> raizComoEntero = (NodoBPlus<Integer>) motorArbol.getRaiz();
            dibujarNodoRecursivo(g2d, raizComoEntero, getWidth() / 2, 50, 1);
        }
    }

    /**
     * Método recursivo principal que dibuja un nodo y calcula matemáticamente
     * la posición en pantalla de todos sus hijos.
     * * @param g2d El motor gráfico de Java 2D.
     * @param nodo El nodo actual que se está dibujando.
     * @param x Coordenada X central del nodo.
     * @param y Coordenada Y superior del nodo.
     * @param nivel Profundidad en el árbol (1 es la raíz).
     */
    @SuppressWarnings("unchecked")
    private void dibujarNodoRecursivo(Graphics2D g2d, NodoBPlus<Integer> nodo, int x, int y, int nivel) {
        // 1. DIBUJAR LA TARJETA DEL NODO (Glassmorphism)
        g2d.setColor(FONDO_TARJETA);
        g2d.fillRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);
        
        // Borde sutil por defecto (se sobreescribirá según el tipo de nodo)
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);

        // 2. DIBUJAR CLAVES DENTRO DEL NODO
        g2d.setColor(TEXTO_CLARO);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22));
        
        // Mostrar claves de forma minimalista
        String clavesString = nodo.toStringClaves(); 
        FontMetrics fm = g2d.getFontMetrics();
        // Centrar el texto matemáticamente dentro de la caja
        g2d.drawString(clavesString, x - fm.stringWidth(clavesString) / 2, y + ALTO_NODO / 2 + fm.getAscent() / 2 - 2);

        // 3. DIBUJAR CONEXIONES HACIA ABAJO (Recursión)
        if (nodo instanceof NodoInterno) {
            NodoInterno<Integer> interno = (NodoInterno<Integer>) nodo;
            
            // Calcular el ancho total que ocuparán los hijos en el nivel inferior
            int numHijos = interno.getNumClaves() + 1;
            int anchoTotalHijos = (numHijos * ANCHO_NODO) + ((numHijos - 1) * ESPACIO_HORIZONTAL);
            int xInicialHijos = x - anchoTotalHijos / 2 + ANCHO_NODO / 2;
            int yHijos = y + ESPACIO_VERTICAL;

            // Diferenciamos los bordes de los nodos internos (Cian)
            g2d.setColor(NEON_AZUL);
            g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);

            // Recursión para cada hijo del arreglo
            for (int i = 0; i < numHijos; i++) {
                if (interno.getHijos()[i] != null) {
                    int xHijo = xInicialHijos + i * (ANCHO_NODO + ESPACIO_HORIZONTAL);
                    
                    // Dibujar flecha (línea) hacia el hijo
                    g2d.setColor(NEON_AZUL);
                    g2d.drawLine(x, y + ALTO_NODO, xHijo, yHijos);
                    
                    // SOLUCIÓN: Casteo explícito del hijo para la llamada recursiva
                    NodoBPlus<Integer> hijoComoEntero = (NodoBPlus<Integer>) interno.getHijos()[i];
                    dibujarNodoRecursivo(g2d, hijoComoEntero, xHijo, yHijos, nivel + 1);
                }
            }
        } else if (nodo instanceof NodoHoja) {
            // Diferenciamos los bordes de los nodos hoja (Fucsia/Magenta)
            g2d.setColor(NEON_FUCSIA);
            g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);
            
            // REGLA DEL B+: Dibujar lista enlazada entre hojas (flechas horizontales)
            NodoHoja<Integer, Boleta> hoja = (NodoHoja<Integer, Boleta>) nodo;
            if (hoja.getSiguiente() != null) {
                g2d.setColor(NEON_FUCSIA);
                int xSiguiente = x + ANCHO_NODO + ESPACIO_HORIZONTAL;
                
                // Dibujar la línea horizontal conectora
                g2d.drawLine(x + ANCHO_NODO / 2, y + ALTO_NODO / 2, xSiguiente - ANCHO_NODO / 2, y + ALTO_NODO / 2);
                
                // Pequeño triángulo para que parezca una flecha ->
                g2d.fillPolygon(
                    new int[]{xSiguiente - ANCHO_NODO / 2 - 5, xSiguiente - ANCHO_NODO / 2 - 5, xSiguiente - ANCHO_NODO / 2},
                    new int[]{y + ALTO_NODO / 2 - 5, y + ALTO_NODO / 2 + 5, y + ALTO_NODO / 2}, 
                    3
                );
            }
        }
        
        // Si el nodo es la Raíz (Nivel 1), sobrescribimos su borde a color Púrpura para identificarla fácilmente
        if (nivel == 1) {
            g2d.setColor(NEON_PURPURA);
            g2d.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 20, 20);
        }
    }
}
