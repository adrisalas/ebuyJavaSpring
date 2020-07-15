package com.example.ebuydb.service;

import com.example.ebuydb.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductServiceUtils {

    static List<Product> getFinalList(HashMap<Product, Integer> productsHashMap, int numberOfFilters) {
    List<Product> productsList = new ArrayList<>();

    for(Product p : productsHashMap.keySet()){
        if (productsHashMap.get(p) == numberOfFilters) {
            productsList.add(p);
        }
    }

    return productsList;
}

    static void addToHashMap(HashMap<Product, Integer> productsHashMap, List<Product> filter) {
    for (Product p : filter) {
        if (productsHashMap.containsKey(p)) {
            productsHashMap.put(p, productsHashMap.get(p) + 1);
        } else {
            productsHashMap.put(p, 1);
        }
    }
}
}
