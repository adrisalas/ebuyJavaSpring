package com.example.ebuydb.controller;

import com.example.ebuydb.dao.*;
import com.example.ebuydb.dto.AccountSessionDTO;
import com.example.ebuydb.entity.*;
import com.example.ebuydb.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
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
    private final AccountRepository accountRepository;
    private final ProductService productService;

    @GetMapping("/productoslistar")
    public String productosListar(@RequestParam(value = "filtro_categoria", required = false) String filtro_categoria,
                                  @RequestParam(value = "filtro_keyword", required = false) String filtro_keyword,
                                  @RequestParam(value = "filtro_date", required = false) String filtro_date,
                                  @RequestParam(value = "filtro_time", required = false) String filtro_time,
                                  @RequestParam(value = "filtro_titulo", required = false) String filtro_titulo,
                                  @RequestParam(value = "filtro_descripcion", required = false) String filtro_descripcion,
                                  Model model, HttpSession session) {
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if (filtro_categoria != null && !filtro_categoria.isEmpty()) {model.addAttribute("str_filtro_categoria",filtro_categoria);}
        if (filtro_keyword != null && !filtro_keyword.isEmpty()) {model.addAttribute("str_filtro_keywords",filtro_keyword);}
        if (filtro_date != null && !filtro_date.isEmpty()) {model.addAttribute("str_filtro_date",filtro_date);}
        if (filtro_time != null && !filtro_time.isEmpty()) {filtro_time += ":00"; model.addAttribute("str_filtro_time",filtro_time);}
        if (filtro_titulo != null && !filtro_titulo.isEmpty()) {model.addAttribute("str_filtro_titulo",filtro_titulo);}
        if (filtro_descripcion != null && !filtro_descripcion.isEmpty()) {model.addAttribute("str_filtro_descripcion",filtro_descripcion);}

        model.addAttribute("listaKeywords", productService.getAllKeywords());
        model.addAttribute("listaCategorias", productService.getAllCategories());
        model.addAttribute("listadoProductos", productService.filterProducts(filtro_categoria,filtro_keyword,filtro_date,filtro_time,filtro_titulo,filtro_descripcion));
        return "listadoProductos";
    }

    @GetMapping("/productomostrar")
    public String productoMostrar(@RequestParam("productId") int productId, HttpSession session, Model model){
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        actualizarStatusComprar(session, model);
        List<Product> listProductsSameVendor = productService.get3ProductsFromSameVendor(productId);
        if(!listProductsSameVendor.isEmpty()){
            model.addAttribute("listProductsSameVendor",productService.get3ProductsFromSameVendor(productId));
        }
        model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("listadoValoraciones",productService.getReviewsByProductId(productId));
        model.addAttribute("mediaValoraciones", productService.getAverageReviewByProductId(productId));

        return "productoMostrar";
    }

    @PostMapping("/productocomprar")
    public String productoComprar(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity,
                                  HttpSession session){
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        productService.buyProduct(productId,quantity,user.getUserId(),session);

        return "redirect:/productomostrar?productId=" + productId;
    }

    @GetMapping("/misproductos")
    public String misProductos(HttpSession session, Model model){
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 1) {
            return "redirect:/";
        }

        model.addAttribute("listadoProductos", productService.getProductsByUserId(user.getUserId()));

        return "listadoMisProductos";
    }

    @GetMapping("/crearproducto")
    public String crearProducto(Model model, HttpSession session){
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 1) {
            return "redirect:/";
        }
        model.addAttribute("listCategory", productService.getAllCategories());
        model.addAttribute("listSubcategory", productService.getAllSubCategories());
        model.addAttribute("listKeyword",productService.getAllKeywords());
        model.addAttribute("product",productService.getEmptyProduct());
        return "producto";
    }

    @GetMapping("/editarproducto")
    public String editarProducto(@RequestParam("productId") String productId, Model model, HttpSession session){
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 1) {
            return "redirect:/";
        }

        if(productId == null || productId == ""){
            return "redirect:/misproductos";
        }

        model.addAttribute("listCategory",productService.getAllCategories());
        model.addAttribute("listSubcategory", productService.getAllSubCategories());
        model.addAttribute("listKeyword",productService.getAllKeywords());
        model.addAttribute("product", productService.getProductById(Integer.parseInt(productId)));

        return "producto";
    }

    @GetMapping("/borrarproducto")
    public String borrarProducto(@RequestParam("productId") String productId, HttpSession session) {
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        productService.deleteProduct(productId);

        return "redirect:/misproductos";
    }

    @PostMapping("/guardarproducto")
    public String guardarProducto(@RequestParam("productId") String productId, @RequestParam("title") String title,
                                  @RequestParam("description") String description, @RequestParam("price") String price,
                                  @RequestParam("urlPhoto") String urlPhoto, @RequestParam("quantity") String quantity,
                                  @RequestParam("subcategoria") String subcategory, @RequestParam("listaOculta") String listaOculta,
                                  HttpSession session) throws UnsupportedEncodingException {
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 1) {
            return "redirect:/";
        }

        String[] keywords = new String(listaOculta.getBytes("ISO-8859-1"), "UTF8").split(",");
        productService.saveProduct(productId,title,description,price,urlPhoto,quantity,subcategory,keywords,user.getUserId());

        return "redirect:/misproductos";
    }

    @GetMapping("/historial")
    public String historialProductos(Model model, HttpSession session){
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 1) {
            return "redirect:/";
        }

        model.addAttribute("listadoCompras",productService.getPurchasedProductsByUserId(user.getUserId()));

        return "historial";
    }



    @PostMapping("/guardarvaloracion")
    public String guardarValoracion(@RequestParam("reviewId") String reviewId, @RequestParam("compraId") int compraId,
                                    @RequestParam("comentario") String comentario, @RequestParam("rating") String rating,
                                    HttpSession session) throws UnsupportedEncodingException {
        AccountSessionDTO user = (AccountSessionDTO) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 1) {
            return "redirect:/";
        }

        productService.saveReview(reviewId,compraId,rating,comentario,user.getUserId());

        return "redirect:/historial";
    }

    private static void actualizarStatusComprar(HttpSession session, Model model){
        //Todo make this more generic in session example: session.messages[]
        String statusComprarOK = (String) session.getAttribute("statusComprarOK");
        if(statusComprarOK != null && statusComprarOK != ""){
            model.addAttribute("statusComprarOK",statusComprarOK);
        }
        session.removeAttribute("statusComprarOK");

        String statusComprar = (String) session.getAttribute("statusComprar");
        if(statusComprar != null && statusComprar != ""){
            model.addAttribute("statusComprar",statusComprar);
        }
        session.removeAttribute("statusComprar");
    }

}