package edu.co.udistrital.modelo;

import java.util.List;

/**
 * DTO que representa la estructura completa del árbol de forma segura y plana
 * para ser consumida exclusivamente por la vista.
 * @param <K> Tipo de dato de la clave (debe ser Comparable)
 * @param <D> Tipo de dato del valor almacenado
 */
public class ArbolBPlusDTO<K, D> {
    private final NodoDTO<K, D> raiz;
    private final int orden;

    /**
     * Construye un DTO del árbol B+.
     * @param raiz Nodo raíz del árbol
     * @param orden Grado del árbol
     */
    public ArbolBPlusDTO(NodoDTO<K, D> raiz, int orden) {
        this.raiz = raiz;
        this.orden = orden;
    }

    /**
     * Obtiene el nodo raíz del árbol.
     * @return El nodo raíz del árbol
     */
    public NodoDTO<K, D> getRaiz() { return raiz; }

    /**
     * Obtiene el grado (orden) del árbol.
     * @return El orden del árbol
     */
    public int getOrden() { return orden; }
}