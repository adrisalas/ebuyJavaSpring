package com.example.ebuydb.controller;

import com.example.ebuydb.dao.*;
import com.example.ebuydb.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@AllArgsConstructor
public class ProductoController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final KeywordRepository keywordRepository;
    private final ReviewRepository reviewRepository;
    private final PurchaseProductRepository purchaseProductRepository;

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
            ProductoControllerUtils.actualizarStatusComprar(session, request);

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

    @PostMapping("/productocomprar")
    public String productoComprar(HttpSession session, HttpServletRequest request, Model model){
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/";
        }

        Integer productId = Integer.valueOf(request.getParameter("productId"));
        Integer quantity = Integer.valueOf(request.getParameter("quantity"));
        Product product = productRepository.findById(productId).get();
        if(product.getQuantity() >= quantity && quantity != 0){
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            PurchasedProduct pp = new PurchasedProduct();
            pp.setAccountByBuyerId(usuario);
            pp.setPrice(product.getPrice());
            pp.setProductByProductId(product);
            pp.setPurchaseDate(new java.sql.Date((System.currentTimeMillis())));
            pp.setQuantity(quantity);
            purchaseProductRepository.save(pp);
            session.setAttribute("statusComprarOK","Compradas " + quantity + " unidades");
        } else {
            session.setAttribute("statusComprar", "ERROR AL COMPRAR");
        }
        return "redirect:/productomostrar?productId=" + product.getProductId();
    }

    @GetMapping("/misproductos")
    public String misProductos(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null || usuario.getIsadmin() == 1) {
            return "redirect:/";
        }

        List<Product> listadoProductos = productRepository.findByVendorId(usuario.getUserId());
        request.setAttribute("listadoProductos", listadoProductos);

        return "listadoMisProductos";
    }

    @GetMapping("/crearproducto")
    public String crearProducto(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null || usuario.getIsadmin() == 1) {
            return "redirect:/";
        }
        List<Subcategory> listSubcategory = subcategoryRepository.findAll();
        List<Category> listCategory = categoryRepository.findAll();
        List<Keyword> listKeyword = keywordRepository.findAll();
        request.setAttribute("listCategory",listCategory);
        request.setAttribute("listSubcategory", listSubcategory);
        request.setAttribute("listKeyword",listKeyword);
        return "producto";
    }

    @GetMapping("/editarproducto")
    public String editarProducto(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null || usuario.getIsadmin() == 1) {
            return "redirect:/";
        }
        String productId = request.getParameter("productId");

        if(productId == null || productId == ""){
            return "redirect:/misproductos";
        }

        Product producto = productRepository.findById(Integer.valueOf(productId)).get();
        List<Subcategory> listSubcategory = subcategoryRepository.findAll();
        List<Category> listCategory = categoryRepository.findAll();
        List<Keyword> listKeyword = keywordRepository.findAll();


        request.setAttribute("listCategory",listCategory);
        request.setAttribute("listSubcategory", listSubcategory);
        request.setAttribute("listKeyword",listKeyword);
        request.setAttribute("producto", producto);

        return "producto";
    }

    @GetMapping("/borrarproducto")
    public String borrarProducto(HttpSession session, HttpServletRequest request) {
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/";
        }
        String productId = request.getParameter("productId");
        if (productId != null && !productId.equals("")) {
            Product p = productRepository.getOne(Integer.valueOf(productId));
            for(PurchasedProduct pp : p.getPurchasedProductsByProductId()){
                reviewRepository.delete(pp.getReview());
                purchaseProductRepository.delete(pp);
            }
            productRepository.delete(p);
        }
        return "redirect:/misproductos";
    }

    @PostMapping("/guardarproducto")
    public String guardarProducto(HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException {
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null || usuario.getIsadmin() == 1) {
            return "redirect:/";
        }

        Product product;

        String productoId = request.getParameter("productId");
        if (productoId == null || productoId.isEmpty()) {
            product = new Product();
        } else {
            product = productRepository.findById(Integer.valueOf(productoId)).get();
        }

        String titulo = new String(request.getParameter("title").getBytes("ISO-8859-1"), "UTF8");
        String descripcion = new String(request.getParameter("description").getBytes("ISO-8859-1"), "UTF8");
        String precio = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF8");
        String foto = new String(request.getParameter("urlPhoto").getBytes("ISO-8859-1"), "UTF8");
        String cantidad = new String(request.getParameter("quantity").getBytes("ISO-8859-1"), "UTF8");
        String subcategoria = new String(request.getParameter("subcategoria").getBytes("ISO-8859-1"), "UTF8");
        String[] keywords = new String(request.getParameter("listaOculta").getBytes("ISO-8859-1"), "UTF8").split(",");
        List<String> keywordList = keywordRepository.findAllName();
        List<Keyword> keyWords = new ArrayList<>();

//        for (String words : keywords) {
//            Keyword key;
//            if (!keywordList.contains(words)) {
//                key = new Keyword();
//                key.setName(words);
//                keywordRepository.save(key);
//            } else {
//                key = keywordRepository.findByName(words).get(0);
//            }
//
//            keyWords.add(key);
//
//        }

//        List<ProductKeyword> productKeywords = new ArrayList<>();
//        for(Keyword k : keyWords){
//            ProductKeyword pk = new ProductKeyword();
//            pk.setKeywordByKeywordId(k);
//            pk.setProductByProductId(product);
//            productKeywords.add(pk);
//        }
//        product.setProductKeywordsByProductId(productKeywords);
        product.setTitle(titulo);
        product.setDescription(descripcion);
        product.setPrice(Double.parseDouble(precio));
        product.setPhotoUrl(foto);
        product.setQuantity(Integer.parseInt(cantidad));
        product.setSubcategoryBySubcategoryId(subcategoryRepository.getOne(Integer.parseInt(subcategoria)));
        product.setAccountByVendorId(usuario);

        long millis = System.currentTimeMillis();

        java.sql.Date date = new java.sql.Date(millis);
        java.sql.Time tiempo = new java.sql.Time(millis);
        product.setCreationDate(date);
        java.util.Date time = tiempo;
        String hora = new SimpleDateFormat("HH:mm").format(time);
        hora += ":00";
        try {
            product.setCreationTime( new Time(new SimpleDateFormat("HH:mm:ss").parse(hora).getTime()));
        } catch (ParseException ex) {
            //TODO log
        }

        HttpStatus.
        productRepository.save(product);

        return "redirect:/misproductos";
    }

    @GetMapping("/historial")
    public String historialProductos(HttpSession session, HttpServletRequest request){
        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null || usuario.getIsadmin() == 1) {
            return "redirect:/";
        }

        List<PurchasedProduct> listadoCompras = purchaseProductRepository.findByBuyerId(usuario.getUserId());
        request.setAttribute("listadoCompras",listadoCompras);
        return "historial";
    }



    @PostMapping("/guardarvaloracion")
    public String guardarValoracion(HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException {

        Account usuario = (Account) session.getAttribute("user");

        if (usuario == null || usuario.getIsadmin() == 1) {
            return "redirect:/";
        }
        Review review;
        boolean esCrearNuevo = false;

        String reviewId = request.getParameter("reviewId");
        if (reviewId == null || reviewId.isEmpty() || reviewId.equals("0")) {
            review = new Review();
            esCrearNuevo = true;
        } else {
            review = reviewRepository.findById(Integer.valueOf(reviewId)).get();
        }

        String compraId = request.getParameter("compraId");
        PurchasedProduct compra = purchaseProductRepository.findById(Integer.valueOf(compraId)).get();
        review.setPurchaseId(compra);

        String comentario = new String(request.getParameter("comentario").getBytes("ISO-8859-1"),"UTF8");
        review.setComment(comentario);

        String rating =request.getParameter("rating");
        if(rating == null || rating.isEmpty() || rating.equalsIgnoreCase("")){
            rating = "0.0";
        }
        review.setStars(Double.valueOf(rating));

        review.setAccountByUserId(usuario);

        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        review.setReviewDate(date);


        reviewRepository.save(review);
        if (esCrearNuevo) {
            compra.setReview(review);
            purchaseProductRepository.save(compra);
        }

        return "redirect:/historial";
    }

}