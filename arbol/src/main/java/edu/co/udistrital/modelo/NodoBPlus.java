package edu.co.udistrital.modelo;

/**
 * Clase abstracta que define el molde para cualquier nodo del árbol.
 * Usa genéricos &lt;K&gt; con la restricción 'Comparable' para asegurar
 * que las claves (como los IDs numéricos) se puedan ordenar de menor a mayor.
 * 
 * @param <K> Tipo de clave que debe implementar Comparable
 * @author Karina Roa
 * @version 1.0
 */
public abstract class NodoBPlus<K extends Comparable<K>> {
    
    /** El grado (m) del árbol. Define el tamaño máximo de los arreglos. */
    protected int orden; 
    
    /** Arreglo que guarda las "señales de tráfico" matemáticas. Capacidad máxima: orden - 1. */
    protected K[] claves; 
    
    /** Contador para saber cuántos espacios del arreglo 'claves' estamos usando realmente. */
    protected int numClaves; 
    
    /** Puntero hacia el nodo padre. Vital para cuando un nodo hace split (se divide). */
    protected NodoInterno<K> padre; 

    /**
     * Constructor del nodo B+.
     * Reserva espacio en memoria para el máximo de claves permitidas (orden-1)
     * e inicializa el contador de claves y la referencia al padre.
     * 
     * @param orden Grado del árbol (define la capacidad del nodo)
     */
    @SuppressWarnings("unchecked")
    public NodoBPlus(int orden) {
        this.orden = orden;
        this.claves = (K[]) new Comparable[orden - 1]; 
        this.numClaves = 0;
        this.padre = null;
    }

    /**
     * Verifica si el nodo alcanzó su máxima capacidad.
     * Si devuelve true durante una inserción, se detonará el algoritmo de división (split).
     * 
     * @return true si el nodo está lleno, false en caso contrario
     */
    public boolean estaLleno() {
        return numClaves == orden - 1;
    }

    /**
     * Expone cuántas claves reales tiene el nodo.
     * Es útil para que la vista dibuje únicamente los hijos ocupados y no toda
     * la capacidad física del arreglo.
     * 
     * @return Número de claves actualmente en el nodo
     */
    public int getNumClaves() {
        return numClaves;
    }
    
    /**
     * Convierte las claves del nodo a un formato de texto limpio
     * para que la vista gráfica pueda dibujarlo dentro del bloque.
     * 
     * @return Representación de claves con formato "[ 10 | 20 | 30 ]"
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