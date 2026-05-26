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
 * 
 * @author Karina Roa
 * @version 1.0
 * @see ArbolBPlusDTO
 * @see NodoDTO
 */
public class VisualizadorArbolCompleto extends JPanel {
    
    /** Color de fondo para las tarjetas de los nodos. */
    private final Color FONDO_TARJETA = new Color(25, 25, 30, 220);
    
    /** Color neón azul para bordes de nodos internos y líneas de conexión. */
    private final Color NEON_AZUL = new Color(0, 229, 255);
    
    /** Color neón fucsia para bordes de nodos hoja y enlaces entre hojas. */
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    
    /** Color neón púrpura para destacar el nodo raíz. */
    private final Color NEON_PURPURA = new Color(177, 0, 255);
    
    /** Color para texto claro dentro de los nodos. */
    private final Color TEXTO_CLARO = new Color(230, 230, 230);

    /** DTO del árbol que se va a visualizar. */
    private ArbolBPlusDTO<Integer, BoletaDTO> dtoArbol;

    /** Ancho mínimo que debe tener un nodo. */
    private final int ANCHO_MINIMO_NODO = 180;
    
    /** Alto fijo de cada nodo. */
    private final int ALTO_NODO = 70;
    
    /** Padding horizontal adicional para el texto del nodo. */
    private final int PADDING_HORIZONTAL_NODO = 42;
    
    /** Ancho aproximado de un carácter en píxeles para cálculos de texto. */
    private final int ANCHO_CARACTER_APROXIMADO = 14;
    
    /** Espacio horizontal entre nodos hermanos. */
    private final int ESPACIO_HORIZONTAL = 60;
    
    /** Espacio vertical entre niveles del árbol. */
    private final int ESPACIO_VERTICAL = 100;
    
    /** Margen horizontal del panel. */
    private final int MARGEN_HORIZONTAL = 40;
    
    /** Margen vertical del panel. */
    private final int MARGEN_VERTICAL = 80;

    /**
     * Constructor del visualizador.
     * Configura el panel como transparente para permitir fondos personalizados.
     */
    public VisualizadorArbolCompleto() {
        setOpaque(false);
    }

    /**
     * Recibe el snapshot estructurado (DTO) y gatilla el redibujado del panel.
     * 
     * @param dto DTO del árbol a visualizar
     */
    public void setArbol(ArbolBPlusDTO<Integer, BoletaDTO> dto) {
        this.dtoArbol = dto;
        actualizarTamanoPreferido();
        revalidate();
        repaint();
    }

    /**
     * Dibuja el árbol completo en el panel.
     * 
     * @param g Objeto Graphics para dibujar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dtoArbol == null || dtoArbol.getRaiz() == null) return; 

        Graphics2D g2d = (Graphics2D) g;
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
     * 
     * @param nodo Nodo a evaluar
     * @return Ancho total necesario para el subárbol
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
     * 
     * @param nodo Nodo a evaluar
     * @return Ancho mínimo del nodo
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

    /**
     * Calcula la profundidad máxima del árbol desde el nodo dado.
     * 
     * @param nodo Nodo a evaluar
     * @return Profundidad del subárbol
     */
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
     * 
     * @param g2d Contexto gráfico 2D
     * @param nodo Nodo a dibujar
     * @param x Coordenada X del centro del nodo
     * @param xInicioSubarbol Coordenada X de inicio del subárbol
     * @param y Coordenada Y del nodo
     * @param nivel Nivel del árbol (1 = raíz)
     * @param hojas Lista acumuladora de hojas para luego dibujar enlaces
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

        g2d.setColor(FONDO_TARJETA);
        g2d.fillRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);
        
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);

        g2d.setColor(TEXTO_CLARO);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22));
        String clavesString = nodo.toStringClaves(); 
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(clavesString, x - fm.stringWidth(clavesString) / 2, y + ALTO_NODO / 2 + fm.getAscent() / 2 - 2);

        if (!nodo.esHoja()) {
            g2d.setColor(NEON_AZUL);
            g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);

            int yHijos = y + ESPACIO_VERTICAL;
            int cursorX = xInicioSubarbol;

            for (int i = 0; i < nodo.getHijos().size(); i++) {
                NodoDTO<Integer, BoletaDTO> hijoDTO = nodo.getHijos().get(i);
                int anchoHijo = calcularAnchoSubarbol(hijoDTO);
                int xHijo = cursorX + anchoHijo / 2;
                
                g2d.setColor(NEON_AZUL);
                g2d.drawLine(x, y + ALTO_NODO, xHijo, yHijos);
                
                dibujarNodoRecursivo(g2d, hijoDTO, xHijo, cursorX, yHijos, nivel + 1, hojas);
                cursorX += anchoHijo + ESPACIO_HORIZONTAL;
            }
        } else {
            g2d.setColor(NEON_FUCSIA);
            g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);
            hojas.add(new HojaDibujada(x, y + ALTO_NODO / 2, anchoNodo));
        }
        
        if (nivel == 1) {
            g2d.setColor(NEON_PURPURA);
            g2d.drawRoundRect(x - anchoNodo / 2, y, anchoNodo, ALTO_NODO, 20, 20);
        }
    }

    /**
     * Dibuja la lista enlazada de hojas usando las posiciones reales calculadas
     * por el layout del árbol.
     * 
     * @param g2d Contexto gráfico 2D
     * @param hojas Lista de hojas con sus posiciones calculadas
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

    /**
     * Clase auxiliar para almacenar la posición de una hoja durante el dibujo.
     */
    private static class HojaDibujada {
        /** Coordenada X del centro de la hoja. */
        private final int xCentro;
        
        /** Coordenada Y del centro de la hoja. */
        private final int yCentro;
        
        /** Ancho de la hoja. */
        private final int ancho;

        /**
         * Constructor de la hoja dibujada.
         * @param xCentro Coordenada X del centro
         * @param yCentro Coordenada Y del centro
         * @param ancho Ancho de la hoja
         */
        private HojaDibujada(int xCentro, int yCentro, int ancho) {
            this.xCentro = xCentro;
            this.yCentro = yCentro;
            this.ancho = ancho;
        }
    }
}