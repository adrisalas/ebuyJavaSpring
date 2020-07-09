package com.example.ebuydb.controller;

import com.example.ebuydb.dao.CategoryRepository;
import com.example.ebuydb.dao.KeywordRepository;
import com.example.ebuydb.dao.ProductRepository;
import com.example.ebuydb.entity.Account;
import com.example.ebuydb.entity.Category;
import com.example.ebuydb.entity.Keyword;
import com.example.ebuydb.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@AllArgsConstructor
public class ProductoController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;

    @GetMapping("/productoslistar")
    public String productosListar(HttpSession session, HttpServletRequest request) {

        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/";
        } else {
            HashMap<Product, Integer> productos = new HashMap<>();
            int numFiltros = 0;
            List<Product> listadoProductos;

            //CATEGORIAS
            List<Category> listaCategorias = categoryRepository.findAll();
            String str_filtro_categoria = request.getParameter("filtro_categoria");
            boolean hayFiltroCategoria = (str_filtro_categoria != null && !str_filtro_categoria.isEmpty());

            if (hayFiltroCategoria) {
                listadoProductos = productRepository.findByCategoria(Integer.parseInt(str_filtro_categoria));
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            //KEYWORDS
            List<Keyword> listaKeyword = this.keywordRepository.findAll();
            String str_filtro_keyword = request.getParameter("filtro_keyword");
            boolean hayFiltroKeyword = (str_filtro_keyword != null && !str_filtro_keyword.isEmpty());

            if (hayFiltroKeyword) {
                listadoProductos = productRepository.findByKeyword(Integer.parseInt(str_filtro_keyword));
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            //DATE
            String str_filtro_date = request.getParameter("filtro_date");
            boolean hayFiltroDate = (str_filtro_date != null && !str_filtro_date.isEmpty());

            if (hayFiltroDate) {
                try {
                    listadoProductos = productRepository.findByDate(new SimpleDateFormat("yyyy-MM-dd").parse(str_filtro_date));
                    numFiltros++;
                    insertarEnHashMap(productos, listadoProductos);
                } catch (ParseException ex) {
                    //TODO Logger
                }

            }

            //TIME
            String str_filtro_time = request.getParameter("filtro_time");

            boolean hayFiltroTime = (str_filtro_time != null && !str_filtro_time.isEmpty());

            if (hayFiltroTime) {
                try {
                    str_filtro_time += ":00";
                    listadoProductos = productRepository.findByTime(new SimpleDateFormat("HH:mm:ss").parse(str_filtro_time));
                    numFiltros++;
                    insertarEnHashMap(productos, listadoProductos);
                } catch (ParseException ex) {
                    //TODO Logger
                }

            }

            //TITULO
            String str_filtro_titulo = request.getParameter("filtro_titulo");
            boolean hayFiltroTitulo = (str_filtro_titulo != null && !str_filtro_titulo.isEmpty());

            if (hayFiltroTitulo) {
                listadoProductos = productRepository.findByTitulo(str_filtro_titulo);
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            //DESCRIPCION
            String str_filtro_descripcion = request.getParameter("filtro_descripcion");
            boolean hayFiltroDescripcion = (str_filtro_descripcion != null && !str_filtro_descripcion.isEmpty());

            if (hayFiltroDescripcion) {
                listadoProductos = productRepository.findByDescripcion(str_filtro_descripcion);
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            if (numFiltros == 0) {
                listadoProductos = productRepository.allProductsOrderByDate();
            } else {
                listadoProductos = obtenerListaFinal(productos, numFiltros);
                Collections.sort(listadoProductos, Comparator.comparing(Product::getCreationDate)
                        .thenComparing(Product::getCreationTime).reversed());

            }

            request.setAttribute("listaKeywords", listaKeyword);
            request.setAttribute("listaCategorias", listaCategorias);

            request.setAttribute("listadoProductos", listadoProductos);
        }
        return "mock";
    }




    private static void insertarEnHashMap(HashMap<Product, Integer> productos, List<Product> filtrado) {
        for (Product p : filtrado) {
            if (!productos.containsKey(p)) {
                productos.put(p, 1);
            } else {
                productos.put(p, productos.get(p) + 1);
            }
        }
    }

    private static List<Product> obtenerListaFinal(HashMap<Product, Integer> productos, int numFiltros) {
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