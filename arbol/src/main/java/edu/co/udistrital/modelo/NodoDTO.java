package edu.co.udistrital.modelo;

import java.util.List;

/**
 * Representa un nodo individual del árbol de forma agnóstica.
 */
public class NodoDTO<K, D> {

    private final List<String> claves;
    private final List<NodoDTO<K, D>> hijos;
    private final List<D> valores;
    private final boolean esHoja;
    private final boolean tieneSiguiente;

    public NodoDTO(List<String> claves, List<NodoDTO<K, D>> hijos, List<D> valores, boolean esHoja, boolean tieneSiguiente) {
        this.claves = claves;
        this.hijos = hijos;
        this.valores = valores;
        this.esHoja = esHoja;
        this.tieneSiguiente = tieneSiguiente;
    }

    public List<String> getClaves() {
        return claves;
    }

    public List<NodoDTO<K, D>> getHijos() {
        return hijos;
    }

    public List<D> getValores() {
        return valores;
    }

    public boolean esHoja() {
        return esHoja;
    }

    public boolean tieneSiguiente() {
        return tieneSiguiente;
    }

    public String toStringClaves() {
        if (claves == null || claves.isEmpty()) {
            return "[ ]";
        }
        return "[ " + String.join(" | ", claves) + " ]";
    }
}
