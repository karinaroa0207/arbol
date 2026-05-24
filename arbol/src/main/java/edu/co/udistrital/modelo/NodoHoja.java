package edu.co.udistrital.modelo;

/**
 * Las Hojas están en el nivel inferior del árbol. 
 * Son los ÚNICOS nodos que guardan los valores (V), como nuestras Boletas.
 */
public class NodoHoja<K extends Comparable<K>, V> extends NodoBPlus<K> {
    
    // Arreglo que guarda los objetos reales. 
    // El índice de un 'valor' corresponde al índice de su 'clave' en el arreglo del padre.
    private V[] valores; 
    
    // Puntero crucial del Árbol B+: conecta esta hoja con la hoja adyacente derecha.
    // Esto forma la Lista Enlazada para hacer búsquedas rápidas por rangos.
    private NodoHoja<K, V> siguiente; 

    @SuppressWarnings("unchecked")
    public NodoHoja(int orden) {
        super(orden); // Llama al constructor de NodoBPlus
        // Mismo tamaño que las claves: orden - 1
        this.valores = (V[]) new Object[orden - 1]; 
        this.siguiente = null; // Al crearse, es el final de la lista enlazada
    }

    public V[] getValores() { return valores; }
    public NodoHoja<K, V> getSiguiente() { return siguiente; }
    public void setSiguiente(NodoHoja<K, V> siguiente) { this.siguiente = siguiente; }
}