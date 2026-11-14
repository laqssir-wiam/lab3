package com.example.lab3.controller;

import com.example.lab3.dto.ProductDto;
import com.example.lab3.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // Créer un produit
    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        ProductDto created = productService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer tous les produits
//    @GetMapping
//    public ResponseEntity<List<ProductDto>> getAll() {
//        return ResponseEntity.ok(productService.findAll());
//    }

    // Récupérer un produit par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // Mettre à jour un produit
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    // Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer les produits de manière paginée et triée
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<ProductDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    // Endpoint utilisant la requête personnalisée par catégorie
    @GetMapping("/category/{catId}")
    public ResponseEntity<List<ProductDto>> getByCategory(@PathVariable Long catId) {
        return ResponseEntity.ok(productService.findByCategoryId(catId));
    }
}