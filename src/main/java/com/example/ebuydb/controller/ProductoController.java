package com.example.ebuydb.controller;

import com.example.ebuydb.dao.CategoryRepository;
import com.example.ebuydb.dao.KeywordRepository;
import com.example.ebuydb.dao.ProductRepository;
import com.example.ebuydb.dao.ReviewRepository;
import com.example.ebuydb.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    private final ReviewRepository reviewRepository;

    @GetMapping("/productoslistar")
    public String productosListar(HttpSession session, HttpServletRequest request) {

        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/";
        }

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
            ProductoControllerUtils.insertarEnHashMap(productos, listadoProductos);
            request.setAttribute("str_filtro_categoria",str_filtro_categoria);
        }

        //KEYWORDS
        List<Keyword> listaKeyword = this.keywordRepository.findAll();
        String str_filtro_keyword = request.getParameter("filtro_keyword");
        boolean hayFiltroKeyword = (str_filtro_keyword != null && !str_filtro_keyword.isEmpty());

        if (hayFiltroKeyword) {
            listadoProductos = productRepository.findByKeyword(Integer.parseInt(str_filtro_keyword));
            numFiltros++;
            ProductoControllerUtils.insertarEnHashMap(productos, listadoProductos);
            request.setAttribute("str_filtro_keywords",str_filtro_keyword);
        }

        //DATE
        String str_filtro_date = request.getParameter("filtro_date");
        boolean hayFiltroDate = (str_filtro_date != null && !str_filtro_date.isEmpty());

        if (hayFiltroDate) {
            try {
                listadoProductos = productRepository.findByDate(new SimpleDateFormat("yyyy-MM-dd").parse(str_filtro_date));
                numFiltros++;
                ProductoControllerUtils.insertarEnHashMap(productos, listadoProductos);
                request.setAttribute("str_filtro_date",str_filtro_date);
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
                ProductoControllerUtils.insertarEnHashMap(productos, listadoProductos);
                request.setAttribute("str_filtro_time",str_filtro_time);
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
            ProductoControllerUtils.insertarEnHashMap(productos, listadoProductos);
            request.setAttribute("str_filtro_titulo",str_filtro_titulo);
        }

        //DESCRIPCION
        String str_filtro_descripcion = request.getParameter("filtro_descripcion");
        boolean hayFiltroDescripcion = (str_filtro_descripcion != null && !str_filtro_descripcion.isEmpty());

        if (hayFiltroDescripcion) {
            listadoProductos = productRepository.findByDescripcion(str_filtro_descripcion);
            numFiltros++;
            ProductoControllerUtils.insertarEnHashMap(productos, listadoProductos);
            request.setAttribute("str_filtro_descripcion",str_filtro_descripcion);
        }

        if (numFiltros == 0) {
            listadoProductos = productRepository.allProductsOrderByDate();
        } else {
            listadoProductos = ProductoControllerUtils.obtenerListaFinal(productos, numFiltros);
            Collections.sort(listadoProductos, Comparator.comparing(Product::getCreationDate).thenComparing(Product::getCreationTime).reversed());
            //TODO delete Product p
            Product p;


        }

        request.setAttribute("listaKeywords", listaKeyword);
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listadoProductos", listadoProductos);
        return "listadoProductos";
    }

    @GetMapping("/productomostrar")
    public String productoMostrar(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/";
        }

        try{
            int productId = Integer.parseInt(request.getParameter("productId"));

            Product product = productRepository.findById(productId).get();
            request.setAttribute("product", product);
            List<Review> listadoValoraciones = reviewRepository.findByProductoId(productId);
            request.setAttribute("listadoValoraciones", listadoValoraciones);
            double mediaValoraciones= reviewRepository.obtenerMediaValoraciones(productId);
            request.setAttribute("mediaValoraciones", mediaValoraciones);
            List<Product> listProductsSameVendor = product.getAccountByVendorId().getProductsByUserId();
            if(listProductsSameVendor.size() > 1){
                listProductsSameVendor.remove(product);
                listProductsSameVendor = listProductsSameVendor.subList(0,3);
                request.setAttribute("listProductsSameVendor",listProductsSameVendor);
            }
            return "productoMostrar";
        } catch(Exception e){
            if (usuario.getIsadmin() == 0) {
                return "redirect:/productoslistar";
            } else {
                return "redirect:/usuarioslistar";
            }
        }

    }





}