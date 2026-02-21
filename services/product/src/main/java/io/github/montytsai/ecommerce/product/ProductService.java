package io.github.montytsai.ecommerce.product;

import io.github.montytsai.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String PRODUCT_NOT_FOUND = "Product not found with ID: %s";
    private static final String CATEGORY_NOT_FOUND = "Category not found with ID: %s";

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts() {
        log.info("Finding all products");
        return repository.findAll().stream()
                .map(mapper::fromProduct)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Integer id) {
        log.info("Finding product by ID: {}", id);
        var product = getProductEntity(id);
        return mapper.fromProduct(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.name());
        var product = mapper.toProduct(request);
        assignCategory(product, request.categoryId());
        return mapper.fromProduct(repository.save(product));
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests) {
        var sortedRequests = requests.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var productIds = sortedRequests.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        log.debug("Purchasing products with IDs: {}", productIds);

        Map<Integer, Product> productMap = repository.findAllByIdInOrderByIdWithLock(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        if (productMap.size() != productIds.size()) {
            throw new ProductPurchaseException("one or more products not found");
        }

        return sortedRequests.stream().map(request -> {
            var product = productMap.get(request.productId());
            if (product.getAvailableQuantity().compareTo(request.quantity()) < 0) {
                throw new ProductPurchaseException("insufficient stock for product ID: " + request.productId());
            }

            product.setAvailableQuantity(product.getAvailableQuantity().subtract(request.quantity()));

            var response = mapper.toProductPurchaseResponse(product, request);
            log.debug("Purchased product: {}, quantity: {}, total price: {}", product.getName(), request.quantity(), response.totalPrice());
            return response;
        }).toList();
    }

    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        var product = getProductEntity(id);
        assignCategory(product, request.categoryId());
        mapper.updateProduct(request, product);
        return mapper.fromProduct(product);
    }

    @Transactional
    public ProductResponse patchProduct(Integer id, ProductRequest request) {
        log.info("Patching product with ID: {}", id);
        var product = getProductEntity(id);
        assignCategory(product, request.categoryId());
        mapper.patchProduct(request, product);
        return mapper.fromProduct(product);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
        }
        repository.deleteById(id);
    }

    private Product getProductEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));
    }

    private void assignCategory(Product product, Integer categoryId) {
        if (categoryId == null) return;

        boolean isSameCategory = product.getCategory() != null &&
                categoryId.equals(product.getCategory().getId());

        if (isSameCategory) return;

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORY_NOT_FOUND, categoryId)));
        product.setCategory(category);
    }

}