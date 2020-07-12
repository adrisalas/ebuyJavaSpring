package com.example.ebuydb.controller;

import com.example.ebuydb.entity.Product;

import java.util.*;

public class ProductoControllerUtils {

    public static void insertarEnHashMap(HashMap<Product, Integer> productos, List<Product> filtrado) {
        for (Product p : filtrado) {
            if (!productos.containsKey(p)) {
                productos.put(p, 1);
            } else {
                productos.put(p, productos.get(p) + 1);
            }
        }
    }

    public static List<Product> obtenerListaFinal(HashMap<Product, Integer> productos, int numFiltros) {
        List<Product> productosFiltro = new ArrayList<>();
        Set<Product> set = productos.keySet();

        Iterator<Product> it = set.iterator();

        while (it.hasNext()) {
            Product p = it.next();
            if (productos.get(p) == numFiltros) {
                productosFiltro.add(p);
            }

        }
        return productosFiltro;
    }
}
