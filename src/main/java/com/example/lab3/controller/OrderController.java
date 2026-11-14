package com.example.lab3.controller;

import com.example.lab3.dto.OrderDto;
import com.example.lab3.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Créer une commande
    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto dto) {
        OrderDto created = orderService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Récupérer une commande par son ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    // Récupérer la liste de toutes les commandes
    @GetMapping
    public ResponseEntity<List<OrderDto>> list() {
        return ResponseEntity.ok(orderService.findAll());
    }
}