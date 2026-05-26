package edu.co.udistrital.modelo;

import java.util.List;

/**
 * DTO que representa la estructura completa del árbol de forma segura y plana
 * para ser consumida exclusivamente por la vista.
 */

public class ArbolBPlusDTO<K, D> {
    private final NodoDTO<K, D> raiz;
    private final int orden;

    public ArbolBPlusDTO(NodoDTO<K, D> raiz, int orden) {
        this.raiz = raiz;
        this.orden = orden;
    }

    public NodoDTO<K, D> getRaiz() { return raiz; }
    public int getOrden() { return orden; }  
}