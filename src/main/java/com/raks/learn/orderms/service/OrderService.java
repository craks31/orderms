package com.raks.learn.orderms.service;

import com.raks.learn.orderms.dto.OrderRequest;
import com.raks.learn.orderms.dto.OrderResponse;
import com.raks.learn.orderms.entity.Order;
import com.raks.learn.orderms.exception.OrderNotFoundException;
import com.raks.learn.orderms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // ── CREATE ───────────────────────────────────────────────
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = mapToEntity(request);
        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    // ── READ (single) ────────────────────────────────────────
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    // ── READ (all) ───────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── READ (by customer email) ─────────────────────────────
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── READ (by status) ────────────────────────────────────
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── UPDATE ───────────────────────────────────────────────
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        existing.setCustomerName(request.getCustomerName());
        existing.setCustomerEmail(request.getCustomerEmail());
        existing.setProduct(request.getProduct());
        existing.setQuantity(request.getQuantity());
        existing.setPrice(request.getPrice());

        Order updated = orderRepository.save(existing);
        return mapToResponse(updated);
    }

    // ── UPDATE STATUS ────────────────────────────────────────
    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        existing.setStatus(status.toUpperCase());
        Order updated = orderRepository.save(existing);
        return mapToResponse(updated);
    }

    // ── DELETE ───────────────────────────────────────────────
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    // ── MAPPERS ──────────────────────────────────────────────
    private Order mapToEntity(OrderRequest request) {
        return Order.builder()
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .product(order.getProduct())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
