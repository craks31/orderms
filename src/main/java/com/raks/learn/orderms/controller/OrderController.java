package com.raks.learn.orderms.controller;

import com.raks.learn.orderms.dto.OrderRequest;
import com.raks.learn.orderms.dto.OrderResponse;
import com.raks.learn.orderms.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ── POST /api/v1/orders ──────────────────────────────────
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── GET /api/v1/orders/{id} ──────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // ── GET /api/v1/orders ───────────────────────────────────
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ── GET /api/v1/orders/customer?email=... ────────────────
    @GetMapping("/customer")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerEmail(email));
    }

    // ── GET /api/v1/orders/status/{status} ───────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    // ── PUT /api/v1/orders/{id} ──────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    // ── PATCH /api/v1/orders/{id}/status ─────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    // ── DELETE /api/v1/orders/{id} ───────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
