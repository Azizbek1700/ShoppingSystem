package uz.master.warehouse.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.master.warehouse.entity.product.Product;
import uz.master.warehouse.entity.product.Product;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Product o set o.deleted = true,o.model = (o.model+''+CURRENT_TIMESTAMP) where o.id = :orgId")
    void deleteProduct(@Param("orgId") Long id);

    Optional<Product> findByIdAndDeletedFalse(Long id);

    List<Product> findAllByDeletedFalse();

    List<Product> findAllByModelAndDeletedFalse(String model);

    Product findByModelAndColorAndDeletedFalse(String model, String color);

    @Query(value = "select  count(*) from product where not deleted and id=?1 ",nativeQuery = true)
    int existsByProduct(Long productId);
}
