package com.example.lab3.service;

import com.example.lab3.dto.ProductDto;
import com.example.lab3.entity.Category;
import com.example.lab3.entity.Product;
import com.example.lab3.repository.CategoryRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //génère automatiquement un constructeur avec les attributs final pour l'injection sans utiliser @Autowired
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    // Créer un produit
    @Transactional //garantit que si une étape échoue, toutes les modifications en base de données sont annulées (rolback)
    public ProductDto create(ProductDto dto) {
        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Product p = new Product(null, dto.name(), dto.price(), cat);
        Product saved = productRepo.save(p);
        return new ProductDto(saved.getId(), saved.getName(), saved.getPrice(), cat.getId());
    }

    // Lire tous les produits
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepo.findAll().stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCategory() != null ? p.getCategory().getId() : null
                ))
                .collect(Collectors.toList());
    }

    // Lire un produit par son ID
    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCategory() != null ? p.getCategory().getId() : null
        );
    }

    // Mettre à jour un produit
    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setCategory(cat);

        Product updated = productRepo.save(p);
        return new ProductDto(updated.getId(), updated.getName(), updated.getPrice(), cat.getId());
    }

    // Supprimer un produit
    @Transactional
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepo.deleteById(id);
    }
    // Récupérer un produit par son ID
    @Transactional(readOnly = true)
    public ProductDto getById(Long id) {
        return findById(id);
    }
    // Récupérer tous les produits avec pagination et tri
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<ProductDto> findAll(org.springframework.data.domain.Pageable pageable) {
        return productRepo.findAll(pageable)
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCategory() != null ? p.getCategory().getId() : null
                ));
    }
    // Récupérer les produits par ID de catégorie via la requête personnalisée
    @Transactional(readOnly = true)
    public List<ProductDto> findByCategoryId(Long categoryId) {
        return productRepo.findByCategoryId(categoryId).stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCategory() != null ? p.getCategory().getId() : null
                ))
                .collect(Collectors.toList());
    }
}