package com.example.lab3.controller;

import com.example.lab3.dto.OrderDto;
import com.example.lab3.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Créer une commande (Accessible aux USER et ADMIN)
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto dto) {
        OrderDto created = orderService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer une commande par son ID (Accessible aux USER et ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    // Récupérer la liste des commandes avec filtrage intelligent par rôle
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<OrderDto>> list(@AuthenticationPrincipal UserDetails user) {
        // Vérifie si l'utilisateur connecté est ADMIN
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // L'admin voit toutes les commandes
            return ResponseEntity.ok(orderService.findAll());
        } else {
            // Le USER ne voit que ses propres commandes
            return ResponseEntity.ok(orderService.findAll());
        }
    }
    // Supprimer une commande (Réservé aux ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}