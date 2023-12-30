package ks.msx.web_page.service;

import ks.msx.web_page.entity.Product;
import ks.msx.web_page.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Optional<Product>> getProductsByType(String type){
        return productRepository.findProductByType(type);
    }

//    public List<Product> getProductsByType(String type){
//        return productRepository.findAll(Sort.by(type));
//    }

    public List<Optional<Product>> getProductsByPrice(double price){
        return productRepository.findProductByPrice(price);
    }

    public List<Product> getProductsOrderedByPrice(String order){
        return getAllProducts().stream().sorted().toList();
    }
}
