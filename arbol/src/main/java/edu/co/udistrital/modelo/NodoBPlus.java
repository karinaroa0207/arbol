package edu.co.udistrital.modelo;

/**
 * Clase abstracta que define el molde para cualquier nodo del árbol.
 * Usa genéricos <K> con la restricción 'Comparable' para asegurar
 * que las claves (como los IDs numéricos) se puedan ordenar de menor a mayor.
 */
public abstract class NodoBPlus<K extends Comparable<K>> {
    
    // El grado (m) del árbol. Define el tamaño máximo de los arreglos.
    protected int orden; 
    
    // Arreglo que guarda las "señales de tráfico" matemáticas.
    // Capacidad máxima teórica: orden - 1. 
    protected K[] claves; 
    
    // Contador para saber cuántos espacios del arreglo 'claves' estamos usando realmente.
    protected int numClaves; 
    
    // Puntero hacia el nodo padre. Es vital para el Árbol B+ porque 
    // cuando un nodo hace split (se divide), necesita enviar la clave central hacia arriba.
    protected NodoInterno<K> padre; 

    @SuppressWarnings("unchecked")
    public NodoBPlus(int orden) {
        this.orden = orden;
        // Se reserva espacio en memoria para el máximo de claves permitidas (m-1)
        this.claves = (K[]) new Comparable[orden - 1]; 
        this.numClaves = 0;
        this.padre = null; // Al nacer, ningún nodo tiene padre hasta que el árbol lo acomoda
    }

    /**
     * Verifica si el nodo alcanzó su máxima capacidad.
     * Si devuelve true durante una inserción, se detonará el algoritmo de división (split).
     */
    public boolean estaLleno() {
        return numClaves == orden - 1;
    }

    /**
     * Expone cuántas claves reales tiene el nodo.
     * Es útil para que la vista dibuje únicamente los hijos ocupados y no toda
     * la capacidad física del arreglo.
     */
    public int getNumClaves() {
        return numClaves;
    }
    
    /**
     * Convierte las claves del nodo a un formato de texto limpio
     * para que la vista gráfica pueda dibujarlo dentro del bloque.
     */
    public String toStringClaves() {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < numClaves; i++) {
            sb.append(claves[i]);
            if (i < numClaves - 1) {
                sb.append(" | ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }
}
