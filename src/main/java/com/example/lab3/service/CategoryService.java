package com.example.lab3.service;

import com.example.lab3.dto.CategoryDto;
import com.example.lab3.dto.ProductDto;
import com.example.lab3.entity.Category;
import com.example.lab3.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepo;

    // Créer une catégorie
    @Transactional
    public CategoryDto create(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.name());
        Category saved = categoryRepo.save(category);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    // Récupérer une catégorie par son ID
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new CategoryDto(category.getId(), category.getName());
    }

    // Lister toutes les catégories
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepo.findAll().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    // Récupérer les produits d'une catégorie spécifique
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        return category.getProducts().stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        category.getId()
                ))
                .collect(Collectors.toList());
    }
}