package ks.msx.web_page.repository;

import ks.msx.web_page.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);

    List<Optional<Product>> findProductByType(String type);

    List<Optional<Product>> findProductByPrice(double price);
}
