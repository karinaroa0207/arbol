package edu.co.udistrital.modelo;

import java.util.ArrayList;

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
    public boolean insertar(K clave, V valor) {
        if (buscar(clave) != null) {
            return false;
        }

        NodoHoja<K, V> hojaObjetivo = encontrarHoja(clave);
        insertarEnHoja(hojaObjetivo, clave, valor);
        return true;
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
    /**
     * Recupera todos los registros cuyas claves estén dentro de un intervalo cerrado.
     * Esta operación aprovecha la característica principal del Árbol B+: las hojas
     * están conectadas entre sí como una lista enlazada, por lo que después de
     * encontrar la primera hoja solo se avanza secuencialmente.
     *
     * @param inicio Límite inferior del rango.
     * @param fin Límite superior del rango.
     * @return Lista dinámica con todos los registros encontrados en orden ascendente.
     */
    public ArrayList<V> buscarRango(K inicio, K fin) {
        ArrayList<V> resultados = new ArrayList<>();

        if (inicio.compareTo(fin) > 0) {
            return resultados;
        }

        NodoHoja<K, V> hojaActual = encontrarHoja(inicio);

        while (hojaActual != null) {
            for (int i = 0; i < hojaActual.numClaves; i++) {
                K claveActual = hojaActual.claves[i];

                if (claveActual.compareTo(fin) > 0) {
                    return resultados;
                }

                if (claveActual.compareTo(inicio) >= 0) {
                    resultados.add(hojaActual.getValores()[i]);
                }
            }

            hojaActual = hojaActual.getSiguiente();
        }

        return resultados;
    }

    /**
     * Elimina un registro por su clave y repara la estructura si algún nodo queda
     * por debajo del mínimo permitido. El proceso sigue la regla clásica de B+:
     * primero intenta redistribuir con hermanos y, si no es posible, fusiona nodos.
     *
     * @param clave Clave única del registro a eliminar.
     * @return true si la clave existía y fue eliminada; false si no estaba en el árbol.
     */
    public boolean eliminar(K clave) {
        NodoHoja<K, V> hoja = encontrarHoja(clave);
        int posicion = buscarIndiceClave(hoja, clave);

        if (posicion == -1) {
            return false;
        }

        eliminarDeHoja(hoja, posicion);

        if (hoja == raiz) {
            return true;
        }

        if (hoja.numClaves >= minimoClavesHoja()) {
            actualizarSeparadoresDespuesDeCambio(hoja);
            return true;
        }

        rebalancearHoja(hoja);
        return true;
    }

    private int buscarIndiceClave(NodoHoja<K, V> hoja, K clave) {
        for (int i = 0; i < hoja.numClaves; i++) {
            if (hoja.claves[i].compareTo(clave) == 0) {
                return i;
            }
        }
        return -1;
    }

    private void eliminarDeHoja(NodoHoja<K, V> hoja, int posicion) {
        for (int i = posicion; i < hoja.numClaves - 1; i++) {
            hoja.claves[i] = hoja.claves[i + 1];
            hoja.getValores()[i] = hoja.getValores()[i + 1];
        }

        hoja.claves[hoja.numClaves - 1] = null;
        hoja.getValores()[hoja.numClaves - 1] = null;
        hoja.numClaves--;
    }

    private int minimoClavesHoja() {
        return (int) Math.ceil((orden - 1) / 2.0);
    }

    private int minimoClavesInterno() {
        return (int) Math.ceil(orden / 2.0) - 1;
    }

    private int indiceHijo(NodoInterno<K> padre, NodoBPlus<K> hijoBuscado) {
        for (int i = 0; i <= padre.numClaves; i++) {
            if (padre.getHijos()[i] == hijoBuscado) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private void rebalancearHoja(NodoHoja<K, V> hoja) {
        NodoInterno<K> padre = hoja.padre;
        int indice = indiceHijo(padre, hoja);
        NodoHoja<K, V> izquierda = indice > 0 ? (NodoHoja<K, V>) padre.getHijos()[indice - 1] : null;
        NodoHoja<K, V> derecha = indice < padre.numClaves ? (NodoHoja<K, V>) padre.getHijos()[indice + 1] : null;

        if (izquierda != null && izquierda.numClaves > minimoClavesHoja()) {
            desplazarHojaDerecha(hoja);
            hoja.claves[0] = izquierda.claves[izquierda.numClaves - 1];
            hoja.getValores()[0] = izquierda.getValores()[izquierda.numClaves - 1];
            izquierda.claves[izquierda.numClaves - 1] = null;
            izquierda.getValores()[izquierda.numClaves - 1] = null;
            izquierda.numClaves--;
            hoja.numClaves++;
            padre.claves[indice - 1] = hoja.claves[0];
            return;
        }

        if (derecha != null && derecha.numClaves > minimoClavesHoja()) {
            hoja.claves[hoja.numClaves] = derecha.claves[0];
            hoja.getValores()[hoja.numClaves] = derecha.getValores()[0];
            hoja.numClaves++;
            eliminarDeHoja(derecha, 0);
            padre.claves[indice] = derecha.claves[0];
            return;
        }

        if (izquierda != null) {
            fusionarHojas(izquierda, hoja);
            eliminarEntradaPadre(padre, indice - 1, indice);
        } else if (derecha != null) {
            fusionarHojas(hoja, derecha);
            eliminarEntradaPadre(padre, indice, indice + 1);
        }
    }

    private void desplazarHojaDerecha(NodoHoja<K, V> hoja) {
        for (int i = hoja.numClaves; i > 0; i--) {
            hoja.claves[i] = hoja.claves[i - 1];
            hoja.getValores()[i] = hoja.getValores()[i - 1];
        }
    }

    private void fusionarHojas(NodoHoja<K, V> izquierda, NodoHoja<K, V> derecha) {
        for (int i = 0; i < derecha.numClaves; i++) {
            izquierda.claves[izquierda.numClaves] = derecha.claves[i];
            izquierda.getValores()[izquierda.numClaves] = derecha.getValores()[i];
            izquierda.numClaves++;
        }

        izquierda.setSiguiente(derecha.getSiguiente());
    }

    private void eliminarEntradaPadre(NodoInterno<K> padre, int indiceClave, int indiceHijo) {
        for (int i = indiceClave; i < padre.numClaves - 1; i++) {
            padre.claves[i] = padre.claves[i + 1];
        }
        padre.claves[padre.numClaves - 1] = null;

        for (int i = indiceHijo; i < padre.numClaves; i++) {
            padre.getHijos()[i] = padre.getHijos()[i + 1];
        }
        padre.getHijos()[padre.numClaves] = null;
        padre.numClaves--;

        actualizarSeparadoresDespuesDeCambio(padre);
        rebalancearInterno(padre);
    }

    private void rebalancearInterno(NodoInterno<K> nodo) {
        if (nodo == raiz) {
            if (nodo.numClaves == 0) {
                raiz = nodo.getHijos()[0];
                if (raiz != null) {
                    raiz.padre = null;
                } else {
                    raiz = new NodoHoja<K, V>(orden);
                }
            }
            return;
        }

        if (nodo.numClaves >= minimoClavesInterno()) {
            return;
        }

        NodoInterno<K> padre = nodo.padre;
        int indice = indiceHijo(padre, nodo);
        NodoInterno<K> izquierda = indice > 0 ? (NodoInterno<K>) padre.getHijos()[indice - 1] : null;
        NodoInterno<K> derecha = indice < padre.numClaves ? (NodoInterno<K>) padre.getHijos()[indice + 1] : null;

        if (izquierda != null && izquierda.numClaves > minimoClavesInterno()) {
            prestarDesdeInternoIzquierdo(nodo, izquierda, padre, indice);
            return;
        }

        if (derecha != null && derecha.numClaves > minimoClavesInterno()) {
            prestarDesdeInternoDerecho(nodo, derecha, padre, indice);
            return;
        }

        if (izquierda != null) {
            fusionarInternos(izquierda, nodo, padre.claves[indice - 1]);
            eliminarEntradaPadre(padre, indice - 1, indice);
        } else if (derecha != null) {
            fusionarInternos(nodo, derecha, padre.claves[indice]);
            eliminarEntradaPadre(padre, indice, indice + 1);
        }
    }

    private void prestarDesdeInternoIzquierdo(NodoInterno<K> nodo, NodoInterno<K> izquierda, NodoInterno<K> padre, int indice) {
        for (int i = nodo.numClaves; i > 0; i--) {
            nodo.claves[i] = nodo.claves[i - 1];
        }
        for (int i = nodo.numClaves + 1; i > 0; i--) {
            nodo.getHijos()[i] = nodo.getHijos()[i - 1];
        }

        nodo.claves[0] = padre.claves[indice - 1];
        nodo.getHijos()[0] = izquierda.getHijos()[izquierda.numClaves];
        nodo.getHijos()[0].padre = nodo;
        nodo.numClaves++;

        padre.claves[indice - 1] = izquierda.claves[izquierda.numClaves - 1];
        izquierda.claves[izquierda.numClaves - 1] = null;
        izquierda.getHijos()[izquierda.numClaves] = null;
        izquierda.numClaves--;
    }

    private void prestarDesdeInternoDerecho(NodoInterno<K> nodo, NodoInterno<K> derecha, NodoInterno<K> padre, int indice) {
        nodo.claves[nodo.numClaves] = padre.claves[indice];
        nodo.getHijos()[nodo.numClaves + 1] = derecha.getHijos()[0];
        nodo.getHijos()[nodo.numClaves + 1].padre = nodo;
        nodo.numClaves++;

        padre.claves[indice] = derecha.claves[0];

        for (int i = 0; i < derecha.numClaves - 1; i++) {
            derecha.claves[i] = derecha.claves[i + 1];
        }
        for (int i = 0; i < derecha.numClaves; i++) {
            derecha.getHijos()[i] = derecha.getHijos()[i + 1];
        }

        derecha.claves[derecha.numClaves - 1] = null;
        derecha.getHijos()[derecha.numClaves] = null;
        derecha.numClaves--;
    }

    private void fusionarInternos(NodoInterno<K> izquierda, NodoInterno<K> derecha, K separadorPadre) {
        int clavesInicialesIzquierda = izquierda.numClaves;
        izquierda.claves[izquierda.numClaves] = separadorPadre;
        izquierda.numClaves++;

        for (int i = 0; i < derecha.numClaves; i++) {
            izquierda.claves[izquierda.numClaves] = derecha.claves[i];
            izquierda.numClaves++;
        }

        int posicionPrimerHijoDerecho = clavesInicialesIzquierda + 1;
        for (int i = 0; i <= derecha.numClaves; i++) {
            izquierda.getHijos()[posicionPrimerHijoDerecho + i] = derecha.getHijos()[i];
            izquierda.getHijos()[posicionPrimerHijoDerecho + i].padre = izquierda;
        }
    }

    private void actualizarSeparadoresDespuesDeCambio(NodoBPlus<K> nodo) {
        if (nodo.padre == null || nodo.numClaves == 0) {
            return;
        }

        NodoInterno<K> padre = nodo.padre;
        int indice = indiceHijo(padre, nodo);

        if (indice > 0) {
            padre.claves[indice - 1] = obtenerPrimeraClave(nodo);
        } else {
            actualizarSeparadoresDespuesDeCambio(padre);
        }
    }

    @SuppressWarnings("unchecked")
    private K obtenerPrimeraClave(NodoBPlus<K> nodo) {
        NodoBPlus<K> actual = nodo;

        while (actual instanceof NodoInterno) {
            actual = ((NodoInterno<K>) actual).getHijos()[0];
        }

        return actual.numClaves > 0 ? actual.claves[0] : null;
    }

     public NodoBPlus<K> getRaiz() {
        return this.raiz;
    }
}
