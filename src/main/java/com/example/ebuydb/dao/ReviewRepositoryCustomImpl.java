package com.example.ebuydb.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

//    @Autowired
//    @Lazy
//    AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public double obtenerMediaValoraciones(int id) {
        Query q = em.createQuery("SELECT AVG(r.stars) FROM Review r WHERE r.purchasedProductByPurchaseId.productByProductId.productId = :idProducto");
        q.setParameter("idProducto", id);
        List<Double> result = q.getResultList();
        List<Double> nullFirst = new ArrayList<>();
        nullFirst.add(null);

        if(result==null || result.isEmpty() || result.equals(nullFirst)){
            return -1;
        } else {
            return (double) result.get(0);
        }
    }
}
