package io.github.montytsai.ecommerce.product;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * use entity graph to fetch category along with products to avoid N+1 problem when fetching all products
     *
     * @return list of all products with their categories eagerly loaded
     */
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category"})
    List<Product> findAll();

    /**
     * use pessimistic lock to prevent concurrent updates on the same products during purchase
     *
     * @param ids product IDs to lock
     * @return list of products with the specified IDs, ordered by ID
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids ORDER BY p.id")
    List<Product> findAllByIdInOrderByIdWithLock(@Param("ids") List<Integer> ids);

}