package edu.co.udistrital.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Clase especialista en transformar la estructura interna y privada del Árbol B+
 * en una representación abstracta, limpia y segura (DTO) para la vista.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class ArbolBPlusMapper {

    /**
     * Constructor privado para evitar instanciación (clase utilitaria).
     */
    private ArbolBPlusMapper() {
        // Constructor vacío y privado
    }

    /**
     * Toma un árbol genérico de backend y lo traduce a un DTO de topología global.
     * @param <K> Tipo de clave
     * @param <V> Tipo de valor original
     * @param <D> Tipo de valor transformado
     * @param arbol El árbol B+ a convertir
     * @param transformadorDatos Función para transformar V en D
     * @return DTO del árbol
     */
    public static <K extends Comparable<K>, V, D> ArbolBPlusDTO<K, D> toDTO(ArbolBPlus<K, V> arbol, Function<V, D> transformadorDatos) {
        if (arbol == null || arbol.getRaiz() == null) {
            return new ArbolBPlusDTO<>(null, arbol != null ? arbol.getOrden() : 4);
        }
        
        NodoDTO<K, D> raizDTO = convertirNodoADTO(arbol.getRaiz(), arbol.getOrden(), transformadorDatos);
        return new ArbolBPlusDTO<>(raizDTO, arbol.getOrden());
    }

    /**
     * Convierte recursivamente un nodo físico del árbol en un NodoDTO para la vista.
     * 
     * @param <K> Tipo de clave
     * @param <V> Tipo de valor original
     * @param <D> Tipo de valor transformado
     * @param nodoReal Nodo físico del árbol B+
     * @param orden Grado del árbol
     * @param transformadorDatos Función para transformar V en D
     * @return DTO del nodo
     */
    @SuppressWarnings("unchecked")
    private static <K extends Comparable<K>, V, D> NodoDTO<K, D> convertirNodoADTO(
            NodoBPlus<K> nodoReal, int orden, Function<V, D> transformadorDatos) {
        
        List<String> listaClaves = new ArrayList<>();
        for (int i = 0; i < nodoReal.numClaves; i++) {
            listaClaves.add(nodoReal.claves[i].toString());
        }

        List<NodoDTO<K, D>> listaHijosDTO = new ArrayList<>();
        List<D> listaDTOSValores = new ArrayList<>();
        
        boolean esHoja = nodoReal instanceof NodoHoja;
        boolean tieneSiguiente = false;

        if (!esHoja) {
            NodoInterno<K> interno = (NodoInterno<K>) nodoReal;
            int numHijos = interno.numClaves + 1;
            for (int i = 0; i < numHijos; i++) {
                if (interno.getHijos()[i] != null) {
                    listaHijosDTO.add(convertirNodoADTO(interno.getHijos()[i], orden, transformadorDatos));
                }
            }
        } else {
            NodoHoja<K, V> hoja = (NodoHoja<K, V>) nodoReal;
            tieneSiguiente = (hoja.getSiguiente() != null);
            
            for (int i = 0; i < hoja.numClaves; i++) {
                if (hoja.getValores()[i] != null) {
                    D dtoTransformado = transformadorDatos.apply(hoja.getValores()[i]);
                    listaDTOSValores.add(dtoTransformado); 
                }
            }
        }

        return new NodoDTO<>(listaClaves, listaHijosDTO, listaDTOSValores, esHoja, tieneSiguiente);
    }
}