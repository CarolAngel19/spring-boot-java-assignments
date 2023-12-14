package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product) {
        Product newProduct =productsService.save(new Product(product));
        URI createdProductUri = URI.create("");
        return ResponseEntity.created(createdProductUri).body(product);
    }

    @GetMapping("/v1/getAll/")
    public ResponseEntity<List<Product>> getAllProducts(ProductDto product) {
        List<Product> products = productsService.all();
        return ResponseEntity.ok((List<Product>) products);

    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Optional<Product> product = productsService.findById(id);

        if (product.isPresent()) {
            Product product1 = product.get();
            return ResponseEntity.ok(product1);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody ProductDto productDto) {
        Optional<Product> optionalUser = productsService.findById(id);

        if (optionalUser.isPresent()) {
            Product existinProduct = optionalUser.get();
            existinProduct.setName(productDto.getName());
            existinProduct.setDescription(productDto.getDescription());
            existinProduct.setCategory(productDto.getCategory());
            existinProduct.setPrice(productDto.getPrice());

            productsService.save(existinProduct);

            return ResponseEntity.ok().build();
        }
        else {
            throw  new ProductNotFoundException(id);
        }
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id")String id) {
        Optional<Product> existingUserOptional = productsService.findById(id);

        if (existingUserOptional.isPresent()) {
            productsService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}