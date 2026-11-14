package com.example.lab3.controller;

import com.example.lab3.dto.CategoryDto;
import com.example.lab3.dto.ProductDto;
import com.example.lab3.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // Créer une catégorie
    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto dto) {
        return new ResponseEntity<>(categoryService.create(dto), HttpStatus.CREATED);
    }

    // Récupérer une catégorie par son ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    // Lister toutes les catégories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> list() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // Endpoint pour récupérer la liste des produits d'une catégorie spécifique
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getProductsByCategory(id));
    }
}