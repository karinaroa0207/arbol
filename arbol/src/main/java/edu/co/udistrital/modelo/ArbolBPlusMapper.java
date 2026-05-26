package edu.co.udistrital.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Clase especialista en transformar la estructura interna y privada del Árbol B+
 * en una representación abstracta, limpia y segura (DTO) para la vista.
 */
public class ArbolBPlusMapper {

    /**
     * Toma un árbol genérico de backend y lo traduce a un DTO de topología global.
     */
    public static <K extends Comparable<K>, V, D> ArbolBPlusDTO<K, D> toDTO(ArbolBPlus<K, V> arbol, Function<V, D> transformadorDatos) {
        if (arbol == null || arbol.getRaiz() == null) {
            return new ArbolBPlusDTO<>(null, arbol != null ? arbol.getOrden() : 4);
        }
        
        // El método ya retorna el NodoDTO independiente directamente
        NodoDTO<K, D> raizDTO = convertirNodoADTO(arbol.getRaiz(), arbol.getOrden(), transformadorDatos);
        return new ArbolBPlusDTO<>(raizDTO, arbol.getOrden());
    }

    @SuppressWarnings("unchecked")
    private static <K extends Comparable<K>, V, D> NodoDTO<K, D> convertirNodoADTO(
            NodoBPlus<K> nodoReal, int orden, Function<V, D> transformadorDatos) {
        
        // 1. Extraer las claves del nodo original como Strings para que la UI las dibuje sin problemas
        List<String> listaClaves = new ArrayList<>();
        for (int i = 0; i < nodoReal.numClaves; i++) {
            listaClaves.add(nodoReal.claves[i].toString());
        }

        List<NodoDTO<K, D>> listaHijosDTO = new ArrayList<>();
        List<D> listaDTOSValores = new ArrayList<>();
        
        boolean esHoja = nodoReal instanceof NodoHoja;
        boolean tieneSiguiente = false;

        // 2. Procesar la estructura de forma recursiva según el tipo de nodo físico
        if (!esHoja) {
            // Nodo Interno (Enrutamiento): Mapeamos sus hijos hacia abajo
            NodoInterno<K> interno = (NodoInterno<K>) nodoReal;
            int numHijos = interno.numClaves + 1;
            for (int i = 0; i < numHijos; i++) {
                if (interno.getHijos()[i] != null) {
                    listaHijosDTO.add(convertirNodoADTO(interno.getHijos()[i], orden, transformadorDatos));
                }
            }
        } else {
            // Nodo Hoja (Datos): Extraemos los valores de negocio y evaluamos enlaces hermanos
            NodoHoja<K, V> hoja = (NodoHoja<K, V>) nodoReal;
            tieneSiguiente = (hoja.getSiguiente() != null);
            
            for (int i = 0; i < hoja.numClaves; i++) {
                if (hoja.getValores()[i] != null) {
                    // Invocamos el transformador externo (ej. BoletaMapper::toDTO)
                    D dtoTransformado = transformadorDatos.apply(hoja.getValores()[i]);
                    listaDTOSValores.add(dtoTransformado); 
                }
            }
        }

        // 3. Retornamos la nueva instancia de la clase NodoDTO independiente
        return new NodoDTO<>(listaClaves, listaHijosDTO, listaDTOSValores, esHoja, tieneSiguiente);
    }
}