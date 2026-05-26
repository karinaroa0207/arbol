package edu.co.udistrital.modelo;

import java.util.List;

/**
 * Representa un nodo individual del árbol de forma agnóstica.
 * Este DTO permite a la vista representar gráficamente la estructura
 * del árbol sin exponer la implementación interna de los nodos.
 * 
 * @param <K> Tipo de clave (referencia)
 * @param <D> Tipo de valor almacenado
 * @author Karina Roa
 * @version 1.0
 */
public class NodoDTO<K, D> {

    private final List<String> claves;
    private final List<NodoDTO<K, D>> hijos;
    private final List<D> valores;
    private final boolean esHoja;
    private final boolean tieneSiguiente;

    /**
     * Construye un DTO de nodo con toda la información necesaria para visualización.
     * 
     * @param claves Lista de claves convertidas a String (para mostrar en UI)
     * @param hijos Lista de nodos hijos (vacía si esHoja es true)
     * @param valores Lista de valores almacenados (solo relevante si esHoja es true)
     * @param esHoja true si el nodo es de tipo hoja, false si es interno
     * @param tieneSiguiente true si existe un nodo hoja hermano a la derecha
     */
    public NodoDTO(List<String> claves, List<NodoDTO<K, D>> hijos, List<D> valores, boolean esHoja, boolean tieneSiguiente) {
        this.claves = claves;
        this.hijos = hijos;
        this.valores = valores;
        this.esHoja = esHoja;
        this.tieneSiguiente = tieneSiguiente;
    }

    /**
     * Obtiene las claves del nodo formateadas como String para la interfaz de usuario.
     * @return Lista de claves como String
     */
    public List<String> getClaves() { return claves; }
    
    /**
     * Obtiene los nodos hijos del nodo actual.
     * @return Lista de nodos hijos (vacía si es hoja)
     */
    public List<NodoDTO<K, D>> getHijos() { return hijos; }
    
    /**
     * Obtiene los valores almacenados en el nodo.
     * @return Lista de valores (solo útil en nodos hoja)
     */
    public List<D> getValores() { return valores; }
    
    /**
     * Indica si el nodo es de tipo hoja.
     * @return true si es nodo hoja, false si es nodo interno
     */
    public boolean esHoja() { return esHoja; }
    
    /**
     * Indica si el nodo tiene un hermano derecho en la lista enlazada.
     * @return true si tiene nodo siguiente, false en caso contrario
     */
    public boolean tieneSiguiente() { return tieneSiguiente; }

    /**
     * Muestra las claves con formato visual para la interfaz.
     * @return Representación de claves con formato "[ 10 | 20 | 30 ]" o "[ ]" si está vacío
     */
    public String toStringClaves() {
        if (claves == null || claves.isEmpty()) return "[ ]";
        return "[ " + String.join(" | ", claves) + " ]";
    }
}