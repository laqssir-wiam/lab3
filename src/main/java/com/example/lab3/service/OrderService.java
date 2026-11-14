package com.example.lab3.service;

import com.example.lab3.dto.OrderDto;
import com.example.lab3.dto.OrderItemDto;
import com.example.lab3.entity.Order;
import com.example.lab3.entity.OrderItem;
import com.example.lab3.entity.Product;
import com.example.lab3.repository.OrderRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    // Créer une commande avec ses items
    @Transactional
    public OrderDto create(OrderDto dto) {
        Order order = new Order();

        // Transformation des OrderItemDto en entités OrderItem
        List<OrderItem> items = dto.items().stream().map(itemDto -> {
            Product product = productRepo.findById(itemDto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemDto.productId()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.getItems().addAll(items);

        // Sauvegarde de la commande
        Order savedOrder = orderRepo.save(order);

        return mapToDto(savedOrder);
    }

    // Récupérer une commande par son ID
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return mapToDto(order);
    }

    // Lister toutes les commandes
    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return orderRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // onversion Entity -> DTO
    private OrderDto mapToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getId(),
                        item.getProduct() != null ? item.getProduct().getId() : null,
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderDto(order.getId(), order.getOrderDate(), itemDtos);
    }
}