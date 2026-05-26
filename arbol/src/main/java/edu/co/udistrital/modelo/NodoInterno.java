package edu.co.udistrital.modelo;

/**
 * Nodos de enrutamiento. Nunca guardan datos reales (V), 
 * solo guardan claves (K) para comparar y punteros hacia los nodos hijos.
 *@param <K> Tipo de clave (debe ser Comparable)
 */
public class NodoInterno<K extends Comparable<K>> extends NodoBPlus<K> {
    
    // Arreglo de punteros a los nodos de nivel inferior.
    // La regla matemática dice que si un nodo tiene N claves, debe tener N+1 hijos.
    // Por eso su tamaño es 'orden' y no 'orden - 1'.
    private NodoBPlus<K>[] hijos; 

    @SuppressWarnings("unchecked")
    public NodoInterno(int orden) {
        super(orden);
        this.hijos = new NodoBPlus[orden]; 
    }

    public NodoBPlus<K>[] getHijos() { return hijos; }
}