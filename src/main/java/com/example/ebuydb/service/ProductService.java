package com.example.ebuydb.service;

import com.example.ebuydb.dao.*;
import com.example.ebuydb.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final SubcategoryRepository subcategoryRepository;

    public List<Product> getAllProducts(){
        return productRepository.allProductsOrderByDate();
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Keyword> getAllKeywords(){
        return keywordRepository.findAll();
    }

    public List<Product> filterProducts(String filtro_categoria, String filtro_keyword, String filtro_date,
                                        String filtro_time, String filtro_titulo, String filtro_descripcion){

        HashMap<Product, Integer> productsHashMap = new HashMap<>();
        List<Product> productsList;
        int numberOfFilters = 0;

        if (filtro_categoria != null && !filtro_categoria.isEmpty()) {
            numberOfFilters++;
            ProductServiceUtils.addToHashMap(productsHashMap, productRepository.findByCategoria(Integer.parseInt(filtro_categoria)));
        }

        if (filtro_keyword != null && !filtro_keyword.isEmpty()) {
            numberOfFilters++;
            ProductServiceUtils.addToHashMap(productsHashMap, productRepository.findByKeyword(Integer.parseInt(filtro_keyword)));
        }

        if (filtro_date != null && !filtro_date.isEmpty()) {
            try {
                numberOfFilters++;
                ProductServiceUtils.addToHashMap(productsHashMap, productRepository.findByDate(new SimpleDateFormat("yyyy-MM-dd").parse(filtro_date)));
            } catch (ParseException ex) {
                //TODO Logger
            }
        }

        if (filtro_time != null && !filtro_time.isEmpty()) {
            try {
                filtro_time += ":00";
                numberOfFilters++;
                ProductServiceUtils.addToHashMap(productsHashMap, productRepository.findByTime(new SimpleDateFormat("HH:mm:ss").parse(filtro_time)));
            } catch (ParseException ex) {
                //TODO Logger
            }
        }
        if (filtro_titulo != null && !filtro_titulo.isEmpty()) {
            numberOfFilters++;
            ProductServiceUtils.addToHashMap(productsHashMap, productRepository.findByTitulo(filtro_titulo));
        }
        if (filtro_descripcion != null && !filtro_descripcion.isEmpty()) {
            numberOfFilters++;
            ProductServiceUtils.addToHashMap(productsHashMap, productRepository.findByDescripcion(filtro_descripcion));
        }
        if (numberOfFilters == 0) {
            productsList = getAllProducts();
        } else {
            productsList = ProductServiceUtils.getFinalList(productsHashMap, numberOfFilters);
            Collections.sort(productsList, Comparator.comparing(Product::getCreationDate).thenComparing(Product::getCreationTime).reversed());
        }
        return productsList;
    }

    public Product getProductById(int productId){
        return productRepository.findById(productId).get();
    }

    public List<Review> getReviewsByProductId(int productId){
        return reviewRepository.findByProductoId(productId);
    }

    public double getAverageReviewByProductId(int productId){
        return reviewRepository.obtenerMediaValoraciones(productId);
    }

    public List<Product> get3ProductsFromSameVendor(int productId){
        Product product = productRepository.findById(productId).get();
        List<Product> productsSameVendor = product.getAccountByVendorId().getProductsByUserId();
        productsSameVendor.remove(product);
        productsSameVendor = productsSameVendor.subList(0, productsSameVendor.size() > 3 ? 3 : productsSameVendor.size());
        return productsSameVendor;
    }

    public void saveReview(String reviewId, int compraId, String rating, String comentario, int accountId) throws UnsupportedEncodingException {
        Review review;
        boolean esCrearNuevo = false;

        if (reviewId == null || reviewId.isEmpty() || reviewId.equals("0")) {
            review = new Review();
            esCrearNuevo = true;
        } else {
            review = reviewRepository.findById(Integer.valueOf(reviewId)).get();
        }
        PurchasedProduct compra = purchaseProductRepository.findById(Integer.valueOf(compraId)).get();
        review.setPurchaseId(compra);

        comentario = new String(comentario.getBytes("ISO-8859-1"),"UTF8");
        review.setComment(comentario);

        if(rating == null || rating.isEmpty() || rating.equalsIgnoreCase("")){
            rating = "0.0";
        }
        review.setStars(Double.valueOf(rating));

        review.setAccountByUserId(accountRepository.findByAccountId(accountId));

        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        review.setReviewDate(date);

        reviewRepository.save(review);
        if (esCrearNuevo) {
            compra.setReview(review);
            purchaseProductRepository.save(compra);
        }
    }

    public void buyProduct(int productId, int quantity, int accountId, HttpSession session){
        Product product = productRepository.findById(productId).get();

        if(product.getQuantity() >= quantity && quantity != 0){
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            PurchasedProduct pp = new PurchasedProduct();
            pp.setAccountByBuyerId(accountRepository.findByAccountId(accountId));
            pp.setPrice(product.getPrice());
            pp.setProductByProductId(product);
            pp.setPurchaseDate(new java.sql.Date((System.currentTimeMillis())));
            pp.setQuantity(quantity);
            purchaseProductRepository.save(pp);

            session.setAttribute("statusComprarOK","Compradas " + quantity + " unidades");
        } else {
            session.setAttribute("statusComprar", "ERROR AL COMPRAR");
        }
    }

    public void saveProduct(String productId, String title, String description, String price, String urlPhoto, String quantity,
                            String subcategory, String[] keywords, int userId){

        Product product;

        if (productId == null || productId.isEmpty()) {
            product = new Product();
        } else {
            Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
            if(optionalProduct.isPresent()){
                product = optionalProduct.get();
            } else {
                product = new Product();
            }
        }

        //Todo fix bug in javascript that does not send keywords correctly
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(price));
        product.setPhotoUrl(urlPhoto);
        product.setQuantity(Integer.parseInt(quantity));
        product.setSubcategoryBySubcategoryId(subcategoryRepository.getOne(Integer.parseInt(subcategory)));
        product.setAccountByVendorId(accountRepository.findByAccountId(userId));

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
        productRepository.save(product);
    }

    public List<PurchasedProduct> getPurchasedProductsByUserId(int userId) {
        return purchaseProductRepository.findByBuyerId(userId);
    }

    public void deleteProduct(String productId) {
        if (productId != null && !productId.equals("")) {
            Product p = productRepository.getOne(Integer.valueOf(productId));
            for(PurchasedProduct pp : p.getPurchasedProductsByProductId()){
                reviewRepository.delete(pp.getReview());
                purchaseProductRepository.delete(pp);
            }
            productRepository.delete(p);
        }
    }

    public List<Product> getProductsByUserId(int userId) {
        return productRepository.findByVendorId(userId);
    }

    public List<Subcategory> getAllSubCategories() {
        return subcategoryRepository.findAll();
    }

    public Product getEmptyProduct() {
        return new Product();
    }
}
