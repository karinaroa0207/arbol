package edu.co.udistrital.vista;

import edu.co.udistrital.modelo.ArbolBPlusDTO;
import edu.co.udistrital.modelo.BoletaDTO;
import edu.co.udistrital.modelo.NodoDTO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    private ArbolBPlusDTO<Integer, BoletaDTO> dtoArbol;

    // Dimensiones de diseño para los componentes del árbol
    private final int ANCHO_MINIMO_NODO = 180;
    private final int ALTO_NODO = 70;
    private final int PADDING_HORIZONTAL_NODO = 42;
    private final int ANCHO_CARACTER_APROXIMADO = 14;
    private final int ESPACIO_HORIZONTAL = 60;
    private final int ESPACIO_VERTICAL = 100;
    private final int MARGEN_HORIZONTAL = 40;
    private final int MARGEN_VERTICAL = 80;

    public VisualizadorArbolCompleto() {
        setOpaque(false); // Permite ver fondos personalizados del contenedor principal
    }

    /**
     * Recibe el snapshot estructurado (DTO) y gatilla el redibujado del panel.
     */
    public void setArbol(ArbolBPlusDTO<Integer, BoletaDTO> dto) {
        this.dtoArbol = dto;
        actualizarTamanoPreferido();
        revalidate();
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

        List<HojaDibujada> hojas = new ArrayList<>();
        int anchoTotal = calcularAnchoSubarbol(dtoArbol.getRaiz());
        int xInicio = Math.max(MARGEN_HORIZONTAL, (getWidth() - anchoTotal) / 2);
        int xRaiz = xInicio + anchoTotal / 2;

        dibujarNodoRecursivo(g2d, dtoArbol.getRaiz(), xRaiz, xInicio, 50, 1, hojas);
        dibujarEnlacesEntreHojas(g2d, hojas);
    }

    /**
     * Calcula cuánto espacio horizontal requiere un nodo incluyendo todos sus
     * descendientes. Esto evita que subárboles vecinos se monten visualmente.
     */
    private int calcularAnchoSubarbol(NodoDTO<Integer, BoletaDTO> nodo) {
        if (nodo.esHoja() || nodo.getHijos().isEmpty()) {
            return calcularAnchoNodo(nodo);
        }

        int ancho = 0;
        for (NodoDTO<Integer, BoletaDTO> hijo : nodo.getHijos()) {
            ancho += calcularAnchoSubarbol(hijo);
        }

        ancho += ESPACIO_HORIZONTAL * (nodo.getHijos().size() - 1);
        return Math.max(calcularAnchoNodo(nodo), ancho);
    }

    /**
     * Calcula el ancho visual mínimo que necesita el nodo para que sus claves
     * no se salgan de la caja, incluso con órdenes altos.
     */
    private int calcularAnchoNodo(NodoDTO<Integer, BoletaDTO> nodo) {
        int anchoTexto = nodo.toStringClaves().length() * ANCHO_CARACTER_APROXIMADO;
        return Math.max(ANCHO_MINIMO_NODO, anchoTexto + PADDING_HORIZONTAL_NODO);
    }

    /**
     * Ajusta el tamaño ideal del lienzo para que el JScrollPane pueda activar
     * barras de desplazamiento cuando el árbol exceda el área visible.
     */
    private void actualizarTamanoPreferido() {
        if (dtoArbol == null || dtoArbol.getRaiz() == null) {
            setPreferredSize(new Dimension(900, 500));
            return;
        }

        int ancho = calcularAnchoSubarbol(dtoArbol.getRaiz()) + (MARGEN_HORIZONTAL * 2);
        int alto = 50 + (calcularProfundidad(dtoArbol.getRaiz()) * ESPACIO_VERTICAL) + ALTO_NODO + MARGEN_VERTICAL;
        setPreferredSize(new Dimension(ancho, alto));
    }

    private int calcularProfundidad(NodoDTO<Integer, BoletaDTO> nodo) {
        if (nodo.esHoja() || nodo.getHijos().isEmpty()) {
            return 0;
        }

        int profundidadMaxima = 0;
        for (NodoDTO<Integer, BoletaDTO> hijo : nodo.getHijos()) {
            profundidadMaxima = Math.max(profundidadMaxima, calcularProfundidad(hijo));
        }

        return profundidadMaxima + 1;
    }

    /**
     * Método recursivo encargado de pintar nodos, calcular posiciones de hijos y trazar enlaces.
     */
    private void dibujarNodoRecursivo(
            Graphics2D g2d,
            NodoDTO<Integer, BoletaDTO> nodo,
            int x,
            int xInicioSubarbol,
            int y,
            int nivel,
            List<HojaDibujada> hojas) {
        int anchoNodo = calcularAnchoNodo(nodo);

        // 1. Dibujar el contenedor base del nodo (Caja con esquinas redondeadas)
        g2d.setColor(FONDO_TARJETA);
        g2d.fillRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);
        
        // Bordes decorativos sutiles
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);

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
            g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);

            int yHijos = y + ESPACIO_VERTICAL;
            int cursorX = xInicioSubarbol;

            for (int i = 0; i < nodo.getHijos().size(); i++) {
                NodoDTO<Integer, BoletaDTO> hijoDTO = nodo.getHijos().get(i); // Uso directo de la clase separada
                int anchoHijo = calcularAnchoSubarbol(hijoDTO);
                int xHijo = cursorX + anchoHijo / 2;
                
                // Trazar línea de unión jerárquica hacia el nodo hijo
                g2d.setColor(NEON_AZUL);
                g2d.drawLine(x, y + ALTO_NODO, xHijo, yHijos);
                
                // Llamada recursiva para procesar el subárbol del hijo
                dibujarNodoRecursivo(g2d, hijoDTO, xHijo, cursorX, yHijos, nivel + 1, hojas);
                cursorX += anchoHijo + ESPACIO_HORIZONTAL;
            }
        } else {
            // Nodos Hoja: Color Fucsia Neón
            g2d.setColor(NEON_FUCSIA);
            g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);
            hojas.add(new HojaDibujada(x, y + ALTO_NODO / 2, anchoNodo));
        }
        
        // Destacar estéticamente la raíz principal con un borde Púrpura Neón adicional
        if (nivel == 1) {
            g2d.setColor(NEON_PURPURA);
            g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);
        }
    }

    /**
     * Dibuja la lista enlazada de hojas usando las posiciones reales calculadas
     * por el layout del árbol.
     */
    private void dibujarEnlacesEntreHojas(Graphics2D g2d, List<HojaDibujada> hojas) {
        g2d.setColor(NEON_FUCSIA);

        for (int i = 0; i < hojas.size() - 1; i++) {
            HojaDibujada actual = hojas.get(i);
            HojaDibujada siguiente = hojas.get(i + 1);
            int xInicio = actual.xCentro + actual.ancho / 2;
            int xFin = siguiente.xCentro - siguiente.ancho / 2;

            g2d.drawLine(xInicio, actual.yCentro, xFin, siguiente.yCentro);
            g2d.fillPolygon(
                new int[]{xFin - 5, xFin - 5, xFin},
                new int[]{siguiente.yCentro - 5, siguiente.yCentro + 5, siguiente.yCentro},
                3
            );
        }
    }

    private static class HojaDibujada {
        private final int xCentro;
        private final int yCentro;
        private final int ancho;

        private HojaDibujada(int xCentro, int yCentro, int ancho) {
            this.xCentro = xCentro;
            this.yCentro = yCentro;
            this.ancho = ancho;
        }
    }
}
