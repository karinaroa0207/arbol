package edu.co.udistrital.modelo;

/**
 * Motor principal del Árbol B+.
 * Estructura de datos de búsqueda multidireccional y autobalanceada.
 * Optimizada para sistemas de bases de datos y recuperación de registros (ej. Sistema QueBoleta)
 * mediante el uso de almacenamiento en hojas y listas enlazadas.
 *
 * @param <K> Tipo de dato de la clave de búsqueda. Debe implementar Comparable para mantener el orden (ej. Integer para ID).
 * @param <V> Tipo de dato del valor o registro real que se almacenará en las hojas (ej. objeto Boleta).
 */
public class ArbolBPlus<K extends Comparable<K>, V> {
    
    /** Punto de entrada a la estructura. Puede ser un NodoHoja o un NodoInterno. */
    private NodoBPlus<K> raiz; 
    
    /** Grado del árbol ($m$). Define la capacidad máxima de punteros ($m$) y claves ($m-1$) por nodo. */
    private int orden; 

    /**
     * Construye un nuevo Árbol B+ vacío.
     * La regla fundamental dicta que un árbol nace exclusivamente con una Raíz de tipo Hoja,
     * ya que los primeros datos no necesitan enrutamiento.
     *
     * @param orden El grado del árbol que determinará los puntos de división (split).
     */
    public ArbolBPlus(int orden) {
        this.orden = orden;
        this.raiz = new NodoHoja<K, V>(orden);
    }

    /**
     * Inserta un nuevo par clave-valor en la estructura manteniendo el balance perfecto.
     * Complejidad de tiempo: $O(\log n)$ en el caso promedio.
     *
     * @param clave El identificador único para ordenar el dato (ej. ID de la boleta).
     * @param valor El objeto real a almacenar en memoria.
     */
    public void insertar(K clave, V valor) {
        NodoHoja<K, V> hojaObjetivo = encontrarHoja(clave);
        insertarEnHoja(hojaObjetivo, clave, valor);
    }

    /**
     * Desciende por la estructura evaluando las "señales de tráfico" de los nodos internos
     * hasta encontrar la hoja exacta donde debería residir una clave.
     *
     * @param clave La clave matemática a evaluar.
     * @return El NodoHoja específico al final del camino de búsqueda.
     */
    private NodoHoja<K, V> encontrarHoja(K clave) {
        NodoBPlus<K> actual = raiz;
        
        // Mientras el nodo sea de enrutamiento (interno), seguimos bajando un nivel
        while (actual instanceof NodoInterno) {
            NodoInterno<K> interno = (NodoInterno<K>) actual;
            int i = 0;
            
            // Búsqueda lineal dentro del arreglo de claves del nodo interno
            while (i < interno.numClaves && clave.compareTo(interno.claves[i]) >= 0) {
                i++;
            }
            // Descender por el puntero hijo correspondiente al intervalo encontrado
            actual = interno.getHijos()[i];
        }
        
        return (NodoHoja<K, V>) actual; 
    }

    /**
     * Inserta los datos físicos en una hoja. Utiliza arreglos temporales para
     * absorber el desbordamiento (overflow) de forma segura antes de aplicar la matemática de división.
     *
     * @param hoja La hoja destino encontrada previamente.
     * @param clave La clave a insertar.
     * @param valor El objeto a insertar.
     */
    @SuppressWarnings("unchecked")
    private void insertarEnHoja(NodoHoja<K, V> hoja, K clave, V valor) {
        // Arreglos temporales dimensionados a 'orden' (permiten 1 elemento más que la capacidad real m-1)
        K[] tempClaves = (K[]) new Comparable[orden];
        V[] tempValores = (V[]) new Object[orden];

        // 1. Copiar y ordenar los datos existentes y el nuevo dato en el espacio temporal
        int i = 0;
        while (i < hoja.numClaves && clave.compareTo(hoja.claves[i]) > 0) {
            tempClaves[i] = hoja.claves[i];
            tempValores[i] = hoja.getValores()[i];
            i++;
        }
        
        tempClaves[i] = clave;
        tempValores[i] = valor;
        
        int j = i + 1;
        while (i < hoja.numClaves) {
            tempClaves[j] = hoja.claves[i];
            tempValores[j] = hoja.getValores()[i];
            i++;
            j++;
        }

        // 2. Evaluación de estado del nodo
        if (hoja.numClaves < orden - 1) {
            // ESTADO SEGURO: Volcar la memoria temporal a la memoria del nodo
            for (int k = 0; k < hoja.numClaves + 1; k++) {
                hoja.claves[k] = tempClaves[k];
                hoja.getValores()[k] = tempValores[k];
            }
            hoja.numClaves++;
        } else {
            // ESTADO DE OVERFLOW: Aplicar algoritmo de división (Split)
            NodoHoja<K, V> nuevaHoja = new NodoHoja<>(orden);
            
            // Índice de corte matemático $k = m / 2$
            int puntoCorte = orden / 2;

            // Distribuir mitad izquierda al nodo original
            hoja.numClaves = 0;
            for (int k = 0; k < puntoCorte; k++) {
                hoja.claves[k] = tempClaves[k];
                hoja.getValores()[k] = tempValores[k];
                hoja.numClaves++;
            }

            // Distribuir mitad derecha al nodo nuevo
            for (int k = puntoCorte; k < orden; k++) {
                nuevaHoja.claves[nuevaHoja.numClaves] = tempClaves[k];
                nuevaHoja.getValores()[nuevaHoja.numClaves] = tempValores[k];
                nuevaHoja.numClaves++;
            }

            // Mantenimiento estructural: Reconectar la lista enlazada de las hojas
            nuevaHoja.setSiguiente(hoja.getSiguiente());
            hoja.setSiguiente(nuevaHoja);

            // Regla de Hojas B+: La clave más pequeña de la derecha se COPIA hacia arriba
            K clavePromovida = nuevaHoja.claves[0];
            insertarEnPadre(hoja, clavePromovida, nuevaHoja);
        }
    }

     
    /**
     * Gestiona la promoción de claves hacia los niveles superiores.
     * Este método es recursivo: si el padre se desborda, se llama a sí mismo
     * propagando el efecto dominó hasta, potencialmente, crear una nueva raíz.
     *
     * @param izquierdo El nodo que sufrió la división (mitad izquierda).
     * @param clavePromovida La clave matemática que actuará como nueva señal de tráfico.
     * @param derecho El nuevo nodo producto de la división (mitad derecha).
     */
    @SuppressWarnings("unchecked")
    private void insertarEnPadre(NodoBPlus<K> izquierdo, K clavePromovida, NodoBPlus<K> derecho) {
        
        // CASO BASE: Expansión de la altura del árbol
        if (izquierdo == raiz) {
            NodoInterno<K> nuevaRaiz = new NodoInterno<>(orden);
            nuevaRaiz.claves[0] = clavePromovida;
            nuevaRaiz.getHijos()[0] = izquierdo;
            nuevaRaiz.getHijos()[1] = derecho;
            nuevaRaiz.numClaves = 1;
            
            izquierdo.padre = nuevaRaiz;
            derecho.padre = nuevaRaiz;
            raiz = nuevaRaiz; // El árbol ha crecido un nivel hacia arriba
            return;
        }

        NodoInterno<K> padre = izquierdo.padre;
        
        // Memoria temporal para absorber overflow en el nodo de enrutamiento
        K[] tempClaves = (K[]) new Comparable[orden];
        NodoBPlus<K>[] tempHijos = new NodoBPlus[orden + 1];

        // Calcular posición de inserción
        int i = 0;
        while (i < padre.numClaves && clavePromovida.compareTo(padre.claves[i]) > 0) {
            i++;
        }

        // Volcado a memoria temporal respetando el ordenamiento
        for (int j = 0; j < i; j++) tempClaves[j] = padre.claves[j];
        tempClaves[i] = clavePromovida;
        for (int j = i; j < padre.numClaves; j++) tempClaves[j + 1] = padre.claves[j];

        for (int j = 0; j < i + 1; j++) tempHijos[j] = padre.getHijos()[j];
        tempHijos[i + 1] = derecho;
        for (int j = i + 1; j < padre.numClaves + 1; j++) tempHijos[j + 1] = padre.getHijos()[j];

        // Evaluación de estado del nodo interno
        if (padre.numClaves < orden - 1) {
            // ESTADO SEGURO
            for (int k = 0; k < padre.numClaves + 1; k++) padre.claves[k] = tempClaves[k];
            for (int k = 0; k < padre.numClaves + 2; k++) padre.getHijos()[k] = tempHijos[k];
            padre.numClaves++;
            derecho.padre = padre;
        } else {
            // ESTADO DE OVERFLOW: División de Nodo Interno
            NodoInterno<K> nuevoInterno = new NodoInterno<>(orden);
            int puntoCorte = orden / 2;

            padre.numClaves = 0;
            for (int k = 0; k < puntoCorte; k++) {
                padre.claves[k] = tempClaves[k];
                padre.numClaves++;
            }
            for (int k = 0; k < puntoCorte + 1; k++) {
                padre.getHijos()[k] = tempHijos[k];
            }

            // Regla de Nodos Internos B+: La clave central se EMPUJA (abandona este nivel)
            K claveAEmpujar = tempClaves[puntoCorte];

            for (int k = puntoCorte + 1; k < orden; k++) {
                nuevoInterno.claves[nuevoInterno.numClaves] = tempClaves[k];
                nuevoInterno.numClaves++;
            }
            for (int k = puntoCorte + 1; k < orden + 1; k++) {
                nuevoInterno.getHijos()[k - (puntoCorte + 1)] = tempHijos[k];
                tempHijos[k].padre = nuevoInterno; // Reasignar paternidad
            }

            // Propagación recursiva del overflow hacia el abuelo
            insertarEnPadre(padre, claveAEmpujar, nuevoInterno);
        }
    }

    
    /**
     * Recupera un registro evaluando matemáticamente su camino.
     * Complejidad de tiempo: $O(\log n)$.
     *
     * @param clave El identificador del registro a buscar.
     * @return El objeto almacenado, o null si la clave no existe en la estructura.
     */
    public V buscar(K clave) {
        NodoBPlus<K> actual = raiz;
        
        // Fase de descenso
        while (actual instanceof NodoInterno) {
            NodoInterno<K> nodoInterno = (NodoInterno<K>) actual;
            int i = 0;
            while (i < nodoInterno.numClaves && clave.compareTo(nodoInterno.claves[i]) >= 0) { 
                i++; 
            }
            actual = nodoInterno.getHijos()[i];
        }
        
        // Fase de escaneo en la hoja encontrada
        NodoHoja<K, V> hoja = (NodoHoja<K, V>) actual;
        for (int i = 0; i < hoja.numClaves; i++) {
            if (hoja.claves[i].compareTo(clave) == 0) {
                return hoja.getValores()[i]; // Match exacto
            }
        }
        
        return null; // Ausencia de datos
    }
    
     public NodoBPlus<K> getRaiz() {
        return this.raiz;
    }
}